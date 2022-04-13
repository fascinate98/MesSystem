package com.returnz3ro.messystem.model

import com.google.gson.annotations.SerializedName
import lombok.Data


@Data
data class LoginResPonse (
    @SerializedName("user_id")
    val userId: String?,
    @SerializedName("user_pw")
    val userPw: String?
)
