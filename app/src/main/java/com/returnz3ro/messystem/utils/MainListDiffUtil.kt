package com.returnz3ro.messystem.utils

import androidx.recyclerview.widget.DiffUtil
import com.returnz3ro.messystem.view.adapter.MainListModel

class MainListDiffUtil(
    private val oldList: List<MainListModel>,
    private val newList: List<MainListModel>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition].id == newList[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition] == newList[newItemPosition]
}