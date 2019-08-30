package ru.net.ndt.locky37.zip

import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.lang.Exception
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class Compress() {

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
            val listFiles: ArrayList<File> = ArrayList()

            for (childFile in children!!) {
                if (childFile.toString().endsWith(".1cl")
                    || (childFile.toString().endsWith(".1CL"))
                    || (childFile.toString().contains("Cv8FTxt"))
                    || (childFile.toString().contains("1CHelpIndex"))
                    || (childFile.toString().contains("1Cv8JobScheduler"))
                )
                else listFiles.add(childFile)
            }

            println("Num file: ${listFiles.size}")

            for (childFile in listFiles) {
                println(childFile)
                zipFolder(childFile, "$fileName/${childFile.name}", zipFileSave)
            }
            return
        }

        val fileInput = FileInputStream(fileToZip)
        val fileBuffer = BufferedInputStream(fileInput)

        val zipEntry = ZipEntry(fileName)
        zipFileSave.putNextEntry(zipEntry)

        try {
            fileBuffer.copyTo(zipFileSave)
        } catch (e: Exception) {
            println("File LOCK")
        } finally {

        }
/*        val bytes = ByteArray(10000)
        var length: Int
        while ((length = fis.read(bytes)) >= 0) {
            zipFileSave.write(bytes, 0, length)
        }*/
        fileInput.close()
    }
}
