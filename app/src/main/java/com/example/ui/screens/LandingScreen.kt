package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.DocuPeraViewModel
import com.example.ui.ScreenRoute
import com.example.ui.theme.*

@Composable
fun LandingScreen(
    viewModel: DocuPeraViewModel,
    onNavigate: (ScreenRoute) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Slate50)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Hero Section
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Navy900),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth().testTag("landing_hero")
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(RoyalBlue600.copy(alpha = 0.3f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.docupera_logo),
                            contentDescription = "DocuPera",
                            modifier = Modifier.size(42.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Surface(
                        color = RoyalBlue600.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "AI DOCUMENT STUDIO",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = RoyalBlue400,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "DOCUPERA",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "“Tell us what you need.\nWe'll make the document.”",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = RoyalBlue400,
                        textAlign = TextAlign.Center,
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Turn your idea, text, photo, screenshot, or existing document into a professional, editable document and downloadable PDF.",
                        fontSize = 13.sp,
                        color = Slate300,
                        textAlign = TextAlign.Center,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = { onNavigate(ScreenRoute.HOME) },
                        colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue600),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("btn_landing_create_document")
                    ) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Create a Document", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedButton(
                        onClick = { onNavigate(ScreenRoute.TEMPLATES) },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Text("Explore 30+ Templates", color = Color.White, fontSize = 13.sp)
                    }
                }
            }
        }

        // How it Works Section
        item {
            Column {
                Text(
                    text = "How DocuPera Works",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Navy900
                )
                Text(
                    text = "No blank page anxiety. From thought to PDF in 60 seconds.",
                    fontSize = 12.sp,
                    color = Slate500
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    WorkflowStepCard(
                        stepNumber = "1",
                        title = "Describe",
                        description = "State your request in plain words or upload an image.",
                        modifier = Modifier.weight(1f)
                    )
                    WorkflowStepCard(
                        stepNumber = "2",
                        title = "Refine",
                        description = "DocuPera asks only key missing details.",
                        modifier = Modifier.weight(1f)
                    )
                    WorkflowStepCard(
                        stepNumber = "3",
                        title = "Export",
                        description = "Download crisp, printable PDF or share instantly.",
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Value Proposition Grid
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Why People Love DocuPera",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Navy900
                )

                ValuePropCard(
                    icon = Icons.Outlined.Translate,
                    title = "Multi-language Ready",
                    description = "Create and translate documents seamlessly in English, Hindi, and Marathi."
                )
                ValuePropCard(
                    icon = Icons.Outlined.Collections,
                    title = "Photo to Multi-Page PDF",
                    description = "Snap notebook pages, certificates, or receipts. Filter, reorder, and compile cleanly."
                )
                ValuePropCard(
                    icon = Icons.Outlined.CropFree,
                    title = "Screenshot to Real Document",
                    description = "Extract dates, fees, and text from WhatsApp notices or posters with AI OCR."
                )
                ValuePropCard(
                    icon = Icons.Outlined.Storefront,
                    title = "Business Studio Mode",
                    description = "Designed for cyber cafes, small agencies, and freelancers with custom branding."
                )
            }
        }

        // Pricing Preview
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Slate200, RoundedCornerShape(16.dp))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Fair, Transparent Pricing",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Navy900
                    )
                    Text(
                        text = "Free starter value for everyone. Micro-exports starting at just ₹9.",
                        fontSize = 12.sp,
                        color = Slate500
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Free Starter Tier", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Slate800)
                            Text("5 Free Credits included", fontSize = 11.5.sp, color = Emerald500)
                        }
                        Text("₹0", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Navy900)
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(color = Slate200)
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("DocuPera Pro", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Navy900)
                            Text("Unlimited AI & 50 HD PDF exports", fontSize = 11.5.sp, color = RoyalBlue600)
                        }
                        Text("₹129/mo", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = RoyalBlue600)
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Button(
                        onClick = { onNavigate(ScreenRoute.BILLING) },
                        colors = ButtonDefaults.buttonColors(containerColor = Navy900),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("View All Pricing Plans", fontSize = 13.sp)
                    }
                }
            }
        }

        // FAQ Accordion
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Frequently Asked Questions",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Navy900
                )

                FaqItem(
                    question = "Can I edit the document before downloading?",
                    answer = "Yes! DocuPera gives you a smart live editor. You can edit text, add sections, and use the AI writing toolbar before exporting."
                )
                FaqItem(
                    question = "Do credits expire?",
                    answer = "No. Credit packs never expire. You can purchase once and use them whenever you need."
                )
                FaqItem(
                    question = "Can I use DocuPera on any mobile device?",
                    answer = "Yes, DocuPera is engineered mobile-first to work smoothly on standard screens from 360px to modern tablets."
                )
            }
        }
    }
}

@Composable
fun WorkflowStepCard(
    stepNumber: String,
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier.border(1.dp, Slate200, RoundedCornerShape(12.dp))
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Surface(
                color = RoyalBlue100,
                shape = RoundedCornerShape(6.dp)
            ) {
                Text(
                    text = "Step $stepNumber",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = RoyalBlue600,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Navy900)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = description, fontSize = 10.sp, color = Slate500, lineHeight = 13.sp)
        }
    }
}

@Composable
fun ValuePropCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Slate200, RoundedCornerShape(12.dp))
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(RoyalBlue100),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = RoyalBlue600, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = title, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = Navy900)
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = description, fontSize = 11.5.sp, color = Slate500, lineHeight = 15.sp)
            }
        }
    }
}

@Composable
fun FaqItem(question: String, answer: String) {
    var expanded by remember { mutableStateOf(false) }
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Slate200, RoundedCornerShape(10.dp))
            .clickable { expanded = !expanded }
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = question,
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Navy900,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = Slate400,
                    modifier = Modifier.size(20.dp)
                )
            }
            if (expanded) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = answer, fontSize = 11.5.sp, color = Slate600, lineHeight = 15.sp)
            }
        }
    }
}
