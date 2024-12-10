package utils;

import com.anish.world.World;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GameRecorder extends Thread {
    private final List<GameSnapshot> recordings = new ArrayList<>();
    private boolean isRecording = true;

    public void run() {
        while (isRecording) {
            GameSnapshot snapshot = new GameSnapshot(World.getInstance());
            recordings.add(snapshot);
            //等待游戏更新
            try {
                TimeUnit.MICROSECONDS.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void stopRecording() {
        isRecording = false;
    }
    public void saveRecord(String outputPath){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
            for (GameSnapshot snapshot : recordings) {
                writer.write("@");
                writer.newLine();
                for (GlyphColorPair pair : snapshot.getTileGlyphs()) {
                    writer.write(pair.getGlyph() + "," + Integer.toHexString(pair.getColor().getRGB() & 0xFFFFFF) + "," + pair.getX()+ "," + pair.getY());
                    writer.newLine();
                }
                for (GlyphColorPair pair : snapshot.getCreatureGlyphs()) {
                    writer.write(pair.getGlyph() + "," + Integer.toHexString(pair.getColor().getRGB() & 0xFFFFFF) + "," + pair.getX()+ "," + pair.getY());
                    writer.newLine();
                }

            }
            System.out.println("Video has been saved to " + outputPath);
        } catch (IOException _e) {
            System.err.println("An error occurred while writing to the file: " + _e.getMessage());
        }
    }
}