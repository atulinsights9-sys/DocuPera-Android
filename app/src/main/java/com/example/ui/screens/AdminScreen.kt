package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.example.BuildConfig
import com.example.data.model.CreditPack
import com.example.data.model.PricingConfig
import com.example.ui.DocuPeraViewModel
import com.example.ui.ScreenRoute
import com.example.ui.theme.*

@Composable
fun AdminScreen(
    viewModel: DocuPeraViewModel,
    onNavigate: (ScreenRoute) -> Unit
) {
    val pricingConfig by viewModel.pricingConfig.collectAsState()
    val totalExports by viewModel.preferences.totalExports.collectAsState()
    val docCount by viewModel.documentCount.collectAsState()

    var proMonthlyInput by remember(pricingConfig) { mutableStateOf(pricingConfig.proMonthlyInr.toString()) }
    var proYearlyInput by remember(pricingConfig) { mutableStateOf(pricingConfig.proYearlyInr.toString()) }
    var singlePackInput by remember(pricingConfig) { mutableStateOf(pricingConfig.creditPacks[0].priceInr.toString()) }
    var pack5Input by remember(pricingConfig) { mutableStateOf(pricingConfig.creditPacks[1].priceInr.toString()) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Slate50)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Navy900),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AdminPanelSettings,
                        contentDescription = null,
                        tint = RoyalBlue400,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "DocuPera Admin Console",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Navy900
                    )
                    Text(
                        text = "Configurable monetization & platform telemetry",
                        fontSize = 12.sp,
                        color = Slate500
                    )
                }
            }
        }

        // Metrics Summary Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Navy900),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Platform Analytics",
                        fontSize = 13.sp,
                        color = Slate400,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Total Documents", fontSize = 11.sp, color = Slate400)
                            Text("$docCount", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                        Column {
                            Text("Total PDF Exports", fontSize = 11.sp, color = Slate400)
                            Text("$totalExports", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Emerald500)
                        }
                        Column {
                            Text("Model Endpoint", fontSize = 11.sp, color = Slate400)
                            Text("Gemini 3.5 Flash", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = RoyalBlue400)
                        }
                    }
                }
            }
        }

        // Configurable Pricing Architecture Editor
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Slate200, RoundedCornerShape(14.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Configurable Pricing Editor",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Navy900
                    )
                    Text(
                        text = "Prices are dynamically evaluated across all billing screens and paywalls.",
                        fontSize = 11.5.sp,
                        color = Slate500
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = singlePackInput,
                            onValueChange = { singlePackInput = it },
                            label = { Text("1 Export (₹)") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp)
                        )
                        OutlinedTextField(
                            value = pack5Input,
                            onValueChange = { pack5Input = it },
                            label = { Text("5 Pack (₹)") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = proMonthlyInput,
                            onValueChange = { proMonthlyInput = it },
                            label = { Text("Pro Monthly (₹)") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp)
                        )
                        OutlinedTextField(
                            value = proYearlyInput,
                            onValueChange = { proYearlyInput = it },
                            label = { Text("Pro Yearly (₹)") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            val singleP = singlePackInput.toIntOrNull() ?: 9
                            val p5 = pack5Input.toIntOrNull() ?: 29
                            val proM = proMonthlyInput.toIntOrNull() ?: 129
                            val proY = proYearlyInput.toIntOrNull() ?: 999

                            val updatedPacks = pricingConfig.creditPacks.map { pack ->
                                when (pack.credits) {
                                    1 -> pack.copy(priceInr = singleP, perCreditInr = "₹$singleP/doc")
                                    5 -> pack.copy(priceInr = p5, perCreditInr = "₹${p5 / 5f}/doc")
                                    else -> pack
                                }
                            }

                            val newConfig = pricingConfig.copy(
                                creditPacks = updatedPacks,
                                proMonthlyInr = proM,
                                proYearlyInr = proY
                            )
                            viewModel.preferences.updatePricingConfig(newConfig)
                            viewModel.showToast("Pricing parameters updated successfully!")
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue600),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth().testTag("btn_save_pricing_config")
                    ) {
                        Text("Apply Live Pricing Updates")
                    }
                }
            }
        }

        // Quick Admin Testing & Shortcuts
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Slate200, RoundedCornerShape(14.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Admin Quick Tools",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Navy900
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(
                            onClick = {
                                viewModel.preferences.addCredits(10)
                                viewModel.showToast("Granted +10 test credits")
                            },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("+10 Credits")
                        }

                        OutlinedButton(
                            onClick = { onNavigate(ScreenRoute.LANDING) },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("View Landing Page")
                        }
                    }
                }
            }
        }
    }
}
