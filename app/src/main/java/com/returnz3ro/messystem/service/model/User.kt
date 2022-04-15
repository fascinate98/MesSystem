package com.returnz3ro.messystem.service.model
import java.io.Serializable
import com.google.gson.annotations.SerializedName
import lombok.Data

@Data
data class User (
    @SerializedName("userId")
    var userId: String? = null,
    @SerializedName("userPw")
    var userPw: String? = null,
    @SerializedName("userName")
    var userName: String? = null,
    @SerializedName("userWorktype")
    var userWorktype: String? = null,
    @SerializedName("userGroup")
    var userGroup: String? = null
): Serializable