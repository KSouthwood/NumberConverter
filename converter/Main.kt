package converter

import java.math.BigInteger

const val digits = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"

fun main() {
    var command = ""

    while (command != "/exit") {
        command = promptForInput("Enter two numbers in format: {source base} {target base} (To quit type /exit) ")
        if (command != "/exit") {
            val (source, target) = command.split(" ")
            convertMenu(source, target)
        }
    }
}

fun convertMenu(sourceBase: String, targetBase: String) {
    val source = sourceBase.toInt()
    val target = targetBase.toInt()
    var sourceNumber = ""

    while (sourceNumber != "/back") {
        sourceNumber = promptForInput("Enter number in base $source to convert to base $target (To go back type /back) ")
        if (sourceNumber != "/back") {
            if (source != 10) {
                println("Conversion result: ${convertDecimal(convertNonDecimal(sourceNumber.toUpperCase(), source), target)}")
            } else {
                println("Conversion result: ${convertDecimal(sourceNumber.toBigInteger(), target)}")
            }
        }
    }
}

fun promptForInput(msg: String) : String {
    print(msg)
    return readLine()!!
}

/**
 * Perform the conversion from decimal to the requested number base (radix)
 */
fun convertDecimal(decimal: BigInteger, base: Int): String {
    var conversion = ""
    var quotient = decimal

    while (quotient > BigInteger.ZERO) {
        val (q, r) = quotient.divideAndRemainder(base.toBigInteger())
        quotient = q
        conversion += digits[r.toInt()]
    }

    return conversion.reversed()
}

/**
 * Perform the conversion from the supplied base back to decimal using BigInteger Java class
 */
fun convertNonDecimal(num: String, base: Int): BigInteger {
    var result = BigInteger.ZERO
    var value = BigInteger.ONE
    val bigIntBase = base.toBigInteger()

    for (index in num.lastIndex downTo 0) {
        result += digits.indexOf(num[index]).toBigInteger() * value
        value *= bigIntBase
    }
    return result
}