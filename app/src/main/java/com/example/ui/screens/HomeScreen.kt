package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.ui.DocuPeraViewModel
import com.example.ui.ScreenRoute
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: DocuPeraViewModel,
    onNavigate: (ScreenRoute) -> Unit
) {
    val documents by viewModel.allDocuments.collectAsState()
    val credits by viewModel.credits.collectAsState()
    val plan by viewModel.currentPlan.collectAsState()
    var selectedCategory by remember { mutableStateOf<DocCategory?>(null) }
    var selectedDocForOptions by remember { mutableStateOf<DocumentData?>(null) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Slate50)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // 1. Hero Prompt Question & Welcome Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Navy900),
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("home_hero_card")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = RoyalBlue600.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = RoyalBlue400,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "AI Document Studio",
                                    fontSize = 11.sp,
                                    color = RoyalBlue400,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Text(
                            text = if (plan != PlanType.FREE) plan.displayName else "$credits Credits Available",
                            fontSize = 12.sp,
                            color = if (credits > 0) Emerald500 else Amber500,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "What do you want to create?",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        lineHeight = 28.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Tell us what you need. We'll make the document.",
                        fontSize = 13.sp,
                        color = Slate400
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // Two Primary Actions
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = { viewModel.startAiCreation() },
                            colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue600),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                                .testTag("btn_create_with_ai")
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Create with AI",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        OutlinedButton(
                            onClick = { onNavigate(ScreenRoute.TOOLS) },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                            border = ButtonDefaults.outlinedButtonBorder.copy(
                                brush = Brush.linearGradient(listOf(Slate600, Slate400))
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                                .testTag("btn_upload_photo_file")
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.PhotoCamera,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Photo / File",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }

        // 2. Quick Action Tool Pills
        item {
            Column {
                Text(
                    text = "Quick Document Tools",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Navy900
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    QuickToolCard(
                        title = "Photo → PDF",
                        icon = Icons.Outlined.Collections,
                        badge = "Multi-page",
                        color = RoyalBlue600,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate(ScreenRoute.TOOLS) }
                    )
                    QuickToolCard(
                        title = "Screenshot → Doc",
                        icon = Icons.Outlined.CropFree,
                        badge = "AI OCR",
                        color = Emerald500,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate(ScreenRoute.TOOLS) }
                    )
                    QuickToolCard(
                        title = "PDF Analyzer",
                        icon = Icons.Outlined.Analytics,
                        badge = "Extract",
                        color = Amber500,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate(ScreenRoute.TOOLS) }
                    )
                }
            }
        }

        // 3. Document Category Selector
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Document Categories",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Navy900
                    )
                    TextButton(onClick = { onNavigate(ScreenRoute.TEMPLATES) }) {
                        Text("View all", fontSize = 12.sp, color = RoyalBlue600)
                    }
                }

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item {
                        FilterChip(
                            selected = selectedCategory == null,
                            onClick = { selectedCategory = null },
                            label = { Text("All", fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = RoyalBlue600,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                    items(DocCategory.values()) { category ->
                        FilterChip(
                            selected = selectedCategory == category,
                            onClick = { selectedCategory = category },
                            label = { Text(category.displayName, fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = RoyalBlue600,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }
        }

        // 4. Popular Starter Templates Carousel
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Popular Templates",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Navy900
                    )
                    Text(
                        text = "One-tap ready",
                        fontSize = 11.sp,
                        color = Slate500
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                val filteredTemplates = remember(selectedCategory) {
                    if (selectedCategory == null) TemplateLibrary.templates
                    else TemplateLibrary.templates.filter { it.category == selectedCategory }
                }

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(filteredTemplates.take(5)) { template ->
                        TemplateCard(
                            template = template,
                            onClick = { viewModel.useTemplate(template) }
                        )
                    }
                }
            }
        }

        // 5. Recent Documents List
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Recent Documents",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Navy900
                )
                if (documents.isNotEmpty()) {
                    TextButton(onClick = { onNavigate(ScreenRoute.DOCUMENTS) }) {
                        Text("History (${documents.size})", fontSize = 12.sp, color = RoyalBlue600)
                    }
                }
            }
        }

        if (documents.isEmpty()) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Slate200, RoundedCornerShape(12.dp))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Description,
                            contentDescription = null,
                            tint = Slate400,
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "No documents created yet",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Slate700
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Describe your idea above or pick a template to begin.",
                            fontSize = 12.sp,
                            color = Slate500
                        )
                    }
                }
            }
        } else {
            val recentList = documents.take(4)
            items(recentList) { doc ->
                DocumentItemCard(
                    document = doc,
                    onOpen = { viewModel.openDocumentForEditing(doc) },
                    onOptionsClick = { selectedDocForOptions = doc }
                )
            }
        }

        // Trust & Safety Notice
        item {
            Surface(
                color = Slate100,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Shield,
                        contentDescription = "Trust & Safety",
                        tint = Slate500,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "DocuPera AI creates structured drafts. Please verify dates, financial amounts, and legal names before official submission.",
                        fontSize = 11.sp,
                        color = Slate500,
                        lineHeight = 15.sp
                    )
                }
            }
        }
    }

    // Document Options Sheet
    if (selectedDocForOptions != null) {
        val doc = selectedDocForOptions!!
        ModalBottomSheet(
            onDismissRequest = { selectedDocForOptions = null },
            containerColor = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
                    .padding(bottom = 24.dp)
            ) {
                Text(
                    text = doc.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Navy900
                )
                Text(
                    text = "${doc.type.displayName} • ${doc.language.displayName}",
                    fontSize = 12.sp,
                    color = Slate500
                )
                Spacer(modifier = Modifier.height(16.dp))

                ListItem(
                    headlineContent = { Text("Open in Editor") },
                    leadingContent = { Icon(Icons.Default.Edit, contentDescription = null, tint = RoyalBlue600) },
                    modifier = Modifier.clickable {
                        selectedDocForOptions = null
                        viewModel.openDocumentForEditing(doc)
                    }
                )
                ListItem(
                    headlineContent = { Text("Export & Download PDF") },
                    leadingContent = { Icon(Icons.Default.PictureAsPdf, contentDescription = null, tint = Emerald500) },
                    modifier = Modifier.clickable {
                        selectedDocForOptions = null
                        viewModel.openDocumentForEditing(doc)
                        viewModel.requestPdfExport { file ->
                            viewModel.pdfService.openPdf(file)
                        }
                    }
                )
                ListItem(
                    headlineContent = { Text("Duplicate Document") },
                    leadingContent = { Icon(Icons.Default.ContentCopy, contentDescription = null, tint = Slate600) },
                    modifier = Modifier.clickable {
                        selectedDocForOptions = null
                        viewModel.duplicateDocument(doc)
                    }
                )
                ListItem(
                    headlineContent = { Text("Delete Document", color = Rose500) },
                    leadingContent = { Icon(Icons.Default.DeleteOutline, contentDescription = null, tint = Rose500) },
                    modifier = Modifier.clickable {
                        selectedDocForOptions = null
                        viewModel.deleteDocument(doc.id)
                    }
                )
            }
        }
    }
}

