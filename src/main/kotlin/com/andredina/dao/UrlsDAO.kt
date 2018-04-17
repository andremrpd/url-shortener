package com.andredina.dao

import com.andredina.model.ShortURL
import org.sql2o.Sql2o
import java.math.BigInteger
import java.net.URL
import javax.inject.Inject

interface UrlsDAO {
    fun insert(url: URL): Long
    fun update(shortUrl: ShortURL)
    fun gerURL(code: String): String?
}

class MySQLUrlsDAO @Inject constructor(private val sql: Sql2o) : UrlsDAO {

    private val TABLE= "urls"
    private val ID = "id"
    private val CODE = "code"
    private val ADDRESS = "address"

    override fun insert(url: URL): Long {
        val con = sql.open()
        val sql = "INSERT INTO $TABLE($ADDRESS) VALUES (:address)"
        val id: BigInteger = con.createQuery(sql, true)
                .addParameter("address", url.toString())
                .executeUpdate()
                .key as BigInteger

        con.close()
        return id.toLong()
    }

    override fun update(shortUrl: ShortURL){
        val con = sql.open()
        val sql = "UPDATE $TABLE SET $CODE = :code, $ADDRESS = :address WHERE $ID = :id"
        con.createQuery(sql)
                .addParameter("code", shortUrl.code)
                .addParameter("address", shortUrl.address)
                .addParameter("id", shortUrl.id)
                .executeUpdate()
        con.close()
    }

    override fun gerURL(code: String): String?{
        val con = sql.open()
        val sql = "SELECT * FROM $TABLE WHERE $CODE = :code"
        val urls = con.createQuery(sql)
                .addParameter("code", code)
                .executeAndFetch(ShortURL::class.java)
        con.close()
        return if (urls.size > 0) urls[0].address
        else null
    }
}