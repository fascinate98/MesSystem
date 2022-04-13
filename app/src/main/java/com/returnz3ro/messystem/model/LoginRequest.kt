package com.returnz3ro.messystem.model

import com.google.gson.annotations.SerializedName
import lombok.Data


@Data
data class LoginRequest (
    @SerializedName("result")
    var resultCode: String? = null,
    @SerializedName("access_token")
    var token: String? = null,
    @SerializedName("user_id")
    val userId: Int? = null,
    @SerializedName("user_pw")
    val userPw: String? = null,
    @SerializedName("user_name")
    val userName: String? = null,
    @SerializedName("user_work_type")
    val userWorkType: String? = null,
    @SerializedName("user_group")
    val userGroup: String? = null
)