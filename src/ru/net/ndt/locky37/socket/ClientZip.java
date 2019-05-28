package ru.net.ndt.locky37.socket;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class ClientZip {

    public void clientZip(String hostName, String srcDir) throws IOException {
        String directory = srcDir;
        String hostDomain = hostName;
        int port = 1234;

        File[] files = new File(directory).listFiles();

        Socket socket = new Socket(InetAddress.getByName(hostDomain), port);

        BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());
        DataOutputStream dos = new DataOutputStream(bos);

        dos.writeInt(files != null ? files.length : 0);

        for(File file : files != null ? files : new File[0])
        {
            long length = file.length();
            dos.writeLong(length);

            String name = file.getName();
            dos.writeUTF(name);

            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);

            int theByte = 0;
            while((theByte = bis.read()) != -1) bos.write(theByte);

            bis.close();
        }

        dos.close();
    }
}
