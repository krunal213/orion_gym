package com.app.orion_gym

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import java.io.File
import android.print.PDFPrint.OnPDFPrintListener
import com.tejpratapsingh.pdfcreator.utils.PDFUtil
import com.tejpratapsingh.pdfcreator.utils.FileManager
import androidx.core.content.FileProvider

fun AppCompatActivity.generatePdfFromHTML(inputHTML : String,fileName : String) {
    PDFUtil.generatePDFFromHTML(applicationContext, FileManager.getInstance()
        .createTempFileWithName(applicationContext, "$fileName.pdf", false),
        inputHTML, object : OnPDFPrintListener {
            override fun onSuccess(file: File) {
                startActivity(with(Intent()){
                    action = Intent.ACTION_SEND
                    type = "application/pdf"
                    putExtra(Intent.EXTRA_STREAM,FileProvider.getUriForFile(
                        this@generatePdfFromHTML,
                        this@generatePdfFromHTML.applicationContext.packageName.toString() + ".provider",
                        file
                    ))
                })
            }
            override fun onError(exception: Exception) {

            }
        })
}