package com.returnz3ro.messystem.view.adapter


import android.animation.ValueAnimator
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.AdapterView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.view.doOnLayout
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.returnz3ro.messystem.R
import com.returnz3ro.messystem.databinding.ItemListBinding
import com.returnz3ro.messystem.service.model.datamodel.Joborder
import com.returnz3ro.messystem.service.model.datamodel.Slitter
import com.returnz3ro.messystem.service.model.datamodel.User
import com.returnz3ro.messystem.service.model.datamodel.WorkResult
import com.returnz3ro.messystem.service.model.datastore.DataStoreModule
import com.returnz3ro.messystem.utils.*
import com.returnz3ro.messystem.view.ui.animationPlaybackSpeed
import com.returnz3ro.messystem.viewmodel.LoginViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.internal.notify

class JoborderAdapter(context: Context, listener: OnItemClickListener): RecyclerView.Adapter<MainViewHolder>(){

    interface OnItemClickListener {
        fun onStartWorkClick(j: Joborder)
        fun onFinishWorkClick(j: Joborder)
    }

    private val context = context
    private val listener = listener
    private var loginUser: User = User("","","","","")
    private var userName: String = ""
    private lateinit var dataStore: DataStoreModule

    private val originalBg: Int by bindColor(context, R.color.white)
    private val emergencyBg: Int by bindColor(context, R.color.zemgred)
    private val expandedBg: Int by bindColor(context, R.color.white)

    private val listItemHorizontalPadding: Float by bindDimen(context, R.dimen.list_item_vertical_padding)
    private val listItemVerticalPadding: Float by bindDimen(context, R.dimen.list_item_vertical_padding)
    private val originalWidth = context.screenWidth - 48.dp
    private val expandedWidth = context.screenWidth - 24.dp
    private var originalHeight = -1 // will be calculated dynamically
    private var expandedHeight = -1 // will be calculated dynamically


    private var joborders = mutableListOf<Joborder>()
    private var slitters = mutableListOf<Slitter>()


    private val listItemExpandDuration: Long get() = (300L / animationPlaybackSpeed).toLong()
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    private lateinit var recyclerView: RecyclerView
    private var expandedModel: Joborder? = null
    private var isScaledDown = false


    ///////////////////////////////////////////////////////////////////////////
    // Methods
    ///////////////////////////////////////////////////////////////////////////


    override fun getItemCount(): Int = joborders.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        Log.d(TAG, " 1")
        dataStore = DataStoreModule(context)
        CoroutineScope(Dispatchers.Default).launch {
            dataStore.user.collect{
                loginUser = it
                // Log.d(TAG, "이름ㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁ" + loginUser)
                userName = loginUser.userName
            }
        }
        Log.d(TAG, "이름ㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁ" + loginUser)
//        var a = joborders.filterIndexed { index, joborder ->
//            joborder.joborderId in filteredItems
//        }
//        modelListFiltered = a.toMutableList()
//        joborderlist = if(isFiltered) modelListFiltered else joborders
//        Log.d(TAG, this.joborderlist.size.toString()+ " sdfsdfsdf씨발")
//        Log.d(TAG, this.modelListFiltered.toString()+ "132323123씨발22")
        //MainViewHolder(inflater.inflate(R.layout.item_list, parent, false))
        val binding = ItemListBinding.inflate(inflater, parent, false)

        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
//        Log.d(TAG, " 2" + this.joborderlist.size.toString())
//        joborderlist = if(isFiltered) modelListFiltered else this.joborders
//        Log.d(TAG, " 3" + this.joborderlist.toString())
        val joborder = joborders[position]
        holder.binding.joborder = joborder
        onBindViewHodlerInit(holder)


