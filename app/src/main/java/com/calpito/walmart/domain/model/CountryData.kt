package com.calpito.walmart.domain.model

import com.calpito.walmart.domain.model.Currency
import com.calpito.walmart.domain.model.Language
import com.google.gson.annotations.SerializedName
data class CountryData (
    @SerializedName("capital"  ) var capital  : String?   = null,
    @SerializedName("code"     ) var code     : String?   = null,
    @SerializedName("currency" ) var currency : Currency? = Currency(),
    @SerializedName("flag"     ) var flag     : String?   = null,
    @SerializedName("language" ) var language : Language? = Language(),
    @SerializedName("name"     ) var name     : String?   = null,
    @SerializedName("region"   ) var region   : String?   = null
)