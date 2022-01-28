package com.dhiva.githubuser.core.utils

import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.dhiva.githubuser.R

fun ImageView.loadImage(url: String?) {
    val placeHolder = CircularProgressDrawable(this.context)
    placeHolder.setColorSchemeColors(
        R.color.blue,
        R.color.dark_blue,
        R.color.blue
    )
    placeHolder.centerRadius = 30f
    placeHolder.strokeWidth = 5f
    placeHolder.start()

    Glide.with(this.context)
        .load(url)
        .placeholder(placeHolder)
        .into(this)
}