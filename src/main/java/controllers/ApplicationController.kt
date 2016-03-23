package controllers

import com.google.inject.Inject
import com.google.inject.Provider
import ninja.Result
import ninja.Results

import com.google.inject.Singleton
import conf.Routes
import model.Estate
import ninja.params.PathParam


@Singleton
class ApplicationController @Inject constructor (val estateProvider: Provider<Estate>) {

    fun index(): Result {
        return Results.redirect("/PROD")
    }

    fun stage(@PathParam("stage") stage: String): Result {
        val estate = estateProvider.get().filterToStage(stage)
        return Results.html().render(estate)
    }

    fun instance(@PathParam("instanceId") instanceId: String): Result {
        val server = estateProvider.get().findServer(instanceId)
        return Results.json().render(server)
        //return Results.html().render(server)
    }
}
