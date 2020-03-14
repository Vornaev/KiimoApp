package org.kiimo.me.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.toolbar_back_button_title.view.*
import org.kiimo.me.R
import org.kiimo.me.app.BaseMainFragment
import org.kiimo.me.databinding.FragmentMenuMyordersListBinding
import org.kiimo.me.main.fragments.adapter.MenuMyDeliveriesAdapter
import org.kiimo.me.main.fragments.adapter.MenuMyOrdersAdatper
import org.kiimo.me.main.fragments.model.sender.SenderOrderListResponse

class MenuMyOrdersFragment : BaseMainFragment() {

    lateinit var binding: FragmentMenuMyordersListBinding
    val adapter: MenuMyOrdersAdatper by lazy {
        MenuMyOrdersAdatper()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMenuMyordersListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.toolbar_title_text_view.text = getString(R.string.my_orders_title)

        binding.toolbar.arrow_back_image_view.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.recycleViewOrders.layoutManager = LinearLayoutManager(requireContext())
        binding.recycleViewOrders.adapter = adapter

        mainDeliveryViewModel().ordersListLiveData.observe(viewLifecycleOwner, Observer {
            adapter.updateAdapter(it)
            setViewState(it)
        })

        mainDeliveryViewModel().getOrdersList()

    }

    private fun setViewState(list: MutableList<SenderOrderListResponse>) {

        binding.isEmptyState = list.size == 0
    }
}


