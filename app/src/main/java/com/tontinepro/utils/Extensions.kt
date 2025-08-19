package com.tontinepro.utils

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.tontinepro.R
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

fun Long.formatCurrency(): String {
    return NumberFormat.getNumberInstance(Locale.FRANCE).format(this)
}

fun String.toFormattedDate(): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE)
        val date = inputFormat.parse(this)
        outputFormat.format(date ?: Date())
    } catch (e: Exception) {
        this
    }
}

fun String.toFormattedDateTime(): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd/MM/yyyy à HH:mm", Locale.FRANCE)
        val date = inputFormat.parse(this)
        outputFormat.format(date ?: Date())
    } catch (e: Exception) {
        this
    }
}

fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun View.setVisible(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}

fun View.setInvisible(invisible: Boolean) {
    visibility = if (invisible) View.INVISIBLE else View.VISIBLE
}

fun ImageView.loadImage(url: String?) {
    Glide.with(context)
        .load(url)
        .placeholder(R.drawable.placeholder_user)
        .error(R.drawable.placeholder_user)
        .circleCrop()
        .into(this)
}

fun ImageView.loadImageRounded(url: String?, cornerRadius: Int = 8) {
    Glide.with(context)
        .load(url)
        .placeholder(R.drawable.placeholder_image)
        .error(R.drawable.placeholder_image)
        .transform(com.bumptech.glide.load.resource.bitmap.RoundedCorners(cornerRadius))
        .into(this)
}