package hs.kr.equus.application.global

import hs.kr.equus.application.global.document.pdf.generator.ApplicationPdfGenerator
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController(
    val applicationPdfGenerator: ApplicationPdfGenerator
) {
    @GetMapping("/test")
    fun test(): ByteArray {
        println("claasspath: "+System.getProperty("java.class.path"))
        return applicationPdfGenerator.generateApplicationPdf()
    }
}