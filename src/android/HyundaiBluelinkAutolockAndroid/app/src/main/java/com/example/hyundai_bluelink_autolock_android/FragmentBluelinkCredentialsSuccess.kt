package com.example.hyundai_bluelink_autolock_android

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.findNavController
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.hyundai_bluelink_autolock_android.databinding.FragmentBluelinkCredentialsSuccessBinding

class FragmentBluelinkCredentialsSuccess : Fragment() {

    private var _binding: FragmentBluelinkCredentialsSuccessBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBluelinkCredentialsSuccessBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding?.nextButton?.setOnClickListener {
            findNavController().navigate(R.id.action_FragmentBluelinkCredentialsSuccess_to_FragmentIdentifyCarBluetooth)
        }

        val imageView = binding.imageView
        Glide.with(this)
            .asGif()
            .load(R.drawable.checkgif)
            .into(object : CustomTarget<GifDrawable>() {
                override fun onResourceReady(resource: GifDrawable, transition: Transition<in GifDrawable>?) {
                    resource.setLoopCount(1)
                    imageView.setImageDrawable(resource)
                    resource.start()
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    // Handle the placeholder if necessary
                }
            })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}