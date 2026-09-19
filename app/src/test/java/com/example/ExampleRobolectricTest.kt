package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.ai.GeminiDocumentService
import com.example.data.local.UserPreferencesRepository
import com.example.data.model.*
import com.example.data.pdf.PdfExportService
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun testAppNameString() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("DocuPera", appName)
    }

    @Test
    fun testStarterCreditsAndMonetization() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val prefs = UserPreferencesRepository(context)

        // Verify starter credits (5 credits)
        assertTrue(prefs.credits.value >= 5)

        // Deduct 1 credit for export
        val initialCredits = prefs.credits.value
        val deducted = prefs.deductCredit(1)
        assertTrue(deducted)
        assertEquals(initialCredits - 1, prefs.credits.value)

        // Test promo code redemption
        val (success, _) = prefs.redeemPromoCode("DOCUFREE")
        assertTrue(success)
        assertEquals(initialCredits - 1 + 5, prefs.credits.value)

        // Upgrade plan
        prefs.setPlan(PlanType.PRO)
        assertEquals(PlanType.PRO, prefs.currentPlan.value)

        // In Pro plan, deductCredit returns true without depleting credits
        val proCreditsBefore = prefs.credits.value
        val proDeduct = prefs.deductCredit(1)
        assertTrue(proDeduct)
        assertEquals(proCreditsBefore, prefs.credits.value)
    }

    @Test
    fun testAiDocumentServiceSynthesis() = runBlocking {
        val aiService = GeminiDocumentService()
        val questions = aiService.generateClarificationQuestions("I need a bank loan application for ₹5,00,000")
        assertTrue(questions.isNotEmpty())

        val answers = mapOf(
            questions.first() to "Aditya Sharma",
            "Bank" to "State Bank of India",
            "Amount" to "₹ 5,00,000"
        )

        val doc = aiService.generateDocument(
            userPrompt = "Bank loan application",
            details = answers,
            style = DocStyle.PROFESSIONAL,
            language = DocLanguage.EN
        )

        assertNotNull(doc)
        assertTrue(doc.title.isNotBlank())
        assertTrue(doc.sections.isNotEmpty())
    }

    @Test
    fun testPdfExportServiceGeneratesRealFile() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val pdfService = PdfExportService(context)

        val testDoc = DocumentData(
            title = "Commercial Agreement",
            type = DocType.BUSINESS_PROPOSAL,
            category = DocCategory.BUSINESS,
            style = DocStyle.BUSINESS,
            metadata = DocumentMetadata(
                senderName = "Acme Corp",
                recipientName = "Client Ltd",
                dateString = "19 Sep 2026",
                refNumber = "REF-2026-001",
                amountTotal = "₹ 50,000"
            ),
            sections = listOf(
                DocumentSection(
                    id = "s1",
                    heading = "Executive Summary",
                    body = "This is a test generated agreement using DocuPera export engine."
                ),
                DocumentSection(
                    id = "s2",
                    heading = "Key Deliverables",
                    bulletPoints = listOf("Deliverable 1", "Deliverable 2", "Deliverable 3")
                )
            )
        )

        val generatedFile = pdfService.generateDocumentPdf(testDoc, isProUser = false)
        assertNotNull(generatedFile)
        assertTrue(generatedFile.exists())
        assertTrue(generatedFile.length() > 0)
    }

    @Test
    fun testTemplateLibraryIntegrity() {
        val templates = TemplateLibrary.templates
        assertTrue(templates.isNotEmpty())
        for (tmpl in templates) {
            assertTrue(tmpl.title.isNotBlank())
            assertTrue(tmpl.initialData.sections.isNotEmpty())
        }

        val packs = TemplateLibrary.templatePacks
        assertTrue(packs.size >= 5)
    }
}
