package com.returnz3ro.messystem.service.model.responsemodel

import com.google.gson.annotations.SerializedName
import lombok.Data


@Data
data class ResponseWorkResult (
    @SerializedName("result")
    var result: String,
    @SerializedName("message")
    var message: String
)