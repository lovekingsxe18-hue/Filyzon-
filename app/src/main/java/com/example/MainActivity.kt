package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.FilyzonBottomNav
import com.example.ui.components.FilyzonTopBar
import com.example.ui.screens.*
import com.example.ui.theme.FilyzonTheme
import com.example.ui.viewmodel.FilyzonTab
import com.example.ui.viewmodel.FilyzonViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FilyzonTheme {
                FilyzonApp()
            }
        }
    }
}

@Composable
fun FilyzonApp(
    viewModel: FilyzonViewModel = viewModel()
) {
    val currentTab by viewModel.currentTab.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val activeCheckout by viewModel.activeCheckout.collectAsState()
    val giftDialogPost by viewModel.giftDialogPost.collectAsState()
    val statusMessage by viewModel.statusMessage.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(statusMessage) {
        statusMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearStatusMessage()
        }
    }

    // Back handling: pop back to FEED if on any secondary tab
    if (currentTab != FilyzonTab.FEED) {
        BackHandler {
            viewModel.selectTab(FilyzonTab.FEED)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            FilyzonTopBar(
                currentUser = currentUser,
                currentTab = currentTab,
                onAdminClick = {
                    if (currentTab == FilyzonTab.ADMIN_HUB) {
                        viewModel.selectTab(FilyzonTab.FEED)
                    } else {
                        viewModel.selectTab(FilyzonTab.ADMIN_HUB)
                    }
                },
                onWalletClick = {
                    viewModel.selectTab(FilyzonTab.VIP_WALLET)
                }
            )
        },
        bottomBar = {
            FilyzonBottomNav(
                currentTab = currentTab,
                onTabSelected = { tab -> viewModel.selectTab(tab) }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                FilyzonTab.FEED -> FeedScreen(viewModel = viewModel)
                FilyzonTab.ENTERTAINMENT -> EntertainmentScreen(viewModel = viewModel)
                FilyzonTab.CREATE -> PostCreationScreen(viewModel = viewModel)
                FilyzonTab.SHOP -> ShopScreen(viewModel = viewModel)
                FilyzonTab.VIP_WALLET -> VipAndWalletScreen(viewModel = viewModel)
                FilyzonTab.ADMIN_HUB -> AdminDashboardScreen(viewModel = viewModel)
            }
        }
    }

    val activeReceipt by viewModel.activeReceipt.collectAsState()

    // Checkout modal for Pakistani payments
    activeCheckout?.let { target ->
        CheckoutDialog(
            target = target,
            officialBanks = viewModel.officialBanks,
            onDismiss = { viewModel.closeCheckout() },
            onConfirm = { method, account, tid ->
                viewModel.processComprehensivePayment(method, account, tid)
            }
        )
    }

    // Payment Confirmation & Digital Invoice Receipt Dialog
    activeReceipt?.let { receipt ->
        PaymentReceiptDialog(
            receipt = receipt,
            onDismiss = { viewModel.closeReceipt() }
        )
    }

    // Creator Gift sending modal
    giftDialogPost?.let { post ->
        GiftDialog(
            post = post,
            availableGifts = viewModel.availableGifts,
            userCoins = currentUser.walletCoins,
            onDismiss = { viewModel.closeGiftDialog() },
            onSendGift = { gift ->
                viewModel.sendGift(post, gift)
            }
        )
    }

    // Public Creator Profile Modal with Green Tick UI
    val selectedPublicProfile by viewModel.selectedPublicProfile.collectAsState()
    selectedPublicProfile?.let { profile ->
        PublicProfileDialog(
            user = profile,
            onDismiss = { viewModel.closePublicProfile() }
        )
    }
}