@Composable
fun QuickToolCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    badge: String,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
            .border(1.dp, Slate200, RoundedCornerShape(12.dp))
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(color.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = icon, contentDescription = title, tint = color, modifier = Modifier.size(18.dp))
                }
                Surface(
                    color = Slate100,
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = badge,
                        fontSize = 8.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate600,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Bold,
                color = Navy900,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun TemplateCard(
    template: DocumentTemplate,
    onClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier
            .width(180.dp)
            .border(1.dp, Slate200, RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .testTag("template_card_${template.id}")
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = if (template.isPro) Amber100 else Emerald100,
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = if (template.isPro) "PRO" else "FREE",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (template.isPro) Amber500 else Emerald500,
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                    )
                }

                Text(
                    text = template.style.displayName,
                    fontSize = 9.5.sp,
                    color = Slate400
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = template.title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Navy900,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = template.subtitle,
                fontSize = 10.5.sp,
                color = Slate500,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(10.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = template.category.displayName,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    color = RoyalBlue600
                )
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Use",
                    tint = RoyalBlue600,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

@Composable
fun DocumentItemCard(
    document: DocumentData,
    onOpen: () -> Unit,
    onOptionsClick: () -> Unit
) {
    val dateStr = remember(document.updatedAt) {
        SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date(document.updatedAt))
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Slate200, RoundedCornerShape(12.dp))
            .clickable { onOpen() }
            .testTag("document_card_${document.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(RoyalBlue100),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Description,
                    contentDescription = null,
                    tint = RoyalBlue600,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = document.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Navy900,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = Slate100,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = document.type.displayName,
                            fontSize = 9.5.sp,
                            color = Slate600,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = dateStr,
                        fontSize = 10.sp,
                        color = Slate400
                    )
                }
            }

            IconButton(onClick = onOptionsClick) {
                Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Options", tint = Slate400)
            }
        }
    }
}
