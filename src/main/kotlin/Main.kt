import org.apache.commons.cli.DefaultParser
import org.apache.commons.cli.Option
import org.apache.commons.cli.Options
import pir.PirFileConverter

fun main(args: Array<String>) {
    val cliOptions = Options()
        .addRequiredOption("d", "directory", true, "Directory from which to read the PIR files (-d, --directory)")
        .addOption(Option("t", "txt", false, "Export to .txt instead of CSV (-t, --txt)"))

    val parser = DefaultParser()
    val commandLine = parser.parse(cliOptions, args)

    println("Program arguments: ${args.joinToString()}")
    /*if (args.isEmpty()) {
        println("Not enough arguments, at least one is expected for the directory of the PIR files")
        exitProcess(1)
    }
    else if (args.size > 2) {
        println("Too many arguments")
        exitProcess(2)
    }*/

    //val directory = args[0]
    val directory = commandLine.getOptionValue("directory")
    //val isSaveAsCsv = if (args.size == 2) args[1] == "--csv" else false
    val isSaveAsCsv = !(commandLine.hasOption("txt"))

    PirFileConverter.runConversion(directory, isSaveAsCsv)
}
