package com.galemu00.acronyms.api

import com.galemu00.acronyms.data.model.Acronyms
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AcronymsService {

    // http://www.nactem.ac.uk/software/acromine/dictionary.py?sf=hmm
    @GET("dictionary.py")
    suspend fun getAcronyms(
        @Query("sf")
        sf: String
    ):Response<Acronyms>
}