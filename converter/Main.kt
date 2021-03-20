package converter

const val digits = "0123456789ABCDEF"

fun main() {
    var command = ""

    while (command != "/exit") {
        print("Do you want to convert /from decimal or /to decimal? (To quit type /exit) ")
        command = readLine()!!
        if (command == "/from") {
            fromDecimal()
        }
        if (command == "/to") {
            toDecimal()
        }
    }
}

fun fromDecimal() {
    val num = promptForInput("Enter a number in decimal system: ").toInt()
    val base = promptForInput("Enter the target base: ").toInt()
    println("Conversion result: ${convertDecimal(num, base)}")
}

fun toDecimal() {
    val num = promptForInput("Enter source number: ").toUpperCase()
    val base = promptForInput("Enter source base: ").toInt()
    println("Conversion to decimal result: ${convertNonDecimal(num, base)}")
}

fun promptForInput(msg: String) : String {
    print(msg)
    return readLine()!!
}

/**
 * Perform the conversion from decimal to the requested number base (radix)
 */
fun convertDecimal(decimal: Int, base: Int): String {
    var conversion = ""
    var quotient = decimal

    while (quotient > 0) {
        val remainder = quotient % base
        quotient /= base
        conversion += digits[remainder]
    }

    return conversion.reversed()
}

/**
 * Perform the conversion from the supplied base back to decimal
 */
fun convertNonDecimal(num: String, base: Int): Int {
    var conversion = 0
    var value = 1

    for (index in num.lastIndex downTo 0) {
        conversion += digits.indexOf(num[index]) * value
        value *= base
    }

    return conversion
}