package org.kiimo.me.main.fragments.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.kiimo.me.R
import org.kiimo.me.databinding.ViewHolderMyDeliversBinding
import org.kiimo.me.main.fragments.model.sender.SenderOrderListResponse
import org.kiimo.me.util.DateUtils
import org.kiimo.me.util.DeliveryTypeID
import org.kiimo.me.util.PACKAGE_SIZE_ID

class MenuMyOrdersAdatper : RecyclerView.Adapter<MenuMyOrdersAdatper.MyOrdersViewHolder>() {

    private val list = mutableListOf<SenderOrderListResponse>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyOrdersViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        // val inflate = inflater.inflate(R.layout.view_holder_my_delivers, parent, false)

        val itemBinding = ViewHolderMyDeliversBinding.inflate(inflater)

        return MyOrdersViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return list.size
    }


    override fun onBindViewHolder(holder: MyOrdersViewHolder, position: Int) {
        holder.bind(item = list[position])
    }

    fun updateAdapter(newItems: MutableList<SenderOrderListResponse>) {
        this.list.clear()
        this.list.addAll(newItems)
        this.notifyDataSetChanged()
    }


    inner class MyOrdersViewHolder(val binding: ViewHolderMyDeliversBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SenderOrderListResponse) {
            with(binding) {

                pickUpText = item.originAddress
                dropOffText = item.destinationAddress

                viewHolderPckSizeImg.setImageResource(
                    when (item.packages.first().packageSizeId) {
                        PACKAGE_SIZE_ID.SMALL -> R.drawable.ic_package_size_small
                        PACKAGE_SIZE_ID.MEDIUM -> R.drawable.ic_package_size_medium
                        PACKAGE_SIZE_ID.LARGE -> R.drawable.ic_package_size_large
                        else -> R.drawable.ic_package_size_small
                    }
                )

                viewHolderTranTypeImg.setImageResource(
                    when (item.carrierDelivery.deliveryTypeId) {
                        DeliveryTypeID.FOOT -> R.drawable.ic_foot
                        DeliveryTypeID.CAR -> R.drawable.ic_car
                        DeliveryTypeID.BIKE -> R.drawable.ic_bicycle
                        DeliveryTypeID.SCOOTER -> R.drawable.ic_scooter
                        else -> R.drawable.ic_foot
                    }
                )

                viewHolderDate.text = DateUtils.formatDate(item.createdAt)

            }
        }
    }
}