package ru.net.ndt.locky37.socket

import java.io.*
import java.lang.Exception
import java.net.ServerSocket
import java.net.Socket
import kotlin.system.exitProcess
import java.io.DataOutputStream
import java.io.DataInputStream


class ServerZip {

    /*    @Throws(IOException::class)*/
    fun serverZip(dirDst: String, port: Int) {

        var serverSocket: ServerSocket? = null
        var socket: Socket? = null

        try {
            serverSocket = ServerSocket(port)
        } catch (e: IOException) {
            println("Address use")
            exitProcess(0)
        }

        while (true) {
            try {
                socket = serverSocket.accept()
                println("A new client is connected : " + socket);

                // obtaining input and out streams
                //val dataInputStream = DataInputStream(socket.getInputStream())
                val bufferedInputStream = BufferedInputStream(socket.getInputStream())
                val dataInputStream = DataInputStream(bufferedInputStream)
                //val dataOutputStream = DataOutputStream(socket.getOutputStream())

                println("Assigning new thread for this client")

                // create a new thread object
                val t = ClientHandler(socket, dirDst, dataInputStream)

                // Invoking the start() method
                t.start()
                t.join()

            } catch (e: Exception) {
                socket?.close()
            }
        }

/*        val socket: Socket?
        var bufferedInputStream: BufferedInputStream? = null
        var dataInputStream: DataInputStream? = null

        var fileOutputStream: FileOutputStream
        var bufferedOutputStream: BufferedOutputStream?

        var serverSocket: ServerSocket? = null*/

/*        try {
            try {
                serverSocket = ServerSocket(port)
            } catch (e: IOException) {
                println("Address use")
                exitProcess(0)
            }*/

        //ServerSocket serverSocket = new ServerSocket(port);
        //socket = serverSocket.accept()

/*            bufferedInputStream = BufferedInputStream(socket.getInputStream())
            dataInputStream = DataInputStream(bufferedInputStream)

            val filesCount = dataInputStream.readInt()
            val files = arrayOfNulls<File>(filesCount)

            for (i in 0 until filesCount) {
                val fileLength = dataInputStream.readLong()
                val fileName = dataInputStream.readUTF()

                println("Copy File: $fileName")

                files[i] = File("$dirDst/$fileName")

                fileOutputStream = FileOutputStream(files[i])
                bufferedOutputStream = BufferedOutputStream(fileOutputStream)

                for (j in 0 until fileLength) bufferedOutputStream.write(dataInputStream.read())

                bufferedOutputStream.close()

                if (socket.keepAlive) {
                    bufferedOutputStream.close()
                    fileOutputStream.close()
                    serverSocket.close()
                }*/
/*            }
        } finally {*/
/*            bufferedInputStream?.close()
            dataInputStream?.close()
            serverSocket?.close()*/
    }
//    }
}

