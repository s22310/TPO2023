package zad1;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class Futil {

    public static void processDir(String dirName, String resultFileName) {
        Path startingDir = Paths.get(dirName);
        Path resultFile = Paths.get(resultFileName);

        try {
            if(!Files.exists(resultFile)) {
                Files.createFile(resultFile);
            } else {
                Files.write(resultFile, new byte[0]);
            }
        } catch (IOException e) {
            System.out.println("Cannot create file:" + resultFileName);
            e.printStackTrace();
            return;
        }

        try (FileChannel resultChannel = FileChannel.open(resultFile, StandardOpenOption.WRITE)) {
            Files.walkFileTree(startingDir, new SimpleFileVisitor<Path>() {

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if(Files.isRegularFile(file) && file.toString().endsWith(".txt")) {
                        try (FileChannel fileChannel = FileChannel.open(file, StandardOpenOption.READ)) {
                            ByteBuffer buffer = ByteBuffer.allocate((int)fileChannel.size());
                            fileChannel.read(buffer);
                            buffer.flip();

                            Charset inputCharset = Charset.forName("Cp1250");
                            Charset outputCharset = Charset.forName("UTF-8");
                            String content = inputCharset.decode(buffer).toString();
                            ByteBuffer encoded = outputCharset.encode(content);

                            resultChannel.write(encoded);
                        }
                    }
                    return FileVisitResult.CONTINUE;
                }
            });

        } catch (IOException e) {
            System.out.println("Error in dir: " + dirName);
            e.printStackTrace();
        }
    }
}