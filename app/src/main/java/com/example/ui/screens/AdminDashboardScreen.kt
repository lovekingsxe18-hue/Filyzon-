package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.UserProfile
import com.example.data.model.VerificationAction
import com.example.ui.components.BadgeStyle
import com.example.ui.components.FilyzonVerifiedBadge
import com.example.ui.components.FilyzonVerifiedGreen
import com.example.ui.theme.*
import com.example.ui.viewmodel.FilyzonViewModel

enum class AdminTabSelection {
    VERIFIED_TICK_PANEL,
    PROFIT_REVENUE_HUB
}

@Composable
fun AdminDashboardScreen(
    viewModel: FilyzonViewModel,
    modifier: Modifier = Modifier
) {
    var selectedAdminTab by remember { mutableStateOf(AdminTabSelection.VERIFIED_TICK_PANEL) }

    // Dialog state for creating new profile
    var showCreateProfileDialog by remember { mutableStateOf(false) }
    var newUsernameInput by remember { mutableStateOf("") }
    var newFullNameInput by remember { mutableStateOf("") }

    // State for changing admin UUID
    var showEditAdminUuidDialog by remember { mutableStateOf(false) }
    var customAdminUuidInput by remember { mutableStateOf("") }

    val adminMetrics by viewModel.adminMetrics.collectAsState()
    val profiles by viewModel.filteredAdminProfiles.collectAsState()
    val verificationLogs by viewModel.verificationLogs.collectAsState()
    val currentAdminUuid by viewModel.currentAdminUuid.collectAsState()
    val isAdminSessionActive by viewModel.isAdminSessionActive.collectAsState()
    val adminSearchQuery by viewModel.adminUserSearchQuery.collectAsState()
    val isUserListLoaded by viewModel.isUserListLoaded.collectAsState()

    var withdrawalAmountInput by remember { mutableStateOf("25000") }
    var selectedBank by remember { mutableStateOf("Meezan Bank (PK64MEZN0001234567890101)") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(FilyzonBackgroundDark)
    ) {
        // Top Switcher Tabs (Verified Tick Management vs Profit Hub)
        Surface(
            color = Color(0xFF0C1929),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedAdminTab == AdminTabSelection.VERIFIED_TICK_PANEL,
                    onClick = { selectedAdminTab = AdminTabSelection.VERIFIED_TICK_PANEL },
                    label = {
                        Text(
                            "🛡️ Green Tick Admin",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = FilyzonVerifiedGreen,
                        selectedLabelColor = Color.Black,
                        containerColor = Color(0xFF162A45),
                        labelColor = Color.White
                    ),
                    modifier = Modifier.weight(1f)
                )

                FilterChip(
                    selected = selectedAdminTab == AdminTabSelection.PROFIT_REVENUE_HUB,
                    onClick = { selectedAdminTab = AdminTabSelection.PROFIT_REVENUE_HUB },
                    label = {
                        Text(
                            "💰 Profit Hub (PKR)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = GoldVip,
                        selectedLabelColor = Color.Black,
                        containerColor = Color(0xFF162A45),
                        labelColor = Color.White
                    ),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        when (selectedAdminTab) {
            AdminTabSelection.VERIFIED_TICK_PANEL -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 14.dp),
                    contentPadding = PaddingValues(top = 12.dp, bottom = 90.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Admin Security Check Header Card
                    item {
                        Card(
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F233A)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    1.dp,
                                    if (isAdminSessionActive) FilyzonVerifiedGreen.copy(alpha = 0.5f) else Color(0xFFE53935),
                                    RoundedCornerShape(18.dp)
                                )
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = if (isAdminSessionActive) FilyzonVerifiedGreen else Color(0xFFD32F2F)
                                        ) {
                                            Text(
                                                text = if (isAdminSessionActive) "ADMIN ACCESS GRANTED 🛡️" else "ADMIN ACCESS REQUIRED ⚠️",
                                                color = if (isAdminSessionActive) Color.Black else Color.White,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Black,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                            )
                                        }
                                    }

                                    // Switch between Admin & Standard user to test access control
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = if (isAdminSessionActive) "Admin Mode" else "User Mode",
                                            color = Color.White,
                                            fontSize = 11.sp
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Switch(
                                            checked = isAdminSessionActive,
                                            onCheckedChange = { viewModel.toggleAdminSession(it) },
                                            modifier = Modifier.height(24.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Text(
                                    text = "Current Auth UUID (public.admin_users):",
                                    color = Color(0xFF90A4AE),
                                    fontSize = 11.sp
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = currentAdminUuid,
                                        color = FilyzonCyan,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace
                                    )

                                    TextButton(
                                        onClick = {
                                            customAdminUuidInput = currentAdminUuid
                                            showEditAdminUuidDialog = true
                                        },
                                        contentPadding = PaddingValues(horizontal = 6.dp, vertical = 0.dp)
                                    ) {
                                        Text("Edit UUID", fontSize = 11.sp, color = Color(0xFF81D4FA))
                                    }
                                }

                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "⚡ Supabase Edge Function 'admin-verify' verifies this UUID against public.admin_users before updating badge records in verified_users.",
                                    color = Color(0xFF78909C),
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }

                    // Main Action Buttons
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                // "Admin user list load karein"
                                Button(
                                    onClick = { viewModel.refreshAdminUserList() },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = FilyzonBluePrimary),
                                    modifier = Modifier
                                        .weight(1.3f)
                                        .height(46.dp)
                                        .testTag("load_admin_user_list_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Refresh,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Admin user list load karein",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                // "Naya Profile Banayein"
                                OutlinedButton(
                                    onClick = { showCreateProfileDialog = true },
                                    shape = RoundedCornerShape(12.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, FilyzonCyan),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(46.dp)
                                        .testTag("create_profile_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PersonAdd,
                                        contentDescription = null,
                                        tint = FilyzonCyan,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Naya Profile",
                                        fontSize = 11.sp,
                                        color = FilyzonCyan,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            // Search bar for admin user list
                            OutlinedTextField(
                                value = adminSearchQuery,
                                onValueChange = { viewModel.updateAdminSearchQuery(it) },
                                placeholder = {
                                    Text(
                                        "Filter by username, name, or UUID...",
                                        color = Color(0xFF78909C),
                                        fontSize = 12.sp
                                    )
                                },
                                leadingIcon = {
                                    Icon(Icons.Default.Search, contentDescription = null, tint = FilyzonCyan)
                                },
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color(0xFF0F1E33),
                                    unfocusedContainerColor = Color(0xFF0F1E33),
                                    focusedBorderColor = FilyzonCyan,
                                    unfocusedBorderColor = Color(0xFF263852),
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                            )
                        }
                    }

                    // Profiles List Header
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "👥 Registered Profiles (${profiles.size})",
                                color = Color.White,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFF132A4A)
                            ) {
                                Text(
                                    text = "public.profiles ⟕ verified_users",
                                    color = Color(0xFF81D4FA),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }
                    }

                    // User Profile Cards with "Green tick dein" & "Green tick hatayein"
                    items(profiles, key = { it.id }) { user ->
                        AdminUserCard(
                            user = user,
                            onGrantGreenTick = { viewModel.grantGreenTick(user.id) },
                            onRevokeGreenTick = { viewModel.revokeGreenTick(user.id) },
                            onViewProfile = { viewModel.openPublicProfile(user) }
                        )
                    }

                    // Edge Function Live Logs Section
                    item {
                        Spacer(modifier = Modifier.height(10.dp))
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF08121E)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, Color(0xFF1E3A5F), RoundedCornerShape(16.dp))
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text("⚡", fontSize = 16.sp)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "Edge Function Console ('admin-verify')",
                                            color = Color.White,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = Color(0xFF00C853).copy(alpha = 0.2f)
                                    ) {
                                        Text(
                                            text = "LIVE HTTP LOGS",
                                            color = Color(0xFF69F0AE),
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                if (verificationLogs.isEmpty()) {
                                    Text(
                                        text = "No Edge Function invocations yet.",
                                        color = Color.Gray,
                                        fontSize = 11.sp
                                    )
                                } else {
                                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                        verificationLogs.take(5).forEach { log ->
                                            Surface(
                                                shape = RoundedCornerShape(8.dp),
                                                color = Color(0xFF0E1A29),
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Column(modifier = Modifier.padding(8.dp)) {
                                                    Row(
                                                        modifier = Modifier.fillMaxWidth(),
                                                        horizontalArrangement = Arrangement.SpaceBetween
                                                    ) {
                                                        Text(
                                                            text = "${if (log.responseStatus == 200) "🟢" else "🔴"} POST /functions/v1/admin-verify",
                                                            color = if (log.responseStatus == 200) Color(0xFF69F0AE) else Color(0xFFFF5252),
                                                            fontSize = 11.sp,
                                                            fontWeight = FontWeight.Bold,
                                                            fontFamily = FontFamily.Monospace
                                                        )
                                                        Text(
                                                            text = "${log.executionMs}ms • ${log.timestamp}",
                                                            color = Color(0xFF78909C),
                                                            fontSize = 10.sp
                                                        )
                                                    }
                                                    Spacer(modifier = Modifier.height(2.dp))
                                                    Text(
                                                        text = "Target: ${log.targetUsername} (${log.action.badgeLabel}) • Caller: ${log.adminUserId.take(12)}...",
                                                        color = Color(0xFFB0BEC5),
                                                        fontSize = 10.sp
                                                    )
                                                    Text(
                                                        text = log.message,
                                                        color = Color(0xFF80CBC4),
                                                        fontSize = 9.sp
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Collapsible Supabase Documentation Card
                    item {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF0D1B2A)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("📖", fontSize = 16.sp)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Supabase Database & Edge Function Architecture",
                                        color = Color.White,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "1. SQL Editor Query for Admin access:\n   insert into public.admin_users (user_id) values ('$currentAdminUuid');",
                                    color = Color(0xFF81D4FA),
                                    fontSize = 10.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "2. Deploy Edge Function:\n   supabase functions deploy admin-verify",
                                    color = Color(0xFF81D4FA),
                                    fontSize = 10.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "3. Security Rule: Browser user seedha verified_users badge table mein write nahi kar sakta. Sirf authorized Edge Function with service_role ye update execute karta hai.",
                                    color = Color(0xFFB0BEC5),
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                }
            }

            AdminTabSelection.PROFIT_REVENUE_HUB -> {
                // Existing Profit and Revenue Hub
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 14.dp),
                    contentPadding = PaddingValues(top = 12.dp, bottom = 90.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Executive Header Card
                    item {
                        Card(
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("admin_revenue_card")
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.linearGradient(
                                            listOf(
                                                Color(0xFF1B5E20),
                                                Color(0xFF004D40),
                                                Color(0xFF00332C)
                                            )
                                        )
                                    )
                                    .padding(18.dp)
                            ) {
                                Column {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Surface(
                                                shape = RoundedCornerShape(8.dp),
                                                color = Color(0xFF69F0AE)
                                            ) {
                                                Text(
                                                    text = "⚡ APP ADMIN PROFIT HUB",
                                                    color = Color.Black,
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Black,
                                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                                )
                                            }
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text(
                                                text = "Total Platform Net Profit",
                                                color = Color(0xFFE0F2F1),
                                                fontSize = 13.sp
                                            )
                                            Text(
                                                text = "Rs. ${adminMetrics.totalNetRevenuePkr.toInt()}",
                                                color = Color.White,
                                                fontSize = 32.sp,
                                                fontWeight = FontWeight.Black
                                            )
                                        }

                                        Box(
                                            modifier = Modifier
                                                .size(54.dp)
                                                .clip(CircleShape)
                                                .background(Color.White.copy(alpha = 0.2f)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.MonetizationOn,
                                                contentDescription = "Profit",
                                                tint = Color(0xFF69F0AE),
                                                modifier = Modifier.size(32.dp)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(14.dp))
                                    Text(
                                        text = "Revenue continuously generated from VIP Creator passes, Store order cuts, Live gifts & Post promotion boosts.",
                                        color = Color(0xFFB2DFDB),
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }
                    }

                    // Revenue Breakdown Stream Cards
                    item {
                        Text(
                            text = "💰 Admin Revenue Streams Breakdown",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            RevenueStreamRow(
                                title = "VIP Creator Subscriptions",
                                amountPkr = adminMetrics.vipSubscriptionRevenuePkr,
                                percentageLabel = "100% Platform Direct",
                                icon = "👑",
                                color = GoldVip
                            )
                            RevenueStreamRow(
                                title = "FZ Shop E-Commerce Commission",
                                amountPkr = adminMetrics.storeCommissionsPkr,
                                percentageLabel = "8% - 15% Fee / Order",
                                icon = "🛍️",
                                color = EasyPaisaGreen
                            )
                            RevenueStreamRow(
                                title = "TikTok Gifts & Coin Platform Cut",
                                amountPkr = adminMetrics.giftsCommissionPkr,
                                percentageLabel = "30% Cut / Gift",
                                icon = "🎁",
                                color = FilyzonCyan
                            )
                            RevenueStreamRow(
                                title = "Post Promotion Boosts (Ads)",
                                amountPkr = adminMetrics.postBoostAdRevenuePkr,
                                percentageLabel = "Rs. 350 / Campaign",
                                icon = "🚀",
                                color = Color(0xFFFF8A80)
                            )
                        }
                    }

                    // Profit Withdrawal Card
                    item {
                        Card(
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = FilyzonCardDark),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("🏦", fontSize = 20.sp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Withdraw Admin Profits to Pakistani Bank",
                                        color = Color.White,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Transfer earned platform revenues directly to your commercial bank or mobile wallet via Raast instant clearing:",
                                    color = Color(0xFF90A4AE),
                                    fontSize = 11.sp
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = "Destination Account:",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.height(4.dp))

                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = Color(0xFF10243C),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = selectedBank,
                                        color = FilyzonCyan,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(12.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = "Withdraw Amount (PKR):",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.height(4.dp))

                                OutlinedTextField(
                                    value = withdrawalAmountInput,
                                    onValueChange = { withdrawalAmountInput = it },
                                    singleLine = true,
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedContainerColor = Color(0xFF10243C),
                                        unfocusedContainerColor = Color(0xFF10243C),
                                        focusedBorderColor = EasyPaisaGreen,
                                        unfocusedBorderColor = Color(0xFF263852),
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Button(
                                    onClick = {
                                        val amount = withdrawalAmountInput.toDoubleOrNull() ?: 10000.0
                                        viewModel.withdrawAdminEarnings(amount, selectedBank)
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = EasyPaisaGreen),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp)
                                        .testTag("admin_withdraw_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Withdraw",
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Transfer Rs. $withdrawalAmountInput to Bank",
                                        color = Color.White,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Modal Dialog: Naya User Profile Banayein (SQL Simulation)
    if (showCreateProfileDialog) {
        AlertDialog(
            onDismissRequest = { showCreateProfileDialog = false },
            title = {
                Text("➕ Naya Profile Banayein (public.profiles)", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        "Replicates: insert into public.profiles (id, username) values ('UUID', 'yourname')",
                        fontSize = 11.sp,
                        color = Color(0xFF90A4AE)
                    )
                    OutlinedTextField(
                        value = newUsernameInput,
                        onValueChange = { newUsernameInput = it },
                        label = { Text("Username (e.g. imran_vlogs)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newFullNameInput,
                        onValueChange = { newFullNameInput = it },
                        label = { Text("Full Name (e.g. Imran Khan)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newUsernameInput.isNotBlank()) {
                            viewModel.createNewProfile(
                                username = newUsernameInput.trim(),
                                name = if (newFullNameInput.isNotBlank()) newFullNameInput.trim() else newUsernameInput.trim()
                            )
                            showCreateProfileDialog = false
                            newUsernameInput = ""
                            newFullNameInput = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = FilyzonBluePrimary)
                ) {
                    Text("Profile Create Karein")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCreateProfileDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Modal Dialog: Change Admin UUID
    if (showEditAdminUuidDialog) {
        AlertDialog(
            onDismissRequest = { showEditAdminUuidDialog = false },
            title = {
                Text("⚙️ Admin UUID Set Karein", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        "Current account UUID jo admin_users table mein check hoga:",
                        fontSize = 11.sp,
                        color = Color(0xFF90A4AE)
                    )
                    OutlinedTextField(
                        value = customAdminUuidInput,
                        onValueChange = { customAdminUuidInput = it },
                        label = { Text("Auth User UUID") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (customAdminUuidInput.isNotBlank()) {
                            viewModel.updateAdminUuid(customAdminUuidInput.trim())
                            showEditAdminUuidDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = FilyzonBluePrimary)
                ) {
                    Text("Save UUID")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditAdminUuidDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

/**
 * Individual User Card in Admin List with "Green tick dein" & "Green tick hatayein"
 */
@Composable
fun AdminUserCard(
    user: UserProfile,
    onGrantGreenTick: () -> Unit,
    onRevokeGreenTick: () -> Unit,
    onViewProfile: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = FilyzonCardDark),
        modifier = modifier
            .fillMaxWidth()
            .testTag("admin_user_card_${user.id}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // User Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onViewProfile() }
                ) {
                    Box {
                        AsyncImage(
                            model = user.avatarUrl,
                            contentDescription = user.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .border(
                                    2.dp,
                                    if (user.isVerified) FilyzonVerifiedGreen else Color(0xFF263852),
                                    CircleShape
                                )
                        )
                        if (user.isVerified) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .size(16.dp)
                            ) {
                                FilyzonVerifiedBadge(isVerified = true, size = 15.dp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = user.name,
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            if (user.isVerified) {
                                Spacer(modifier = Modifier.width(4.dp))
                                FilyzonVerifiedBadge(isVerified = true, size = 14.dp)
                            }
                        }
                        Text(
                            text = user.handle,
                            color = FilyzonCyan,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "UUID: ${user.authUuid}",
                            color = Color(0xFF78909C),
                            fontSize = 9.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }

                // Verification Badge Status Pill
                if (user.isVerified) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFF003822),
                        border = androidx.compose.foundation.BorderStroke(1.dp, FilyzonVerifiedGreen)
                    ) {
                        Text(
                            text = "VERIFIED 🟢",
                            color = Color(0xFF69F0AE),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                        )
                    }
                } else {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFF1E2836)
                    ) {
                        Text(
                            text = "UNVERIFIED ⚪",
                            color = Color(0xFF90A4AE),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Verified Metadata if available
            if (user.isVerified && user.verifiedDate != null) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFF092017),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "✓ Verified on: ${user.verifiedDate} via Edge Function",
                        color = Color(0xFF80CBC4),
                        fontSize = 10.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Action Buttons: "Green tick dein" & "Green tick hatayein" & "Profile"
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (!user.isVerified) {
                    // "Green tick dein"
                    Button(
                        onClick = onGrantGreenTick,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = FilyzonVerifiedGreen),
                        modifier = Modifier
                            .weight(1.4f)
                            .height(38.dp)
                            .testTag("grant_green_tick_${user.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Green tick dein",
                            color = Color.Black,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                } else {
                    // "Green tick hatayein"
                    OutlinedButton(
                        onClick = onRevokeGreenTick,
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEF5350)),
                        modifier = Modifier
                            .weight(1.4f)
                            .height(38.dp)
                            .testTag("revoke_green_tick_${user.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            tint = Color(0xFFFF8A80),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Green tick hatayein",
                            color = Color(0xFFFF8A80),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Public Profile preview button
                FilledTonalButton(
                    onClick = onViewProfile,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.filledTonalButtonColors(containerColor = Color(0xFF162A45)),
                    modifier = Modifier
                        .weight(1f)
                        .height(38.dp)
                        .testTag("view_profile_${user.id}")
                ) {
                    Text(
                        text = "Profile 🔍",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun RevenueStreamRow(
    title: String,
    amountPkr: Double,
    percentageLabel: String,
    icon: String,
    color: Color
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = FilyzonCardDark),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = CircleShape,
                    color = color.copy(alpha = 0.15f),
                    modifier = Modifier.size(38.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(icon, fontSize = 18.sp)
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        title,
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        percentageLabel,
                        color = Color(0xFF90A4AE),
                        fontSize = 11.sp
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    "Rs. ${amountPkr.toInt()}",
                    color = color,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black
                )
                Text(
                    "Profit",
                    color = Color(0xFF81C784),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
