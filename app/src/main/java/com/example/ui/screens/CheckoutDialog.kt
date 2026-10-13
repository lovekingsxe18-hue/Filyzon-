package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.PakistaniBankInfo
import com.example.data.model.PaymentCategory
import com.example.data.model.PaymentMethodType
import com.example.ui.theme.*
import com.example.ui.viewmodel.CheckoutTarget
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CheckoutDialog(
    target: CheckoutTarget,
    officialBanks: List<PakistaniBankInfo>,
    onDismiss: () -> Unit,
    onConfirm: (PaymentMethodType, String, String) -> Unit
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val coroutineScope = rememberCoroutineScope()

    var selectedCategory by remember { mutableStateOf(PaymentCategory.MOBILE_WALLET) }
    var selectedMethod by remember { mutableStateOf(PaymentMethodType.EASYPAISA) }
    var selectedBank by remember { mutableStateOf(officialBanks.first()) }

    var mobileNumberInput by remember { mutableStateOf("0312-4567890") }
    var senderNameInput by remember { mutableStateOf("Hamza Abbasi") }
    var referenceTidInput by remember { mutableStateOf("") }
    var otpInput by remember { mutableStateOf("4819") }
    var isProcessing by remember { mutableStateOf(false) }

    val (title, pricePkr, subtitle) = when (target) {
        is CheckoutTarget.ProductCheckout -> Triple(
            target.product.title,
            target.product.pricePkr,
            "Seller: ${target.product.sellerName}"
        )
        is CheckoutTarget.VipCheckout -> Triple(
            "VIP: ${target.plan.title}",
            target.plan.pricePkr,
            "${target.plan.durationLabel} Subscription • Bonus ${target.plan.coinBonus} Coins"
        )
        is CheckoutTarget.CoinReloadCheckout -> Triple(
            "${target.coins} Filyzon Coins Package",
            target.pricePkr,
            "Instant in-app credit reload"
        )
        is CheckoutTarget.WalletDepositCheckout -> Triple(
            "Wallet Credits Deposit",
            target.amountPkr,
            "Add funds for post boosting & creator gifts"
        )
    }

    Dialog(onDismissRequest = { if (!isProcessing) onDismiss() }) {
        Card(
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = FilyzonSurfaceDark),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
                .testTag("checkout_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header with Pakistani Flag & Security
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("🇵🇰", fontSize = 24.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                "Pakistani Payment Gateway",
                                color = Color.White,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "1Link • Raast • State Bank Verified",
                                color = FilyzonCyan,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    if (!isProcessing) {
                        IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.Gray)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Item Price Banner
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = Color(0xFF10263E),
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
                                text = title,
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1
                            )
                            Text(
                                text = subtitle,
                                color = Color(0xFF90A4AE),
                                fontSize = 11.sp,
                                maxLines = 1
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text("Payable", color = Color(0xFFB0BEC5), fontSize = 10.sp)
                            Text(
                                "PKR ${pricePkr.toInt()}",
                                color = EasyPaisaGreen,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Category Switcher Tabs
                ScrollableTabRow(
                    selectedTabIndex = PaymentCategory.values().indexOf(selectedCategory),
                    containerColor = Color(0xFF0F1E32),
                    contentColor = FilyzonCyan,
                    edgePadding = 0.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    PaymentCategory.values().forEach { cat ->
                        val isSelected = selectedCategory == cat
                        Tab(
                            selected = isSelected,
                            onClick = {
                                selectedCategory = cat
                                selectedMethod = when (cat) {
                                    PaymentCategory.MOBILE_WALLET -> PaymentMethodType.EASYPAISA
                                    PaymentCategory.BANK_TRANSFER -> PaymentMethodType.PAKISTANI_BANK
                                    PaymentCategory.DIGITAL_WALLET -> PaymentMethodType.SADAPAY
                                    PaymentCategory.IN_APP_WALLET -> PaymentMethodType.FILYZON_COINS
                                }
                            },
                            text = {
                                Text(
                                    when (cat) {
                                        PaymentCategory.MOBILE_WALLET -> "📱 Wallets"
                                        PaymentCategory.BANK_TRANSFER -> "🏦 Banks & Raast"
                                        PaymentCategory.DIGITAL_WALLET -> "💳 Digital Cards"
                                        PaymentCategory.IN_APP_WALLET -> "🪙 Credits"
                                    },
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) FilyzonCyan else Color(0xFF90A4AE)
                                )
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Methods within chosen category
                when (selectedCategory) {
                    PaymentCategory.MOBILE_WALLET -> {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            MethodSelectionChip(
                                label = "Easypaisa",
                                isSelected = selectedMethod == PaymentMethodType.EASYPAISA,
                                brandColor = EasyPaisaGreen,
                                modifier = Modifier.weight(1f),
                                onClick = { selectedMethod = PaymentMethodType.EASYPAISA }
                            )
                            MethodSelectionChip(
                                label = "JazzCash",
                                isSelected = selectedMethod == PaymentMethodType.JAZZCASH,
                                brandColor = JazzCashOrange,
                                modifier = Modifier.weight(1f),
                                onClick = { selectedMethod = PaymentMethodType.JAZZCASH }
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Mobile number input
                        Text(
                            text = "${selectedMethod.brandTag} Account Number:",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = mobileNumberInput,
                            onValueChange = { mobileNumberInput = it },
                            placeholder = { Text("03xx-xxxxxxx", color = Color.Gray) },
                            singleLine = true,
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFF102034),
                                unfocusedContainerColor = Color(0xFF102034),
                                focusedBorderColor = Color(selectedMethod.iconColor),
                                unfocusedBorderColor = Color(0xFF263852),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("payment_mobile_input")
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Simulated OTP verification prompt
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFF0D253A)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("🔐", fontSize = 16.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        "Instant MPIN Authorization",
                                        color = FilyzonCyan,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        "Upon clicking pay, an official ${selectedMethod.brandTag} USSD / In-App popup will authorize debit of Rs. ${pricePkr.toInt()}.",
                                        color = Color(0xFF90A4AE),
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        }
                    }

                    PaymentCategory.BANK_TRANSFER -> {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            MethodSelectionChip(
                                label = "IBFT Bank",
                                isSelected = selectedMethod == PaymentMethodType.PAKISTANI_BANK,
                                brandColor = Color(0xFF1565C0),
                                modifier = Modifier.weight(1f),
                                onClick = { selectedMethod = PaymentMethodType.PAKISTANI_BANK }
                            )
                            MethodSelectionChip(
                                label = "Raast Instant",
                                isSelected = selectedMethod == PaymentMethodType.RAAST_INSTANT,
                                brandColor = Color(0xFF2E7D32),
                                modifier = Modifier.weight(1f),
                                onClick = { selectedMethod = PaymentMethodType.RAAST_INSTANT }
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Select Destination Bank
                        Text(
                            "Select Official Filyzon Destination Bank:",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        officialBanks.take(4).forEach { bank ->
                            val isSelected = selectedBank == bank
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSelected) Color(0xFF163252) else Color(0xFF102034),
                                border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, FilyzonCyan) else null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp)
                                    .clickable { selectedBank = bank }
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(bank.bankName, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                        Text("IBAN: ${bank.iban}", color = FilyzonCyan, fontSize = 10.sp)
                                        Text("Title: ${bank.accountTitle}", color = Color(0xFF90A4AE), fontSize = 9.sp)
                                    }

                                    IconButton(
                                        onClick = {
                                            clipboardManager.setText(AnnotatedString(bank.iban))
                                            Toast.makeText(context, "IBAN Copied: ${bank.iban}", Toast.LENGTH_SHORT).show()
                                        },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(Icons.Default.ContentCopy, contentDescription = "Copy IBAN", tint = FilyzonCyan, modifier = Modifier.size(16.dp))
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Sender details input
                        Text("Sender Account Name:", color = Color.White, fontSize = 11.sp)
                        OutlinedTextField(
                            value = senderNameInput,
                            onValueChange = { senderNameInput = it },
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFF102034),
                                unfocusedContainerColor = Color(0xFF102034),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            modifier = Modifier.fillMaxWidth().height(48.dp)
                        )

                        Spacer(modifier = Modifier.height(6.dp))
                        Text("6-Digit Bank Reference / TID:", color = Color.White, fontSize = 11.sp)
                        OutlinedTextField(
                            value = referenceTidInput,
                            onValueChange = { referenceTidInput = it },
                            placeholder = { Text("e.g. 784912", color = Color.Gray, fontSize = 12.sp) },
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFF102034),
                                unfocusedContainerColor = Color(0xFF102034),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            modifier = Modifier.fillMaxWidth().height(48.dp)
                        )
                    }

                    PaymentCategory.DIGITAL_WALLET -> {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            MethodSelectionChip(
                                label = "SadaPay",
                                isSelected = selectedMethod == PaymentMethodType.SADAPAY,
                                brandColor = Color(0xFF00897B),
                                modifier = Modifier.weight(1f),
                                onClick = { selectedMethod = PaymentMethodType.SADAPAY }
                            )
                            MethodSelectionChip(
                                label = "NayaPay",
                                isSelected = selectedMethod == PaymentMethodType.NAYAPAY,
                                brandColor = Color(0xFFFF6D00),
                                modifier = Modifier.weight(1f),
                                onClick = { selectedMethod = PaymentMethodType.NAYAPAY }
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "${selectedMethod.brandTag} Registered Mobile / Numberless Card:",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = mobileNumberInput,
                            onValueChange = { mobileNumberInput = it },
                            singleLine = true,
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFF102034),
                                unfocusedContainerColor = Color(0xFF102034),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    PaymentCategory.IN_APP_WALLET -> {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFF18304D),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    "Filyzon Wallet Credits",
                                    color = GoldVip,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    "Your accumulated platform balance or coin earnings will be instantly deducted without requiring external bank redirection.",
                                    color = Color(0xFF90A4AE),
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Pay Now Button with Processing Indicator
                Button(
                    onClick = {
                        isProcessing = true
                        coroutineScope.launch {
                            delay(1200) // Realistic banking network handshake delay
                            val tid = if (referenceTidInput.isNotBlank()) referenceTidInput else "PK-TID-${(100000..999999).random()}"
                            val accountId = when (selectedCategory) {
                                PaymentCategory.MOBILE_WALLET -> mobileNumberInput
                                PaymentCategory.BANK_TRANSFER -> "${selectedBank.bankName} - $senderNameInput"
                                PaymentCategory.DIGITAL_WALLET -> mobileNumberInput
                                PaymentCategory.IN_APP_WALLET -> "Filyzon Wallet"
                            }
                            isProcessing = false
                            onConfirm(selectedMethod, accountId, tid)
                        }
                    },
                    enabled = !isProcessing,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = when (selectedMethod) {
                            PaymentMethodType.EASYPAISA -> EasyPaisaGreen
                            PaymentMethodType.JAZZCASH -> JazzCashOrange
                            PaymentMethodType.FILYZON_COINS -> GoldVip
                            else -> FilyzonCyan
                        }
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("checkout_confirm_button")
                ) {
                    if (isProcessing) {
                        CircularProgressIndicator(
                            color = Color.Black,
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Processing 1Link / Raast Transfer...",
                            color = Color.Black,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Pay",
                            tint = if (selectedMethod == PaymentMethodType.EASYPAISA || selectedMethod == PaymentMethodType.JAZZCASH) Color.White else Color.Black,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Confirm PKR ${pricePkr.toInt()} Payment",
                            color = if (selectedMethod == PaymentMethodType.EASYPAISA || selectedMethod == PaymentMethodType.JAZZCASH) Color.White else Color.Black,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MethodSelectionChip(
    label: String,
    isSelected: Boolean,
    brandColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = if (isSelected) brandColor.copy(alpha = 0.25f) else Color(0xFF102034),
        border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, brandColor) else androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF263852)),
        modifier = modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier.padding(vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                color = if (isSelected) brandColor else Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
