package org.kiimo.me.main.sender.map

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import org.kiimo.me.R

import org.kiimo.me.util.Image.ImageUtil

object MarkerBitmapLoader {


    fun loadImageResourceAsBitmapDescriptior(context: Context, iconId: Int): BitmapDescriptor {

        val bitmapFac: Bitmap = BitmapFactory.decodeResource(context.resources, iconId)
        return BitmapDescriptorFactory.fromBitmap(bitmapFac)
    }

    fun getBitmapDescriptorFromVector(id: Int, context: Context): BitmapDescriptor {
        val vectorDrawable: Drawable = context.getDrawable(id)
        val h = (24 * context.resources.displayMetrics.density).toInt();
        val w = (24 * context.resources.displayMetrics.density).toInt();
        vectorDrawable.setBounds(0, 0, w, h)
        val bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bm)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bm)
    }

    fun getSpecificVector(context: Context, vectorDrawableResourceId: Int): BitmapDescriptor? {
        val vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId)
        vectorDrawable?.setBounds(
            0,
            0,
            vectorDrawable.intrinsicWidth / 2,
            vectorDrawable.intrinsicHeight / 2
        )
        if (vectorDrawable == null) return null
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth / 2,
            vectorDrawable.intrinsicHeight / 2,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }


    fun loadIconFromURl(context: Context, marker: Marker, url: String, view: Int) {

        val rootView = LayoutInflater.from(context).inflate(view, null)


        val glideRequestListener: RequestListener<Drawable> = object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                return true
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: com.bumptech.glide.load.DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                if (resource != null) {
                    rootView.background = resource
                    rootView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
                    rootView.layout(0, 0, rootView.measuredWidth, rootView.measuredHeight)
                    marker.setIcon(
                        BitmapDescriptorFactory.fromBitmap(
                            ImageUtil.getBitmapFromView(
                                rootView,
                                rootView.measuredWidth,
                                rootView.measuredHeight
                            )
                        )
                    )
                }
                return true
            }
        }
        Glide.with(context).load(url).listener(glideRequestListener)
            .apply(RequestOptions.circleCropTransform()).submit()
    }


    fun getBitmapFromView(context: Context, resID: Int): BitmapDescriptor {
        val rootView = LayoutInflater.from(context).inflate(resID, null)

        rootView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        rootView.layout(0, 0, rootView.measuredWidth, rootView.measuredHeight)

        return BitmapDescriptorFactory.fromBitmap(
            ImageUtil.getBitmapFromView(
                rootView,
                rootView.measuredWidth,
                rootView.measuredHeight
            )
        )

    }
}