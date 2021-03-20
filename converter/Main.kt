package converter

fun main() {
    print("Enter number in decimal system: ")
    val num = readLine()!!.toInt()
    print("Enter target base: ")
    val base = readLine()!!.toInt()
    println("Conversion result: ${convertDecimal(num, base)}")
}

fun convertDecimal(decimal: Int, base: Int): String {
    val digits = "0123456789ABCDEF"
    var conversion = ""
    var quotient = decimal

    while (quotient > 0) {
        val remainder = quotient % base
        quotient /= base
        conversion += digits[remainder]
    }

    return conversion.reversed()
}
