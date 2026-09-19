package com.example.data.model

enum class DocCategory(val displayName: String) {
    PERSONAL("Personal"),
    CAREER("Career"),
    BUSINESS("Business"),
    EDUCATION("Education"),
    CONTENT("Content"),
    CUSTOM("Custom")
}

enum class DocType(val displayName: String, val category: DocCategory) {
    // Personal
    FORMAL_LETTER("Formal Letter", DocCategory.PERSONAL),
    APPLICATION("Application", DocCategory.PERSONAL),
    LEAVE_APPLICATION("Leave Application", DocCategory.PERSONAL),
    REQUEST_LETTER("Request Letter", DocCategory.PERSONAL),
    COMPLAINT_LETTER("Complaint Letter", DocCategory.PERSONAL),
    DECLARATION("Declaration", DocCategory.PERSONAL),
    PERSONAL_STATEMENT("Personal Statement", DocCategory.PERSONAL),

    // Career
    RESUME("Resume / CV", DocCategory.CAREER),
    COVER_LETTER("Cover Letter", DocCategory.CAREER),
    JOB_APPLICATION("Job Application", DocCategory.CAREER),
    PROFESSIONAL_PROFILE("Professional Profile", DocCategory.CAREER),
    EXPERIENCE_LETTER("Experience Letter", DocCategory.CAREER),

    // Business
    INVOICE("Invoice", DocCategory.BUSINESS),
    QUOTATION("Quotation", DocCategory.BUSINESS),
    ESTIMATE("Estimate", DocCategory.BUSINESS),
    RECEIPT("Receipt", DocCategory.BUSINESS),
    BUSINESS_PROPOSAL("Business Proposal", DocCategory.BUSINESS),
    BUSINESS_LETTER("Business Letter", DocCategory.BUSINESS),
    PURCHASE_ORDER("Purchase Order", DocCategory.BUSINESS),
    PAYMENT_REMINDER("Payment Reminder", DocCategory.BUSINESS),

    // Education
    ASSIGNMENT("Assignment", DocCategory.EDUCATION),
    PROJECT_REPORT("Project Report", DocCategory.EDUCATION),
    NOTES("Study Notes", DocCategory.EDUCATION),
    CERTIFICATE("Certificate", DocCategory.EDUCATION),
    STUDY_MATERIAL("Study Material", DocCategory.EDUCATION),
    PROJECT_COVER_PAGE("Project Cover Page", DocCategory.EDUCATION),

    // Content
    EBOOK("Ebook / Guide", DocCategory.CONTENT),
    GUIDE("Instructional Guide", DocCategory.CONTENT),
    REPORT("Formal Report", DocCategory.CONTENT),
    CHECKLIST("Checklist", DocCategory.CONTENT),
    MEETING_NOTES("Meeting Minutes", DocCategory.CONTENT),
    CUSTOM_DOC("Custom Document", DocCategory.CONTENT)
}

enum class DocStyle(val displayName: String, val description: String) {
    FORMAL("Formal", "Traditional, structured, and authoritative"),
    PROFESSIONAL("Professional", "Crisp, business-ready, clean typography"),
    MODERN("Modern", "Sleek margins, bold headers, contemporary flow"),
    MINIMAL("Minimal", "Clean negative space, high readability"),
    ACADEMIC("Academic", "Standardized citation and scholarly layout"),
    BUSINESS("Business", "Corporate structure with tabular highlights"),
    ELEGANT("Elegant", "Refined spacing, serif accents, executive finish")
}

enum class DocLanguage(val code: String, val displayName: String, val nativeName: String) {
    EN("en", "English", "English"),
    HI("hi", "Hindi", "हिन्दी"),
    MR("mr", "Marathi", "मराठी")
}

enum class DocPageSize(val displayName: String, val widthPt: Int, val heightPt: Int) {
    A4("A4 (210 × 297 mm)", 595, 842),
    LETTER("US Letter (8.5 × 11 in)", 612, 792)
}

enum class DocOrientation {
    PORTRAIT,
    LANDSCAPE
}

data class DocumentSection(
    val id: String,
    val heading: String = "",
    val body: String = "",
    val bulletPoints: List<String> = emptyList(),
    val isHeader: Boolean = false,
    val isSignature: Boolean = false
)

data class DocumentMetadata(
    val senderName: String = "",
    val senderTitle: String = "",
    val senderAddress: String = "",
    val senderContact: String = "",
    val recipientName: String = "",
    val recipientTitle: String = "",
    val recipientAddress: String = "",
    val dateString: String = "",
    val refNumber: String = "",
    val clientName: String = "",
    val currencySymbol: String = "₹",
    val amountTotal: String = "",
    val customFields: Map<String, String> = emptyMap()
)

data class DocumentData(
    val id: Long = 0,
    val title: String,
    val type: DocType,
    val category: DocCategory,
    val style: DocStyle = DocStyle.PROFESSIONAL,
    val language: DocLanguage = DocLanguage.EN,
    val pageSize: DocPageSize = DocPageSize.A4,
    val orientation: DocOrientation = DocOrientation.PORTRAIT,
    val metadata: DocumentMetadata = DocumentMetadata(),
    val sections: List<DocumentSection> = emptyList(),
    val footerText: String = "DocuPera Document Studio",
    val showPageNumbers: Boolean = true,
    val isPro: Boolean = false,
    val isWatermarked: Boolean = true,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
