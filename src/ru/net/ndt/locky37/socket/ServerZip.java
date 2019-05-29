package ru.net.ndt.locky37.socket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerZip {

    public void serverZip(String dirDst) throws IOException {

        //"E:\\Test\\Test2"
        //String dirPath = dir;

        ServerSocket serverSocket = new ServerSocket(3670);
        Socket socket = serverSocket.accept();

        BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
        DataInputStream dis = new DataInputStream(bis);

        int filesCount = dis.readInt();
        File[] files = new File[filesCount];

        for (int i = 0; i < filesCount; i++) {
            long fileLength = dis.readLong();
            String fileName = dis.readUTF();

            System.out.println("Copy File: " + fileName);

            files[i] = new File(dirDst + "/" + fileName);

            FileOutputStream fos = new FileOutputStream(files[i]);
            BufferedOutputStream bos = new BufferedOutputStream(fos);

            for (int j = 0; j < fileLength; j++) bos.write(bis.read());

            bos.close();
            fos.close();
        }
        dis.close();
        bis.close();
        serverSocket.close();
    }
}


