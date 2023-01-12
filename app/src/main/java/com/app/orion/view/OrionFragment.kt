package com.app.orion.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController
import com.app.orion.R

class OrionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_orion, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.buttonAdmissionForm)
            .setOnClickListener {
                view.findNavController().navigate(R.id.action_orionFragment_to_admissionFormFragment)
            }

        view.findViewById<Button>(R.id.buttonReceipt)
            .setOnClickListener {
                view.findNavController().navigate(R.id.action_orionFragment_to_receiptFormFragment)
            }
    }

}