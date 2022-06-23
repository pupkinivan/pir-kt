import pir.PirFileConverter
import java.util.Scanner

fun main() {
    val scanner = Scanner(System.`in`)
    print("Enter the directory that contains the PIR files:\n> ")
    val inputDirectory = scanner.nextLine()
    print("Save as .txt (enter 'y') instead of CSV (default, press 'n' or enter)?\n> ")
    val isSaveAsCsv: Boolean
    while (true) {
        val asCsvInput = scanner.nextLine()
        if (asCsvInput !in setOf("", "y", "n"))
            print("Wrong input. Enter 'n' for CSV, 'y' for .txt\n> ")
        else {
            isSaveAsCsv = asCsvInput != "y"
            break
        }
    }

    println("Program arguments:\n  - Directory: $inputDirectory\n  - Save as .txt?: $isSaveAsCsv")

    PirFileConverter.runConversion(inputDirectory, isSaveAsCsv)
}
