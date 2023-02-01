package com.app.orion_gym.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.orion_gym.R
import com.app.orion_gym.databinding.ContentPreviewBinding
import com.app.orion_gym.databinding.FragmentRenewalBinding

class RenewalFragment : ContentPreviewFragment() {

    private lateinit var fragmentRenewalBinding : FragmentRenewalBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentRenewalBinding = FragmentRenewalBinding.inflate(inflater,container,false)
        return fragmentRenewalBinding.root
    }

    override fun getContentPreviewBinding(): ContentPreviewBinding = fragmentRenewalBinding.contentPreview

    override fun getSubtitle(): String = resources.getString(R.string.title_receipt)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentRenewalBinding
            .buttonSubmit
            .setOnClickListener(this)
    }

}