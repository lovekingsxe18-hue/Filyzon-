package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.EasyPaisaGreen
import com.example.ui.theme.FilyzonCyan

val FilyzonVerifiedGreen = Color(0xFF00C853)
val FilyzonVerifiedGreenDark = Color(0xFF00897B)
val FilyzonBadgeGold = Color(0xFFFFD54F)

enum class BadgeStyle {
    COMPACT_ICON,
    CHIP_LABEL,
    DETAILED_BANNER,
    OFFICIAL_FZ_EMBLEM
}

/**
 * High-profile Filyzon Verified Badge component.
 * Features the signature Emerald Green Tick and FZ Official badge.
 */
@Composable
fun FilyzonVerifiedBadge(
    isVerified: Boolean,
    modifier: Modifier = Modifier,
    style: BadgeStyle = BadgeStyle.COMPACT_ICON,
    isOfficialFz: Boolean = false,
    size: Dp = 16.dp,
    onClick: (() -> Unit)? = null
) {
    if (!isVerified && !isOfficialFz) return

    val clickableModifier = if (onClick != null) {
        modifier.clickable { onClick() }
    } else {
        modifier
    }

    when (style) {
        BadgeStyle.COMPACT_ICON -> {
            Box(
                modifier = clickableModifier
                    .size(size)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            listOf(
                                if (isOfficialFz) Color(0xFF1976D2) else Color(0xFF00E676),
                                if (isOfficialFz) Color(0xFF0D47A1) else Color(0xFF00B0FF)
                            )
                        )
                    )
                    .border(
                        1.dp,
                        Color.White.copy(alpha = 0.8f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = if (isOfficialFz) "FZ Official" else "Verified Green Tick",
                    tint = Color.White,
                    modifier = Modifier.size(size * 0.65f)
                )
            }
        }

        BadgeStyle.CHIP_LABEL -> {
            Surface(
                modifier = clickableModifier,
                shape = RoundedCornerShape(12.dp),
                color = if (isOfficialFz) Color(0xFF0D253F) else Color(0xFF0A2B1D),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isOfficialFz) FilyzonCyan.copy(alpha = 0.6f) else FilyzonVerifiedGreen.copy(alpha = 0.6f)
                )
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(13.dp)
                            .clip(CircleShape)
                            .background(if (isOfficialFz) Color(0xFF1E88E5) else FilyzonVerifiedGreen),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(9.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isOfficialFz) "FZ OFFICIAL 🦅" else "VERIFIED 🟢",
                        color = if (isOfficialFz) Color(0xFF81D4FA) else Color(0xFFB9F6CA),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }

        BadgeStyle.DETAILED_BANNER -> {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                modifier = clickableModifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    Color(0xFF04271A),
                                    Color(0xFF094D33),
                                    Color(0xFF0D2840)
                                )
                            )
                        )
                        .border(
                            1.dp,
                            Brush.horizontalGradient(
                                listOf(Color(0xFF00E676), Color(0xFF00B0FF))
                            ),
                            RoundedCornerShape(16.dp)
                        )
                        .padding(14.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.linearGradient(
                                            listOf(Color(0xFF00E676), Color(0xFF00B0FF))
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Verified Green Tick",
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "FILYZON VERIFIED CREATOR",
                                        color = Color.White,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Black
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("🟢", fontSize = 11.sp)
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Authentic Creator Identity • Verified via Supabase Edge Function",
                                    color = Color(0xFFB9F6CA),
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        BadgeStyle.OFFICIAL_FZ_EMBLEM -> {
            // Emulating the uploaded FZ Filyzon Official diamond emblem
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFF002244),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFF1E88E5)),
                modifier = clickableModifier
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("🦅", fontSize = 12.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "FZ OFFICIAL",
                        color = Color(0xFF64B5F6),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 0.8.sp
                    )
                }
            }
        }
    }
}
