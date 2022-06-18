package util

import CsvIncoherentNumberOfHeadersAndData
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import java.io.File
import java.io.FileWriter
import kotlin.io.path.Path
import kotlin.io.path.absolutePathString
import kotlin.system.exitProcess

object FileUtils {
    fun scanPirFiles(directory: String) = File(directory).walk()
            .filter { it.isFile and it.endsWith(".pir") }

    fun scanPirFiles(directory: File) = directory.walk()
        .filter { it.isFile and it.name.endsWith(".pir")}

    fun openDirectory(directoryString: String): File {
        val directory = File(Path(directoryString).absolutePathString())
        if (!directory.isDirectory) {
            println("Path $directoryString does not correspond to a directory")
            exitProcess(3)
        } else {
            return directory
        }
    }

    fun openFile(filePath: String, overwrite: Boolean = true): File {
        return File(filePath).let {
            if (it.exists() and overwrite) it.delete()
            it
        }
    }

    fun writeToCsv(csvData: CsvData, outputFile: File) {
        if (csvData.headers.size != csvData.dataArgs.size)
            throw CsvIncoherentNumberOfHeadersAndData("The number of headers does not match the number of data columns")

        val fileWriter = FileWriter(outputFile, false)
        val csvWriter = CSVPrinter(fileWriter, CSVFormat.DEFAULT)

        csvWriter.printRecord(csvData.headers)
        for (i in 0 until csvData.dataArgs[0].size) {
            val row = Array<String>(csvData.dataArgs.size) { "" }
            for ((columnIndex, column) in csvData.dataArgs.withIndex())
                row[columnIndex] = column[i]
            csvWriter.printRecord(*row)
        }
    }
}

class CsvData constructor(val headers: List<String>, vararg val dataArgs: List<String>)
