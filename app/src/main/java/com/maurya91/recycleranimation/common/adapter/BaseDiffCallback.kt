package com.maurya91.recycleranimation.common.adapter

import androidx.recyclerview.widget.DiffUtil

class BaseDiffCallback<M>(val oldList: List<M>, val newList: List<M>, val listener: Listener<M>) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        listener.areItemAreSame(oldList.get(oldItemPosition), newList.get(newItemPosition))

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        listener.areContentsTheSame(oldList.get(oldItemPosition), newList.get(newItemPosition))

    interface Listener<O> {
        fun areItemAreSame(oldItem: O, newItem: O): Boolean

        fun areContentsTheSame(oldItem: O, newItem: O): Boolean
    }
}