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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.ui.DocuPeraViewModel
import com.example.ui.ScreenRoute
import com.example.ui.theme.*
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorScreen(
    viewModel: DocuPeraViewModel,
    onNavigate: (ScreenRoute) -> Unit
) {
    val document by viewModel.currentDocument.collectAsState()
    val aiWritingStatus by viewModel.aiWritingStatus.collectAsState()
    var isPreviewMode by remember { mutableStateOf(false) }
    var selectedSectionForAi by remember { mutableStateOf<String?>(null) }
    var exportedFile by remember { mutableStateOf<File?>(null) }
    var showExportSuccessDialog by remember { mutableStateOf(false) }

    if (document == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("No document open.", fontSize = 16.sp, color = Slate600)
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { onNavigate(ScreenRoute.HOME) }) {
                    Text("Return to Dashboard")
                }
            }
        }
        return
    }

    val doc = document!!

    val aiActions = listOf(
        "Make professional",
        "Make formal",
        "Make simple",
        "Shorten",
        "Expand",
        "Fix grammar",
        "Translate to Hindi",
        "Translate"
    )

    Scaffold(
        topBar = {
            Surface(
                color = Color.White,
                tonalElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { onNavigate(ScreenRoute.DOCUMENTS) }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Navy900)
                        }
                        Column {
                            Text(
                                text = doc.title,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Navy900,
                                maxLines = 1
                            )
                            Text(
                                text = "${doc.style.displayName} • ${doc.language.displayName}",
                                fontSize = 11.sp,
                                color = Slate500
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Undo button
                        IconButton(onClick = { viewModel.undoEdit() }) {
                            Icon(Icons.Default.Undo, contentDescription = "Undo", tint = Slate600)
                        }

                        // Preview / Edit Toggle
                        FilledTonalIconToggleButton(
                            checked = isPreviewMode,
                            onCheckedChange = { isPreviewMode = it },
                            modifier = Modifier.testTag("btn_toggle_preview")
                        ) {
                            Icon(
                                imageVector = if (isPreviewMode) Icons.Default.Edit else Icons.Default.Visibility,
                                contentDescription = "Toggle Preview"
                            )
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        // Download / Export Button
                        Button(
                            onClick = {
                                viewModel.requestPdfExport { file ->
                                    exportedFile = file
                                    showExportSuccessDialog = true
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue600),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                            modifier = Modifier.testTag("btn_export_pdf")
                        ) {
                            Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("PDF", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        },
        containerColor = Slate100
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // AI Action Progress Bar
            if (aiWritingStatus != null) {
                Surface(
                    color = RoyalBlue100,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            color = RoyalBlue600,
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = aiWritingStatus ?: "",
                            fontSize = 12.sp,
                            color = RoyalBlue600,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            if (isPreviewMode) {
                // --- Live A4 Printable Preview Mode ---
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(4.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(8.dp, RoundedCornerShape(4.dp))
                                .border(1.dp, Slate200, RoundedCornerShape(4.dp))
                                .testTag("live_a4_preview_card")
                        ) {
                            Column(modifier = Modifier.padding(20.dp)) {
                                // Top Accent Bar
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(4.dp)
                                        .background(RoyalBlue600)
                                )
                                Spacer(modifier = Modifier.height(16.dp))

                                // Document Title
                                Text(
                                    text = doc.title,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Slate900
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                HorizontalDivider(color = Slate200, thickness = 1.dp)
                                Spacer(modifier = Modifier.height(12.dp))

                                // Metadata Block
                                val meta = doc.metadata
                                if (meta.dateString.isNotBlank()) {
                                    Text(
                                        text = "Date: ${meta.dateString}",
                                        fontSize = 10.sp,
                                        color = Slate600,
                                        modifier = Modifier.align(Alignment.End)
                                    )
                                }

                                if (meta.senderName.isNotBlank() || meta.senderAddress.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text("FROM:", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Slate400)
                                    Text(meta.senderName, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Slate800)
                                    if (meta.senderTitle.isNotBlank()) Text(meta.senderTitle, fontSize = 10.sp, color = Slate600)
                                    if (meta.senderAddress.isNotBlank()) Text(meta.senderAddress, fontSize = 10.sp, color = Slate600)
                                    if (meta.senderContact.isNotBlank()) Text(meta.senderContact, fontSize = 10.sp, color = Slate600)
                                }

                                if (meta.recipientName.isNotBlank() || meta.recipientAddress.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text("TO:", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Slate400)
                                    Text(meta.recipientName, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Slate800)
                                    if (meta.recipientTitle.isNotBlank()) Text(meta.recipientTitle, fontSize = 10.sp, color = Slate600)
                                    if (meta.recipientAddress.isNotBlank()) Text(meta.recipientAddress, fontSize = 10.sp, color = Slate600)
                                }

                                if (meta.refNumber.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text(
                                        text = meta.refNumber,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Slate900
                                    )
                                }

                                if (meta.amountTotal.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Surface(
                                        color = RoyalBlue100,
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = "Total Amount: ${meta.amountTotal}",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = RoyalBlue600,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))
                                HorizontalDivider(color = Slate200, thickness = 1.dp)
                                Spacer(modifier = Modifier.height(14.dp))

                                // Sections
                                for (sec in doc.sections) {
                                    if (sec.heading.isNotBlank()) {
                                        Text(
                                            text = sec.heading,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = RoyalBlue600
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                    }
                                    if (sec.body.isNotBlank()) {
                                        Text(
                                            text = sec.body,
                                            fontSize = 10.5.sp,
                                            color = Slate700,
                                            lineHeight = 15.sp
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                    }
                                    if (sec.bulletPoints.isNotEmpty()) {
                                        for (b in sec.bulletPoints) {
                                            Row(modifier = Modifier.padding(vertical = 2.dp)) {
                                                Text("• ", fontSize = 11.sp, color = RoyalBlue600, fontWeight = FontWeight.Bold)
                                                Text(b, fontSize = 10.5.sp, color = Slate700, lineHeight = 15.sp)
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(8.dp))
                                    }
                                }

                                Spacer(modifier = Modifier.height(24.dp))
                                HorizontalDivider(color = Slate200, thickness = 1.dp)
                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(doc.footerText, fontSize = 8.sp, color = Slate400)
                                    Text("Page 1 • DocuPera Studio", fontSize = 8.sp, color = Slate400)
                                }
                            }
                        }
                    }
                }
            } else {
                // --- Edit Mode with AI Writing Toolbar ---
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(top = 12.dp, bottom = 80.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Title Card
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text("Document Title", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Slate500)
                                Spacer(modifier = Modifier.height(4.dp))
                                OutlinedTextField(
                                    value = doc.title,
                                    onValueChange = { viewModel.updateCurrentDocument(doc.copy(title = it)) },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true,
                                    shape = RoundedCornerShape(8.dp)
                                )
                            }
                        }
                    }

                    // Metadata Accordion Card
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text("Header & Parties", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Navy900)
                                Spacer(modifier = Modifier.height(10.dp))

                                OutlinedTextField(
                                    value = doc.metadata.senderName,
                                    onValueChange = { viewModel.updateCurrentDocument(doc.copy(metadata = doc.metadata.copy(senderName = it))) },
                                    label = { Text("Sender / Creator Name") },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                OutlinedTextField(
                                    value = doc.metadata.recipientName,
                                    onValueChange = { viewModel.updateCurrentDocument(doc.copy(metadata = doc.metadata.copy(recipientName = it))) },
                                    label = { Text("Recipient / Company Name") },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    OutlinedTextField(
                                        value = doc.metadata.dateString,
                                        onValueChange = { viewModel.updateCurrentDocument(doc.copy(metadata = doc.metadata.copy(dateString = it))) },
                                        label = { Text("Date") },
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    OutlinedTextField(
                                        value = doc.metadata.refNumber,
                                        onValueChange = { viewModel.updateCurrentDocument(doc.copy(metadata = doc.metadata.copy(refNumber = it))) },
                                        label = { Text("Ref / Subject") },
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Sections List with AI Tools
                    items(doc.sections) { sec ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    OutlinedTextField(
                                        value = sec.heading,
                                        onValueChange = { newHeading ->
                                            val updated = doc.sections.map { if (it.id == sec.id) it.copy(heading = newHeading) else it }
                                            viewModel.updateCurrentDocument(doc.copy(sections = updated))
                                        },
                                        placeholder = { Text("Section Heading") },
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(8.dp),
                                        singleLine = true
                                    )
                                    IconButton(onClick = { viewModel.deleteSection(sec.id) }) {
                                        Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = Rose500)
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                OutlinedTextField(
                                    value = sec.body,
                                    onValueChange = { newBody ->
                                        val updated = doc.sections.map { if (it.id == sec.id) it.copy(body = newBody) else it }
                                        viewModel.updateCurrentDocument(doc.copy(sections = updated))
                                    },
                                    placeholder = { Text("Section content...") },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(8.dp),
                                    minLines = 3
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                // AI Writing Toolbar for this section
                                Text(
                                    text = "AI Writing Assistant:",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Slate500
                                )
                                Spacer(modifier = Modifier.height(4.dp))

                                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    items(aiActions) { action ->
                                        SuggestionChip(
                                            onClick = { viewModel.applyAiWritingToSection(sec.id, action) },
                                            label = { Text(action, fontSize = 11.sp) },
                                            icon = {
                                                Icon(
                                                    imageVector = Icons.Default.AutoAwesome,
                                                    contentDescription = null,
                                                    tint = RoyalBlue600,
                                                    modifier = Modifier.size(12.dp)
                                                )
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Add Section Button
                    item {
                        OutlinedButton(
                            onClick = { viewModel.addSectionToCurrentDocument() },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Add Document Section")
                        }
                    }
                }
            }
        }
    }

    // Export Success Dialog
    if (showExportSuccessDialog && exportedFile != null) {
        val file = exportedFile!!
        AlertDialog(
            onDismissRequest = { showExportSuccessDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Emerald500,
                    modifier = Modifier.size(36.dp)
                )
            },
            title = { Text("PDF Ready to Download") },
            text = {
                Text(
                    "Your PDF has been generated: ${file.name}\nYou can immediately open or share it via WhatsApp, Gmail, or Drive.",
                    fontSize = 13.sp,
                    color = Slate600
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showExportSuccessDialog = false
                        viewModel.pdfService.sharePdf(file)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue600)
                ) {
                    Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Share PDF")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = {
                        showExportSuccessDialog = false
                        viewModel.pdfService.openPdf(file)
                    }
                ) {
                    Text("Open")
                }
            }
        )
    }
}
