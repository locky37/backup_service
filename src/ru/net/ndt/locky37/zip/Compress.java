package ru.net.ndt.locky37.zip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Compress {

    private List<String> filesListInDir = new ArrayList<>();

    /**
     * This method zips the directory
     *
     * @param dir
     * @param zipDirName
     */
    public void zipDirectory(String dir, String zipDirName) {
        File directory = new File(dir);
        getFileList(directory);

        try (FileOutputStream fos = new FileOutputStream(zipDirName);
             ZipOutputStream zos = new ZipOutputStream(fos)) {

            for (String filePath : filesListInDir) {
                System.out.println("Compressing: " + filePath);

                // Creates a zip entry.
                String name = filePath.substring(directory.getAbsolutePath().length() + 1);

                ZipEntry zipEntry = new ZipEntry(name);
                zos.putNextEntry(zipEntry);

                // Read file content and write to zip output stream.
                try (FileInputStream fis = new FileInputStream(filePath)) {
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = fis.read(buffer)) > 0) {
                        zos.write(buffer, 0, length);
                    }

                    // Close the zip entry.
                    zos.closeEntry();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getFileList(File directory) {
/*        int i = 0;*/
        File[] files = directory.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                if (file.isFile()) {
                    filesListInDir.add(file.getAbsolutePath());
/*                    System.out.println(filesListInDir.get(i));
                    i++;*/
                } else {
                    getFileList(file);
                }
            }
        }
    }

    /**
     * This method compresses the single file to zip format
     *
     * @param file
     * @param zipFileName
     */
    public static void zipSingleFile(File file, String zipFileName) {

        Charset charset = Charset.forName("cp866");

        try {
            //create ZipOutputStream to write to the zip file
            FileOutputStream fos = new FileOutputStream(zipFileName);
            ZipOutputStream zos = new ZipOutputStream(fos, charset);
            //add a new Zip Entry to the ZipOutputStream
            ZipEntry ze = new ZipEntry(file.getName());
            zos.putNextEntry(ze);
            //read the file and write to ZipOutputStream
            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[2048];
            int len;
            while ((len = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, len);
            }

            //Close the zip entry to write to zip file
            zos.closeEntry();
            //Close resources
            zos.close();
            fis.close();
            fos.close();
            System.out.println(file.getCanonicalPath() + " is zipped to " + zipFileName);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

