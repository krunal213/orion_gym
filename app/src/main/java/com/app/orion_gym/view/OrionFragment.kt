package com.app.orion_gym.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.app.orion_gym.R
import com.app.orion_gym.databinding.FragmentOrionBinding
import com.app.orion_gym.viewmodel.OrionViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.Task
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.util.ExponentialBackOff
import com.google.api.services.sheets.v4.SheetsScopes
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class OrionFragment : Fragment() {

    private val orionViewModel by activityViewModels<OrionViewModel>()
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN: Int = 2
    private lateinit var fragmentOrionBinding: FragmentOrionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentOrionBinding = FragmentOrionBinding.inflate(inflater, container, false)
        return fragmentOrionBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentOrionBinding.buttonAdmissionForm
            .setOnClickListener {
                it.findNavController()
                    .navigate(R.id.action_orionFragment_to_admissionFormFragment)
            }
        fragmentOrionBinding.buttonReceipt
            .setOnClickListener {
                it.findNavController()
                    .navigate(R.id.action_orionFragment_to_receiptFormFragment)
            }
        initGoogleSignInOptions()
    }

    private fun initGoogleSignInOptions() {
        mGoogleSignInClient = GoogleSignIn.getClient(
            requireActivity(),
            GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(Scope(SheetsScopes.SPREADSHEETS))
                .requestScopes(Scope(SheetsScopes.SPREADSHEETS_READONLY))
                .build()
        )
        val account = GoogleSignIn.getLastSignedInAccount(requireContext())
        if (account != null) {
            initGoogleAccountCredential(account)
        } else {
            signIn()
        }
    }

    private fun signIn() {
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                handleSignInResult(GoogleSignIn.getSignedInAccountFromIntent(it.data))
            }
        }.launch(mGoogleSignInClient.signInIntent)
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        val account = completedTask.getResult(ApiException::class.java)
        initGoogleAccountCredential(account)
    }

    private fun initGoogleAccountCredential(account: GoogleSignInAccount?) {
        val googleAccountCredential = GoogleAccountCredential
            .usingOAuth2(requireContext(), Arrays.asList(SheetsScopes.SPREADSHEETS_READONLY))
            .setBackOff(ExponentialBackOff())
        googleAccountCredential.selectedAccount = account?.account
        orionViewModel.googleAccountCredential = googleAccountCredential
    }

}