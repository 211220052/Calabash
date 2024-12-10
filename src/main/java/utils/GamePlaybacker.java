package utils;

import asciiPanel.AsciiPanel;
import com.anish.world.World;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class GamePlaybacker extends Thread{
    AsciiPanel terminal;
    String inputPath;

    JButton button;

    private boolean isPlaybacking = true;
    public GamePlaybacker(AsciiPanel terminal, String inputPath, JButton button){
        this.terminal = terminal;
        this.inputPath = inputPath;
        this.button = button;
    }

    public void stopPlaybacking() {
        isPlaybacking = false;
    }
    public void run() {
        terminal.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(inputPath))) {
            String line;
            while ((line = reader.readLine()) != null && isPlaybacking) {
                if(line.equals("@")){
                    TimeUnit.MICROSECONDS.sleep(300);
                    //System.out.println(2);
                    continue;
                }
                String[] parts = line.split(",");
                char glyph = parts[0].charAt(0);
                Color color = Color.decode("#" + parts[1]);
                int x = Integer.parseInt(parts[2]);
                int y = Integer.parseInt(parts[3]);
                terminal.write(glyph, x, y, color);
                //TimeUnit.MICROSECONDS.sleep(200);
                System.out.print(1);
            }
            System.out.println();
            System.out.println("回放结束");
            button.setText("回放游戏");

        }catch (IOException | InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }



}
