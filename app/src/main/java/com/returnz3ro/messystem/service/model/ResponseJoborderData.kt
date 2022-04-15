package com.returnz3ro.messystem.service.model

import com.google.gson.annotations.SerializedName
import lombok.Data


@Data
data class ResponseJoborderData (
    @SerializedName("result")
    var result: String,
    @SerializedName("data")
    var joborders: List<Joborder>,
    @SerializedName("message")
    var message: String
)