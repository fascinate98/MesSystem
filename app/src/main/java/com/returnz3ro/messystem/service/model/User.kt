package com.returnz3ro.messystem.service.model
import java.io.Serializable
import com.google.gson.annotations.SerializedName
import lombok.Data

@Data
data class User (
    @SerializedName("userId")
    var userId: String,
    @SerializedName("userPw")
    var userPw: String,
    @SerializedName("userName")
    var userName: String,
    @SerializedName("userWorktype")
    var userWorktype: String,
    @SerializedName("userGroup")
    var userGroup: String
): Serializable