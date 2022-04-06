package com.returnz3ro.messystem.model

import lombok.Data


@Data
data class User (
    val user_id: Int,
    val user_pw: String,
    val user_name: String,
    val user_work_type: String,
    val user_group: String
)