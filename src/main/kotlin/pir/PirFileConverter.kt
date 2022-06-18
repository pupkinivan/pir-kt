package pir

import util.FileUtils
import java.nio.file.Paths
import kotlin.io.path.Path
import kotlin.io.path.name

class PirFileConverter {
    companion object {
        @JvmStatic
        fun runConversion(directoryString: String, asCsv: Boolean = false): Int =
            FileUtils.openDirectory(directoryString)
                .let { FileUtils.scanPirFiles(it).also { printSequence(it) } }
                .map { Pair(it, Pir.of(it)) }
                .map {
                    println("Converting file ${it.first.name}")
                    it.also {
                        val buildOutputFileName = { extension: String ->
                            Paths.get(directoryString, it.first.name.substringBefore('.').plus(extension))
                        }
                        if (asCsv) it.second.saveToCsv(buildOutputFileName(".csv"))
                        else it.second.saveToTxt(buildOutputFileName(".txt"))
                        println("Converted file ${it.first.name} to ${if (asCsv) "csv" else "txt"}")
                    }
                }
                .count()

        private inline fun <reified T> printSequence(sequence: Sequence<T>) {
            println("In sequence of type ${T::class.simpleName} found:")
            for (element in sequence) println("  - $element")
        }
    }
}