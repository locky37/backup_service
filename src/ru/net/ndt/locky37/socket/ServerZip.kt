package ru.net.ndt.locky37.socket

import java.io.*
import java.lang.Exception
import java.net.ServerSocket
import java.net.Socket
import kotlin.system.exitProcess
import java.io.DataInputStream


class ServerZip {

    @Throws(IOException::class)
    fun serverZip(dirDst: String, port: Int) {

        val serverSocket: ServerSocket
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
                ////println("A new client is connected : " + socket);

                val bufferedInputStream = BufferedInputStream(socket.getInputStream())
                val dataInputStream = DataInputStream(bufferedInputStream)

                val t = ClientHandler(dirDst, dataInputStream)
                t.start()
            } catch (e: Exception) {
                socket?.close()
            } finally {
                socket?.close()
            }
        }
    }
}

