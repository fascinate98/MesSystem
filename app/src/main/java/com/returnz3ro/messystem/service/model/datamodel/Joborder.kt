package com.returnz3ro.messystem.service.model.datamodel

import com.google.gson.annotations.SerializedName
import lombok.Data

@Data
data class Joborder (
    // 작지명
    @SerializedName("joborderJobname")
    var joborderJobname: String? = null,

    // 작지명
    @SerializedName("joborderId")
    var joborderId: String? = null,

    // 부모 품번
    @SerializedName("materialProdId")
    var materialProdId: String? = null,

    //부모이름
    @SerializedName("materialProdName")
    var materialProdName: String? = null,

    //lotno
    @SerializedName("materialLotno")
    var materialLotno: String? = null,

    //줄수
    @SerializedName("joborderLine")
    var joborderLine: String? = null,

    //상태
    @SerializedName("joborderStatus")
    var joborderStatus: Int? = null,

    //
    @SerializedName("userName")
    var userName: String? = null,

    //작업자
    @SerializedName("joborderWorkerName")
    var joborderWorkerName: String? = null,

    //깅금
   @SerializedName("joborderEmg")
    var joborderEmg: Int? = null,

    //부모폭
    @SerializedName("joborderWidth")
    var joborderWidth: String? = null,

    //슬리터명
    @SerializedName("slitterName")
    var slitterName: String? = null,

    //설비번호
    @SerializedName("joborderSlitterNo")
    var joborderSlitterNo: Int = 0

)