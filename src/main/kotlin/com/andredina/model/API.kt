package com.andredina.model

data class URLIn(var address: String)

abstract class Out
data class URLOut(var generatedAddress: String): Out()
data class ErrorOut(var erroMsg: String): Out()