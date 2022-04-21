package com.returnz3ro.messystem.view.adapter


import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.opengl.Visibility
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Adapter
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.content.ContextCompat
import androidx.core.view.doOnLayout
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.returnz3ro.messystem.R
import com.returnz3ro.messystem.databinding.ItemListBinding
import com.returnz3ro.messystem.service.model.Joborder
import com.returnz3ro.messystem.service.model.Slitter
import com.returnz3ro.messystem.utils.*
import com.returnz3ro.messystem.view.ui.animationPlaybackSpeed
import okhttp3.internal.toImmutableList

class JoborderAdapter(context : Context): RecyclerView.Adapter<MainViewHolder>(){

    private val context = context

    private val originalBg: Int by bindColor(context, R.color.white)
    private val emergencyBg: Int by bindColor(context, R.color.zemgred)
    private val expandedBg: Int by bindColor(context, R.color.white)


    private val listItemHorizontalPadding: Float by bindDimen(context, R.dimen.list_item_vertical_padding)
    private val listItemVerticalPadding: Float by bindDimen(context, R.dimen.list_item_vertical_padding)
    private val originalWidth = context.screenWidth - 48.dp
    private val expandedWidth = context.screenWidth - 24.dp
    private var originalHeight = -1 // will be calculated dynamically
    private var expandedHeight = -1 // will be calculated dynamically

    private val listItemExpandDuration: Long get() = (300L / animationPlaybackSpeed).toLong()
    private lateinit var recyclerView: RecyclerView
    private var expandedModel: Joborder? = null
    private var isScaledDown = false


    ///////////////////////////////////////////////////////////////////////////
    // Methods
    ///////////////////////////////////////////////////////////////////////////

    var joborders = mutableListOf<Joborder>()
    var slitters = mutableListOf<Slitter>()

    override fun getItemCount(): Int {
        return joborders.size
    }

    fun setJoborderlist(joborders: List<Joborder>){
        this.joborders = joborders.toMutableList()
        notifyDataSetChanged()
    }

    fun setSlitterList(slitters: List<Slitter>){
        this.slitters = slitters.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemListBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val joborder = joborders[position]
        holder.binding.joborder = joborder
        onBindViewHolerInit(holder)

        holder.binding.btnWorkstart.setOnClickListener{

        }

        expandItem(holder, joborder == expandedModel, animate = false)
        scaleDownItem(holder, position, isScaledDown)
        holder.binding.cardContainer.setOnClickListener {
            if (expandedModel == null) {

                // expand clicked view
                expandItem(holder, expand = true, animate = true)
                expandedModel = joborder
            } else if (expandedModel == joborder) {

                // collapse clicked view
                expandItem(holder, expand = false, animate = true)
                expandedModel = null
            } else {

                // collapse previously expanded view
                val expandedModelPosition = joborders.indexOf(expandedModel!!)
                val oldViewHolder =
                    recyclerView.findViewHolderForAdapterPosition(expandedModelPosition) as? MainViewHolder
                if (oldViewHolder != null) expandItem(oldViewHolder, expand = false, animate = true)

                // expand clicked view
                expandItem(holder, expand = true, animate = true)
                expandedModel = joborder
            }
        }
    }

    private fun expandItem(holder: MainViewHolder, expand: Boolean, animate: Boolean) {
        if (animate) {
            val animator = getValueAnimator(
                expand, listItemExpandDuration, AccelerateDecelerateInterpolator()
            ) { progress -> setExpandProgress(holder, progress) }

            if (expand) animator.doOnStart { holder.binding.expandView.isVisible = true }
            else animator.doOnEnd { holder.binding.expandView.isVisible = false }

            animator.start()
        } else {

            // show expandView only if we have expandedHeight (onViewAttached)
            holder.binding.expandView.isVisible = expand && expandedHeight >= 0
            setExpandProgress(holder, if (expand) 1f else 0f)
        }
    }

    private fun onBindViewHolerInit(holder: MainViewHolder) {
        setJoborderStateIconColor(holder)
        setJoborderWorker(holder)
        setSlitterAdapter(holder)
    }

    private fun setSlitterAdapter(holder: MainViewHolder) {
        var slittername = mutableListOf<String>()
        for(s in slitters){
            slittername.add(s.slitterName)
        }
        holder.binding.spnSlitter.setIsFocusable(true)
        holder.binding.spnSlitter.setItems(slittername)
        holder.binding.joborder?.joborderSlitterNo?.let {
            holder.binding.spnSlitter.selectItemByIndex(
                it - 1
            )
        }
    }

    private fun setJoborderStateIconColor(holder: MainViewHolder) {
        if(holder.binding.joborder?.joborderStatus == 1){
            holder.binding.statusIcon.setColorFilter(context.getColor(R.color.zgray));
        } else if(holder.binding.joborder?.joborderStatus == 2){
            holder.binding.statusIcon.setColorFilter(context.getColor(R.color.zgreen));
        }
    }

