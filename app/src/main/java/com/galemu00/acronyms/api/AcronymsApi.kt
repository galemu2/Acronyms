package com.galemu00.acronyms.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AcronymsApi {
    companion object {

        private var INSTANCE: Retrofit? = null

        fun getInstance(): AcronymsService {
            var tmp = INSTANCE
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = Retrofit.Builder()
                        .baseUrl(BaseUrl.URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                    tmp = INSTANCE
                }
            }
            return tmp!!.create(AcronymsService::class.java)
        }
    }
}

object BaseUrl {
    const val URL = "http://www.nactem.ac.uk/software/acromine/"
}