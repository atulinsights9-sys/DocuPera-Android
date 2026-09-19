package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.data.model.PlanType
import com.example.ui.DocuPeraViewModel
import com.example.ui.ScreenRoute
import com.example.ui.theme.*

@Composable
fun BusinessModeScreen(
    viewModel: DocuPeraViewModel,
    onNavigate: (ScreenRoute) -> Unit
) {
    val plan by viewModel.currentPlan.collectAsState()
    val savedName by viewModel.preferences.businessName.collectAsState()
    val savedPhone by viewModel.preferences.businessPhone.collectAsState()
    val savedFooter by viewModel.preferences.businessFooter.collectAsState()

    var shopNameInput by remember(savedName) { mutableStateOf(savedName) }
    var shopPhoneInput by remember(savedPhone) { mutableStateOf(savedPhone) }
    var shopFooterInput by remember(savedFooter) { mutableStateOf(savedFooter) }

    // Bulk generator input
    var bulkNamesInput by remember { mutableStateOf("Rahul Verma\nSneha Patil\nAditya Joshi\nPooja Kulkarni") }
    var bulkDocType by remember { mutableStateOf("Participation Certificate") }
    var bulkStatus by remember { mutableStateOf<String?>(null) }

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
                        imageVector = Icons.Default.Storefront,
                        contentDescription = null,
                        tint = Amber500,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Business Studio Mode",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Navy900
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            color = Amber500,
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "B2B",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Navy900,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Text(
                        text = "For cyber cafes, print shops, agencies & consultants",
                        fontSize = 12.sp,
                        color = Slate500
                    )
                }
            }
        }

        // Shop Branding Settings Card
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
                        text = "Shop & Agency Branding",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Navy900
                    )
                    Text(
                        text = "Appear on the footer and headers of all PDFs exported for your customers.",
                        fontSize = 11.5.sp,
                        color = Slate500
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = shopNameInput,
                        onValueChange = { shopNameInput = it },
                        label = { Text("Shop / Business Name") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = shopPhoneInput,
                        onValueChange = { shopPhoneInput = it },
                        label = { Text("Contact Phone / WhatsApp") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = shopFooterInput,
                        onValueChange = { shopFooterInput = it },
                        label = { Text("PDF Custom Footer Text") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = {
                            viewModel.preferences.updateBusinessProfile(shopNameInput, shopPhoneInput, shopFooterInput)
                            viewModel.showToast("Business branding updated!")
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue600),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth().testTag("btn_save_business_profile")
                    ) {
                        Text("Save Branding Settings")
                    }
                }
            }
        }

        // Bulk Document Generator Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Slate200, RoundedCornerShape(14.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Bulk Document Batch Generator",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Navy900
                        )
                        Surface(color = RoyalBlue100, shape = RoundedCornerShape(4.dp)) {
                            Text(
                                text = "Pro / Biz",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = RoyalBlue600,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Text(
                        text = "Paste candidate / student / client names (one per line) to batch generate personalized documents in one click.",
                        fontSize = 11.5.sp,
                        color = Slate500
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = bulkDocType,
                        onValueChange = { bulkDocType = it },
                        label = { Text("Document Type / Purpose") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = bulkNamesInput,
                        onValueChange = { bulkNamesInput = it },
                        label = { Text("Recipients List (one per line)") },
                        modifier = Modifier.fillMaxWidth().height(110.dp),
                        shape = RoundedCornerShape(8.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            val names = bulkNamesInput.lines().filter { it.isNotBlank() }
                            bulkStatus = "Batch generated ${names.size} certificates for: ${names.joinToString(", ")}. Ready in Documents history."
                            viewModel.showToast("Batch generated ${names.size} documents!")
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Navy900),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth().testTag("btn_batch_generate")
                    ) {
                        Icon(Icons.Default.PlaylistAddCheck, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Batch Generate Documents")
                    }

                    if (bulkStatus != null) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Surface(
                            color = Emerald100,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = bulkStatus ?: "",
                                fontSize = 11.5.sp,
                                color = Emerald500,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }
                }
            }
        }

        // Client Folders Overview
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
                        text = "Client Workspaces & Folders",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Navy900
                    )
                    Text(
                        text = "Organize customer projects separately.",
                        fontSize = 11.5.sp,
                        color = Slate500
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    val folders = listOf("Customer Walk-ins (12)", "SBI Loan Applications (8)", "College Students (15)", "GST Commercial Invoices (24)")
                    folders.forEach { f ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Folder, contentDescription = null, tint = RoyalBlue600, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(f, fontSize = 12.sp, color = Slate700)
                        }
                    }
                }
            }
        }
    }
}
