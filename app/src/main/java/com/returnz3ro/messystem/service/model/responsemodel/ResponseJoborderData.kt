package com.returnz3ro.messystem.service.model.responsemodel

import com.google.gson.annotations.SerializedName
import com.returnz3ro.messystem.service.model.datamodel.Joborder
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