        holder.binding.btnWorkstart.setOnClickListener{
            var joborder = holder.binding.joborder
            joborder?.let{
                listener.onStartWorkClick(joborder)

            }
        }
        holder.binding.btnWorkfinish.setOnClickListener{
            var joborder = holder.binding.joborder
            joborder?.let{
                listener.onFinishWorkClick(joborder)
            }
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
        holder.binding.executePendingBindings()
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

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
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


    fun setJoborderList(joborders: List<Joborder>){
        this.joborders = joborders.toMutableList()


        notifyDataSetChanged()
//        Log.d(TAG, " sdfsdfsdf씨발")
//        var a = joborders.filterNot { joborder ->
//            joborder.joborderId !in filteredItems
//        }
//        Log.d(TAG,  " 으애응애응애애애애애여기야여기" + a.toString())
//        modelListFiltered = a.toMutableList()
//        joborderlist = if(isFiltered) modelListFiltered else this.joborders
//        Log.d(TAG, this.joborderlist.size.toString()+ " sdfsdfsdf씨발")
//        Log.d(TAG, this.modelListFiltered.size.toString()+ "  132323123씨발22")

    }

    fun setFilteredJoborderList(joborders: List<Joborder>){
//        var a : List<String> = listOf()
//        joborders.forEach {
//            a.plus(it.joborderId)
//        }
//        Log.d(TAG, a.toString()+ " 개씨발")
//        this.filteredItems = a
//        var b = joborders.filterNot { joborder ->
//            joborder.joborderId !in filteredItems
//        }
//        modelListFiltered = b.toMutableList()
//        joborderlist = if(isFiltered) modelListFiltered else this.joborders


//        joborders.forEachIndexed{ index, i ->
//            i.joborderId?.let { this.filteredItems.add(index, i.joborderId!!) }
//
//        }
    }

    fun setSlitterList(slitters: List<Slitter>){
        this.slitters = slitters.toMutableList()
        notifyDataSetChanged()
    }

    private fun onBindViewHodlerInit(holder: MainViewHolder) {
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
        holder.binding.spnSlitter.setOnSpinnerItemSelectedListener<String> {
                oldIndex, oldItem, newIndex, newItem ->
            holder.binding.joborder?.joborderSlitterNo?.let {
                holder.binding.joborder?.joborderSlitterNo = newIndex + 1

            }
        }

    }

    private fun setJoborderStateIconColor(holder: MainViewHolder) {
        if(holder.binding.joborder?.joborderStatus == 1){
            holder.binding.statusIcon.setColorFilter(context.getColor(R.color.zgray));
        } else if(holder.binding.joborder?.joborderStatus == 2){
            holder.binding.statusIcon.setColorFilter(context.getColor(R.color.zgreen));
        }else{
            holder.binding.statusIcon.setColorFilter(context.getColor(R.color.zblue));
        }
    }

    private fun setJoborderWorker(holder: MainViewHolder) {
        //작업 전
        if(holder.binding.joborder?.joborderStatus == 0){
            holder.binding.joborderWorker.visibility = View.GONE
            holder.binding.joborderWorkerLabel.visibility = View.GONE
            holder.binding.btnWorkstart.visibility = View.VISIBLE
            holder.binding.btnWorkfinish.visibility = View.GONE
            holder.binding.joborderSlitterLabel.visibility = View.GONE
            holder.binding.joborderSlittername.visibility = View.GONE
            holder.binding.spnSlitter.visibility = View.VISIBLE

        }
        //작업중
        else if(holder.binding.joborder?.joborderStatus == 1){
            holder.binding.joborderSlitterLabel.visibility = View.VISIBLE
            holder.binding.joborderSlittername.visibility = View.VISIBLE
            holder.binding.spnSlitter.visibility = View.GONE
            Log.d(TAG,holder.binding.joborder?.joborderWorkerName + "앵ㅇ애")
            Log.d(TAG,loginUser.userName + "앵ㅇ애2222")
            //작업한 사람이 나야
            if(holder.binding.joborder?.joborderWorkerName.equals(loginUser.userName) ){
                holder.binding.joborderWorker.visibility = View.GONE
                holder.binding.joborderWorkerLabel.visibility = View.GONE
                holder.binding.btnWorkstart.visibility = View.GONE
                holder.binding.btnWorkfinish.visibility = View.VISIBLE

            }
            //작업한사람이 나가 아니야
            else{
                holder.binding.joborderWorker.visibility = View.VISIBLE
                holder.binding.joborderWorkerLabel.visibility = View.VISIBLE
                holder.binding.btnWorkstart.visibility = View.GONE
                holder.binding.btnWorkfinish.visibility = View.GONE

            }
        }
        //작업 완료
        else if(holder.binding.joborder?.joborderStatus == 2){
            holder.binding.joborderSlitterLabel.visibility = View.VISIBLE
            holder.binding.joborderSlittername.visibility = View.VISIBLE
            holder.binding.spnSlitter.visibility = View.GONE
            holder.binding.joborderWorker.visibility = View.VISIBLE
            holder.binding.joborderWorkerLabel.visibility = View.VISIBLE
            holder.binding.btnWorkstart.visibility = View.GONE
            holder.binding.btnWorkfinish.visibility = View.GONE
        }
    }

}
///////////////////////////////////////////////////////////////////////////
// ViewHolder
///////////////////////////////////////////////////////////////////////////


class MainViewHolder(val binding: ItemListBinding) : RecyclerView.ViewHolder(binding.root){
    fun bind(joborder : Joborder){
        binding.joborder = joborder
        binding.executePendingBindings()
    }
}