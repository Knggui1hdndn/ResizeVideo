package com.tearas.resizevideo.core

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseAdapter<VB : ViewBinding, O : Any> :
    RecyclerView.Adapter<BaseAdapter<VB, O>.RecyclerViewHolder>() {

    private var onClickListener: ((O,Int) -> Unit)? = null
    private var onLongClickListener: ((O,Int) -> Unit)? = null

    @SuppressLint("NotifyDataSetChanged")
    var submitData = ArrayList<O>()
        set(newList) {
            field.clear()
            field.addAll(newList)
            notifyDataSetChanged()
        }

    abstract fun getViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): VB

    abstract fun onBind(binding: VB, item: O)

    inner class RecyclerViewHolder(val binding: VB) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = getViewBinding(inflater, parent)
        return RecyclerViewHolder(binding)
    }

    override fun getItemCount(): Int = submitData.size

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val data = submitData[position]
        onBind(holder.binding, data)

        holder.binding.root.setOnClickListener {
            onClickListener?.invoke(data,position)
        }

        holder.binding.root.setOnLongClickListener {
            onLongClickListener?.invoke(data,position)
            true
        }
    }


    fun setOnClickListener(onClick: (O,Int) -> Unit) {
        onClickListener = onClick
    }

    fun setOnLongClickListener(onClick: (O,Int) -> Unit) {
        onLongClickListener = onClick
    }
}
