package com.mehmetkaradana.cryptomk.service

import com.mehmetkaradana.cryptomk.model.cryptoModel
import io.reactivex.Completable
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET

interface CryptoService {
//https://raw.githubusercontent.com/atilsamancioglu/K21-JSONDataSet/master/crypto.json
    @GET("atilsamancioglu/K21-JSONDataSet/master/crypto.json")
    fun getData() : Observable<List<cryptoModel>>

// fun getData() : Call<List<cryptoModel>>

}