package com.example.dell.registerapps.ui.main

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.dell.registerapps.R
import com.example.dell.registerapps.data.PatientData
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item.*

class MainAdapter (private val listData:MutableList<PatientData>, private val listener: (PatientData, View) -> Unit):
        RecyclerView.Adapter<MainAdapter.ViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.item,p0,false))
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.bind(listData[p1],listener)
    }

    inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(data: PatientData, listener: (PatientData, View) -> Unit) {
            tv_name.text = data.name
            tv_gender.text = data.gender
            tv_address.text = data.address
            tv_option_menu.setOnClickListener {
                listener(data,tv_option_menu)
            }
        }
    }
}