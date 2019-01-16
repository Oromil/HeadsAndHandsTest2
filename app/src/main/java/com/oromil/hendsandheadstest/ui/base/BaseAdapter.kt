package com.oromil.hendsandheadstest.ui.base

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class BaseAdapter<H:BaseAdapter.BaseViewHolder, M> : RecyclerView.Adapter<H>() {

    var dataList:ArrayList<M> = ArrayList()

    override fun getItemCount(): Int {
        return dataList.size
    }

    protected fun getView(layoutId: Int, parent: ViewGroup):View{
        return LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
    }

    fun updateData(newData:List<M>){
        dataList.clear()
        dataList.addAll(newData)
        updateData()
    }

    fun updateData(item:M){
        dataList.add(item)
        updateData()
    }

    private fun updateData(){
        notifyDataSetChanged()
    }

    abstract class BaseViewHolder(v:View): RecyclerView.ViewHolder(v){
        abstract fun init(item : Any)
    }
}