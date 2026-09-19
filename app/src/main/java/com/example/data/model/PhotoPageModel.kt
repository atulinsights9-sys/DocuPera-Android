package com.example.data.model

import android.net.Uri

enum class PhotoFilter {
    ORIGINAL,
    BLACK_WHITE,
    DOCUMENT_CLEANUP,
    HIGH_CONTRAST
}

data class PhotoDocPage(
    val id: String,
    val imageUri: Uri,
    val rotationDegrees: Float = 0f,
    val brightness: Float = 1.0f,
    val contrast: Float = 1.0f,
    val filter: PhotoFilter = PhotoFilter.DOCUMENT_CLEANUP,
    val order: Int = 0
)

data class ExtractedDocumentEntities(
    val detectedType: String = "Unknown Document",
    val title: String = "",
    val names: List<String> = emptyList(),
    val dates: List<String> = emptyList(),
    val amounts: List<String> = emptyList(),
    val addresses: List<String> = emptyList(),
    val phoneNumbers: List<String> = emptyList(),
    val keySummary: String = "",
    val fullRawText: String = "",
    val suggestedActions: List<String> = listOf("Create clean document", "Extract text", "Create PDF", "Summarize", "Translate")
)
