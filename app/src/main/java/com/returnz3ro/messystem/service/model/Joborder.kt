package com.returnz3ro.messystem.service.model

import com.google.gson.annotations.SerializedName
import lombok.Data

@Data
data class Joborder (
    // 작지명
    @SerializedName("joborerJobname")
    val joborerJobname: String? = null,

    // 부모 품번
    @SerializedName("materialProdId")
    val materialProbId: String? = null,

    //부모이름
    @SerializedName("materialProdName")
    val materialProbName: String? = null,

    //줄수
    @SerializedName("materialLine")
    val materialLine: String? = null,

    //작업자
    @SerializedName("joborderWorker")
    val joborderWorker: String? = null,

    //깅금
   @SerializedName("joborderEmergency")
    val joborderEmergency: String? = null,

    //부모폭
    @SerializedName("joborderBasicinfoWidth")
    val joborderBasicinfoWidth: String? = null,

    //설비번호
    @SerializedName("joborderSetEquipEquipId")
    val joborderSetEquipEquipId: String? = null

)