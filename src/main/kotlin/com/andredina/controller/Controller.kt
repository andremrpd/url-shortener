package com.andredina.controller

import com.andredina.ROOT_URL
import com.andredina.dao.UrlsDAO
import com.andredina.model.ErrorOut
import com.andredina.model.Out
import com.andredina.model.ShortURL
import com.andredina.model.URLOut
import com.andredina.util.CodeNotFoundException
import com.andredina.util.URLGenerator
import spark.Request
import spark.Response
import java.net.MalformedURLException
import java.net.URL
import java.util.logging.Level
import java.util.logging.Logger
import javax.inject.Inject

class Controller @Inject constructor(private val dao: UrlsDAO, private val generator: URLGenerator){

    private val logger = Logger.getLogger(Controller::class.java.simpleName)

    fun generate(request: Request, response: Response): Out{

        return try {
            val address = URL(request.queryParams("address"))
            val id = dao.insert(address)
            val code = generator.encode(id)
            dao.update(ShortURL(id, code, address.toString()))

            val generatedUrl = ROOT_URL.plus(code)
            logger.info("ShortURL Generated: $generatedUrl")

            URLOut(generatedUrl)

        }catch(e: MalformedURLException){
            response.status(500)

            ErrorOut("Invalid Address")

        }catch(e: Exception){
            logger.log(Level.SEVERE, e.localizedMessage, e)
            response.status(500)

            ErrorOut(e.localizedMessage)
        }
    }

    fun redirect(request: Request, response: Response): String?{

        return try {
            val code = request.params("code")
            val address = dao.gerURL(code)

            if (address == null || address.isBlank()) throw CodeNotFoundException()

            response.redirect(address)
            null

        }catch (e: Exception){
            logger.log(Level.SEVERE, e.localizedMessage, e)
            response.status(500)

            e.localizedMessage
        }

    }

}