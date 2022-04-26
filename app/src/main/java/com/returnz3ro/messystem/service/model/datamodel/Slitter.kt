package com.returnz3ro.messystem.service.model.datamodel
import java.io.Serializable
import com.google.gson.annotations.SerializedName
import lombok.Data

@Data
data class Slitter (
    @SerializedName("slitterId")
    var slitterId: Int,
    @SerializedName("slitterName")
    var slitterName: String
): Serializable