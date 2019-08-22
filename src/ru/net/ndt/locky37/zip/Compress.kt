package ru.net.ndt.locky37.zip

import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class Compress {

    @Throws(IOException::class)
    fun zipFolder(fileToZip: File, fileName: String, zipFileSave: ZipOutputStream) {
        if (fileToZip.isHidden) {
            return
        }
        if (fileToZip.isDirectory) {
            if (fileName.endsWith("/")) {
                zipFileSave.putNextEntry(ZipEntry(fileName))
                zipFileSave.closeEntry()
            } else {
                zipFileSave.putNextEntry(ZipEntry("$fileName/"))
                zipFileSave.closeEntry()
            }
            val children = fileToZip.listFiles()
            for (childFile in children!!) {
                zipFolder(childFile, "$fileName/${childFile.name}", zipFileSave)
            }
            return
        }

        val fileInput = FileInputStream(fileToZip)
        val fileBuffer = BufferedInputStream(fileInput)

        val zipEntry = ZipEntry(fileName)
        zipFileSave.putNextEntry(zipEntry)

        fileBuffer.copyTo(zipFileSave)


/*        val bytes = ByteArray(10000)
        var length: Int
        while ((length = fis.read(bytes)) >= 0) {
            zipFileSave.write(bytes, 0, length)
        }*/
        fileInput.close()
    }
}
