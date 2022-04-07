package com.returnz3ro.messystem.model

import com.google.gson.annotations.SerializedName
import lombok.Data


@Data
data class User (
    @SerializedName("user_id")
    val user_id: Int? = null,
    @SerializedName("user_pw")
    val user_pw: String? = null,
    @SerializedName("user_name")
    val user_name: String? = null,
    @SerializedName("user_work_type")
    val user_work_type: String? = null,
    @SerializedName("user_group")
    val user_group: String? = null
)