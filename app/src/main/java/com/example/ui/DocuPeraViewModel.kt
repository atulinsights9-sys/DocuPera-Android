package com.example.ui

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ai.GeminiDocumentService
import com.example.data.local.AppDatabase
import com.example.data.local.DocumentRepository
import com.example.data.local.UserPreferencesRepository
import com.example.data.model.*
import com.example.data.pdf.PdfExportService
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File

enum class ScreenRoute {
    LANDING,
    HOME,
    AI_BUILDER,
    EDITOR,
    TEMPLATES,
    TOOLS,
    DOCUMENTS,
    BILLING,
    BUSINESS_MODE,
    ADMIN
}

sealed interface AiGenerationState {
    object Idle : AiGenerationState
    data class AskingQuestions(val prompt: String, val questions: List<String>) : AiGenerationState
    data class Generating(val step: String) : AiGenerationState
    data class Completed(val document: DocumentData) : AiGenerationState
    data class Error(val message: String) : AiGenerationState
}

class DocuPeraViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    private val repository = DocumentRepository(db.documentDao())
    val preferences = UserPreferencesRepository(application)
    private val aiService = GeminiDocumentService()
    val pdfService = PdfExportService(application)

    // Navigation state
    private val _currentScreen = MutableStateFlow(ScreenRoute.HOME)
    val currentScreen: StateFlow<ScreenRoute> = _currentScreen.asStateFlow()

    // History and counts
    val allDocuments: StateFlow<List<DocumentData>> = repository.allDocuments
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val documentCount: StateFlow<Int> = repository.documentCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val credits: StateFlow<Int> = preferences.credits
    val currentPlan: StateFlow<PlanType> = preferences.currentPlan
    val pricingConfig: StateFlow<PricingConfig> = preferences.pricingConfig

    // Active Document Editor State
    private val _currentDocument = MutableStateFlow<DocumentData?>(null)
    val currentDocument: StateFlow<DocumentData?> = _currentDocument.asStateFlow()

    private val _editorUndoStack = mutableListOf<DocumentData>()
    private val _editorRedoStack = mutableListOf<DocumentData>()

    // AI Document Builder State
    private val _aiState = MutableStateFlow<AiGenerationState>(AiGenerationState.Idle)
    val aiState: StateFlow<AiGenerationState> = _aiState.asStateFlow()

    val promptInput = MutableStateFlow("")
    val selectedStyle = MutableStateFlow(DocStyle.PROFESSIONAL)
    val selectedLanguage = MutableStateFlow(DocLanguage.EN)
    val questionAnswers = MutableStateFlow<Map<String, String>>(emptyMap())

    // AI Writing Tools State
    private val _aiWritingStatus = MutableStateFlow<String?>(null)
    val aiWritingStatus: StateFlow<String?> = _aiWritingStatus.asStateFlow()

    // Photo to PDF State
    private val _photoPages = MutableStateFlow<List<PhotoDocPage>>(emptyList())
    val photoPages: StateFlow<List<PhotoDocPage>> = _photoPages.asStateFlow()

    // Screenshot to Document State
    private val _extractedEntities = MutableStateFlow<ExtractedDocumentEntities?>(null)
    val extractedEntities: StateFlow<ExtractedDocumentEntities?> = _extractedEntities.asStateFlow()

    private val _isAnalyzingImage = MutableStateFlow(false)
    val isAnalyzingImage: StateFlow<Boolean> = _isAnalyzingImage.asStateFlow()

    // Monetization Paywall Modal
    private val _showPaywallModal = MutableStateFlow(false)
    val showPaywallModal: StateFlow<Boolean> = _showPaywallModal.asStateFlow()

    // Status Message / Toast
    private val _userMessage = MutableStateFlow<String?>(null)
    val userMessage: StateFlow<String?> = _userMessage.asStateFlow()

    // Search & Filter
    val searchQuery = MutableStateFlow("")
    val selectedCategoryFilter = MutableStateFlow<DocCategory?>(null)

    init {
        viewModelScope.launch {
            repository.seedInitialSampleDocumentsIfEmpty()
        }
    }

    fun navigateTo(route: ScreenRoute) {
        _currentScreen.value = route
    }

    fun clearUserMessage() {
        _userMessage.value = null
    }

    fun showToast(msg: String) {
        _userMessage.value = msg
    }

    // --- AI Builder Flow ---
    fun startAiCreation(initialPrompt: String = "") {
        promptInput.value = initialPrompt
        questionAnswers.value = emptyMap()
        _aiState.value = AiGenerationState.Idle
        navigateTo(ScreenRoute.AI_BUILDER)
    }

    fun submitPromptForQuestions() {
        val prompt = promptInput.value.trim()
        if (prompt.isBlank()) {
            showToast("Please describe the document you need.")
            return
        }

        viewModelScope.launch {
            _aiState.value = AiGenerationState.Generating("Understanding your request...")
            val questions = aiService.generateClarificationQuestions(prompt, selectedLanguage.value)
            _aiState.value = AiGenerationState.AskingQuestions(prompt, questions)
        }
    }

    fun updateQuestionAnswer(question: String, answer: String) {
        val current = questionAnswers.value.toMutableMap()
        current[question] = answer
        questionAnswers.value = current
    }

    fun generateFinalDocument() {
        val prompt = promptInput.value.trim()
        viewModelScope.launch {
            _aiState.value = AiGenerationState.Generating("Structuring document sections...")
            val doc = aiService.generateDocument(
                userPrompt = prompt,
                details = questionAnswers.value,
                style = selectedStyle.value,
                language = selectedLanguage.value
            )
            val savedId = repository.saveDocument(doc)
            val savedDoc = doc.copy(id = savedId)
            _currentDocument.value = savedDoc
            _aiState.value = AiGenerationState.Completed(savedDoc)
            _editorUndoStack.clear()
            _editorRedoStack.clear()
            navigateTo(ScreenRoute.EDITOR)
            showToast("Document successfully created!")
        }
    }

    // --- Document Editor Actions ---
    fun openDocumentForEditing(doc: DocumentData) {
        _currentDocument.value = doc
        _editorUndoStack.clear()
        _editorRedoStack.clear()
        navigateTo(ScreenRoute.EDITOR)
    }

    fun updateCurrentDocument(newDoc: DocumentData) {
        val current = _currentDocument.value
        if (current != null) {
            _editorUndoStack.add(current)
            _editorRedoStack.clear()
        }
        _currentDocument.value = newDoc
        viewModelScope.launch {
            repository.saveDocument(newDoc)
        }
    }

    fun undoEdit() {
        if (_editorUndoStack.isNotEmpty()) {
            val previous = _editorUndoStack.removeAt(_editorUndoStack.lastIndex)
            val current = _currentDocument.value
            if (current != null) {
                _editorRedoStack.add(current)
            }
            _currentDocument.value = previous
            viewModelScope.launch { repository.saveDocument(previous) }
        }
    }

    fun redoEdit() {
        if (_editorRedoStack.isNotEmpty()) {
            val next = _editorRedoStack.removeAt(_editorRedoStack.lastIndex)
            val current = _currentDocument.value
            if (current != null) {
                _editorUndoStack.add(current)
            }
            _currentDocument.value = next
            viewModelScope.launch { repository.saveDocument(next) }
        }
    }

    fun applyAiWritingToSection(sectionId: String, actionName: String) {
        val doc = _currentDocument.value ?: return
        val section = doc.sections.find { it.id == sectionId } ?: return
        val originalText = if (section.body.isNotBlank()) section.body else section.bulletPoints.joinToString("\n")

        viewModelScope.launch {
            _aiWritingStatus.value = "AI applying: $actionName..."
            val result = aiService.applyAiWritingTool(originalText, actionName, doc.language)
            val updatedSections = doc.sections.map { sec ->
                if (sec.id == sectionId) {
                    sec.copy(body = result, bulletPoints = emptyList())
                } else sec
            }
            updateCurrentDocument(doc.copy(sections = updatedSections))
            _aiWritingStatus.value = null
            showToast("Section updated with AI ($actionName)")
        }
    }

    fun addSectionToCurrentDocument() {
        val doc = _currentDocument.value ?: return
        val newSec = DocumentSection(
            id = "sec_${System.currentTimeMillis()}",
            heading = "New Section",
            body = "Add your content here..."
        )
        updateCurrentDocument(doc.copy(sections = doc.sections + newSec))
    }

    fun deleteSection(sectionId: String) {
        val doc = _currentDocument.value ?: return
        updateCurrentDocument(doc.copy(sections = doc.sections.filterNot { it.id == sectionId }))
    }

    // --- Export PDF Flow & Credit Deduction ---
    fun requestPdfExport(onExportSuccess: (File) -> Unit) {
        val doc = _currentDocument.value ?: return
        val hasPlan = currentPlan.value != PlanType.FREE
        val hasCredits = credits.value >= 1

        if (!hasPlan && !hasCredits) {
            _showPaywallModal.value = true
            return
        }

        // Deduct 1 credit or unlimited if pro
        val success = preferences.deductCredit(1)
        if (success) {
            viewModelScope.launch {
                val isPro = currentPlan.value != PlanType.FREE
                val customFooter = if (currentPlan.value == PlanType.BUSINESS) preferences.businessFooter.value else null
                val file = pdfService.generateDocumentPdf(doc, isPro, customFooter)
                showToast("PDF exported successfully!")
                onExportSuccess(file)
            }
        } else {
            _showPaywallModal.value = true
        }
    }

    fun dismissPaywall() {
        _showPaywallModal.value = false
    }

    fun purchaseCredits(pack: CreditPack) {
        preferences.addCredits(pack.credits)
        showToast("Purchased ${pack.credits} credits successfully!")
        _showPaywallModal.value = false
    }

    fun upgradeSubscription(plan: PlanType) {
        preferences.setPlan(plan)
        showToast("Upgraded to ${plan.displayName}!")
        _showPaywallModal.value = false
    }

    // --- Templates Flow ---
    fun useTemplate(template: DocumentTemplate) {
        val doc = template.initialData.copy(
            id = 0,
            title = template.title,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        viewModelScope.launch {
            val id = repository.saveDocument(doc)
            openDocumentForEditing(doc.copy(id = id))
        }
    }

    // --- Photo to PDF Tool ---
    fun addPhotoPages(uris: List<Uri>) {
        val current = _photoPages.value.toMutableList()
        var startOrder = current.size
        for (uri in uris) {
            current.add(
                PhotoDocPage(
                    id = "photo_${System.currentTimeMillis()}_${startOrder++}",
                    imageUri = uri,
                    order = startOrder
                )
            )
        }
        _photoPages.value = current
    }

    fun updatePhotoPage(page: PhotoDocPage) {
        val list = _photoPages.value.map { if (it.id == page.id) page else it }
        _photoPages.value = list
    }

    fun removePhotoPage(pageId: String) {
        _photoPages.value = _photoPages.value.filterNot { it.id == pageId }
    }

    fun reorderPhotoPages(from: Int, to: Int) {
        val list = _photoPages.value.toMutableList()
        if (from in list.indices && to in list.indices) {
            val item = list.removeAt(from)
            list.add(to, item)
            _photoPages.value = list
        }
    }

    fun exportPhotoDocToPdf(title: String, onComplete: (File) -> Unit) {
        val pages = _photoPages.value
        if (pages.isEmpty()) {
            showToast("Please add at least one photo")
            return
        }
        viewModelScope.launch {
            val file = pdfService.generatePhotosToPdf(pages, title)
            showToast("Multi-page Photo PDF created!")
            onComplete(file)
        }
    }

    // --- Screenshot to Document Analyzer ---
    fun analyzeScreenshot(bitmap: Bitmap) {
        viewModelScope.launch {
            _isAnalyzingImage.value = true
            val entities = aiService.analyzeImageOrScreenshot(bitmap, selectedLanguage.value)
            _extractedEntities.value = entities
            _isAnalyzingImage.value = false
        }
    }

    fun createDocumentFromExtractedEntities(entities: ExtractedDocumentEntities) {
        val doc = DocumentData(
            title = entities.title.ifBlank { "Cleaned Document from Image" },
            type = DocType.CUSTOM_DOC,
            category = DocCategory.CONTENT,
            style = DocStyle.PROFESSIONAL,
            metadata = DocumentMetadata(
                senderName = entities.names.firstOrNull() ?: "",
                senderContact = entities.phoneNumbers.firstOrNull() ?: "",
                senderAddress = entities.addresses.firstOrNull() ?: "",
                dateString = entities.dates.firstOrNull() ?: "19 Sep 2026",
                refNumber = "Ref: Extracted Visual Source"
            ),
            sections = listOf(
                DocumentSection(
                    id = "summary",
                    heading = "Document Summary",
                    body = entities.keySummary
                ),
                DocumentSection(
                    id = "details",
                    heading = "Key Details & Records",
                    bulletPoints = (entities.dates.map { "Date: $it" } +
                            entities.amounts.map { "Amount: $it" } +
                            entities.names.map { "Name: $it" } +
                            entities.phoneNumbers.map { "Contact: $it" }).ifEmpty {
                        listOf("Extracted data verified from visual scan")
                    }
                ),
                DocumentSection(
                    id = "full_text",
                    heading = "Complete Transcript",
                    body = entities.fullRawText
                )
            )
        )
        viewModelScope.launch {
            val id = repository.saveDocument(doc)
            openDocumentForEditing(doc.copy(id = id))
        }
    }

    // --- Documents History Management ---
    fun deleteDocument(id: Long) {
        viewModelScope.launch {
            repository.deleteDocument(id)
            showToast("Document deleted")
        }
    }

    fun duplicateDocument(doc: DocumentData) {
        viewModelScope.launch {
            repository.duplicateDocument(doc)
            showToast("Document duplicated")
        }
    }

    fun renameDocument(doc: DocumentData, newTitle: String) {
        viewModelScope.launch {
            repository.saveDocument(doc.copy(title = newTitle))
            showToast("Renamed to $newTitle")
        }
    }
}
