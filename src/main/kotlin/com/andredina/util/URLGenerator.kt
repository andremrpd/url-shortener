package com.andredina.util

class URLGenerator {

    val ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
    val BASE = ALPHABET.length

    fun encode(id: Long): String {
        var num = id
        val str = StringBuilder()

        while (num > 0) {
            str.insert(0, ALPHABET[(num % BASE).toInt()])
            num /= BASE
        }
        return str.toString()
    }

    fun decode(str: String): Long {
        var num = 0L
        for (i in 0 until str.length) {
            num = num * BASE + ALPHABET.indexOf(str[i])
        }
        return num
    }

}