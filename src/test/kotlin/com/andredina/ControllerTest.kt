package com.andredina

import com.andredina.controller.Controller
import com.andredina.dao.UrlsDAO
import com.andredina.model.ErrorOut
import com.andredina.model.URLOut
import com.andredina.util.URLGenerator
import org.junit.Test
import org.mockito.Mockito
import spark.Request
import spark.Response
import java.net.MalformedURLException
import java.net.URL
import kotlin.test.assertEquals

class ControllerTest {

    inline fun <reified T : Any> mock() = Mockito.mock(T::class.java)

    val dao =  mock<UrlsDAO>()
    val request = mock<Request>()
    val response = mock<Response>()
    val urlGenerator = URLGenerator()
    val controller: Controller = Controller(dao, urlGenerator)

    @Test
    fun generate_success() {
        val address = "http://www.google.com"
        val url = URL(address)

        Mockito.`when`(request.queryParams("address")).thenReturn(address)
        Mockito.`when`(dao.insert(url)).thenReturn(100L)

        assertEquals(URLOut("http://localhost:8080/bM"), controller.generate(request, response))
    }

    @Test(expected = MalformedURLException::class)
    fun generate_invalid_address() {
        val address = "google"
        val url = URL(address)

        Mockito.`when`(request.queryParams("address")).thenReturn(address)
        Mockito.`when`(dao.insert(url)).thenReturn(100L)

        assertEquals(ErrorOut("Invalid Address"), controller.generate(request, response))
        Mockito.verify(response, Mockito.times(1)).status(500)
    }

    @Test
    fun redirect_success() {
        Mockito.`when`(request.params("code")).thenReturn("bM")
        Mockito.`when`(dao.gerURL("bM")).thenReturn("http://google.com")

        controller.redirect(request, response)

        Mockito.verify(response, Mockito.times(1)).redirect("http://google.com")
    }

    @Test
    fun redirect_notfound(){
        Mockito.`when`(request.params("code")).thenReturn("bM")
        Mockito.`when`(dao.gerURL("bM")).thenReturn(null)

        assertEquals("This address does not exist", controller.redirect(request, response))
        Mockito.verify(response, Mockito.times(1)).status(500)

    }

}