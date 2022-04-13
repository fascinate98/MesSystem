package com.returnz3ro.messystem.service.model

import com.google.gson.annotations.SerializedName
import lombok.Data
import java.util.*

@Data
data class ResponseData (
        @SerializedName("result")
        var result: String,
        @SerializedName("data")
        var data: Objects,
        @SerializedName("message")
        var message: String)