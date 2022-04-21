package com.returnz3ro.messystem.service.model

import com.google.gson.annotations.SerializedName
import lombok.Data


@Data
data class ResponseSlitterData (
    @SerializedName("result")
    var result: String,
    @SerializedName("data")
    var slitters: List<Slitter>,
    @SerializedName("message")
    var message: String
)