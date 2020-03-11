package org.kiimo.me.main.fragments.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.kiimo.me.databinding.ViewHolderMyDeliversBinding

class MenuMyDeliveriesAdapter : RecyclerView.Adapter<MenuMyDeliveriesAdapter.MyDeliveryViewHolder>() {

    private val list = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyDeliveryViewHolder {

        val inflater = LayoutInflater.from(parent.context)
       // val inflate = inflater.inflate(R.layout.view_holder_my_delivers, parent, false)

        val itemBinding = ViewHolderMyDeliversBinding.inflate(inflater)

        return MyDeliveryViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyDeliveryViewHolder, position: Int) {
        holder.bind(list[position])
    }


    inner class MyDeliveryViewHolder(val binding: ViewHolderMyDeliversBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: String) {
            with(binding) {


            }
        }
    }
}