    private fun setJoborderWorker(holder: MainViewHolder) {
        if(holder.binding.joborder?.joborderStatus == 0){
            holder.binding.joborderWorker.visibility = View.GONE
            holder.binding.joborderWorkerLabel.visibility = View.GONE
            holder.binding.btnWorkstart.visibility = View.VISIBLE
        }
        else if(holder.binding.joborder?.joborderStatus == 1 || holder.binding.joborder?.joborderStatus == 2){
            holder.binding.joborderWorker.visibility = View.VISIBLE
            holder.binding.joborderWorkerLabel.visibility = View.VISIBLE
            holder.binding.btnWorkstart.visibility = View.GONE
        }
    }

    override fun onViewAttachedToWindow(holder: MainViewHolder) {
        super.onViewAttachedToWindow(holder)

        // get originalHeight & expandedHeight if not gotten before
        if (expandedHeight < 0) {
            expandedHeight = 0 // so that this block is only called once

            holder.binding.cardContainer.doOnLayout { view ->
                originalHeight = view.height

                holder.binding.expandView.isVisible = true
                view.doOnPreDraw {
                    expandedHeight = view.height
                    holder.binding.expandView.isVisible = false

                }
            }
        }
    }

    private fun setExpandProgress(holder: MainViewHolder, progress: Float) {
        if (expandedHeight > 0 && originalHeight > 0) {
            holder.binding.cardContainer.layoutParams.height =
                (originalHeight + (expandedHeight - originalHeight) * progress).toInt()
        }
        holder.binding.cardContainer.layoutParams.width =
            (originalWidth + (expandedWidth - originalWidth) * progress).toInt()

        if(holder.binding.joborder?.joborderEmg == 0)
            holder.binding.cardContainer.setBackgroundColor(blendColors(originalBg, expandedBg, progress))
        else if(holder.binding.joborder?.joborderEmg == 1)
            holder.binding.cardContainer.setBackgroundColor(blendColors(emergencyBg, expandedBg, progress))

        holder.binding.cardContainer.requestLayout()
        holder.binding.chevron.rotation = 90 * progress
    }

    ///////////////////////////////////////////////////////////////////////////
    // Scale Down Animation
    ///////////////////////////////////////////////////////////////////////////

    private inline val LinearLayoutManager.visibleItemsRange: IntRange
        get() = findFirstVisibleItemPosition()..findLastVisibleItemPosition()

    fun getScaleDownAnimator(isScaledDown: Boolean): ValueAnimator {
        val lm = recyclerView.layoutManager as LinearLayoutManager

        val animator = getValueAnimator(isScaledDown,
            duration = 300L, interpolator = AccelerateDecelerateInterpolator()
        ) { progress ->

            // Get viewHolder for all visible items and animate attributes
            for (i in lm.visibleItemsRange) {
                val holder = recyclerView.findViewHolderForLayoutPosition(i) as MainViewHolder
                setScaleDownProgress(holder, i, progress)
            }
        }

        // Set adapter variable when animation starts so that newly binded views in
        // onBindViewHolder will respect the new size when they come into the screen
        animator.doOnStart { this.isScaledDown = isScaledDown }

        // For all the non visible items in the layout manager, notify them to adjust the
        // view to the new size
        animator.doOnEnd {
            repeat(lm.itemCount) { if (it !in lm.visibleItemsRange) notifyItemChanged(it) }
        }
        return animator
    }


    private fun setScaleDownProgress(holder: MainViewHolder, position: Int, progress: Float) {
        val itemExpanded = position >= 0 && joborders[position] == expandedModel
        holder.binding.cardContainer.layoutParams.apply {
            width = ((if (itemExpanded) expandedWidth else originalWidth) * (1 - 0.1f * progress)).toInt()
            height = ((if (itemExpanded) expandedHeight else originalHeight) * (1 - 0.1f * progress)).toInt()
//            log("width=$width, height=$height [${"%.2f".format(progress)}]")
        }
        holder.binding.cardContainer.requestLayout()

        holder.binding.scaleContainer.scaleX = 1 - 0.05f * progress
        holder.binding.scaleContainer.scaleY = 1 - 0.05f * progress

        holder.binding.scaleContainer.setPadding(
            (listItemHorizontalPadding * (1 - 0.2f * progress)).toInt(),
            (listItemVerticalPadding * (1 - 0.2f * progress)).toInt(),
            (listItemHorizontalPadding * (1 - 0.2f * progress)).toInt(),
            (listItemVerticalPadding * (1 - 0.2f * progress)).toInt()
        )

        holder.binding.listItemFg.alpha = progress
    }

    private fun scaleDownItem(holder: MainViewHolder, position: Int, isScaleDown: Boolean) {
        setScaleDownProgress(holder, position, if (isScaleDown) 1f else 0f)
    }
}
///////////////////////////////////////////////////////////////////////////
// ViewHolder
///////////////////////////////////////////////////////////////////////////


class MainViewHolder(val binding: ItemListBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(joborder: Joborder) {
        binding.joborder = joborder
        binding.executePendingBindings() //데이터가 수정되면 즉각 바인딩
    }
}