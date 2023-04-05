package com.galemu00.acronyms.data

import com.galemu00.acronyms.api.AcronymsApi

class AcronymRepository {

    suspend fun getAcronyms(sf: String) = AcronymsApi.getInstance().getAcronyms(sf)

}