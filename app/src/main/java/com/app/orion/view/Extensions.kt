package com.app.orion.view

import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.gkemon.XMLtoPDF.PdfGenerator
import com.gkemon.XMLtoPDF.PdfGeneratorListener

fun AppCompatActivity.generatePdf(layout: ConstraintLayout){
    //val xmlToPDFLifecycleObserver = PdfGenerator.XmlToPDFLifecycleObserver(this)
    //lifecycle.addObserver(xmlToPDFLifecycleObserver)
    PdfGenerator.getBuilder()
        .setContext(this)
        .fromViewSource()
        .fromView(layout)
        .setFileName("Test-PDF")
        //.setFolderName("FolderA/FolderB/FolderC")
        .actionAfterPDFGeneration(PdfGenerator.ActionAfterPDFGeneration.SHARE)
        //.savePDFSharedStorage(xmlToPDFLifecycleObserver)
        .build(object : PdfGeneratorListener() {
            override fun onStartPDFGeneration() {

            }

            override fun onFinishPDFGeneration() {

            }

        })
}