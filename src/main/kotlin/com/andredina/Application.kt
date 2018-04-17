package com.andredina

import com.andredina.di.DaggerAppComponent
import spark.Route
import spark.Spark.*


fun main(args: Array<String>) {

    val component = DaggerAppComponent.builder().build()
    val controller = component.providesController()
    val transformer = component.providesResponseTransformer()

    port(8080)
    get("/") { _, _ -> "ShortURL Shortener Service 1.0" }
    post("/", Route { req, res -> controller.generate(req, res) } , transformer)
    get("/:code", controller::redirect)

}


