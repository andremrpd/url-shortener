package com.andredina.util

import com.google.gson.Gson
import spark.ResponseTransformer
import javax.inject.Inject

class GsonTransformer @Inject constructor(private val gson: Gson): ResponseTransformer {

    override fun render(model: Any?): String = gson.toJson(model)

}