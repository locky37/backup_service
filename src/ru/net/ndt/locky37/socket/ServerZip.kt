package ru.net.ndt.locky37.socket

import java.io.*
import java.net.ServerSocket
import java.net.Socket
import kotlin.system.exitProcess

class ServerZip {

    /*    @Throws(IOException::class)*/
    fun serverZip(dirDst: String, port: Int) {

        val socket: Socket?
        var bufferedInputStream: BufferedInputStream? = null
        var dataInputStream: DataInputStream? = null

        var fileOutputStream: FileOutputStream
        var bufferedOutputStream: BufferedOutputStream?

        var serverSocket: ServerSocket? = null

        try {
            try {
                serverSocket = ServerSocket(port)
            } catch (e: IOException) {
                println("Address use")
                exitProcess(0)
            }

            //ServerSocket serverSocket = new ServerSocket(port);
            socket = serverSocket.accept()

            bufferedInputStream = BufferedInputStream(socket.getInputStream())
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
                }
            }
        } finally {
            bufferedInputStream?.close()
            dataInputStream?.close()
            serverSocket?.close()
        }
    }
}


