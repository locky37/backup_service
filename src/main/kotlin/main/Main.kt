package main

import Copy
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import socket.Client
import socket.Server
import zip.Compress
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.nio.file.*
import java.util.zip.ZipOutputStream

import java.lang.Exception
import kotlin.system.exitProcess
import java.nio.file.Paths
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.attribute.BasicFileAttributes
import java.nio.file.SimpleFileVisitor
import java.time.LocalDate
import kotlin.math.log


fun main(args: Array<String>) {

    val startTime = System.currentTimeMillis()

    val logger: Logger = LogManager.getLogger("Main")

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
        val server = Server()
        //logger.info("Server running on port:$port")
        //println(port)

        try {
            while (true) {
                //val server = ServerZip()
                server.serverZip(args[2], port)
                logger.info("Server Copy with compression complete!")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

    } else if (args[0] == "-client") {
        val port = Integer.parseInt(args[1])
        sourceDir = Paths.get(args[3])
        logger.info("Start copy")
        //println(port)

        val date = LocalDate.now()
        val time = System.currentTimeMillis()

        val temp = System.getProperty("java.io.tmpdir")
        val tempFile = File("${temp}copy\\${sourceDir.fileName}$time$date.zip")

        if (!tempFile.exists()) {
            makeDir(temp)

            //sourceDir = Paths.get(args[3])
            targetDir = Paths.get("${temp}copy\\${sourceDir.fileName}$time$date.zip")
            val fileToZip = File(sourceDir.toString())
            val fos = FileOutputStream(targetDir.toString())
            val zipOut = ZipOutputStream(fos)

            //val zipFiles = Thread (Compress(fileToZip, fileToZip.name, zipOut));
            //zipFiles.start()

            val zipFiles = Compress()
            zipFiles.zipFolder(fileToZip, fileToZip.name, zipOut)
            zipOut.close()
            fos.close()
            val endTimeF = System.currentTimeMillis()
            logger.info("Time elapsed: ${endTimeF - startTime}")
            logger.info("Compression complete!")
        }

        val client = Client()
        try {
            client.clientZip(port, args[2], "${temp}copy\\")
            deleteDir(Paths.get("${temp}copy\\"))
            logger.info("Client Copy with compression complete!")
            val endTime = System.currentTimeMillis()
            logger.info("Time elapsed: ${endTime - startTime}")
        } catch (e: ArrayIndexOutOfBoundsException) {
            println("Please enter hostname")
        }

    } else if (args[0] == "-c") {
        logger.info("Start simple copy")
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
        logger.info("Simple copy with compression complete!")
    } else if (args[0].isNotEmpty() && args[1].isNotEmpty()) {
        sourceDir = Paths.get(args[0])
        targetDir = Paths.get(args[1])
        Copy(sourceDir, targetDir)
        //Files.walkFileTree(sourceDir, new Main(sourceDir, targetDir));
        logger.info("Copy complete!")
    }
}

fun deleteDir(getDir: Path) {

    val logger: Logger = LogManager.getLogger("Main")

    Files.walkFileTree(getDir, object : SimpleFileVisitor<Path>() {
        override fun postVisitDirectory(
            dir: Path, exc: IOException?
        ): FileVisitResult {
            try {
                Files.delete(dir)
            } catch (e: Exception) {
                logger.info("${e.message}")
            }
            return FileVisitResult.CONTINUE
        }

        override fun visitFile(
            file: Path, attrs: BasicFileAttributes
        ): FileVisitResult {
            try {
                Files.delete(file)
            } catch (e: Exception) {
                logger.info("${e.message}")
            }
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