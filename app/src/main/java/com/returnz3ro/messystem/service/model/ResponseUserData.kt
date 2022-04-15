package com.returnz3ro.messystem.service.model

import com.google.gson.annotations.SerializedName
import lombok.Data


@Data
data class ResponseUserData (
    @SerializedName("result")
    var result: String,
    @SerializedName("data")
    var user: User,
    @SerializedName("message")
    var message: String
)