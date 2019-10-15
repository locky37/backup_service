package socket

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.io.*
import java.lang.Exception
import java.net.InetAddress
import java.net.Socket

class Client {

    @Throws(IOException::class)
    fun clientZip(port: Int, hostName: String, srcDir: String) {

        val logger: Logger = LogManager.getLogger(javaClass.simpleName)
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
                logger.info("File LOCK: ${e.message}")
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
