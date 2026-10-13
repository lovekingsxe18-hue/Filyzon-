package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.Post
import com.example.data.model.PostCategory
import com.example.data.model.UserProfile
import com.example.ui.theme.*
import com.example.ui.viewmodel.FilyzonViewModel

@Composable
fun FeedScreen(
    viewModel: FilyzonViewModel,
    modifier: Modifier = Modifier
) {
    val posts by viewModel.filteredPosts.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    var searchInput by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(FilyzonBackgroundDark)
    ) {
        // Search & Filter Bar
        Surface(
            color = FilyzonSurfaceDark,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                OutlinedTextField(
                    value = searchInput,
                    onValueChange = {
                        searchInput = it
                        viewModel.updateSearchQuery(it)
                    },
                    placeholder = {
                        Text(
                            "Search entertainment, TikTok reels, creators...",
                            color = Color(0xFF78909C),
                            fontSize = 13.sp
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = FilyzonCyan
                        )
                    },
                    trailingIcon = {
                        if (searchInput.isNotEmpty()) {
                            IconButton(onClick = {
                                searchInput = ""
                                viewModel.updateSearchQuery("")
                            }) {
                                Icon(Icons.Default.Close, contentDescription = "Clear", tint = Color.Gray)
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF132238),
                        unfocusedContainerColor = Color(0xFF132238),
                        focusedBorderColor = FilyzonCyan,
                        unfocusedBorderColor = Color(0xFF263852),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("feed_search_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Category chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        FilterChip(
                            selected = selectedCategory == null,
                            onClick = { viewModel.selectCategory(null) },
                            label = { Text("🔥 All Feed", fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = FilyzonCyan,
                                selectedLabelColor = Color(0xFF003049),
                                containerColor = Color(0xFF162A45),
                                labelColor = Color.White
                            )
                        )
                    }
                    items(PostCategory.values()) { category ->
                        FilterChip(
                            selected = selectedCategory == category,
                            onClick = { viewModel.selectCategory(category) },
                            label = {
                                Text("${category.iconLabel} ${category.displayName}", fontSize = 12.sp)
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = FilyzonCyan,
                                selectedLabelColor = Color(0xFF003049),
                                containerColor = Color(0xFF162A45),
                                labelColor = Color.White
                            )
                        )
                    }
                }
            }
        }

        // Posts List
        if (posts.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("📷", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "No posts found",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Be the first creator to post in this category!",
                        color = Color.Gray,
                        fontSize = 13.sp
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 80.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(posts, key = { it.id }) { post ->
                    PostCard(
                        post = post,
                        currentUser = currentUser,
                        onLike = { viewModel.toggleLike(post.id) },
                        onGiftClick = { viewModel.openGiftDialog(post) },
                        onAuthorClick = { viewModel.openPublicProfileByHandle(post.authorHandle) },
                        onShare = { /* Share simulated */ }
                    )
                }
            }
        }
    }
}

@Composable
fun PostCard(
    post: Post,
    currentUser: UserProfile,
    onLike: () -> Unit,
    onGiftClick: () -> Unit,
    onAuthorClick: () -> Unit,
    onShare: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .testTag("post_card_${post.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = FilyzonCardDark)
    ) {
        Column {
            // Post Author Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onAuthorClick() }
                ) {
                    Box {
                        AsyncImage(
                            model = post.authorAvatarUrl,
                            contentDescription = post.authorName,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .border(
                                    2.dp,
                                    if (post.isVerified) Color(0xFF00E676) else if (post.isVipCreator) GoldVip else FilyzonBlueSecondary,
                                    CircleShape
                                )
                        )
                        if (post.isVerified) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .size(15.dp)
                            ) {
                                com.example.ui.components.FilyzonVerifiedBadge(
                                    isVerified = true,
                                    size = 14.dp
                                )
                            }
                        } else if (post.isVipCreator) {
                            Text(
                                text = "👑",
                                fontSize = 12.sp,
                                modifier = Modifier.align(Alignment.TopEnd)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = post.authorName,
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            if (post.isVerified) {
                                Spacer(modifier = Modifier.width(5.dp))
                                com.example.ui.components.FilyzonVerifiedBadge(
                                    isVerified = true,
                                    size = 15.dp,
                                    isOfficialFz = post.authorHandle.contains("official", ignoreCase = true)
                                )
                            }
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = post.authorHandle,
                                color = Color(0xFF90A4AE),
                                fontSize = 11.sp
                            )
                            Text(
                                text = " • ${post.timeAgo}",
                                color = Color(0xFF78909C),
                                fontSize = 11.sp
                            )
                        }
                        if (post.location.isNotBlank()) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "📍 ${post.location}",
                                    color = Color(0xFF80CBC4),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                // Badges (Boosted by Admin / VIP Exclusive)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (post.isBoosted) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFF004D40),
                            modifier = Modifier.padding(end = 4.dp)
                        ) {
                            Text(
                                text = "🚀 Sponsored",
                                color = Color(0xFF80CBC4),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                    }
                    if (post.isVipExclusive) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFF4A3400)
                        ) {
                            Text(
                                text = "👑 VIP Only",
                                color = GoldVip,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                    }
                }
            }

            // Post Photo with TikTok-style floating action buttons
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(340.dp)
                    .background(Color.Black)
            ) {
                AsyncImage(
                    model = post.imageUrl,
                    contentDescription = "Post photo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Category Tag overlay
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.Black.copy(alpha = 0.65f),
                    modifier = Modifier
                        .padding(10.dp)
                        .align(Alignment.TopStart)
                ) {
                    Text(
                        text = "${post.category.iconLabel} ${post.category.displayName}",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                // Vertical Actions Bar (TikTok style right side)
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 12.dp, bottom = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Like button
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable { onLike() }
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.65f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (post.isLiked) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                                contentDescription = "Like",
                                tint = if (post.isLiked) Color(0xFFFF1744) else Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Text(
                            text = "${post.likesCount}",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Comments
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.65f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ChatBubbleOutline,
                                contentDescription = "Comments",
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Text(
                            text = "${post.commentsCount}",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Send Gift Button (Earns creator money + Admin 30% platform cut!)
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .testTag("gift_button_${post.id}")
                            .clickable { onGiftClick() }
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(
                                        listOf(GoldVip, Color(0xFFFF6F00))
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🎁", fontSize = 20.sp)
                        }
                        Text(
                            text = "Gift (${post.giftsCount})",
                            color = GoldVipLight,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Share
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.65f))
                            .clickable { onShare() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // Post Caption & Tags
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = post.caption,
                    color = Color.White,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )

                if (post.tags.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = post.tags.joinToString(" "),
                        color = FilyzonCyan,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Monetization note
                if (post.isMonetized) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF0F263E), RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 6.dp)
                    ) {
                        Text("⚡", fontSize = 12.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Monetized Creator Post • Platform fee directly supports Filyzon creators & admin",
                            color = Color(0xFF81D4FA),
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }
    }
}
