package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CreditPack
import com.example.data.model.PlanType
import com.example.data.model.PricingConfig
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaywallModal(
    credits: Int,
    pricingConfig: PricingConfig,
    onDismiss: () -> Unit,
    onBuyPack: (CreditPack) -> Unit,
    onUpgradePro: () -> Unit,
    onNavigateToBilling: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Amber100),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Premium Export",
                            tint = Amber500,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Premium Export Required",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Navy900
                        )
                        Text(
                            text = "1 Credit needed to export clean HD PDF",
                            fontSize = 12.sp,
                            color = Slate500
                        )
                    }
                }

                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Slate400)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Pro Subscription Highlight Card
            Card(
                colors = CardDefaults.cardColors(containerColor = Navy900),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onUpgradePro() }
                    .testTag("paywall_upgrade_pro_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Stars, contentDescription = null, tint = Amber500)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "DocuPera Pro",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                        Surface(
                            color = RoyalBlue600,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "₹${pricingConfig.proMonthlyInr}/month",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Unlimited AI generations, 50 HD PDF exports, zero watermarks & all PRO templates.",
                        fontSize = 12.sp,
                        color = Slate200,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = onUpgradePro,
                        colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue600),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Upgrade to Pro", fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Or Pay Per Use (Credits)",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Slate900
            )
            Text(
                text = "Credits never expire. Use whenever you need.",
                fontSize = 12.sp,
                color = Slate500
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Credit Packs row
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(pricingConfig.creditPacks) { pack ->
                    val isSingle = pack.credits == 1
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (pack.badge == "Most Popular") RoyalBlue100 else Slate100
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .width(135.dp)
                            .border(
                                width = if (pack.badge == "Most Popular") 1.5.dp else 1.dp,
                                color = if (pack.badge == "Most Popular") RoyalBlue600 else Slate200,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable { onBuyPack(pack) }
                            .testTag("credit_pack_${pack.id}")
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            if (pack.badge != null) {
                                Surface(
                                    color = if (pack.badge == "Most Popular") RoyalBlue600 else Slate400,
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = pack.badge,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                            } else {
                                Spacer(modifier = Modifier.height(14.dp))
                            }

                            Text(
                                text = "${pack.credits} ${if (pack.credits == 1) "Credit" else "Credits"}",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Navy900
                            )
                            Text(
                                text = "₹${pack.priceInr}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = RoyalBlue600
                            )
                            Text(
                                text = pack.perCreditInr,
                                fontSize = 10.sp,
                                color = Slate500
                            )

                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = { onBuyPack(pack) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (pack.badge == "Most Popular") RoyalBlue600 else Slate700
                                ),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier.fillMaxWidth().height(32.dp)
                            ) {
                                Text("Get", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = onNavigateToBilling,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("View Full Pricing Plans & Business Mode →", fontSize = 12.sp, color = RoyalBlue600)
            }
        }
    }
}
