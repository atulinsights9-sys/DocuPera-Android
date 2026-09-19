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
import com.example.data.model.DocLanguage
import com.example.data.model.DocStyle
import com.example.ui.AiGenerationState
import com.example.ui.DocuPeraViewModel
import com.example.ui.ScreenRoute
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiBuilderScreen(
    viewModel: DocuPeraViewModel,
    onNavigate: (ScreenRoute) -> Unit
) {
    val aiState by viewModel.aiState.collectAsState()
    val promptInput by viewModel.promptInput.collectAsState()
    val selectedStyle by viewModel.selectedStyle.collectAsState()
    val selectedLanguage by viewModel.selectedLanguage.collectAsState()
    val questionAnswers by viewModel.questionAnswers.collectAsState()

    val samplePrompts = listOf(
        "Application for home renovation bank loan of ₹5 Lakhs",
        "3-day medical leave application to manager due to viral fever",
        "GST Freelance invoice for website development ₹45,000",
        "Formal resignation letter with 30-day notice period",
        "College research report on mobile artificial intelligence",
        "Commercial price quotation for corporate gift hampers"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Slate50)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            // Studio Header
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(RoyalBlue100),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = RoyalBlue600,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "AI Document Studio",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Navy900
                        )
                        Text(
                            text = "Tell us what you need. We'll make the document.",
                            fontSize = 12.sp,
                            color = Slate500
                        )
                    }
                }
            }
        }

        when (val state = aiState) {
            is AiGenerationState.Idle -> {
                // Step 1: Input prompt & configurations
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Slate200, RoundedCornerShape(16.dp))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Describe your document",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Navy900
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = promptInput,
                                onValueChange = { viewModel.promptInput.value = it },
                                placeholder = {
                                    Text(
                                        "e.g. I need a formal letter to State Bank of India requesting a home loan of ₹5 Lakhs...",
                                        fontSize = 13.sp,
                                        color = Slate400
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(130.dp)
                                    .testTag("ai_prompt_input"),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = RoyalBlue600,
                                    unfocusedBorderColor = Slate200
                                )
                            )

                            Spacer(modifier = Modifier.height(14.dp))
                            Text(
                                text = "Quick Ideas:",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Slate600
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                items(samplePrompts) { idea ->
                                    SuggestionChip(
                                        onClick = { viewModel.promptInput.value = idea },
                                        label = { Text(idea, fontSize = 11.sp) }
                                    )
                                }
                            }
                        }
                    }
                }

                // Document Settings: Tone/Style and Language
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Slate200, RoundedCornerShape(16.dp))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Document Tone & Style",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Navy900
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(DocStyle.values()) { style ->
                                    FilterChip(
                                        selected = selectedStyle == style,
                                        onClick = { viewModel.selectedStyle.value = style },
                                        label = { Text(style.displayName, fontSize = 12.sp) },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = RoyalBlue600,
                                            selectedLabelColor = Color.White
                                        )
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))
                            Text(
                                text = "Language",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Navy900
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                DocLanguage.values().forEach { lang ->
                                    FilterChip(
                                        selected = selectedLanguage == lang,
                                        onClick = { viewModel.selectedLanguage.value = lang },
                                        label = { Text("${lang.displayName} (${lang.nativeName})", fontSize = 12.sp) },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = RoyalBlue600,
                                            selectedLabelColor = Color.White
                                        )
                                    )
                                }
                            }
                        }
                    }
                }

                item {
                    Button(
                        onClick = { viewModel.submitPromptForQuestions() },
                        colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue600),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("btn_continue_to_questions")
                    ) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Continue & Review Details", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            is AiGenerationState.AskingQuestions -> {
                // Step 2: Intelligent clarification questionnaire
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = RoyalBlue100.copy(alpha = 0.6f)),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = RoyalBlue600,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "DocuPera asks only necessary questions",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Navy900
                                )
                                Text(
                                    text = "Fill in what you know. Our AI will handle formatting & tone seamlessly.",
                                    fontSize = 11.5.sp,
                                    color = Slate600
                                )
                            }
                        }
                    }
                }

                item {
                    Text(
                        text = "Document Details:",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Navy900
                    )
                }

                items(state.questions) { question ->
                    val answer = questionAnswers[question] ?: ""
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Slate200, RoundedCornerShape(12.dp))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = question,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Navy900
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            OutlinedTextField(
                                value = answer,
                                onValueChange = { viewModel.updateQuestionAnswer(question, it) },
                                placeholder = { Text("Enter answer (or leave blank for AI default)", fontSize = 12.sp, color = Slate400) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = RoyalBlue600,
                                    unfocusedBorderColor = Slate200
                                )
                            )
                        }
                    }
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = { viewModel.startAiCreation(promptInput) },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                        ) {
                            Text("Back", color = Slate700)
                        }

                        Button(
                            onClick = { viewModel.generateFinalDocument() },
                            colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue600),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(2f)
                                .height(50.dp)
                                .testTag("btn_build_document")
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Make Document", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            is AiGenerationState.Generating -> {
                // Step 3: Synthesis progress
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Slate200, RoundedCornerShape(16.dp))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(
                                color = RoyalBlue600,
                                modifier = Modifier.size(48.dp),
                                strokeWidth = 4.dp
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            Text(
                                text = "DocuPera AI Studio",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Navy900
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = state.step,
                                fontSize = 13.sp,
                                color = RoyalBlue600,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            LinearProgressIndicator(
                                color = RoyalBlue600,
                                trackColor = Slate100,
                                modifier = Modifier
                                    .fillMaxWidth(0.7f)
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp))
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Structuring sections, checking tone, and formatting printable page layout...",
                                fontSize = 11.5.sp,
                                color = Slate400,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                    }
                }
            }

            is AiGenerationState.Error -> {
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Rose100),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Generation Notice",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Rose500
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = state.message, fontSize = 12.sp, color = Slate700)
                            Spacer(modifier = Modifier.height(10.dp))
                            Button(
                                onClick = { viewModel.startAiCreation(promptInput) },
                                colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue600)
                            ) {
                                Text("Try Again")
                            }
                        }
                    }
                }
            }

            is AiGenerationState.Completed -> {
                // Handled via navigation to Editor
            }
        }
    }
}
