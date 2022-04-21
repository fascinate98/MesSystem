package com.returnz3ro.messystem.service.model

import com.google.gson.annotations.SerializedName
import lombok.Data

@Data
data class WorkResult (
    // 작업자
    @SerializedName("joborderResultWorkerId")
    var joborderResultWorkerId: String? = null,

    // 작지아이디
    @SerializedName("joborderResultJoborderId")
    var joborderResultJoborderId: String? = null,

    //설비번호
    @SerializedName("joborderSlitterNo")
    var joborderSlitterNo: String? = null,

)