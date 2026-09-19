package com.example.data.local

import android.content.Context
import android.content.SharedPreferences
import com.example.data.model.PlanType
import com.example.data.model.PricingConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserPreferencesRepository(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("docupera_prefs", Context.MODE_PRIVATE)

    private val _credits = MutableStateFlow(prefs.getInt(KEY_CREDITS, 5))
    val credits: StateFlow<Int> = _credits.asStateFlow()

    private val _currentPlan = MutableStateFlow(
        try {
            PlanType.valueOf(prefs.getString(KEY_PLAN, PlanType.FREE.name) ?: PlanType.FREE.name)
        } catch (_: Exception) {
            PlanType.FREE
        }
    )
    val currentPlan: StateFlow<PlanType> = _currentPlan.asStateFlow()

    private val _pricingConfig = MutableStateFlow(PricingConfig())
    val pricingConfig: StateFlow<PricingConfig> = _pricingConfig.asStateFlow()

    // Business profile settings
    private val _businessName = MutableStateFlow(prefs.getString(KEY_BIZ_NAME, "Sai Cyber Cafe & Print Hub") ?: "")
    val businessName: StateFlow<String> = _businessName.asStateFlow()

    private val _businessPhone = MutableStateFlow(prefs.getString(KEY_BIZ_PHONE, "+91 98200 12345") ?: "")
    val businessPhone: StateFlow<String> = _businessPhone.asStateFlow()

    private val _businessFooter = MutableStateFlow(prefs.getString(KEY_BIZ_FOOTER, "Printed & Formatted at Sai Cyber Hub • Quality Assured") ?: "")
    val businessFooter: StateFlow<String> = _businessFooter.asStateFlow()

    private val _totalExports = MutableStateFlow(prefs.getInt(KEY_TOTAL_EXPORTS, 0))
    val totalExports: StateFlow<Int> = _totalExports.asStateFlow()

    fun deductCredit(amount: Int = 1): Boolean {
        if (_currentPlan.value != PlanType.FREE) {
            // Pro & Business have unlimited / high allowance
            incrementExportCount()
            return true
        }
        val current = _credits.value
        if (current >= amount) {
            val updated = current - amount
            _credits.value = updated
            prefs.edit().putInt(KEY_CREDITS, updated).apply()
            incrementExportCount()
            return true
        }
        return false
    }

    fun addCredits(amount: Int) {
        val updated = _credits.value + amount
        _credits.value = updated
        prefs.edit().putInt(KEY_CREDITS, updated).apply()
    }

    fun setPlan(plan: PlanType) {
        _currentPlan.value = plan
        prefs.edit().putString(KEY_PLAN, plan.name).apply()
    }

    fun updatePricingConfig(newConfig: PricingConfig) {
        _pricingConfig.value = newConfig
    }

    fun updateBusinessProfile(name: String, phone: String, footer: String) {
        _businessName.value = name
        _businessPhone.value = phone
        _businessFooter.value = footer
        prefs.edit()
            .putString(KEY_BIZ_NAME, name)
            .putString(KEY_BIZ_PHONE, phone)
            .putString(KEY_BIZ_FOOTER, footer)
            .apply()
    }

    private fun incrementExportCount() {
        val count = _totalExports.value + 1
        _totalExports.value = count
        prefs.edit().putInt(KEY_TOTAL_EXPORTS, count).apply()
    }

    fun redeemPromoCode(code: String): Pair<Boolean, String> {
        val cleanCode = code.trim().uppercase()
        val bonus = _pricingConfig.value.promoCodes[cleanCode]
        return if (bonus != null) {
            addCredits(bonus)
            Pair(true, "Successfully added $bonus bonus credits!")
        } else {
            Pair(false, "Invalid or expired promo code.")
        }
    }

    companion object {
        private const val KEY_CREDITS = "user_credits"
        private const val KEY_PLAN = "user_plan"
        private const val KEY_BIZ_NAME = "biz_name"
        private const val KEY_BIZ_PHONE = "biz_phone"
        private const val KEY_BIZ_FOOTER = "biz_footer"
        private const val KEY_TOTAL_EXPORTS = "total_exports"
    }
}
