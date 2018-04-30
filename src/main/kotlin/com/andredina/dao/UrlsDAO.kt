package com.andredina.dao

import com.andredina.model.ShortURL
import com.andredina.util.URLGenerator
import org.sql2o.Connection
import org.sql2o.Sql2o
import java.math.BigInteger
import java.net.URL
import javax.inject.Inject

interface UrlsDAO {
    fun create(url: URL): ShortURL
    fun gerURL(code: String): String?
}

class MySQLUrlsDAO @Inject constructor(private val sql: Sql2o, private val generator: URLGenerator) : UrlsDAO {

    private val TABLE= "urls"
    private val ID = "id"
    private val CODE = "code"
    private val ADDRESS = "address"

    override fun create(url: URL): ShortURL{
        sql.beginTransaction().use {
            val id = insert(it, url)
            val code = generator.encode(id)
            val shortURL = ShortURL(id, code, url.toString())
            update(it, shortURL)
            it.commit()
            return  shortURL
        }
    }

    private fun insert(con: Connection, url: URL): Long {
        val sql = "INSERT INTO $TABLE($ADDRESS) VALUES (:address)"
        val id: BigInteger = con.createQuery(sql, true)
                .addParameter("address", url.toString())
                .executeUpdate()
                .key as BigInteger
        return id.toLong()
    }

    private fun update(con: Connection, shortUrl: ShortURL){
        val sql = "UPDATE $TABLE SET $CODE = :code, $ADDRESS = :address WHERE $ID = :id"
        con.createQuery(sql)
                .addParameter("code", shortUrl.code)
                .addParameter("address", shortUrl.address)
                .addParameter("id", shortUrl.id)
                .executeUpdate()
    }

    override fun gerURL(code: String): String?{
        sql.open().use{
            val sql = "SELECT * FROM $TABLE WHERE $CODE = :code"
            val urls = it.createQuery(sql)
                    .addParameter("code", code)
                    .executeAndFetch(ShortURL::class.java)

            return if (urls.size > 0) urls[0].address
            else null
        }
    }
}