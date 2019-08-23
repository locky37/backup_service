package ru.net.ndt.locky37.main

import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.nio.file.*
import java.util.zip.ZipOutputStream

import ru.net.ndt.locky37.zip.Compress
import ru.net.ndt.locky37.socket.*
import kotlin.system.exitProcess
import java.nio.file.Paths
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.attribute.BasicFileAttributes
import java.nio.file.SimpleFileVisitor
import java.time.LocalDate

fun main(args: Array<String>) {

    val sourceDir: Path
    val targetDir: Path

    if (args.isEmpty()) {
        println("Example Copy Dir: java -jar backup_service.jar source... target")
        println("Example Copy Dir with ZIP: java -jar backup_service.jar -c source... target_file")
        println("Example Copy Dir with ZIP: java -jar backup_service.jar -server PORT target_dir")
        println("Example Copy Dir with ZIP: java -jar backup_service.jar -client PORT HOSTNAME source_dir")
        exitProcess(0)
    } else if (args[0] == "-server") {
        val port = Integer.parseInt(args[1])
        println("modification = server")
        val server = ServerZip()
        println(port)

        try {
            while (true) {
                //val server = ServerZip()
                server.serverZip(args[2], port)
                println("Server Copy with compression complete!")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

    } else if (args[0] == "-client") {
        val port = Integer.parseInt(args[1])
        sourceDir = Paths.get(args[3])
        println("modification = client")
        println(port)

        val date = LocalDate.now()

        val temp = System.getProperty("java.io.tmpdir")
        val tempFile = File("${temp}copy\\${sourceDir.fileName}$date.zip")

        if (!tempFile.exists()) {
            makeDir(temp)

            //sourceDir = Paths.get(args[3])
            targetDir = Paths.get("${temp}copy\\${sourceDir.fileName}$date.zip")
            val fileToZip = File(sourceDir.toString())
            val fos = FileOutputStream(targetDir.toString())
            val zipOut = ZipOutputStream(fos)

            val zipFiles = Compress()
            zipFiles.zipFolder(fileToZip, fileToZip.name, zipOut)
            zipOut.close()
            fos.close()
            println("Compression complete!")
        }

        val client = ClientZip()
        try {
            client.clientZip(port, args[2], "${temp}copy\\")
            deleteDir(Paths.get("${temp}copy\\"))
            println("Client Copy with compression complete!")
        } catch (e: ArrayIndexOutOfBoundsException) {
            println("Please enter hostname")
        }

    } else if (args[0] == "-c") {
        println("modification = c")
        //String sourceFile = "zipTest";
        sourceDir = Paths.get(args[1])
        targetDir = Paths.get(args[2])
        val fileToZip = File(sourceDir.toString())
        val fos = FileOutputStream(targetDir.toString())
        val zipOut = ZipOutputStream(fos)

        val zipFiles = Compress()
        zipFiles.zipFolder(fileToZip, fileToZip.name, zipOut)
        zipOut.close()
        fos.close()
        println("Copy with compression complete!")
    } else if (args[0].isNotEmpty() && args[1].isNotEmpty()) {
        sourceDir = Paths.get(args[0])
        targetDir = Paths.get(args[1])
        Copy(sourceDir, targetDir)
        //Files.walkFileTree(sourceDir, new Main(sourceDir, targetDir));
        println("Copy complete!")
    }
}

fun deleteDir(getDir: Path) {
    Files.walkFileTree(getDir, object : SimpleFileVisitor<Path>() {
        override fun postVisitDirectory(
                dir: Path, exc: IOException?): FileVisitResult {
            Files.delete(dir)
            return FileVisitResult.CONTINUE
        }

        override fun visitFile(
                file: Path, attrs: BasicFileAttributes): FileVisitResult {
            Files.delete(file)
            return FileVisitResult.CONTINUE
        }
    })
}

fun makeDir(temp: String) {
    val mkdir = File("${temp}copy")
    if (!mkdir.exists()) {
        if (mkdir.mkdir()) {
            println("Directory is created!")
        } else {
            println("Failed to create directory!")
        }
    }
}
