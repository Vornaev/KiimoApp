package org.kiimo.me.adapters

import android.widget.Button
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import org.kiimo.me.R

@BindingAdapter("imageFromBoolean")
fun bindImageFromInt(view: ImageView, value: Boolean?) {
    if (value == true) {
        when (view.id) {
            R.id.bicycle_image_view -> view.setImageResource(R.drawable.ic_bicycle_active)
            R.id.scooter_image_view -> view.setImageResource(R.drawable.ic_scooter_active)
            R.id.car_image_view -> view.setImageResource(R.drawable.ic_car_active)
            else -> view.setImageResource(R.drawable.ic_foot_active)
        }
    } else {
        when (view.id) {
            R.id.bicycle_image_view -> view.setImageResource(R.drawable.ic_bicycle)
            R.id.scooter_image_view -> view.setImageResource(R.drawable.ic_scooter)
            R.id.car_image_view -> view.setImageResource(R.drawable.ic_car)
            else -> view.setImageResource(R.drawable.ic_foot)
        }
    }
}

@BindingAdapter("confirmText")
fun bindConfirmText(view: Button, value: Boolean) {
    if (value) {
        view.text = view.context.getString(R.string.confirm_drop_off)
    } else {
        view.text = view.context.getString(R.string.confirm_pick_up)
    }
}