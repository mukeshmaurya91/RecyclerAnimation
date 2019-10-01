package com.maurya91.recycleranimation.view.home

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.ImageButton
import android.widget.RelativeLayout
import com.maurya91.recycleranimation.R
import com.maurya91.recycleranimation.common.adapter.BaseAdapter
import com.maurya91.recycleranimation.common.adapter.BaseViewHolder
import com.maurya91.recycleranimation.data.HomeData

class HomeRecyclerAdapter : BaseAdapter<HomeData, HomeRecyclerAdapter.HomeItemHolder>() {
    var lastPosition: Int = 0
    override fun instantiateViewHolder(view: View): HomeItemHolder = HomeItemHolder(view)

    override fun getItemViewType(position: Int): Int = R.layout.item_home_layout

    override fun areContentsTheSame(oldItem: HomeData, newItem: HomeData): Boolean {
        return oldItem.equals(newItem)
    }

    override fun areItemAreSame(oldItem: HomeData, newItem: HomeData): Boolean {
        return oldItem.id.equals(newItem.id)
    }

    inner class HomeItemHolder(itemView: View) : BaseViewHolder<HomeData>(itemView) {
        val rootLayout: RelativeLayout = itemView.findViewById(R.id.rootLayout)
        val removeButton: ImageButton = itemView.findViewById(R.id.removeButton)
        override fun onBind(item: HomeData) {
//            rootLayout.setBackgroundColor(item.color)  // only enable if color values assigned in model
            removeButton.setOnClickListener {
                getOnItemClickListener().invoke(
                    it,
                    item,
                    adapterPosition
                )
            }
            if (lastPosition < adapterPosition) { //this fix animation when scrolling up
                slideFromRight(itemView)
            }
            lastPosition = adapterPosition
        }

        fun fallDown(itemView: View) {

        }

        fun slideFromRight(itemView: View) {
            val xTrans = ObjectAnimator.ofFloat(itemView, View.TRANSLATION_X, 1000f, 0f).apply {
                interpolator = DecelerateInterpolator()
            }
//            val yTrans = ObjectAnimator.ofFloat(itemView, View.TRANSLATION_Y, 1000f, 0f)
            val alpha = ObjectAnimator.ofFloat(itemView, View.ALPHA, 0.5f, 1f).apply {
                interpolator = AccelerateInterpolator()
            }
            AnimatorSet().apply {
                duration = 300
                play(xTrans).with(alpha)
                start()
            }
        }
        fun slideFromBottom(itemView: View){

        }

    }
}