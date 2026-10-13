package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.window.Dialog
import com.example.data.model.GiftItem
import com.example.data.model.Post
import com.example.ui.theme.*

@Composable
fun GiftDialog(
    post: Post,
    availableGifts: List<GiftItem>,
    userCoins: Int,
    onDismiss: () -> Unit,
    onSendGift: (GiftItem) -> Unit
) {
    var selectedGift by remember { mutableStateOf(availableGifts.firstOrNull()) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = FilyzonSurfaceDark),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .testTag("gift_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "🎁 Send Creator Gift",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "To: ${post.authorName}",
                            color = FilyzonCyan,
                            fontSize = 12.sp
                        )
                    }

                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.Gray)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // User coins balance
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFF10263E),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Your Filyzon Balance:", color = Color(0xFFB0BEC5), fontSize = 11.sp)
                        Text("🪙 $userCoins Coins", color = GoldVip, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Gifts Grid
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.height(200.dp)
                ) {
                    items(availableGifts) { gift ->
                        val isSelected = selectedGift?.id == gift.id
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) Color(0xFF1E3A5F) else Color(0xFF112034),
                            border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, GoldVip) else null,
                            modifier = Modifier
                                .clickable { selectedGift = gift }
                                .padding(2.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(gift.emoji, fontSize = 28.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    gift.name,
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    "🪙 ${gift.costCoins}",
                                    color = GoldVipLight,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Admin fee distribution note ("Jo app admin ko fayda pahuncha sake")
                Text(
                    text = "Platform split: 70% direct to Creator wallet • 30% platform cut to App Admin",
                    color = Color(0xFF90A4AE),
                    fontSize = 10.sp,
                    lineHeight = 14.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Send Button
                Button(
                    onClick = {
                        selectedGift?.let { onSendGift(it) }
                    },
                    enabled = selectedGift != null && userCoins >= (selectedGift?.costCoins ?: 0),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GoldVip,
                        disabledContainerColor = Color(0xFF1E3A5F)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .testTag("confirm_send_gift_button")
                ) {
                    Text(
                        text = if (userCoins < (selectedGift?.costCoins ?: 0)) "Insufficient Coins" else "Send ${selectedGift?.emoji ?: ""} (${selectedGift?.costCoins ?: 0} Coins)",
                        color = Color.Black,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
