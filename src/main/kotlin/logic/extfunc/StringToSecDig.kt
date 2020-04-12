package logic.extfunc

fun String.toSecDigit(): String {
    var result = ""
    for (ind in 0 .. ((this.length - 1) / 3)){
        val startIndex = ind * 3
        val endIndex = if ((startIndex + 3)<this.length) (startIndex + 3) else this.length
        result = "${this.subSequence(this.length - startIndex,this.length - endIndex)}." + result
    }
    result = result.subSequence(0, result.length - 1).toString()
    return result
}