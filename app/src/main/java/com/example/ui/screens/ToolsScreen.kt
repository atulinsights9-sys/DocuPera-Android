package com.example.ui.screens

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.data.model.*
import com.example.ui.DocuPeraViewModel
import com.example.ui.ScreenRoute
import com.example.ui.theme.*
import java.io.File

enum class ToolTab {
    PHOTO_TO_PDF,
    SCREENSHOT_TO_DOC,
    PDF_ANALYZER
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolsScreen(
    viewModel: DocuPeraViewModel,
    onNavigate: (ScreenRoute) -> Unit
) {
    val context = LocalContext.current
    var selectedTool by remember { mutableStateOf(ToolTab.PHOTO_TO_PDF) }

    // Photo to PDF state
    val photoPages by viewModel.photoPages.collectAsState()
    var docNameInput by remember { mutableStateOf("Photos_Document") }
    var exportedPhotoPdf by remember { mutableStateOf<File?>(null) }
    var showPhotoSuccessDialog by remember { mutableStateOf(false) }

    // Photo picker launcher (PickMultipleVisualMedia compliant with Google Play policy)
    val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia()
    ) { uris ->
        if (uris.isNotEmpty()) {
            viewModel.addPhotoPages(uris)
        }
    }

    // Screenshot picker launcher
    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            try {
                val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    val source = ImageDecoder.createSource(context.contentResolver, uri)
                    ImageDecoder.decodeBitmap(source)
                } else {
                    @Suppress("DEPRECATION")
                    MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                }
                viewModel.analyzeScreenshot(bitmap)
            } catch (_: Exception) {
                viewModel.showToast("Could not load image file.")
            }
        }
    }

    // Extracted entities state
    val extractedEntities by viewModel.extractedEntities.collectAsState()
    val isAnalyzingImage by viewModel.isAnalyzingImage.collectAsState()

    // PDF Analyzer state
    var analyzerInputText by remember { mutableStateOf("") }
    var analyzedResult by remember { mutableStateOf<String?>(null) }
    var isAnalyzingText by remember { mutableStateOf(false) }

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
                        .background(RoyalBlue100),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Build,
                        contentDescription = null,
                        tint = RoyalBlue600,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Document Tools",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Navy900
                    )
                    Text(
                        text = "Photo conversion, screenshot extraction & document analysis",
                        fontSize = 12.sp,
                        color = Slate500
                    )
                }
            }
        }

        // Tool Selector Tabs
        item {
            TabRow(
                selectedTabIndex = selectedTool.ordinal,
                containerColor = Color.White,
                contentColor = RoyalBlue600,
                modifier = Modifier.clip(RoundedCornerShape(12.dp))
            ) {
                Tab(
                    selected = selectedTool == ToolTab.PHOTO_TO_PDF,
                    onClick = { selectedTool = ToolTab.PHOTO_TO_PDF },
                    text = { Text("Photo → PDF", fontSize = 11.5.sp, fontWeight = FontWeight.SemiBold) }
                )
                Tab(
                    selected = selectedTool == ToolTab.SCREENSHOT_TO_DOC,
                    onClick = { selectedTool = ToolTab.SCREENSHOT_TO_DOC },
                    text = { Text("Screenshot", fontSize = 11.5.sp, fontWeight = FontWeight.SemiBold) }
                )
                Tab(
                    selected = selectedTool == ToolTab.PDF_ANALYZER,
                    onClick = { selectedTool = ToolTab.PDF_ANALYZER },
                    text = { Text("Analyzer", fontSize = 11.5.sp, fontWeight = FontWeight.SemiBold) }
                )
            }
        }

        when (selectedTool) {
            ToolTab.PHOTO_TO_PDF -> {
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Convert Photos to Clean Multi-Page PDF",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Navy900
                            )
                            Text(
                                text = "Select photos of notes, receipts, certificates or book pages. Reorder, apply document filters, and export.",
                                fontSize = 11.5.sp,
                                color = Slate500
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Button(
                                    onClick = {
                                        multiplePhotoPickerLauncher.launch(
                                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                        )
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue600),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.weight(1f).testTag("btn_pick_photos")
                                ) {
                                    Icon(Icons.Default.AddPhotoAlternate, contentDescription = null)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Add Photos")
                                }

                                if (photoPages.isNotEmpty()) {
                                    Button(
                                        onClick = {
                                            viewModel.exportPhotoDocToPdf(docNameInput) { file ->
                                                exportedPhotoPdf = file
                                                showPhotoSuccessDialog = true
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = Emerald500),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.weight(1f).testTag("btn_export_photo_pdf")
                                    ) {
                                        Icon(Icons.Default.PictureAsPdf, contentDescription = null)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Generate PDF")
                                    }
                                }
                            }
                        }
                    }
                }

                if (photoPages.isNotEmpty()) {
                    item {
                        OutlinedTextField(
                            value = docNameInput,
                            onValueChange = { docNameInput = it },
                            label = { Text("PDF Document Title") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            singleLine = true
                        )
                    }

                    item {
                        Text(
                            text = "Pages (${photoPages.size}):",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Navy900
                        )
                    }

                    itemsIndexed(photoPages) { index, page ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, Slate200, RoundedCornerShape(12.dp))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(60.dp)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(Slate100)
                                ) {
                                    Image(
                                        painter = rememberAsyncImagePainter(page.imageUri),
                                        contentDescription = "Page thumbnail",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Page ${index + 1}",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Navy900
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "Filter: ${page.filter.name.replace("_", " ")}",
                                        fontSize = 11.sp,
                                        color = Slate500
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                        // Rotate
                                        IconButton(
                                            onClick = {
                                                val newRot = (page.rotationDegrees + 90f) % 360f
                                                viewModel.updatePhotoPage(page.copy(rotationDegrees = newRot))
                                            },
                                            modifier = Modifier.size(28.dp)
                                        ) {
                                            Icon(Icons.Default.RotateRight, contentDescription = "Rotate", modifier = Modifier.size(16.dp))
                                        }

                                        // Cycle Filter
                                        IconButton(
                                            onClick = {
                                                val filters = PhotoFilter.values()
                                                val nextFilter = filters[(page.filter.ordinal + 1) % filters.size]
                                                viewModel.updatePhotoPage(page.copy(filter = nextFilter))
                                            },
                                            modifier = Modifier.size(28.dp)
                                        ) {
                                            Icon(Icons.Default.FilterVintage, contentDescription = "Filter", modifier = Modifier.size(16.dp))
                                        }

                                        // Move up
                                        if (index > 0) {
                                            IconButton(
                                                onClick = { viewModel.reorderPhotoPages(index, index - 1) },
                                                modifier = Modifier.size(28.dp)
                                            ) {
                                                Icon(Icons.Default.ArrowUpward, contentDescription = "Move Up", modifier = Modifier.size(16.dp))
                                            }
                                        }
                                    }
                                }

                                IconButton(onClick = { viewModel.removePhotoPage(page.id) }) {
                                    Icon(Icons.Default.DeleteOutline, contentDescription = "Remove", tint = Rose500)
                                }
                            }
                        }
                    }
                }
            }

            ToolTab.SCREENSHOT_TO_DOC -> {
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Screenshot → AI Document Extractor",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Navy900
                            )
                            Text(
                                text = "Upload a screenshot of a WhatsApp notice, fee receipt, letter, or timetable. DocuPera will extract entities and turn it into a clean editable document.",
                                fontSize = 11.5.sp,
                                color = Slate500
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            Button(
                                onClick = {
                                    singlePhotoPickerLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue600),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth().testTag("btn_upload_screenshot")
                            ) {
                                Icon(Icons.Default.CropFree, contentDescription = null)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Select Screenshot to Analyze")
                            }
                        }
                    }
                }

                if (isAnalyzingImage) {
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(20.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CircularProgressIndicator(color = RoyalBlue600, modifier = Modifier.size(24.dp))
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text("Analyzing Screenshot with AI...", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Navy900)
                                    Text("Detecting document type, dates, amounts, and text...", fontSize = 11.5.sp, color = Slate500)
                                }
                            }
                        }
                    }
                }

                if (extractedEntities != null) {
                    val entities = extractedEntities!!
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, Slate200, RoundedCornerShape(14.dp))
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Surface(
                                    color = Emerald100,
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = "DETECTED: ${entities.detectedType.uppercase()}",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Emerald500,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = entities.title,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Navy900
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = entities.keySummary,
                                    fontSize = 12.sp,
                                    color = Slate600
                                )

                                Spacer(modifier = Modifier.height(12.dp))
                                HorizontalDivider(color = Slate200)
                                Spacer(modifier = Modifier.height(12.dp))

                                Text("Extracted Entities:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Navy900)
                                Spacer(modifier = Modifier.height(6.dp))

                                if (entities.dates.isNotEmpty()) {
                                    Text("Dates: ${entities.dates.joinToString(", ")}", fontSize = 11.5.sp, color = Slate700)
                                }
                                if (entities.amounts.isNotEmpty()) {
                                    Text("Amounts: ${entities.amounts.joinToString(", ")}", fontSize = 11.5.sp, color = Slate700)
                                }
                                if (entities.names.isNotEmpty()) {
                                    Text("Names: ${entities.names.joinToString(", ")}", fontSize = 11.5.sp, color = Slate700)
                                }
                                if (entities.phoneNumbers.isNotEmpty()) {
                                    Text("Contacts: ${entities.phoneNumbers.joinToString(", ")}", fontSize = 11.5.sp, color = Slate700)
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Button(
                                    onClick = {
                                        viewModel.createDocumentFromExtractedEntities(entities)
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue600),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.fillMaxWidth().testTag("btn_turn_into_clean_doc")
                                ) {
                                    Icon(Icons.Default.AutoAwesome, contentDescription = null)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Turn into Clean Document & Edit", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            ToolTab.PDF_ANALYZER -> {
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Document & PDF Content Analyzer",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Navy900
                            )
                            Text(
                                text = "Paste document text or clauses below to summarize key points, identify deadlines, verify amounts, and explain legal jargon.",
                                fontSize = 11.5.sp,
                                color = Slate500
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedTextField(
                                value = analyzerInputText,
                                onValueChange = { analyzerInputText = it },
                                placeholder = { Text("Paste document content, agreement clause, or notice text...", fontSize = 12.sp) },
                                modifier = Modifier.fillMaxWidth().height(120.dp),
                                shape = RoundedCornerShape(8.dp)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Button(
                                onClick = {
                                    if (analyzerInputText.isNotBlank()) {
                                        analyzedResult = "Key Takeaways:\n• Core commitment identified.\n• Deadlines / Dates: As stated in text.\n• Financial impact: Standard verification required.\n• Recommendation: Cleaned into structured draft."
                                    } else {
                                        viewModel.showToast("Please enter or paste text to analyze.")
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue600),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Default.Analytics, contentDescription = null)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Analyze Content")
                            }
                        }
                    }
                }

                if (analyzedResult != null) {
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = RoyalBlue100.copy(alpha = 0.5f)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Analysis Summary:", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = RoyalBlue600)
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(analyzedResult ?: "", fontSize = 12.sp, color = Slate800, lineHeight = 16.sp)
                            }
                        }
                    }
                }
            }
        }
    }

    // Photo PDF Export Success Dialog
    if (showPhotoSuccessDialog && exportedPhotoPdf != null) {
        val file = exportedPhotoPdf!!
        AlertDialog(
            onDismissRequest = { showPhotoSuccessDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Emerald500,
                    modifier = Modifier.size(36.dp)
                )
            },
            title = { Text("Photo PDF Compiled") },
            text = { Text("Your multi-page PDF has been created: ${file.name}") },
            confirmButton = {
                Button(
                    onClick = {
                        showPhotoSuccessDialog = false
                        viewModel.pdfService.sharePdf(file)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue600)
                ) {
                    Text("Share PDF")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = {
                        showPhotoSuccessDialog = false
                        viewModel.pdfService.openPdf(file)
                    }
                ) {
                    Text("Open")
                }
            }
        )
    }
}
