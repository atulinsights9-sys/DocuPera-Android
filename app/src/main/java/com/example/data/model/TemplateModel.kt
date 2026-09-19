package com.example.data.model

data class DocumentTemplate(
    val id: String,
    val title: String,
    val subtitle: String,
    val docType: DocType,
    val category: DocCategory,
    val style: DocStyle,
    val isPro: Boolean,
    val packId: String? = null,
    val initialData: DocumentData
)

data class TemplatePack(
    val id: String,
    val title: String,
    val description: String,
    val templateCount: Int,
    val isPro: Boolean,
    val priceInr: Int = 199,
    val badge: String = "Popular"
)

object TemplateLibrary {
    val templatePacks = listOf(
        TemplatePack(
            id = "job_seeker",
            title = "Job Seeker Pro Pack",
            description = "High-impact resumes, ATS-friendly cover letters, salary request & experience letters.",
            templateCount = 8,
            isPro = true,
            badge = "Bestseller"
        ),
        TemplatePack(
            id = "small_business",
            title = "Small Business Growth Pack",
            description = "GST-compliant invoices, itemized quotations, receipts, purchase orders, payment reminders.",
            templateCount = 10,
            isPro = true,
            badge = "Essential"
        ),
        TemplatePack(
            id = "student_pack",
            title = "Student & Academic Pack",
            description = "College project reports, formal internship applications, seminar notes, certificate layouts.",
            templateCount = 6,
            isPro = false,
            badge = "Free Tier"
        ),
        TemplatePack(
            id = "freelancer_pack",
            title = "Freelancer Toolkit",
            description = "Client proposals, scope of work contracts, milestone invoices, NDAs.",
            templateCount = 7,
            isPro = true,
            badge = "Pro"
        ),
        TemplatePack(
            id = "creator_pack",
            title = "Creator & Media Kit Pack",
            description = "Ebooks, brand collaboration pitch, media kit summary, release forms.",
            templateCount = 5,
            isPro = true,
            badge = "Creator"
        )
    )

