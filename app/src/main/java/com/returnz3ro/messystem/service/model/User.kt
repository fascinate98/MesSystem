package com.returnz3ro.messystem.service.model

import com.google.gson.annotations.SerializedName
import lombok.Data

@Data
data class User (

    @SerializedName("userId")
    val userId: Int? = null,
    @SerializedName("userPw")
    val userPw: String? = null,
    @SerializedName("userName")
    val userName: String? = null,
    @SerializedName("userWorktype")
    val userWorktype: String? = null,
    @SerializedName("userGroup")
    val userGroup: String? = null
)

data class UserRequest (
    @SerializedName("userId")
    val userId: Int? = null,
    @SerializedName("userPw")
    val userPw: String? = null
)