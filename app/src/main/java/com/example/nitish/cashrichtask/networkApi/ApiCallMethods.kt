package com.example.nitish.cashrichtask.networkApi

import com.example.nitish.cashrichtask.model.CashrichModel
import com.google.gson.JsonArray
import retrofit2.Call
import retrofit2.http.GET

interface ApiCallMethods {
    @GET("testCashRich")
    fun getTestCashRich() : Call<ArrayList<CashrichModel>>
}