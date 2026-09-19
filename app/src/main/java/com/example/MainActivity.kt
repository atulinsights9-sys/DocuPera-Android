package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.ui.DocuPeraViewModel
import com.example.ui.ScreenRoute
import com.example.ui.components.DocuPeraAppBar
import com.example.ui.components.DocuPeraBottomNav
import com.example.ui.components.PaywallModal
import com.example.ui.screens.*
import com.example.ui.theme.DocuPeraTheme

class MainActivity : ComponentActivity() {

    private val viewModel: DocuPeraViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            DocuPeraTheme {
                DocuPeraApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun DocuPeraApp(viewModel: DocuPeraViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val credits by viewModel.credits.collectAsState()
    val currentPlan by viewModel.currentPlan.collectAsState()
    val pricingConfig by viewModel.pricingConfig.collectAsState()
    val showPaywall by viewModel.showPaywallModal.collectAsState()
    val userMessage by viewModel.userMessage.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(userMessage) {
        userMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearUserMessage()
        }
    }

    // Back handling
    BackHandler(enabled = currentScreen != ScreenRoute.HOME) {
        when (currentScreen) {
            ScreenRoute.EDITOR -> viewModel.navigateTo(ScreenRoute.DOCUMENTS)
            ScreenRoute.AI_BUILDER -> viewModel.navigateTo(ScreenRoute.HOME)
            ScreenRoute.LANDING -> viewModel.navigateTo(ScreenRoute.HOME)
            ScreenRoute.BUSINESS_MODE -> viewModel.navigateTo(ScreenRoute.BILLING)
            ScreenRoute.ADMIN -> viewModel.navigateTo(ScreenRoute.HOME)
            else -> viewModel.navigateTo(ScreenRoute.HOME)
        }
    }

    val showBottomNav = currentScreen != ScreenRoute.EDITOR && currentScreen != ScreenRoute.LANDING
    val showTopBar = currentScreen != ScreenRoute.EDITOR && currentScreen != ScreenRoute.LANDING

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (showTopBar) {
                DocuPeraAppBar(
                    currentScreen = currentScreen,
                    credits = credits,
                    currentPlan = currentPlan,
                    onCreditClick = { viewModel.navigateTo(ScreenRoute.BILLING) },
                    onAdminClick = { viewModel.navigateTo(ScreenRoute.ADMIN) },
                    onLandingClick = { viewModel.navigateTo(ScreenRoute.LANDING) },
                    onBackClick = if (currentScreen != ScreenRoute.HOME) {
                        { viewModel.navigateTo(ScreenRoute.HOME) }
                    } else null
                )
            }
        },
        bottomBar = {
            if (showBottomNav) {
                DocuPeraBottomNav(
                    currentScreen = currentScreen,
                    onNavigate = { viewModel.navigateTo(it) }
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                ScreenRoute.LANDING -> LandingScreen(
                    viewModel = viewModel,
                    onNavigate = { viewModel.navigateTo(it) }
                )
                ScreenRoute.HOME -> HomeScreen(
                    viewModel = viewModel,
                    onNavigate = { viewModel.navigateTo(it) }
                )
                ScreenRoute.AI_BUILDER -> AiBuilderScreen(
                    viewModel = viewModel,
                    onNavigate = { viewModel.navigateTo(it) }
                )
                ScreenRoute.EDITOR -> EditorScreen(
                    viewModel = viewModel,
                    onNavigate = { viewModel.navigateTo(it) }
                )
                ScreenRoute.TEMPLATES -> TemplatesScreen(
                    viewModel = viewModel,
                    onNavigate = { viewModel.navigateTo(it) }
                )
                ScreenRoute.TOOLS -> ToolsScreen(
                    viewModel = viewModel,
                    onNavigate = { viewModel.navigateTo(it) }
                )
                ScreenRoute.DOCUMENTS -> DocumentsHistoryScreen(
                    viewModel = viewModel,
                    onNavigate = { viewModel.navigateTo(it) }
                )
                ScreenRoute.BILLING -> BillingScreen(
                    viewModel = viewModel,
                    onNavigate = { viewModel.navigateTo(it) }
                )
                ScreenRoute.BUSINESS_MODE -> BusinessModeScreen(
                    viewModel = viewModel,
                    onNavigate = { viewModel.navigateTo(it) }
                )
                ScreenRoute.ADMIN -> AdminScreen(
                    viewModel = viewModel,
                    onNavigate = { viewModel.navigateTo(it) }
                )
            }

            // Paywall Modal
            if (showPaywall) {
                PaywallModal(
                    credits = credits,
                    pricingConfig = pricingConfig,
                    onDismiss = { viewModel.dismissPaywall() },
                    onBuyPack = { pack -> viewModel.purchaseCredits(pack) },
                    onUpgradePro = { viewModel.upgradeSubscription(com.example.data.model.PlanType.PRO) },
                    onNavigateToBilling = {
                        viewModel.dismissPaywall()
                        viewModel.navigateTo(ScreenRoute.BILLING)
                    }
                )
            }
        }
    }
}
