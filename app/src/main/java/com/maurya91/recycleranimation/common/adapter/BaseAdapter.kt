package com.maurya91.recycleranimation.common.adapter

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView


abstract class BaseAdapter<M, VH : BaseViewHolder<M>> : androidx.recyclerview.widget.RecyclerView.Adapter<VH>(),
    BaseDiffCallback.Listener<M>, Filterable {

    protected var itemList: ArrayList<M> = ArrayList()
    private var originalList: ArrayList<M> = ArrayList()
    private lateinit var onItemClickListener: (v: View, item: M, position: Int) -> Unit
    private val now = System.currentTimeMillis()

    override fun getItemCount() = itemList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return instantiateViewHolder(view)
    }

    fun relativeTimeString(time: Long): String {
        return DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS).toString()
    }

    fun setOnItemClickListener(onItemClickListener: (v: View, item: M, position: Int) -> Unit) {
        this.onItemClickListener = onItemClickListener
    }

    fun getOnItemClickListener(): (v: View, item: M, position: Int) -> Unit {
        return this.onItemClickListener
    }

    private fun publishItems(items: ArrayList<M>) {
        val diffCallback = BaseDiffCallback<M>(itemList, items, this)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.itemList.clear()
        this.itemList.addAll(items)
        diffResult.dispatchUpdatesTo(this)
    }
    fun setItem(item: M) {
        val pos=originalList.indexOf(item)
        originalList.set(pos,item)
        publishItems(originalList)
    }
    fun setItem(position: Int,item: M) {
        originalList.set(position,item)
        publishItems(originalList)
    }
    fun addItems(items: ArrayList<M>) {
        originalList.addAll(items)
        publishItems(originalList)
    }

    fun addItem(item: M) {
        originalList.add(item)
        publishItems(originalList)
    }

    fun addItem(position: Int, item: M) {
        originalList.add(position, item)
        publishItems(originalList)
    }

    fun removeItem(item: M) {
        originalList.remove(item)
        publishItems(originalList)
    }

    fun removeAt(position: Int) {
        originalList.removeAt(position)
        publishItems(originalList)
    }

    fun clearAll() {
        originalList.clear()
        publishItems(originalList)
    }

    fun swapItems(pos1: Int, pos2: Int) {
        val item1 = originalList.get(pos1)
        originalList.set(pos1, originalList.get(pos2))
        originalList.set(pos2, item1)
        publishItems(originalList)
    }

    fun getItem(position: Int): M = itemList.get(position)

    protected open fun doFiltering(text: String): ArrayList<M>? =itemList

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val result = FilterResults()
                if (constraint.isNullOrBlank())
                    result.values = originalList
                else
                    result.values = doFiltering(constraint.toString())
                return result
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                publishItems(results?.values as ArrayList<M>)
            }

        }
    }

    override fun areItemAreSame(oldItem: M, newItem: M): Boolean = false

    override fun areContentsTheSame(oldItem: M, newItem: M): Boolean = true

    abstract fun instantiateViewHolder(view: View): VH

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.onBind(getItem(position))
    }

    fun setRecyclerViewItemTouchListener(recyclerView: RecyclerView,longPressDragEnabled:Boolean=false, listener: (position: Int) -> Unit) {
        val itemTouchCallback =
            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(
                    rv: RecyclerView,
                    holder1: RecyclerView.ViewHolder,
                    holder2: RecyclerView.ViewHolder
                ): Boolean {
                    swapItems(holder1.adapterPosition, holder2.adapterPosition)
                    return true
                }

                override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
                    val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
                    val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
                    return makeMovementFlags(dragFlags, swipeFlags)
                }

                override fun isLongPressDragEnabled(): Boolean = longPressDragEnabled
                override fun isItemViewSwipeEnabled(): Boolean = true

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                    removeAt(viewHolder.adapterPosition)
                    listener.invoke(viewHolder.adapterPosition)
                }
            }

        //4
        val itemTouchHelper = ItemTouchHelper(itemTouchCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }
}