import asciiPanel.AsciiFont;
import asciiPanel.AsciiPanel;
import com.anish.screen.Screen;
import com.anish.screen.WorldScreen;
import com.anish.world.Calabash;
import com.anish.world.Monster;
import com.anish.world.Thing;
import com.anish.world.World;
import maze.BattleFieldGenerator;
import utils.GamePlaybacker;
import utils.GameRecorder;
import utils.GameSnapshot;
import utils.GlyphColorPair;

import javax.swing.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class IntegratedEchoServer {
    private Screen screen;
    private Selector selector;
    private InetSocketAddress listenAddress;
    private final static int PORT = 9093;
    private SocketChannel clientAChannel;
    private SocketChannel clientBChannel;

    public IntegratedEchoServer(String address, int port) throws IOException {
        listenAddress = new InetSocketAddress(address, port);
        screen = new WorldScreen();
        World.getInstance().setCreatures();
    }
    public static void main(String[] args){
        try {
            new IntegratedEchoServer("localhost", PORT).startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startServer() throws IOException {
        this.selector = Selector.open();
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);
        serverChannel.socket().bind(listenAddress);
        serverChannel.register(this.selector, SelectionKey.OP_ACCEPT);
        System.out.println("Server started on port >> " + PORT);
        while (true) {
            int readyCount = selector.select();
            if (readyCount == 0) {
                continue;
            }
            Set<SelectionKey> readyKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = readyKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                if (!key.isValid()) {
                    continue;
                }
                if (key.isAcceptable()) {
                    this.accept(key);
                }
                else if (key.isReadable()) {
                    System.out.println("key.isReadable()");
                    this.read(key);
                }
            }
            if(clientAChannel != null && clientBChannel != null){
                GameSnapshot snapshot = new GameSnapshot(World.getInstance());
                byte[] dataReply = serialize(snapshot);
                write(clientBChannel, dataReply);
                write(clientAChannel, dataReply);

            }
        }
    }

    private void accept(SelectionKey key) throws IOException {
        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
        SocketChannel channel = serverChannel.accept();
        channel.configureBlocking(false);

        Socket socket = channel.socket();
        SocketAddress remoteAddr = socket.getRemoteSocketAddress();
        System.out.println("Connected to: " + remoteAddr);

        if (clientAChannel == null) {
            clientAChannel = channel;
        } else if (clientBChannel == null) {
            clientBChannel = channel;
        }

        channel.register(this.selector, SelectionKey.OP_READ);

//        if(clientAChannel != null && clientBChannel != null){
//            new Scanner(System.in).nextLine();
//            World.getInstance().startCreatures();
//        }
    }

    private void read(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int numRead = channel.read(buffer);
        if (numRead == -1) {
            Socket socket = channel.socket();
            SocketAddress remoteAddr = socket.getRemoteSocketAddress();
            System.out.println("Connection closed by client: " + remoteAddr);
            channel.close();
            key.cancel();
            if (clientAChannel == channel) {
                clientAChannel = null;
            } else if (clientBChannel == channel) {
                clientBChannel = null;
            }
            return;
        }
        byte[] data = new byte[numRead];
        System.arraycopy(buffer.array(), 0, data, 0, numRead);
        System.out.println("Got: " + new String(data));
        String messageReceived = new String(data);

        String client = messageReceived.split(": ")[0];
        String client_value = messageReceived.split(": ")[1];

        if("Client-A".equals(client)){
            screen = screen.respondToUserAInput(client_value);
        }
        else if ("Client-B".equals(client)) {
            screen = screen.respondToUserBInput(client_value);
        }

        GameSnapshot snapshot = new GameSnapshot(World.getInstance());

        byte[] dataReply = serialize(snapshot);
        System.out.println("serialize(snapshot)");

        /*if (channel == clientAChannel){
            write(clientAChannel, dataReply);
            System.out.println("write(clientAChannel, dataReply)");
        }*/

        if (channel == clientAChannel && clientBChannel != null) {
            write(clientBChannel, dataReply);
            System.out.println("Reply clientA");
        } else if (channel == clientBChannel && clientAChannel != null) {
            write(clientAChannel, dataReply);
            System.out.println("Reply clientB");
        }
    }

    private void write(SocketChannel channel, byte[] data) throws IOException {
        ByteBuffer buffer = ByteBuffer.wrap(data);
        while (buffer.hasRemaining()) {
            channel.write(buffer);
        }
    }

    public static byte[] serialize(GameSnapshot snapshot) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            // 写入 GameSnapshot 对象
            oos.writeObject(snapshot);
            return baos.toByteArray();
        }
    }
}