package socket

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.io.*
import java.lang.Exception
import java.net.ServerSocket
import java.net.Socket
import kotlin.system.exitProcess
import java.io.DataInputStream


class Server {

    val logger: Logger = LogManager.getLogger(javaClass.simpleName)

    @Throws(IOException::class)
    fun serverZip(dirDst: String, port: Int) {

        val serverSocket: ServerSocket

        try {
            serverSocket = ServerSocket(port)
            logger.info("Server running on port:$port")
        } catch (e: IOException) {
            logger.info("Address uses")
            exitProcess(0)
        }

        while (true) {

            var socket: Socket? = null

            try {
                socket = serverSocket.accept()
                logger.info("A new client is connected : " + socket)

                val bufferedInputStream = BufferedInputStream(socket.getInputStream())
                val dataInputStream = DataInputStream(bufferedInputStream)

                val t = ClientHandler(dirDst, dataInputStream)
                t.start()
            } catch (e: Exception) {
                socket?.close()
            }
        }
    }
}