    val templates = listOf(
        // CAREER
        DocumentTemplate(
            id = "career_resume_modern",
            title = "Modern Tech Resume",
            subtitle = "Clean two-column header with ATS-optimized typography",
            docType = DocType.RESUME,
            category = DocCategory.CAREER,
            style = DocStyle.MODERN,
            isPro = false,
            packId = "job_seeker",
            initialData = DocumentData(
                title = "Senior Software Engineer Resume",
                type = DocType.RESUME,
                category = DocCategory.CAREER,
                style = DocStyle.MODERN,
                metadata = DocumentMetadata(
                    senderName = "Rahul Sharma",
                    senderTitle = "Lead Mobile Architect",
                    senderContact = "rahul.sharma@email.com | +91 98765 43210 | Bengaluru, India",
                    senderAddress = "LinkedIn: linkedin.com/in/rahulsharma | GitHub: github.com/rahul"
                ),
                sections = listOf(
                    DocumentSection(
                        id = "summary",
                        heading = "Professional Summary",
                        body = "Results-driven Software Engineer with 6+ years of expertise in building enterprise-scale Android and cross-platform applications. Proven track record of architecting apps serving 2M+ active users with high crash-free rates and top-tier UI responsiveness."
                    ),
                    DocumentSection(
                        id = "skills",
                        heading = "Core Competencies",
                        bulletPoints = listOf(
                            "Kotlin, Jetpack Compose, Coroutines & Flow, Modern Android Architecture (MVVM/MVI)",
                            "Room Database, SQLite, Offline-first caching, RESTful APIs, OkHttp & Retrofit",
                            "Unit & UI Testing, CI/CD pipeline automation, Google Play Store compliance & release"
                        )
                    ),
                    DocumentSection(
                        id = "experience",
                        heading = "Work Experience",
                        body = "Senior Android Engineer — FinTech Solutions Pvt Ltd (2022 - Present)\n• Architected modern banking flow reducing transaction drop-offs by 18%.\n• Spearheaded migration from legacy XML to Jetpack Compose across 40+ screens.\n\nSoftware Engineer — Digital Ventures (2019 - 2022)\n• Built responsive customer dashboard and real-time push notification system."
                    ),
                    DocumentSection(
                        id = "education",
                        heading = "Education & Certifications",
                        body = "Bachelor of Technology in Computer Science — Pune University (2015 - 2019)\nGoogle Certified Associate Android Developer (AAD)"
                    )
                )
            )
        ),
        DocumentTemplate(
            id = "career_cover_letter_pro",
            title = "Executive Cover Letter",
            subtitle = "High-persuasion formal letter for competitive roles",
            docType = DocType.COVER_LETTER,
            category = DocCategory.CAREER,
            style = DocStyle.PROFESSIONAL,
            isPro = false,
            packId = "job_seeker",
            initialData = DocumentData(
                title = "Cover Letter - Senior Product Manager",
                type = DocType.COVER_LETTER,
                category = DocCategory.CAREER,
                style = DocStyle.PROFESSIONAL,
                metadata = DocumentMetadata(
                    senderName = "Ananya Sen",
                    senderTitle = "Product Strategist",
                    senderAddress = "Mumbai, Maharashtra | +91 99887 66554",
                    recipientName = "Hiring Team",
                    recipientTitle = "Director of Talent Acquisition",
                    recipientAddress = "Global Tech Labs, Cyber City, Gurugram",
                    dateString = "September 19, 2026",
                    refNumber = "Ref: Application for Senior Product Manager (Job ID: GTL-894)"
                ),
                sections = listOf(
                    DocumentSection(
                        id = "salutation",
                        heading = "",
                        body = "Dear Hiring Committee,"
                    ),
                    DocumentSection(
                        id = "intro",
                        heading = "Interest in Senior Product Leadership",
                        body = "I am writing to express my strong enthusiasm for the Senior Product Manager opening at Global Tech Labs. With over seven years leading cross-functional teams and launching data-driven SaaS platforms, I have admired your company's relentless commitment to user-centric mobile innovation."
                    ),
                    DocumentSection(
                        id = "alignment",
                        heading = "Value Proposition & Experience",
                        body = "In my recent role, I steered product development from initial zero-to-one validation through to $1.8M ARR in first-year revenue. My core strengths center around rapid prototyping, hypothesis-driven development, and collaborating closely with engineering and growth teams."
                    ),
                    DocumentSection(
                        id = "closing",
                        heading = "Next Steps",
                        body = "I would welcome the opportunity to discuss how my hands-on product acumen can help accelerate your upcoming product roadmap. Thank you for your time and consideration."
                    ),
                    DocumentSection(
                        id = "signature",
                        heading = "",
                        body = "Sincerely,\n\nAnanya Sen",
                        isSignature = true
                    )
                )
            )
        ),

        // BUSINESS
        DocumentTemplate(
            id = "biz_invoice_pro",
            title = "GST Commercial Invoice",
            subtitle = "Itemized billing with tax calculation and payment details",
            docType = DocType.INVOICE,
            category = DocCategory.BUSINESS,
            style = DocStyle.BUSINESS,
            isPro = true,
            packId = "small_business",
            initialData = DocumentData(
                title = "Tax Invoice - #INV-2026-089",
                type = DocType.INVOICE,
                category = DocCategory.BUSINESS,
                style = DocStyle.BUSINESS,
                isPro = true,
                metadata = DocumentMetadata(
                    senderName = "Apex Digital Studio",
                    senderTitle = "Digital Solutions & Consulting",
                    senderAddress = "Plot 42, Tech Park, Pune, Maharashtra 411057\nGSTIN: 27AAAAA0000A1Z5",
                    recipientName = "Sunrise Enterprises Ltd",
                    recipientAddress = "12th Floor, Commercial Tower, Nariman Point, Mumbai\nGSTIN: 27BBBBB1111B1Z9",
                    dateString = "19 Sep 2026",
                    refNumber = "INV-2026-089",
                    currencySymbol = "₹",
                    amountTotal = "₹ 44,250"
                ),
                sections = listOf(
                    DocumentSection(
                        id = "items",
                        heading = "Scope of Services & Deliverables",
                        bulletPoints = listOf(
                            "Mobile UI/UX Design System & Prototypes (10 screens) — ₹ 25,000",
                            "AI Document Integration & API Engine Setup — ₹ 12,500",
                            "Cloud Deployment & UAT Sandbox Verification — ₹ 2,500"
                        )
                    ),
                    DocumentSection(
                        id = "tax_breakdown",
                        heading = "Tax Summary",
                        body = "Subtotal: ₹ 40,000\nCGST (9%): ₹ 3,600\nSGST (9%): ₹ 3,600\nDiscount / Adjustment: - ₹ 2,950\nTotal Payable: ₹ 44,250"
                    ),
                    DocumentSection(
                        id = "banking",
                        heading = "Payment Instructions",
                        body = "Account Name: Apex Digital Studio\nBank: HDFC Bank Ltd | Branch: Tech Park Branch\nA/C No: 50200012345678 | IFSC: HDFC0001234\nUPI ID: apexstudio@okhdfcbank"
                    )
                )
            )
        ),
        DocumentTemplate(
            id = "biz_quotation_clean",
            title = "Formal Quotation & Estimate",
            subtitle = "Comprehensive estimate with valid terms & scope overview",
            docType = DocType.QUOTATION,
            category = DocCategory.BUSINESS,
            style = DocStyle.PROFESSIONAL,
            isPro = false,
            packId = "small_business",
            initialData = DocumentData(
                title = "Commercial Quotation - QTN-2026-104",
                type = DocType.QUOTATION,
                category = DocCategory.BUSINESS,
                style = DocStyle.PROFESSIONAL,
                metadata = DocumentMetadata(
                    senderName = "Creative Media Works",
                    senderAddress = "Bandra West, Mumbai | contact@creativemedia.in",
                    recipientName = "Metro Retail Solutions",
                    recipientAddress = "Andheri East, Mumbai",
                    dateString = "19 September 2026",
                    refNumber = "QTN-2026-104",
                    currencySymbol = "₹",
                    amountTotal = "₹ 75,000"
                ),
                sections = listOf(
                    DocumentSection(
                        id = "quote_overview",
                        heading = "Project Scope",
                        body = "Quotation for comprehensive social media marketing, product catalog photography, and brand identity redesign for the festive season campaign."
                    ),
                    DocumentSection(
                        id = "schedule",
                        heading = "Key Milestones",
                        bulletPoints = listOf(
                            "Milestone 1: Brand Style Guide & Color Palette — 7 Days",
                            "Milestone 2: High-Resolution Product Shoot (30 SKUs) — 14 Days",
                            "Milestone 3: Final Campaign Launch & Asset Handover — 21 Days"
                        )
                    ),
                    DocumentSection(
                        id = "terms",
                        heading = "Terms & Validity",
                        body = "This quotation is valid for 30 calendar days from the date of issuance. 50% advance upon contract signing, remaining 50% upon final delivery."
                    )
                )
            )
        ),

        // PERSONAL
        DocumentTemplate(
            id = "personal_leave_application",
            title = "Standard Leave Application",
            subtitle = "Polished medical, casual, or emergency leave letter",
            docType = DocType.LEAVE_APPLICATION,
            category = DocCategory.PERSONAL,
            style = DocStyle.FORMAL,
            isPro = false,
            initialData = DocumentData(
                title = "Application for Medical Leave",
                type = DocType.LEAVE_APPLICATION,
                category = DocCategory.PERSONAL,
                style = DocStyle.FORMAL,
                metadata = DocumentMetadata(
                    senderName = "Pooja Deshmukh",
                    senderTitle = "Senior Analyst | Employee ID: EMP-4091",
                    recipientName = "Siddharth Verma",
                    recipientTitle = "Operations Manager",
                    recipientAddress = "Zenith Tech Systems Pvt Ltd",
                    dateString = "19 September 2026",
                    refNumber = "Subject: Application for 3-Day Medical Leave"
                ),
                sections = listOf(
                    DocumentSection(
                        id = "body",
                        heading = "Formal Request",
                        body = "Respected Sir,\n\nI am writing to formally request leave of absence from 21st September to 23rd September 2026 due to sudden viral infection. My physician has advised complete bed rest and clinical recovery for three days.\n\nI have handed over my ongoing client queries to my teammate, Vikram Patil, to ensure smooth continuity in daily tasks. I will be available via email for any urgent escalations.\n\nKindly approve my leave. Medical certificate is attached herewith for company records."
                    ),
                    DocumentSection(
                        id = "sign",
                        heading = "",
                        body = "Yours sincerely,\n\nPooja Deshmukh",
                        isSignature = true
                    )
                )
            )
        ),
        DocumentTemplate(
            id = "personal_bank_loan_app",
            title = "Bank Loan Application Letter",
            subtitle = "Structured request for home, personal, or business credit",
            docType = DocType.APPLICATION,
            category = DocCategory.PERSONAL,
            style = DocStyle.FORMAL,
            isPro = true,
            initialData = DocumentData(
                title = "Application for Personal Home Renovation Loan",
                type = DocType.APPLICATION,
                category = DocCategory.PERSONAL,
                style = DocStyle.FORMAL,
                isPro = true,
                metadata = DocumentMetadata(
                    senderName = "Mahesh Joshi",
                    senderAddress = "Flat 302, Green Meadows, Kothrud, Pune - 411038\nContact: +91 94220 11223 | Account No: 1029384756",
                    recipientName = "The Branch Manager",
                    recipientTitle = "State Bank of India",
                    recipientAddress = "Kothrud Branch, Pune",
                    dateString = "19 September 2026",
                    refNumber = "Subject: Application for Home Renovation Loan of ₹ 5,00,000"
                ),
                sections = listOf(
                    DocumentSection(
                        id = "loan_request",
                        heading = "Request Details",
                        body = "Dear Sir/Madam,\n\nI have maintained a primary savings account (A/C No: 1029384756) with your esteemed branch for the past seven years. I wish to apply for a personal home improvement loan of ₹ 5,00,000 (Rupees Five Lakhs only) for undertaking essential civil and electrical renovations at my residential property."
                    ),
                    DocumentSection(
                        id = "financial_standing",
                        heading = "Income & Repayment Capability",
                        body = "I am currently employed as a Principal Engineer at Infosys Ltd with a steady net monthly salary of ₹ 1,45,000. I propose an EMI repayment tenure of 36 months.\n\nAll requisite supporting documents including past 3 months' salary slips, Form 16, Bank statements, and KYC verification proofs are enclosed."
                    ),
                    DocumentSection(
                        id = "closing",
                        heading = "Declaration",
                        body = "I request you to kindly sanction and disburse the loan at the earliest favorable terms.\n\nYours faithfully,\n\nMahesh Joshi",
                        isSignature = true
                    )
                )
            )
        ),

        // EDUCATION
        DocumentTemplate(
            id = "edu_project_report",
            title = "College Project Report",
            subtitle = "Academic structure with abstract, objectives, and conclusion",
            docType = DocType.PROJECT_REPORT,
            category = DocCategory.EDUCATION,
            style = DocStyle.ACADEMIC,
            isPro = false,
            packId = "student_pack",
            initialData = DocumentData(
                title = "AI-Driven Document Extraction System",
                type = DocType.PROJECT_REPORT,
                category = DocCategory.EDUCATION,
                style = DocStyle.ACADEMIC,
                metadata = DocumentMetadata(
                    senderName = "Submitted by: Aditya Kulkarni (Roll No: 2023-CS-042)",
                    recipientName = "Project Supervisor: Dr. S. N. Roy",
                    recipientTitle = "Department of Computer Engineering",
                    recipientAddress = "Government College of Engineering",
                    dateString = "Academic Year 2025 - 2026"
                ),
                sections = listOf(
                    DocumentSection(
                        id = "abstract",
                        heading = "1. Abstract",
                        body = "In this research project, we present an end-to-end mobile architecture designed to convert unstructured multi-format imagery and natural language prompts into formatted PDF documents. By leveraging lightweight multimodal models, the system democratizes document publishing for grassroots businesses."
                    ),
                    DocumentSection(
                        id = "objectives",
                        heading = "2. Project Objectives",
                        bulletPoints = listOf(
                            "Eliminate formatting friction for users with minimal technical expertise.",
                            "Support multi-language translation and OCR entity extraction.",
                            "Generate crisp, vectorized printable PDF outputs natively on mobile."
                        )
                    ),
                    DocumentSection(
                        id = "methodology",
                        heading = "3. Methodology & Results",
                        body = "The prototype achieved an average processing latency under 1.8 seconds on standard 4G mobile devices with 99.2% line layout fidelity."
                    )
                )
            )
        ),

        // CONTENT
        DocumentTemplate(
            id = "content_meeting_minutes",
            title = "Executive Meeting Minutes",
            subtitle = "Structured agenda, key discussion points, and action items",
            docType = DocType.MEETING_NOTES,
            category = DocCategory.CONTENT,
            style = DocStyle.MODERN,
            isPro = false,
            initialData = DocumentData(
                title = "Product Strategy & Q4 Roadmap Meeting",
                type = DocType.MEETING_NOTES,
                category = DocCategory.CONTENT,
                style = DocStyle.MODERN,
                metadata = DocumentMetadata(
                    senderName = "Recorded by: Project PM",
                    recipientName = "Attendees: Engineering, Design, Growth Leads",
                    dateString = "19 Sep 2026 | 10:00 AM - 11:30 AM IST",
                    refNumber = "Meeting Room: Alpha / Virtual Link"
                ),
                sections = listOf(
                    DocumentSection(
                        id = "agenda",
                        heading = "1. Agenda",
                        bulletPoints = listOf(
                            "Review of Q3 customer feedback on mobile PDF export speeds",
                            "Pricing strategy: introduction of ₹9 micro-credit exports",
                            "Hindi and Marathi localized document builder rollout"
                        )
                    ),
                    DocumentSection(
                        id = "decisions",
                        heading = "2. Decisions Taken",
                        body = "• Unanimously agreed to keep free tier generous (5 credits upon onboarding).\n• Added instant Photo to PDF compiler directly in the mobile tools menu."
                    ),
                    DocumentSection(
                        id = "action_items",
                        heading = "3. Action Items & Deadlines",
                        bulletPoints = listOf(
                            "Aditya: Finalize Room database schema migration by Friday",
                            "Sneha: Prepare localized Marathi and Hindi template prompts",
                            "Karan: Conduct user testing on 360px Android devices"
                        )
                    )
                )
            )
        )
    )
}
