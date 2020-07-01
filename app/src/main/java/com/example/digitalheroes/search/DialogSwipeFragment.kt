package com.example.digitalheroes.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.example.digitalheroes.R
import com.example.digitalheroes.databinding.SwipeHintViewBinding

class DialogSwipeFragment  : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout to use as dialog or embedded fragment
        val binding : SwipeHintViewBinding = DataBindingUtil.inflate(
            inflater, R.layout.swipe_hint_view, container, false)

        binding.relativeLayout.setOnClickListener {
            this.dismiss()
//            this.fragmentManager?.popBackStackImmediate()
        }



        return binding.root
    }

}