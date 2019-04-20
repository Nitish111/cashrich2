package com.example.nitish.cashrichtask.adapter

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.nitish.cashrichtask.R
import com.example.nitish.cashrichtask.listener.ClickListener
import com.example.nitish.cashrichtask.model.CashrichModel

class YearAdapter(var context: Context,var click: ClickListener) : RecyclerView.Adapter<YearAdapter.Holder>() {
    var list :  ArrayList<CashrichModel> ?= null
    var itemSelected = 0
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): Holder {
        return Holder(LayoutInflater.from(context).inflate(R.layout.year_list_item,p0,false))
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    override fun onBindViewHolder(p0: Holder, p1: Int) {

        var data = list!![p1].date.split(" ")
        p0.text.text = data?.get(data?.size -1)
        if(itemSelected == p1)
        {
            p0.backbround.setCardBackgroundColor(Color.parseColor("#4d9ef7"))
            p0.text.setTextColor(Color.parseColor("#ffffff"))
        }
        else
        {
            p0.backbround.setCardBackgroundColor(Color.parseColor("#ffffff"))
            p0.text.setTextColor(Color.parseColor("#313131"))
        }
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var text = itemView.findViewById<TextView>(R.id.item)
        var backbround = itemView.findViewById<CardView>(R.id.backbround)
        init {
            itemView?.setOnClickListener{
                itemSelected = layoutPosition
                click.clickedItem(layoutPosition)
                notifyDataSetChanged()
            }
        }
    }

    fun setData(list : ArrayList<CashrichModel>)
    {
        this.list = list
        notifyDataSetChanged()
    }
}