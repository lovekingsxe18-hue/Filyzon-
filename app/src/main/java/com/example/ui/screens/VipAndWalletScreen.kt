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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PaymentMethodType
import com.example.data.model.VipPlan
import com.example.ui.theme.*
import com.example.ui.viewmodel.CheckoutTarget
import com.example.ui.viewmodel.FilyzonViewModel

@Composable
fun VipAndWalletScreen(
    viewModel: FilyzonViewModel,
    modifier: Modifier = Modifier
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val transactions by viewModel.transactions.collectAsState()
    val vipPlans = viewModel.vipPlans

    val coinPackages = listOf(
        Pair(500, 250.0),
        Pair(1200, 550.0),
        Pair(3000, 1200.0),
        Pair(10000, 3500.0)
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(FilyzonBackgroundDark)
            .padding(horizontal = 14.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // Balance & VIP Status Hero Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    Color(0xFF0F2B48),
                                    Color(0xFF1565C0),
                                    Color(0xFF0D47A1)
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
                                Text(
                                    "Filyzon Wallet & Credits",
                                    color = Color(0xFFB0BEC5),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    "Rs. ${currentUser.walletBalancePkr.toInt()}",
                                    color = Color.White,
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }

                            // VIP Badge
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (currentUser.isVip) GoldVip else Color(0xFF1E3A5F)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = if (currentUser.isVip) "👑 VIP Active" else "Regular Creator",
                                        color = if (currentUser.isVip) Color.Black else Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Coins row & quick actions
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color.Black.copy(alpha = 0.35f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("🪙 ${currentUser.walletCoins} Coins Available", color = GoldVipLight, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            Button(
                                onClick = {
                                    viewModel.openCheckout(CheckoutTarget.WalletDepositCheckout(1000.0))
                                },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = EasyPaisaGreen),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Deposit", tint = Color.White, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Deposit Rs.", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // Green Tick Identity Status Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (currentUser.isVerified) Color(0xFF072B1D) else Color(0xFF142234)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        if (currentUser.isVerified) Color(0xFF00E676) else Color(0xFF263852),
                        RoundedCornerShape(16.dp)
                    )
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            com.example.ui.components.FilyzonVerifiedBadge(
                                isVerified = currentUser.isVerified,
                                size = 20.dp
                            )
                            if (!currentUser.isVerified) {
                                Text("⚪", fontSize = 16.sp)
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (currentUser.isVerified) "Filyzon Verified Identity Active 🟢" else "Green Tick: Unverified Account",
                                color = if (currentUser.isVerified) Color(0xFF69F0AE) else Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Button(
                            onClick = { viewModel.openPublicProfile(currentUser) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (currentUser.isVerified) Color(0xFF00C853) else FilyzonBluePrimary
                            ),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "Preview Profile",
                                fontSize = 10.sp,
                                color = if (currentUser.isVerified) Color.Black else Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = if (currentUser.isVerified) {
                            "Your profile has an active Green Tick badge issued by Admin via Supabase Edge Function 'admin-verify'. Your posts display the official verified emblem."
                        } else {
                            "You do not have the Green Tick badge yet. Visit the Admin Panel to grant verification or test the Supabase Edge Function workflow."
                        },
                        color = if (currentUser.isVerified) Color(0xFFB9F6CA) else Color(0xFF90A4AE),
                        fontSize = 11.sp
                    )
                }
            }
        }

        // Pakistani Payment Methods Banner
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = FilyzonCardDark)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("🇵🇰", fontSize = 18.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Pakistani Payment Gateway Integrated",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        "Instant subscription & coin purchasing supported across all Pakistani services:",
                        color = Color(0xFF90A4AE),
                        fontSize = 11.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        PaymentBadge("Easypaisa", EasyPaisaGreen, Modifier.weight(1f))
                        PaymentBadge("JazzCash", JazzCashOrange, Modifier.weight(1f))
                        PaymentBadge("Bank / Raast", Color(0xFF1976D2), Modifier.weight(1f))
                        PaymentBadge("SadaPay", Color(0xFF00897B), Modifier.weight(1f))
                    }
                }
            }
        }

        // VIP Creator Subscription Options
        item {
            Text(
                "👑 VIP Creator Subscriptions",
                color = GoldVip,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                "Boost your creator status with verified badge, free coins & feed priority",
                color = Color(0xFF90A4AE),
                fontSize = 12.sp
            )
        }

        items(vipPlans, key = { it.id }) { plan ->
            VipPlanCard(
                plan = plan,
                isCurrentPlan = currentUser.isVip && currentUser.vipTier == plan.tierCode,
                onSubscribe = {
                    viewModel.openCheckout(CheckoutTarget.VipCheckout(plan))
                }
            )
        }

        // Purchase Filyzon Coins & Credits
        item {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "🪙 Purchase In-App Coins (Credits)",
                color = Color.White,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Used for gifting creators, boosting photos & shopping perks",
                color = Color(0xFF90A4AE),
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                coinPackages.forEach { (coins, pricePkr) ->
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
                                    color = Color(0xFF332A00),
                                    modifier = Modifier.size(40.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text("🪙", fontSize = 20.sp)
                                    }
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        "$coins Filyzon Coins",
                                        color = Color.White,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        "Instant delivery to wallet",
                                        color = Color(0xFF90A4AE),
                                        fontSize = 11.sp
                                    )
                                }
                            }

                            Button(
                                onClick = {
                                    viewModel.openCheckout(CheckoutTarget.CoinReloadCheckout(coins, pricePkr))
                                },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = FilyzonCyan),
                                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    "Rs. ${pricePkr.toInt()}",
                                    color = Color(0xFF003049),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }
                    }
                }
            }
        }

        // Recent Transactions
        item {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                "🧾 Payment & Transaction History",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                transactions.take(5).forEach { tx ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F1E32)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    tx.title,
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    "${tx.paymentMethod} • ${tx.date}",
                                    color = Color(0xFF90A4AE),
                                    fontSize = 11.sp
                                )
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    "Rs. ${tx.amountPkr.toInt()}",
                                    color = EasyPaisaGreen,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    tx.status,
                                    color = FilyzonCyan,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PaymentBadge(name: String, color: Color, modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = color.copy(alpha = 0.2f),
        border = androidx.compose.foundation.BorderStroke(1.dp, color),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier.padding(vertical = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = name,
                color = color,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun VipPlanCard(
    plan: VipPlan,
    isCurrentPlan: Boolean,
    onSubscribe: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = FilyzonCardDark),
        border = if (plan.isPopular) androidx.compose.foundation.BorderStroke(1.5.dp, GoldVip) else null,
        modifier = modifier
            .fillMaxWidth()
            .testTag("vip_plan_${plan.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = plan.title,
                            color = Color(plan.badgeColorHex),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        if (plan.isPopular) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = GoldVip
                            ) {
                                Text(
                                    text = "MOST POPULAR",
                                    color = Color.Black,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Black,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                    Text(
                        text = "Includes ${plan.coinBonus} Bonus Coins",
                        color = Color(0xFF90A4AE),
                        fontSize = 12.sp
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Rs. ${plan.pricePkr.toInt()}",
                        color = EasyPaisaGreen,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = "/ ${plan.durationLabel}",
                        color = Color(0xFF90A4AE),
                        fontSize = 11.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Perks
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                plan.perks.forEach { perk ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Perk",
                            tint = Color(plan.badgeColorHex),
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = perk,
                            color = Color.White,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = onSubscribe,
                enabled = !isCurrentPlan,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (plan.isPopular) GoldVip else FilyzonCyan,
                    disabledContainerColor = Color(0xFF1E3A5F)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
            ) {
                Text(
                    text = if (isCurrentPlan) "✓ Currently Active Plan" else "Subscribe via Easypaisa / JazzCash / Bank",
                    color = if (isCurrentPlan) Color(0xFF90A4AE) else Color.Black,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
