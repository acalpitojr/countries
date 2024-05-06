package com.calpito.walmart.domain.model

sealed class RecyclerData{
    data class CountryData(
        val capital  : String?   = null,
        val code     : String?   = null,
        val currency : Currency? = Currency(),
        val flag     : String?   = null,
        val language : Language? = Language(),
        val name     : String?   = null,
        val region   : String?   = null
    ): RecyclerData()

    data class CountryHeader(val text:String):RecyclerData()

}

