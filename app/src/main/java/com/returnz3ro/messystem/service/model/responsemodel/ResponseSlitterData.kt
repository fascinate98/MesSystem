package com.returnz3ro.messystem.service.model.responsemodel

import com.google.gson.annotations.SerializedName
import com.returnz3ro.messystem.service.model.datamodel.Slitter
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