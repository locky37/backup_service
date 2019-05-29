package ru.net.ndt.locky37.main;

import java.io.IOException;
import java.nio.file.*;

import ru.net.ndt.locky37.zip.Compress;
import ru.net.ndt.locky37.socket.*;

class Main {

    public static void main(String[] args) throws IOException {

        Path sourceDir;
        Path targetDir;

        if (args.length == 0) {
            System.out.println("Example Copy Dir: java -jar backup_service.jar source... target");
            System.out.println("Example Copy Dir with ZIP: java -jar backup_service.jar -c source... target_file");
            System.out.println("Example Copy Dir with ZIP: java -jar backup_service.jar -server target_dir");
            System.out.println("Example Copy Dir with ZIP: java -jar backup_service.jar -client hostname source_dir");
            System.exit(0);
        } else if (args[0].equals("-server")) {
            System.out.println("modification = server");
            try {
                while (true) {
                    ServerZip server = new ServerZip();
                    server.serverZip(args[1]);
                    System.out.println("Server Copy with compression complete!");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (args[0].equals("-client")) {
            System.out.println("modification = client");

            ClientZip client = new ClientZip();
            client.clientZip(args[1], args[2]);
            System.out.println("Client Copy with compression complete!");

        } else if (args[0].equals("-c")) {
            System.out.println("modification = c");
            sourceDir = Paths.get(args[1]);
            targetDir = Paths.get(args[2]);

            Compress zipFiles = new Compress();
            zipFiles.zipDirectory(sourceDir, targetDir);
            System.out.println("Copy with compression complete!");
        } else if (args[0].length() != 0 && args[1].length() != 0) {
            sourceDir = Paths.get(args[0]);
            targetDir = Paths.get(args[1]);
            new Copy(sourceDir, targetDir);
            //Files.walkFileTree(sourceDir, new Main(sourceDir, targetDir));
            System.out.println("Copy complete!");
        }
    }
}