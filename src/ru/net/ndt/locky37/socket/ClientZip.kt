package ru.net.ndt.locky37.socket

import java.io.*
import java.lang.Exception
import java.net.InetAddress
import java.net.Socket

class ClientZip {

    @Throws(IOException::class)
    fun clientZip(port: Int, hostName: String, srcDir: String) {
        // int port = 3670;

        val files = File(srcDir).listFiles()

        val socket = Socket(InetAddress.getByName(hostName), port)

        val bufferedOutputStream = BufferedOutputStream(socket.getOutputStream())
        val dataInputStream = DataOutputStream(bufferedOutputStream)

        dataInputStream.writeInt(files?.size ?: 0)

        for (file in files ?: arrayOfNulls(0)) {
            val length = file.length()
            dataInputStream.writeLong(length)

            val name = file.name
            dataInputStream.writeUTF(name)

            val fileInputStream = FileInputStream(file)
            val bufferedInputStream = BufferedInputStream(fileInputStream)

            //bufferedInputStream.copyTo(bufferedOutputStream)
            try {
                bufferedInputStream.copyTo(bufferedOutputStream)
            } catch (e: Exception) {
                println("File LOCK")
            }
            finally {
                bufferedInputStream.close()
                fileInputStream.close()

/*            while (bufferedInputStream.read() != 0) {
                val theByte = bufferedInputStream.read()
                bufferedOutputStream.write(theByte)*/
            }

/*            bufferedInputStream.close()
            fileInputStream.close()*/
        }

        bufferedOutputStream.close()
        dataInputStream.close()
        socket.close()
    }
}
