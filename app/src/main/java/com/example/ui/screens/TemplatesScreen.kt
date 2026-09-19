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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.ui.DocuPeraViewModel
import com.example.ui.ScreenRoute
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TemplatesScreen(
    viewModel: DocuPeraViewModel,
    onNavigate: (ScreenRoute) -> Unit
) {
    var selectedCategory by remember { mutableStateOf<DocCategory?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var filterProOnly by remember { mutableStateOf(false) }

    val allTemplates = TemplateLibrary.templates
    val filteredTemplates = remember(selectedCategory, searchQuery, filterProOnly) {
        allTemplates.filter { template ->
            (selectedCategory == null || template.category == selectedCategory) &&
                    (!filterProOnly || template.isPro) &&
                    (searchQuery.isBlank() ||
                            template.title.contains(searchQuery, ignoreCase = true) ||
                            template.subtitle.contains(searchQuery, ignoreCase = true) ||
                            template.docType.displayName.contains(searchQuery, ignoreCase = true))
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
        item {
            // Header
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(RoyalBlue100),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.GridView,
                        contentDescription = null,
                        tint = RoyalBlue600,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Template Library",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Navy900
                    )
                    Text(
                        text = "Tested layouts ready for instant editing & export",
                        fontSize = 12.sp,
                        color = Slate500
                    )
                }
            }
        }

        // Search Bar
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search templates (e.g. invoice, resume, leave)...", fontSize = 13.sp) },
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
                    .testTag("templates_search_input"),
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

        // Template Packs Showcase
        item {
            Column {
                Text(
                    text = "Professional Template Packs",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Navy900
                )
                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(TemplateLibrary.templatePacks) { pack ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Navy900),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .width(220.dp)
                                .clickable {
                                    // Filter to templates matching this pack or category
                                    searchQuery = pack.title.split(" ").first()
                                }
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        color = Amber500,
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = pack.badge,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Navy900,
                                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                        )
                                    }
                                    Text(
                                        text = "${pack.templateCount} Templates",
                                        fontSize = 10.sp,
                                        color = Slate400
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = pack.title,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = pack.description,
                                    fontSize = 11.sp,
                                    color = Slate300,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }
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

        // Template Grid / List
        if (filteredTemplates.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No templates found matching query.", fontSize = 13.sp, color = Slate500)
                }
            }
        } else {
            items(filteredTemplates) { template ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Slate200, RoundedCornerShape(14.dp))
                        .clickable { viewModel.useTemplate(template) }
                        .testTag("template_item_${template.id}")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
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
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }

                            Text(
                                text = "${template.category.displayName} • ${template.style.displayName}",
                                fontSize = 11.sp,
                                color = Slate400
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = template.title,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Navy900
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = template.subtitle,
                            fontSize = 12.sp,
                            color = Slate600
                        )

                        Spacer(modifier = Modifier.height(14.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${template.initialData.sections.size} Sections structured",
                                fontSize = 11.sp,
                                color = Slate400
                            )

                            Button(
                                onClick = { viewModel.useTemplate(template) },
                                colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue600),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                            ) {
                                Text("Use Template", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
            }
        }
    }
}
