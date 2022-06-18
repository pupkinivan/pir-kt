package pir

import com.google.common.io.LittleEndianDataInputStream
import util.FileUtils
import util.CsvData
import java.io.File
import java.io.FileInputStream
import java.nio.file.Path
import kotlin.io.path.absolutePathString

data class Pir(
    val uintVersion: UInt,
    val reserved1: Int,
    val reserved2: Int,
    val sampleRate: Int,
    val inputDevice: Int,
    val deviceSensitivity: Float,
    val measurementType: Int,
    val averagingType: Int,
    val numberOfAverages: Int,
    val betaFiltered: Int,
    val generatorType: Int,
    val generatorSubtype: Int,
    val peakLeft: Float,
    val peakRight: Float,
    val pirData: List<Float>,
    val infoText: String,
    val cursorPosition: Int?,
    val markerPosition: Int?,
) {
    private val csvHeaders = listOf("Time [s]", "Amplitude [eV]")

    companion object {
        fun of(file: File): Pir {
            val dataInputStream = LittleEndianDataInputStream(FileInputStream(file))

            val pirHeader = dataInputStream.readNBytes(4)
            val uintVersion = dataInputStream.readInt().toUInt()
            val infoSize = dataInputStream.readInt()
            val reserved1 = dataInputStream.readInt()
            val reserved2 = dataInputStream.readInt()
            val sampleRateFloat = dataInputStream.readFloat()
            val sampleRateInt = dataInputStream.readInt()
            val pirLength = dataInputStream.readInt()
            val inputDevice = dataInputStream.readInt()
            val deviceSensitivity = dataInputStream.readFloat()
            val measurementType = dataInputStream.readInt()
            val averagingType = dataInputStream.readInt()
            val numberOfAverages = dataInputStream.readInt()
            val bfiltered = dataInputStream.readInt()
            val generatorType = dataInputStream.readInt()
            val peakLeft = dataInputStream.readFloat()
            val peakRight = dataInputStream.readFloat()
            val generatorSubtype = dataInputStream.readInt()

            var cursorPosition: Int? = null
            var markerPosition: Int? = null
            var reserved3: Int? = null
            var reserved4: Int? = null
            val pirData = MutableList(pirLength) { 0.0f }
            val infoTextBuilder = StringBuilder(infoSize)

            if (uintVersion >= 5u) {
                cursorPosition = dataInputStream.readInt()
                markerPosition = dataInputStream.readInt()
            } else {
                reserved3 = dataInputStream.readInt()
                reserved4 = dataInputStream.readInt()
            }

            for (i in 0 until pirLength) {
                pirData[i] = dataInputStream.readFloat()
            }
            for (i in 0 until infoSize) {
                infoTextBuilder.append(dataInputStream.readChar())
            }

            return Pir(
                uintVersion,
                reserved1,
                reserved2,
                sampleRateInt,
                inputDevice,
                deviceSensitivity,
                measurementType,
                averagingType,
                numberOfAverages,
                bfiltered,
                generatorType,
                generatorSubtype,
                peakLeft,
                peakRight,
                pirData,
                infoTextBuilder.toString(),
                cursorPosition,
                markerPosition,
            )
        }
    }

    private fun getPirDataAsAsciiList() = pirData
        .asSequence()
        .map { it.toString() }
        .toList()

    fun saveToCsv(outputFileName: String) {
        println("Output file: $outputFileName")
        val outputFile = FileUtils.openFile(outputFileName, overwrite = true)

        val pirDataString = getPirDataAsAsciiList()
        val timeDataString: List<String> = List(pirData.size) { n -> (n * 1f / sampleRate).toString() }

        FileUtils.writeToCsv(
            CsvData(csvHeaders, timeDataString, pirDataString),
            outputFile
        )
    }

    fun saveToCsv(outputPath: Path) {
        println("Output file: ${outputPath.absolutePathString()}")

        val pirDataString = getPirDataAsAsciiList()
        val timeDataString: List<String> = List(pirData.size) { n -> (n * 1f / sampleRate).toString() }
        FileUtils.writeToCsv(
            CsvData(csvHeaders, timeDataString, pirDataString),
            outputPath.toFile()
        )
    }

    fun saveToTxt(outputFileName: String) {
        val outputFile = FileUtils.openFile(outputFileName, overwrite = true)
        for (sample in pirData) outputFile.writeText("$sample\n")
    }

    fun saveToTxt(outputPath: Path) {
        val outputFile = FileUtils.openFile(outputPath.toString(), overwrite = true)
        for (sample in pirData) outputFile.writeText("$sample\n")
    }
}