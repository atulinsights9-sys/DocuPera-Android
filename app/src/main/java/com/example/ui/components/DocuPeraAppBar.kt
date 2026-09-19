package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.PlanType
import com.example.ui.ScreenRoute
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocuPeraAppBar(
    currentScreen: ScreenRoute,
    credits: Int,
    currentPlan: PlanType,
    onCreditClick: () -> Unit,
    onAdminClick: () -> Unit,
    onLandingClick: () -> Unit,
    onBackClick: (() -> Unit)? = null
) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onLandingClick() }
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Navy900),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.docupera_logo),
                        contentDescription = "DocuPera Logo",
                        modifier = Modifier.size(28.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "DocuPera",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Navy900
                        )
                        if (currentPlan != PlanType.FREE) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                color = if (currentPlan == PlanType.PRO) RoyalBlue600 else Navy900,
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = currentPlan.name,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                    Text(
                        text = "AI Document Studio",
                        fontSize = 10.sp,
                        color = Slate500
                    )
                }
            }
        },
        navigationIcon = {
            if (onBackClick != null) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.testTag("app_bar_back_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Navy900
                    )
                }
            }
        },
        actions = {
            // Credits / Plan Chip
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = if (credits > 0) RoyalBlue100 else Amber100,
                modifier = Modifier
                    .padding(end = 6.dp)
                    .clickable { onCreditClick() }
                    .testTag("credit_balance_chip")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = if (currentPlan != PlanType.FREE) Icons.Default.Bolt else Icons.Default.Stars,
                        contentDescription = "Credits",
                        tint = if (credits > 0) RoyalBlue600 else Amber500,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (currentPlan != PlanType.FREE) currentPlan.displayName else "$credits Credits",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (credits > 0) RoyalBlue600 else Slate900
                    )
                }
            }

            // Quick Menu: Admin / Business
            IconButton(
                onClick = onAdminClick,
                modifier = Modifier.testTag("admin_settings_button")
            ) {
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = "Admin & Settings",
                    tint = Slate500
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White,
            titleContentColor = Navy900
        )
    )
}

@Composable
fun DocuPeraBottomNav(
    currentScreen: ScreenRoute,
    onNavigate: (ScreenRoute) -> Unit
) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 6.dp,
        modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        NavigationBarItem(
            selected = currentScreen == ScreenRoute.HOME,
            onClick = { onNavigate(ScreenRoute.HOME) },
            icon = {
                Icon(
                    imageVector = if (currentScreen == ScreenRoute.HOME) Icons.Filled.Home else Icons.Outlined.Home,
                    contentDescription = "Home"
                )
            },
            label = { Text("Home", fontSize = 11.sp) },
            modifier = Modifier.testTag("nav_home")
        )

        NavigationBarItem(
            selected = currentScreen == ScreenRoute.AI_BUILDER,
            onClick = { onNavigate(ScreenRoute.AI_BUILDER) },
            icon = {
                Icon(
                    imageVector = if (currentScreen == ScreenRoute.AI_BUILDER) Icons.Filled.AutoAwesome else Icons.Outlined.AutoAwesome,
                    contentDescription = "AI Studio"
                )
            },
            label = { Text("Create", fontSize = 11.sp) },
            modifier = Modifier.testTag("nav_ai_builder")
        )

        NavigationBarItem(
            selected = currentScreen == ScreenRoute.TEMPLATES,
            onClick = { onNavigate(ScreenRoute.TEMPLATES) },
            icon = {
                Icon(
                    imageVector = if (currentScreen == ScreenRoute.TEMPLATES) Icons.Filled.GridView else Icons.Outlined.GridView,
                    contentDescription = "Templates"
                )
            },
            label = { Text("Templates", fontSize = 11.sp) },
            modifier = Modifier.testTag("nav_templates")
        )

        NavigationBarItem(
            selected = currentScreen == ScreenRoute.TOOLS,
            onClick = { onNavigate(ScreenRoute.TOOLS) },
            icon = {
                Icon(
                    imageVector = if (currentScreen == ScreenRoute.TOOLS) Icons.Filled.Build else Icons.Outlined.Build,
                    contentDescription = "Tools"
                )
            },
            label = { Text("Tools", fontSize = 11.sp) },
            modifier = Modifier.testTag("nav_tools")
        )

        NavigationBarItem(
            selected = currentScreen == ScreenRoute.DOCUMENTS,
            onClick = { onNavigate(ScreenRoute.DOCUMENTS) },
            icon = {
                Icon(
                    imageVector = if (currentScreen == ScreenRoute.DOCUMENTS) Icons.Filled.Description else Icons.Outlined.Description,
                    contentDescription = "Documents"
                )
            },
            label = { Text("History", fontSize = 11.sp) },
            modifier = Modifier.testTag("nav_documents")
        )

        NavigationBarItem(
            selected = currentScreen == ScreenRoute.BILLING,
            onClick = { onNavigate(ScreenRoute.BILLING) },
            icon = {
                Icon(
                    imageVector = if (currentScreen == ScreenRoute.BILLING) Icons.Filled.AccountBalanceWallet else Icons.Outlined.AccountBalanceWallet,
                    contentDescription = "Billing"
                )
            },
            label = { Text("Billing", fontSize = 11.sp) },
            modifier = Modifier.testTag("nav_billing")
        )
    }
}
