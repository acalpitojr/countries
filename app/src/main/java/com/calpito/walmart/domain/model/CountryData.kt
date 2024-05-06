package com.calpito.walmart.domain.model

data class CountryData(
    val capital  : String?   = null,
    val code     : String?   = null,
    val currency : Currency? = Currency(),
    val flag     : String?   = null,
    val language : Language? = Language(),
    val name     : String?   = null,
    val region   : String?   = null
)