package com.app.orion.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.app.orion.R

class RenewalFragment : ContentPreviewFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_renewal, container, false)

    override fun getSubtitle(): String = resources.getString(R.string.title_receipt)

}