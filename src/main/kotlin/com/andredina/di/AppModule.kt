package com.andredina.di

import com.andredina.*
import com.andredina.controller.Controller
import com.andredina.dao.MySQLUrlsDAO
import com.andredina.dao.UrlsDAO
import com.andredina.util.GsonTransformer
import com.andredina.util.URLGenerator
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import org.sql2o.Sql2o
import spark.ResponseTransformer
import javax.inject.Singleton

@Module class AppModule {

    @Singleton
    @Provides fun providesController(urlsDAO: UrlsDAO) = Controller(urlsDAO)

    @Singleton
    @Provides fun providesUrlsDAO(sql2o: Sql2o, urlGenerator: URLGenerator): UrlsDAO = MySQLUrlsDAO(sql2o, urlGenerator)

    @Singleton
    @Provides fun providesURLGenerator() = URLGenerator()

    @Singleton
    @Provides fun providesSql2o() = Sql2o("jdbc:mysql://$MYSQL_SERVER/$MYSQL_DATABASE", MYSQL_USER, MYSQL_PASS)

    @Provides fun providesGson() = GsonBuilder().setPrettyPrinting().create()

    @Provides fun providesResponseTransformer(gsonTransformer: GsonTransformer): ResponseTransformer = gsonTransformer
}