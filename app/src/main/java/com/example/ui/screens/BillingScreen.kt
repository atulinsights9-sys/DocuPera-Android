package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.ui.DocuPeraViewModel
import com.example.ui.ScreenRoute
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BillingScreen(
    viewModel: DocuPeraViewModel,
    onNavigate: (ScreenRoute) -> Unit
) {
    val credits by viewModel.credits.collectAsState()
    val currentPlan by viewModel.currentPlan.collectAsState()
    val pricingConfig by viewModel.pricingConfig.collectAsState()

    var isYearly by remember { mutableStateOf(false) }
    var promoCodeInput by remember { mutableStateOf("") }

    val subscriptionPlans = remember(pricingConfig) {
        PricingConfig.defaultSubscriptionPlans.map { plan ->
            when (plan.planType) {
                PlanType.PRO -> plan.copy(
                    monthlyPriceInr = pricingConfig.proMonthlyInr,
                    yearlyPriceInr = pricingConfig.proYearlyInr
                )
                PlanType.BUSINESS -> plan.copy(
                    monthlyPriceInr = pricingConfig.businessMonthlyInr,
                    yearlyPriceInr = pricingConfig.businessYearlyInr
                )
                else -> plan
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Slate50)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header & Active Plan Status Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Navy900),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Current Plan",
                                fontSize = 11.sp,
                                color = Slate400,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = currentPlan.displayName,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        Surface(
                            color = if (credits > 0) RoyalBlue600 else Amber500,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "$credits Credits",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = if (currentPlan == PlanType.FREE)
                            "1 Credit = 1 Professional Watermark-Free PDF Export. Upgrade for unlimited AI creations & HD downloads."
                        else
                            "You have full access to ${currentPlan.displayName} features with zero watermarks.",
                        fontSize = 12.sp,
                        color = Slate300,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { onNavigate(ScreenRoute.BUSINESS_MODE) },
                            colors = ButtonDefaults.buttonColors(containerColor = Slate700),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                        ) {
                            Icon(Icons.Default.Storefront, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Business Mode", fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // Section: Pay Per Use (Credits)
        item {
            Column {
                Text(
                    text = "Pay As You Go (Micro-Credit Packs)",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Navy900
                )
                Text(
                    text = "No monthly commitments. Credits never expire.",
                    fontSize = 12.sp,
                    color = Slate500
                )
            }
        }

        items(pricingConfig.creditPacks) { pack ->
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (pack.badge == "Most Popular") RoyalBlue100.copy(alpha = 0.4f) else Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = if (pack.badge == "Most Popular") 1.5.dp else 1.dp,
                        color = if (pack.badge == "Most Popular") RoyalBlue600 else Slate200,
                        shape = RoundedCornerShape(12.dp)
                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${pack.credits} Credits (${pack.title})",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Navy900
                            )
                            if (pack.badge != null) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    color = if (pack.badge == "Most Popular") RoyalBlue600 else Slate500,
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = pack.badge,
                                        fontSize = 8.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${pack.perCreditInr} • Instant activation",
                            fontSize = 11.5.sp,
                            color = Slate500
                        )
                    }

                    Button(
                        onClick = { viewModel.purchaseCredits(pack) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (pack.badge == "Most Popular") RoyalBlue600 else Navy900
                        ),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("btn_buy_pack_${pack.id}")
                    ) {
                        Text("₹${pack.priceInr}", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }
        }

        // Section: Subscriptions (Monthly vs Yearly)
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Subscriptions (Pro & Business)",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Navy900
                        )
                        Text(
                            text = "For heavy users, freelancers & printing shops",
                            fontSize = 12.sp,
                            color = Slate500
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Monthly", fontSize = 11.sp, color = if (!isYearly) Navy900 else Slate400)
                        Switch(
                            checked = isYearly,
                            onCheckedChange = { isYearly = it },
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                        Text("Yearly (-20%)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (isYearly) Emerald500 else Slate400)
                    }
                }
            }
        }

        items(subscriptionPlans) { plan ->
            val isCurrent = currentPlan == plan.planType
            val price = if (isYearly) "₹${plan.yearlyPriceInr}/yr" else "₹${plan.monthlyPriceInr}/mo"

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (plan.isPopular) RoyalBlue100.copy(alpha = 0.3f) else Color.White
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = if (plan.isPopular) 1.5.dp else 1.dp,
                        color = if (plan.isPopular) RoyalBlue600 else Slate200,
                        shape = RoundedCornerShape(16.dp)
                    )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = plan.name,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Navy900
                            )
                            if (plan.isPopular) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(color = Amber500, shape = RoundedCornerShape(4.dp)) {
                                    Text(
                                        text = "RECOMMENDED",
                                        fontSize = 8.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Navy900,
                                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }

                        if (plan.planType != PlanType.FREE) {
                            Text(
                                text = price,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = RoyalBlue600
                            )
                        } else {
                            Text(
                                text = "Free Forever",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Slate500
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = plan.tagLine, fontSize = 11.5.sp, color = Slate600)

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = Slate200)
                    Spacer(modifier = Modifier.height(12.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        plan.features.forEach { feat ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = if (plan.isPopular) RoyalBlue600 else Emerald500,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = feat, fontSize = 12.sp, color = Slate700)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (isCurrent) {
                        OutlinedButton(
                            onClick = {},
                            enabled = false,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Active Current Plan")
                        }
                    } else if (plan.planType != PlanType.FREE) {
                        Button(
                            onClick = { viewModel.upgradeSubscription(plan.planType) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (plan.planType == PlanType.PRO) RoyalBlue600 else Navy900
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth().testTag("btn_upgrade_${plan.id}")
                        ) {
                            Text("Upgrade to ${plan.name}", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Section: Redeem Promo Code
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Slate200, RoundedCornerShape(12.dp))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("Redeem Voucher or Promo Code", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Navy900)
                    Text("Try 'DOCUFREE', 'STARTUP50', or 'STUDENTPRO'", fontSize = 11.sp, color = Slate500)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = promoCodeInput,
                            onValueChange = { promoCodeInput = it },
                            placeholder = { Text("Enter Promo Code", fontSize = 12.sp) },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp)
                        )
                        Button(
                            onClick = {
                                val (success, msg) = viewModel.preferences.redeemPromoCode(promoCodeInput)
                                viewModel.showToast(msg)
                                if (success) promoCodeInput = ""
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue600),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Apply")
                        }
                    }
                }
            }
        }
    }
}
