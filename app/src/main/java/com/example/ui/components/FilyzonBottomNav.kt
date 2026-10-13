package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.viewmodel.FilyzonTab

@Composable
fun FilyzonBottomNav(
    currentTab: FilyzonTab,
    onTabSelected: (FilyzonTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = FilyzonNavyDark,
        tonalElevation = 8.dp,
        shadowElevation = 12.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavItem(
                icon = if (currentTab == FilyzonTab.FEED) Icons.Filled.Home else Icons.Outlined.Home,
                label = "Feed",
                isSelected = currentTab == FilyzonTab.FEED,
                tag = "nav_feed",
                onClick = { onTabSelected(FilyzonTab.FEED) }
            )

            NavItem(
                icon = if (currentTab == FilyzonTab.ENTERTAINMENT) Icons.Filled.LiveTv else Icons.Outlined.LiveTv,
                label = "Fun & Reels",
                isSelected = currentTab == FilyzonTab.ENTERTAINMENT,
                tag = "nav_entertainment",
                onClick = { onTabSelected(FilyzonTab.ENTERTAINMENT) }
            )

            // Distinct Center Post Button
            Box(
                modifier = Modifier
                    .testTag("nav_post_button")
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(FilyzonCyan)
                    .clickable { onTabSelected(FilyzonTab.CREATE) },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Post Photo",
                    tint = Color(0xFF003049),
                    modifier = Modifier.size(28.dp)
                )
            }

            NavItem(
                icon = if (currentTab == FilyzonTab.SHOP) Icons.Filled.ShoppingBag else Icons.Outlined.ShoppingBag,
                label = "FZ Shop",
                isSelected = currentTab == FilyzonTab.SHOP,
                tag = "nav_shop",
                onClick = { onTabSelected(FilyzonTab.SHOP) }
            )

            NavItem(
                icon = if (currentTab == FilyzonTab.VIP_WALLET) Icons.Filled.AccountBalanceWallet else Icons.Outlined.AccountBalanceWallet,
                label = "VIP & Pay",
                isSelected = currentTab == FilyzonTab.VIP_WALLET,
                tag = "nav_vip_wallet",
                onClick = { onTabSelected(FilyzonTab.VIP_WALLET) }
            )
        }
    }
}

@Composable
private fun NavItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    tag: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .testTag(tag)
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 2.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) FilyzonCyan else Color(0xFF90A4AE),
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            color = if (isSelected) FilyzonCyan else Color(0xFF90A4AE),
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}
