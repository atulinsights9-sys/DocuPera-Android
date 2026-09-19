package com.example.data.model

enum class PlanType(val displayName: String, val badgeColorHex: Long) {
    FREE("Free Plan", 0xFF64748B),
    PRO("DocuPera Pro", 0xFF2563EB),
    BUSINESS("Business Studio", 0xFF0F172A)
}

data class CreditPack(
    val id: String,
    val credits: Int,
    val priceInr: Int,
    val title: String,
    val badge: String? = null,
    val perCreditInr: String
)

data class SubscriptionPlan(
    val id: String,
    val planType: PlanType,
    val name: String,
    val monthlyPriceInr: Int,
    val yearlyPriceInr: Int,
    val tagLine: String,
    val features: List<String>,
    val isPopular: Boolean = false
)

data class PricingConfig(
    val creditPacks: List<CreditPack> = defaultCreditPacks,
    val proMonthlyInr: Int = 129,
    val proYearlyInr: Int = 999,
    val businessMonthlyInr: Int = 499,
    val businessYearlyInr: Int = 3999,
    val defaultFreeCredits: Int = 5,
    val promoCodes: Map<String, Int> = mapOf(
        "DOCUFREE" to 5,
        "STARTUP50" to 20,
        "STUDENTPRO" to 10
    )
) {
    companion object {
        val defaultCreditPacks = listOf(
            CreditPack(
                id = "pack_single",
                credits = 1,
                priceInr = 9,
                title = "Single Export",
                badge = "Pay Per Use",
                perCreditInr = "₹9/doc"
            ),
            CreditPack(
                id = "pack_5",
                credits = 5,
                priceInr = 29,
                title = "Starter Pack",
                badge = "Save 35%",
                perCreditInr = "₹5.8/doc"
            ),
            CreditPack(
                id = "pack_20",
                credits = 20,
                priceInr = 79,
                title = "Popular Value",
                badge = "Most Popular",
                perCreditInr = "₹3.9/doc"
            ),
            CreditPack(
                id = "pack_50",
                credits = 50,
                priceInr = 149,
                title = "Studio Pack",
                badge = "Best Rate",
                perCreditInr = "₹2.9/doc"
            )
        )

        val defaultSubscriptionPlans = listOf(
            SubscriptionPlan(
                id = "plan_free",
                planType = PlanType.FREE,
                name = "Free Tier",
                monthlyPriceInr = 0,
                yearlyPriceInr = 0,
                tagLine = "Try before you buy. Free starter value for everyone.",
                features = listOf(
                    "5 Free starter credits",
                    "Basic AI document generations",
                    "Access to all Free templates",
                    "Standard PDF export (with small watermark)",
                    "Basic Photo → PDF tool",
                    "Saved document history"
                ),
                isPopular = false
            ),
            SubscriptionPlan(
                id = "plan_pro",
                planType = PlanType.PRO,
                name = "DocuPera Pro",
                monthlyPriceInr = 129,
                yearlyPriceInr = 999,
                tagLine = "For ambitious professionals, job seekers, and freelancers.",
                features = listOf(
                    "Unlimited AI generations & rewrites",
                    "50 HD PDF exports per month",
                    "Zero watermarks on exported PDFs",
                    "Full access to all PRO templates & packs",
                    "AI Screenshot → Document analyzer",
                    "Multilingual translation (EN, HI, MR)",
                    "Priority OCR & document cleanup",
                    "Permanent document cloud backup"
                ),
                isPopular = true
            ),
            SubscriptionPlan(
                id = "plan_business",
                planType = PlanType.BUSINESS,
                name = "Business Studio",
                monthlyPriceInr = 499,
                yearlyPriceInr = 3999,
                tagLine = "Built for cyber cafes, printing shops, consultants & agencies.",
                features = listOf(
                    "Unlimited HD PDF exports & zero watermark",
                    "Bulk document generator (Excel/CSV mode)",
                    "Client Folders & workspace tagging",
                    "Custom shop header, logo & watermark branding",
                    "Commercial GST invoice & quotation templates",
                    "Receipt generator with custom shop QR code",
                    "Team/Operator multi-seat access (Coming Soon)",
                    "Dedicated WhatsApp support"
                ),
                isPopular = false
            )
        )
    }
}
