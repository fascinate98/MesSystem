package com.returnz3ro.messystem.utils

import androidx.recyclerview.widget.DiffUtil
import com.returnz3ro.messystem.service.model.datamodel.Joborder

class MainListDiffUtil(
    private val oldList: List<Joborder>,
    private val newList: List<Joborder>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition] == newList[newItemPosition]

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition] == newList[newItemPosition]
}