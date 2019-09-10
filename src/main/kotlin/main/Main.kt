package main

import Copy
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.stream.MalformedJsonException
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import socket.Client
import socket.Server
import zip.Compress
import java.io.*
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


fun main(args: Array<String>) {

    val configFile = jsonReader()

    val startTime = System.currentTimeMillis()

    val logger: Logger = LogManager.getLogger("Main")

    val sourceDir: Path
    val targetDir: Path

    if (args.isEmpty() && configFile.dir == "C:/example") {
        val fileContent = {}::class.java.getResource("/doc/README.MD").readText()
        println(fileContent)
/*        println("Example Copy Dir: java -jar backup_service.jar source... target")
        println("Example Copy Dir with ZIP: java -jar backup_service.jar -c source... target_file")
        println("Example Copy Dir with ZIP: java -jar backup_service.jar -server PORT target_dir")
        println("Example Copy Dir with ZIP: java -jar backup_service.jar -client PORT HOSTNAME source_dir")
        println("OR Change default conf.json")*/
        logger.info(configFile.dir)
        exitProcess(0)
    } else if (args.isNotEmpty() && args[0] == "-server" || configFile.type == "Server") {
        val port: Int = if (args.isNotEmpty()) {
            Integer.parseInt(args[1])
        } else {
            configFile.port
        }

        val server = Server()

        try {
            while (true) {
                if (args.isNotEmpty()) {
                    server.serverZip(args[2], port)
                } else {
                    server.serverZip(configFile.dir, port)
                }
                logger.info("Server Copy with compression complete!")
            }
        } catch (e: IOException) {
            logger.error(e.message)
        }

    } else if (args.isNotEmpty() && args[0] == "-client" || configFile.type == "Client") {
        val port: Int = if (args.isNotEmpty()) {
            Integer.parseInt(args[1])
        } else {
            configFile.port
        }

        sourceDir = if (args.isNotEmpty()) {
            Paths.get(args[3])
        } else {
            Paths.get(configFile.dir)
        }

        logger.info("Start copy")

        val date = LocalDate.now()
        val time = System.currentTimeMillis()

        val temp = System.getProperty("java.io.tmpdir")
        val tempFile = File("${temp}copy\\${sourceDir.fileName}$time$date.zip")

        if (!tempFile.exists()) {
            makeDir(temp)

            targetDir = Paths.get("${temp}copy\\${sourceDir.fileName}$time$date.zip")
            val fileToZip = File(sourceDir.toString())
            val fos = FileOutputStream(targetDir.toString())
            val zipOut = ZipOutputStream(fos)

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
            val host: String = if (args.isNotEmpty()) {
                args[2]
            } else {
                configFile.hostname
            }
            client.clientZip(port, host, "${temp}copy\\")
            deleteDir(Paths.get("${temp}copy\\"))
            logger.info("Client Copy with compression complete!")
            val endTime = System.currentTimeMillis()
            logger.info("Time elapsed: ${endTime - startTime}")
        } catch (e: ArrayIndexOutOfBoundsException) {
            println("Please enter hostname")
        }

    } else if (args.isNotEmpty() && args[0] == "-c") {
        logger.info("Start simple copy")
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
    } else if (args.isNotEmpty() && args[0].isNotEmpty() && args[1].isNotEmpty()) {
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

    val logger: Logger = LogManager.getLogger("MakeDir")

    val mkdir = File("${temp}copy")
    if (!mkdir.exists()) {
        if (mkdir.mkdir()) {
            logger.info("Directory is created!")
        } else {
            logger.info("Failed to create directory!")
        }
    }
}

fun jsonReader(): Config {

    val logger: Logger = LogManager.getLogger("Main")

    val configFilePath = "./conf.json"

    var readFile: BufferedReader
    val writeFile: BufferedWriter
    var jsonRead: Config?
    val gson = Gson()

    try {
        readFile = BufferedReader(FileReader(configFilePath))
        jsonRead = gson.fromJson(readFile, Config::class.java)
        logger.info("Read file: conf.json")
        readFile.close()
        return Config(jsonRead.type, jsonRead.port, jsonRead.hostname, jsonRead.dir)
    } catch (e: MalformedJsonException) {
        logger.error(e.message)
        exitProcess(0)
    } catch (e: JsonSyntaxException) {
        logger.error(e.message)
        exitProcess(0)
    } catch (e: FileNotFoundException) {
        logger.error(e.message)

        writeFile = BufferedWriter(FileWriter(configFilePath))
        val jsonWrite = gson.toJson(Config())
        writeFile.write(jsonWrite)
        writeFile.close()

        readFile = BufferedReader(FileReader(configFilePath))
        jsonRead = gson.fromJson(readFile, Config::class.java)
        logger.info("Create default file: conf.json")
        readFile.close()
        return Config(jsonRead.type, jsonRead.port, jsonRead.hostname, jsonRead.dir)
    }
}