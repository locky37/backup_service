package socket

import java.io.*
import java.io.DataInputStream
import java.net.Socket

class ClientHandler(private val dirDst: String, private var dataInputStream: DataInputStream): Thread() {

    //@Throws(IOException::class)
    override fun run() {

        ///println(Thread.currentThread().name)

        val filesCount = dataInputStream.readInt()
        val files = arrayOfNulls<File>(filesCount)

        for (i in 0 until filesCount) {
            val fileLength = dataInputStream.readLong()
            val fileName = dataInputStream.readUTF()

           ////println("Copy File: $fileName")

            files[i] = File("$dirDst/$fileName")

            val fileOutputStream = FileOutputStream(files[i])
            val bufferedOutputStream = BufferedOutputStream(fileOutputStream)

            for (j in 0 until fileLength) bufferedOutputStream.write(dataInputStream.read())

            bufferedOutputStream.close()
        }
        dataInputStream.close()
    }
}