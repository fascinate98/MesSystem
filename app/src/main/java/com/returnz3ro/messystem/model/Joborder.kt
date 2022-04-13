package com.returnz3ro.messystem.model

import com.google.gson.annotations.SerializedName
import lombok.Data

@Data
data class Joborder (
    @SerializedName("material_prod_id")
    val materialProbId: Int? = null,

    @SerializedName("material_prod_name")
    val materialProbName: String? = null,

    @SerializedName("material_line")
    val materialLine: String? = null,

    @SerializedName("joborder_worker")
    val joborderWorker: String? = null,

    @SerializedName("joborder_emergency")
    val joborderEmergency: Int? = null,

    @SerializedName("joborder_basicinfo_width")
    val joborderBasicinfoWidth: Int? = null,

    @SerializedName("joborder_set_equip_equip_id")
    val joborderSetEquipEquipId: Int? = null

)