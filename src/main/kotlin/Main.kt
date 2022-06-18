import pir.PirFileConverter
import java.util.Scanner

fun main() {
    val scanner = Scanner(System.`in`)
    println("Enter the directory that contains the PIR files:")
    val inputDirectory = scanner.nextLine()
    println("Save as .txt (enter 'y') instead of CSV (default, press 'n' or enter)?")
    val isSaveAsCsv: Boolean
    while (true) {
        val asCsvInput = scanner.nextLine()
        if (asCsvInput !in setOf("", "y", "n"))
            println("Wrong input. Enter 'n' for CSV, 'y' for .txt")
        else {
            isSaveAsCsv = asCsvInput != "y"
            break
        }
    }

    println("Program arguments:\n  - Directory: $inputDirectory\n  - Save as .txt?: $isSaveAsCsv")

    PirFileConverter.runConversion(inputDirectory, isSaveAsCsv)
}
