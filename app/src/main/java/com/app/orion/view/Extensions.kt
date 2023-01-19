package com.app.orion.view

import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.gkemon.XMLtoPDF.PdfGenerator
import com.gkemon.XMLtoPDF.PdfGeneratorListener
import com.gkemon.XMLtoPDF.model.SuccessResponse
import androidx.core.content.ContextCompat.startActivity

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.print.PrintHelper
import com.google.android.material.dialog.MaterialAlertDialogBuilder


fun AppCompatActivity.generatePdfWithShare(layout: View) {
    PdfGenerator.getBuilder()
        .setContext(this)
        .fromViewSource()
        .fromView(layout)
        .setFileName("Test-PDF")
        //.setFolderName("FolderA/FolderB/FolderC")
        .actionAfterPDFGeneration(PdfGenerator.ActionAfterPDFGeneration.SHARE)
        //.savePDFSharedStorage(xmlToPDFLifecycleObserver)
        .build(object : PdfGeneratorListener() {
            override fun onStartPDFGeneration() {}
            override fun onFinishPDFGeneration() {}
        })
}

fun AppCompatActivity.generatePdf(layout: View) {
    PdfGenerator.getBuilder()
        .setContext(this)
        .fromViewSource()
        .fromView(layout)
        .setFileName("Test-PDF")
        //.setFolderName("FolderA/FolderB/FolderC")
        .actionAfterPDFGeneration(PdfGenerator.ActionAfterPDFGeneration.NONE)
        //.savePDFSharedStorage(xmlToPDFLifecycleObserver)
        .build(object : PdfGeneratorListener() {
            override fun onStartPDFGeneration() {}
            override fun onFinishPDFGeneration() {}
            override fun onSuccess(response: SuccessResponse?) {
                /*try {
                    with(Intent("org.androidprinting.intent.action.PRINT")){
                        intent.data = Uri.fromFile(response?.file)
                        if (intent.resolveActivity(packageManager) != null) {
                            startActivity(intent)
                        }
                    }
                }catch (ex : Exception){
                    MaterialAlertDialogBuilder(this@generatePdf)
                        .setMessage(ex.message)
                        .show()
                    //Toast.makeText(this@generatePdf,ex.message,Toast.LENGTH_LONG).show()
                }*/

                //super.onSuccess(response)
            }
        })
}