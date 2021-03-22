package converter

import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode

const val digits = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"

fun main() {
    var command = ""

    while (command != "/exit") {
        command = promptForInput("Enter two numbers in format: {source base} {target base} (To quit type /exit) ")
        if (command != "/exit") {
            val (source, target) = command.split(" ")
            if ((source.toInt() in 2..36) && (target.toInt() in 2..36)) {
                convertMenu(source, target)
            } else {
                println("Please enter two integers between 2 and 36 separated by a space.")
            }
        }
    }
}

fun convertMenu(sourceBase: String, targetBase: String) {
    val source = sourceBase.toInt()
    val target = targetBase.toInt()
    var sourceNumber = ""

    while (sourceNumber != "/back") {
        sourceNumber =
            promptForInput("Enter number in base $source to convert to base $target (To go back type /back) ")
        if (sourceNumber != "/back") {
            convertNumber(sourceNumber, source, target)
        }
    }
}

fun promptForInput(msg: String): String {
    print(msg)
    return readLine()!!.toLowerCase()
}

/**
 * Perform error checking on the source number input to ensure it's valid for the specified base and only has one decimal
 * point.
 */
fun convertNumber(sourceNumber: String, source: Int, target: Int) {
    val parts = sourceNumber.toUpperCase().split(".")

    // ensure the digits are valid for the source radix
    for (index in parts.indices) {
        for (ch in parts[index]) {
            if (ch !in digits.substring(0..source)) {
                println("Source number doesn't match the base specified!")
                return
            }
        }
    }

    // make sure we only have two parts
    if (parts.size > 2) {
        println("Invalid number! Only one decimal point allowed.")
        return
    }

    var integer: String = sourceNumber
    if (source != target) { // only convert if our bases are not the same
        integer = if (parts[0].isNotEmpty()) integerConvertDecimalToRadix(
            integerConvertRadixToDecimal(parts[0], source),
            target
        ) else "0"
        if (parts.size == 2) {
            integer += ".${fractionalConvertDecimalToRadix(fractionalConvertRadixToDecimal(parts[1], source), target)}"
        }
    }
    println("Conversion result: $integer")

}

/**
 * Convert an integer part of a decimal number from a sourceRadix to decimal (radix 10) and return it as a BigInteger.
 */
fun integerConvertRadixToDecimal(integer: String, sourceRadix: Int): BigInteger {
    if (sourceRadix == 10) {            // if our integer is already base 10,
        return integer.toBigInteger()   // just return it as a BigInteger
    }

    var result = BigInteger.ZERO
    var value = BigInteger.ONE

    for (index in integer.lastIndex downTo 0) {
        result += digits.indexOf(integer[index]).toBigInteger() * value
        value *= sourceRadix.toBigInteger()
    }

    return result
}

/**
 * Convert the integer part of a decimal number from decimal (radix 10) to the targetRadix and return it as a String.
 */
fun integerConvertDecimalToRadix(integer: BigInteger, targetRadix: Int): String {
    if (targetRadix == 10) {        // If our target radix is base 10,
        return integer.toString()   // just return the integer as a String
    }

    if (integer == BigInteger.ZERO) {   // If the integer is 0,
        return "0"                      // just return a String of "0"
    }

    var conversion = ""
    var quotient = integer

    while (quotient > BigInteger.ZERO) {
        val (q, r) = quotient.divideAndRemainder(targetRadix.toBigInteger())
        quotient = q
        conversion += digits[r.toInt()]
    }

    return conversion.reversed()
}

/**
 * Perform a conversion of a fractional (to the right of the decimal point) part of a number from the specified base
 * and return it in base 10 as a BigDecimal.
 */
fun fractionalConvertRadixToDecimal(fraction: String, sourceRadix: Int): BigDecimal {
    if (sourceRadix == 10) {
        return ("0.$fraction").toBigDecimal()
    }

    val radixBD = sourceRadix.toBigDecimal()
    var result = BigDecimal.ZERO.setScale(8)

    for (index in fraction.indices) {
        val multiplicand = BigDecimal.ONE.divide(radixBD.pow(index + 1), 8, RoundingMode.HALF_UP)
        result = result.add(multiplicand.multiply(digits.indexOf(fraction[index]).toBigDecimal()))
    }

    return result.setScale(5, RoundingMode.HALF_UP)
}

/**
 * Convert the fractional part (to the right of the decimal point) of a number from base 10 (decimal) to the specified
 * base and return it as a String.
 */
fun fractionalConvertDecimalToRadix(fraction: BigDecimal, targetRadix: Int): String {
    // return a string of five digits if our target radix is already base 10
    if (targetRadix == 10) {
        return ("${fraction}00000").substring(2, 7)
    }

    // return a string of zeroes if our fraction is 0
    if (fraction == BigDecimal.ZERO.setScale(fraction.scale())) {
        return "00000"
    }

    val radixBD = targetRadix.toBigDecimal()
    var conversion = ""
    var quotient = fraction

    while ((quotient != BigDecimal.ZERO.setScale(quotient.scale())) && (conversion.length < 5)) {
        quotient = quotient.multiply(radixBD)
        val (q, r) = quotient.divideAndRemainder(BigDecimal.ONE)
        conversion += digits[q.toInt()]
        quotient = r
    }

    return ("${conversion}00000").substring(0, 5)
}