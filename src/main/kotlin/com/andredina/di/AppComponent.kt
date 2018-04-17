package com.andredina.di

import com.andredina.controller.Controller
import dagger.Component
import spark.ResponseTransformer
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    fun providesController(): Controller
    fun providesResponseTransformer(): ResponseTransformer

}