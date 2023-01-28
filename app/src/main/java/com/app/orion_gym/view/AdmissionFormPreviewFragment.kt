package com.app.orion_gym.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.orion_gym.R
import com.app.orion_gym.databinding.ContentPreviewBinding
import com.app.orion_gym.databinding.FragmentAdmissionFormBinding

class AdmissionFormPreviewFragment : ContentPreviewFragment() {

    private lateinit var fragmentAdmissionFormBinding : FragmentAdmissionFormBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentAdmissionFormBinding = FragmentAdmissionFormBinding.inflate(inflater,container,false)
        return fragmentAdmissionFormBinding.root
    }

    override fun getContentPreviewBinding(): ContentPreviewBinding = fragmentAdmissionFormBinding.contentPreview

    override fun getSubtitle(): String = resources.getString(R.string.title_admission_form)

}