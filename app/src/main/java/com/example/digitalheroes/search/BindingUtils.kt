package com.example.digitalheroes.search

import android.annotation.SuppressLint
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.core.text.set
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.digitalheroes.R
import com.example.digitalheroes.domain.SuperHeroPieces
import com.example.digitalheroes.network.SuperHeroes

@SuppressLint("SetTextI18n")
@BindingAdapter("feed_data", "feedFilter")
fun TextView.feedData(listResult: SuperHeroPieces, showResults : Boolean) {

    if(!showResults){
        text =  "Published by : ${listResult.biography.publisher}\n" +
                "Full name        : ${listResult.biography.fullName}\n"+
                "Intelligence    : ${listResult.powerstats.intelligence}\n" +
                "Strength          : ${listResult.powerstats.strength}\n" +
                "Speed                 : ${listResult.powerstats.speed}"
    }else{
        text =  "Full name        : ${listResult.biography.fullName}\n"+
                "Intelligence    : ${listResult.powerstats.intelligence}\n" +
                "Strength          : ${listResult.powerstats.strength}\n" +
                "Speed                 : ${listResult.powerstats.speed}\n" +
                "Combat             : ${listResult.powerstats.combat}\n" +
                "Height               : ${listResult.appearance.height[0]}\n"+
                "Weight              : ${listResult.appearance.weight[1]}"
    }

}

@SuppressLint("SetTextI18n")
@BindingAdapter("compare_data", "feed_filter")
fun TextView.compareData(listResult: SuperHeroes, showResults : Boolean) {

    if(!showResults){
        text =  "Published by : ${listResult.biography.publisher}\n" +
                "Full name        : ${listResult.biography.fullName}\n"+
                "Intelligence    : ${listResult.powerstats.intelligence}\n" +
                "Strength          : ${listResult.powerstats.strength}\n" +
                "Speed                 : ${listResult.powerstats.speed}"
    }else{
        text =  "Full name        : ${listResult.biography.fullName}\n"+
                "Intelligence    : ${listResult.powerstats.intelligence}\n" +
                "Strength          : ${listResult.powerstats.strength}\n" +
                "Speed                 : ${listResult.powerstats.speed}\n" +
                "Combat             : ${listResult.powerstats.combat}\n" +
                "Height               : ${listResult.appearance.height[0]}\n"+
                "Weight              : ${listResult.appearance.weight[1]}"
    }

}

@BindingAdapter("set_family")
fun TextView.setFamily(listResult: String) {
    val s = listResult.replace("), ", "),\n")
    text = "Relatives : $s"
}


@BindingAdapter("image_binding")
fun ImageView.bindImage(url: String) {
    url.let {
        val imgUri = url.toUri().buildUpon().scheme("https").build()
        Glide.with(this.context)
            .load(imgUri)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken_image)
            )
            .into(this)
    }
}

@BindingAdapter("set_publisher")
fun TextView.setPublisher(name: String) {

    if (name.isNotEmpty()) {
        val finalString = "Name : $name"
        val spannable = SpannableString(finalString)
        spannable.setSpan(ForegroundColorSpan(resources.getColor(R.color.light_blue)), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        setText(spannable, TextView.BufferType.SPANNABLE)
    }

}
//
//@BindingAdapter("automate_visibility")
//fun com.airbnb.lottie.LottieAnimationView.changeVisibility(status: LoadStatus) {
//
//    when (status) {
//        LoadStatus.NOT_FOUND -> {
//            if (this.isAnimating) {
//                this.pauseAnimation()
//            }
//            if (this.visibility == View.GONE)
//                this.visibility = View.VISIBLE
//            this.setAnimation(R.raw.notfound)
//            this.playAnimation()
//            this.loop(true)
//        }
//
//        LoadStatus.ERROR -> {
//            if (this.isAnimating) {
//                this.pauseAnimation()
//            }
//            if (this.visibility == View.GONE)
//                this.visibility = View.VISIBLE
//            this.setAnimation(R.raw.noconnection)
//            this.playAnimation()
//            this.loop(true)
//        }
//
//        LoadStatus.LOADING -> {
//            if (this.isAnimating) {
//                this.pauseAnimation()
//            }
//            if (this.visibility == View.GONE)
//                this.visibility = View.VISIBLE
//            this.setAnimation(R.raw.loadinglottie)
//            this.playAnimation()
//            this.loop(true)
//        }
//
//        LoadStatus.DONE -> {
//            if (this.isAnimating) {
//                this.pauseAnimation()
//            }
//            if (this.visibility == View.VISIBLE)
//                this.visibility = View.GONE
//        }
//    }
//
//}



