package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import com.example.data.model.DocCategory
import com.example.data.model.DocumentData
import com.example.ui.DocuPeraViewModel
import com.example.ui.ScreenRoute
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentsHistoryScreen(
    viewModel: DocuPeraViewModel,
    onNavigate: (ScreenRoute) -> Unit
) {
    val documents by viewModel.allDocuments.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<DocCategory?>(null) }
    var docForRename by remember { mutableStateOf<DocumentData?>(null) }
    var newTitleInput by remember { mutableStateOf("") }

    val filteredList = remember(documents, searchQuery, selectedCategory) {
        documents.filter { doc ->
            (selectedCategory == null || doc.category == selectedCategory) &&
                    (searchQuery.isBlank() ||
                            doc.title.contains(searchQuery, ignoreCase = true) ||
                            doc.metadata.senderName.contains(searchQuery, ignoreCase = true) ||
                            doc.type.displayName.contains(searchQuery, ignoreCase = true))
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Slate50)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(RoyalBlue100),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Description,
                            contentDescription = null,
                            tint = RoyalBlue600,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "My Documents",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Navy900
                        )
                        Text(
                            text = "${documents.size} saved documents",
                            fontSize = 12.sp,
                            color = Slate500
                        )
                    }
                }

                Button(
                    onClick = { viewModel.startAiCreation() },
                    colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue600),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("New", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Search Bar
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search by title, party name or category...", fontSize = 13.sp) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Slate400) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Close, contentDescription = "Clear", tint = Slate400)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("history_search_input"),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = RoyalBlue600,
                    unfocusedBorderColor = Slate200,
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                ),
                singleLine = true
            )
        }

        // Category Filter Chips
        item {
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
                items(DocCategory.values()) { cat ->
                    FilterChip(
                        selected = selectedCategory == cat,
                        onClick = { selectedCategory = cat },
                        label = { Text(cat.displayName, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = RoyalBlue600,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }

        // Documents List
        if (filteredList.isEmpty()) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp)
                        .border(1.dp, Slate200, RoundedCornerShape(12.dp))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.FolderOpen,
                            contentDescription = null,
                            tint = Slate400,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = if (searchQuery.isNotBlank()) "No matching documents" else "No saved documents yet",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Navy900
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Create with AI or select a pre-made template.",
                            fontSize = 12.sp,
                            color = Slate500
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.startAiCreation() },
                            colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue600),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Create with AI")
                        }
                    }
                }
            }
        } else {
            items(filteredList) { doc ->
                var showOptionsSheet by remember { mutableStateOf(false) }

                DocumentItemCard(
                    document = doc,
                    onOpen = { viewModel.openDocumentForEditing(doc) },
                    onOptionsClick = { showOptionsSheet = true }
                )

                if (showOptionsSheet) {
                    ModalBottomSheet(
                        onDismissRequest = { showOptionsSheet = false },
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
                            Spacer(modifier = Modifier.height(12.dp))

                            ListItem(
                                headlineContent = { Text("Open & Edit") },
                                leadingContent = { Icon(Icons.Default.Edit, contentDescription = null, tint = RoyalBlue600) },
                                modifier = Modifier.clickable {
                                    showOptionsSheet = false
                                    viewModel.openDocumentForEditing(doc)
                                }
                            )
                            ListItem(
                                headlineContent = { Text("Download PDF") },
                                leadingContent = { Icon(Icons.Default.PictureAsPdf, contentDescription = null, tint = Emerald500) },
                                modifier = Modifier.clickable {
                                    showOptionsSheet = false
                                    viewModel.openDocumentForEditing(doc)
                                    viewModel.requestPdfExport { file ->
                                        viewModel.pdfService.openPdf(file)
                                    }
                                }
                            )
                            ListItem(
                                headlineContent = { Text("Rename Title") },
                                leadingContent = { Icon(Icons.Default.DriveFileRenameOutline, contentDescription = null, tint = Slate600) },
                                modifier = Modifier.clickable {
                                    showOptionsSheet = false
                                    docForRename = doc
                                    newTitleInput = doc.title
                                }
                            )
                            ListItem(
                                headlineContent = { Text("Duplicate Document") },
                                leadingContent = { Icon(Icons.Default.ContentCopy, contentDescription = null, tint = Slate600) },
                                modifier = Modifier.clickable {
                                    showOptionsSheet = false
                                    viewModel.duplicateDocument(doc)
                                }
                            )
                            ListItem(
                                headlineContent = { Text("Delete Document", color = Rose500) },
                                leadingContent = { Icon(Icons.Default.DeleteOutline, contentDescription = null, tint = Rose500) },
                                modifier = Modifier.clickable {
                                    showOptionsSheet = false
                                    viewModel.deleteDocument(doc.id)
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    // Rename Dialog
    if (docForRename != null) {
        val target = docForRename!!
        AlertDialog(
            onDismissRequest = { docForRename = null },
            title = { Text("Rename Document") },
            text = {
                OutlinedTextField(
                    value = newTitleInput,
                    onValueChange = { newTitleInput = it },
                    label = { Text("Document Title") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newTitleInput.isNotBlank()) {
                            viewModel.renameDocument(target, newTitleInput.trim())
                            docForRename = null
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue600)
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { docForRename = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}
