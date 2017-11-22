package ru.net.ndt.locky37;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * This program copies a whole directory (including its sub files and
 * sub directories) to another, using the Java NIO API.
 *
 * @author www.codejava.net
 */

class Main extends SimpleFileVisitor<Path> {
    private Path sourceDir;
    private Path targetDir;

    private Main(Path sourceDir, Path targetDir) {
        this.sourceDir = sourceDir;
        this.targetDir = targetDir;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) {

        try {
            Path targetFile = targetDir.resolve(sourceDir.relativize(file));
            Files.copy(file, targetFile);
        } catch (IOException ex) {
            System.err.println("IO Error File");
        }

        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attributes) {
        try {
            Path newDir = targetDir.resolve(sourceDir.relativize(dir));
            Files.createDirectory(newDir);
        } catch (IOException ex) {
            System.err.println("Specify a directory");
            System.exit(0);
        }

        return FileVisitResult.CONTINUE;
    }

    public static void main(String[] args) throws IOException {

        Path sourceDir;
        Path targetDir;

        if (args.length == 0) {
            System.out.println("Example: java -jar backup_service.jar SRC_DIR DST_DIR");
            System.out.println("Example with ZIP: java -jar backup_service.jar -Z SRC_DIR DST_DIR");
            System.exit(0);
        } else if (args[0].equals("-c")) {
            System.out.println("modification = c");
            sourceDir = Paths.get(args[1]);
            targetDir = Paths.get(args[2]);

            File dir = new File(String.valueOf(sourceDir));
            String zipDirName = String.valueOf(targetDir);

            Compress zipFiles = new Compress();
            zipFiles.zipDirectory(dir, zipDirName);
            System.out.println("Copy with compression complete!");
        } else {
            sourceDir = Paths.get(args[0]);
            targetDir = Paths.get(args[1]);
            Files.walkFileTree(sourceDir, new Main(sourceDir, targetDir));
            System.out.println("Copy complete!");
        }
    }
}