package hs.kr.equus.application.global.document.pdf.generator

import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.kernel.utils.PdfMerger
import com.itextpdf.layout.Document
import hs.kr.equus.application.global.document.pdf.facade.PdfDocumentFacade
import java.io.ByteArrayOutputStream
import org.springframework.stereotype.Component

@Component
class ApplicationPdfGenerator(
    private val pdfProcessor: PdfProcessor,
    private val templateProcessor: TemplateProcessor,
    private val pdfDocumentFacade: PdfDocumentFacade
) {

    fun generateApplicationPdf(): ByteArray {

        val templates = listOf("/test.html")

        val outStream = templates.parallelStream()
            .map { template ->
                templateProcessor.convertTemplateIntoHtmlString(template, mutableMapOf())
            }
            .map { html ->
                pdfProcessor.convertHtmlToPdf(html)
            }
            .toArray { size -> arrayOfNulls<ByteArrayOutputStream>(size) }

        val outputStream = ByteArrayOutputStream()
        val mergedDocument = PdfDocument(PdfWriter(outputStream))
        val pdfMerger = PdfMerger(mergedDocument)
        val document = Document(mergedDocument)

        for (pdfStream in outStream) {
            val pdfDoc = pdfDocumentFacade.getPdfDocument(pdfStream!!)
            mergeDocument(pdfMerger, pdfDoc)
        }

        document.close()

        return outputStream.toByteArray()
    }

    private fun mergeDocument(merger: PdfMerger, document: PdfDocument?) {
        if (document != null) {
            merger.merge(document, 1, document.numberOfPages)
            document.close()
        }
    }
}
