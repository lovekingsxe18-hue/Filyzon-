package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.PostCategory
import com.example.ui.theme.*
import com.example.ui.viewmodel.FilyzonViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PostCreationScreen(
    viewModel: FilyzonViewModel,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val currentUser by viewModel.currentUser.collectAsState()

    val samplePhotos = listOf(
        "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=800&auto=format&fit=crop&q=80",
        "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=800&auto=format&fit=crop&q=80",
        "https://images.unsplash.com/photo-1506744038136-46273834b3fb?w=800&auto=format&fit=crop&q=80",
        "https://images.unsplash.com/photo-1492684223066-81342ee5ff30?w=800&auto=format&fit=crop&q=80",
        "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=800&auto=format&fit=crop&q=80"
    )

    val locations = listOf(
        "Lahore, Pakistan 🇵🇰",
        "Islamabad, Pakistan 🇵🇰",
        "Karachi, Pakistan 🇵🇰",
        "Rawalpindi, Pakistan 🇵🇰",
        "Peshawar, Pakistan 🇵🇰",
        "Faisalabad, Pakistan 🇵🇰"
    )

    val filters = listOf("Normal", "Vivid", "Cyberpunk", "Golden", "Monochrome")

    var selectedPhotoUri by remember { mutableStateOf<Uri?>(null) }
    var selectedPhotoUrl by remember { mutableStateOf(samplePhotos[0]) }
    var captionText by remember { mutableStateOf("") }
    var tagsText by remember { mutableStateOf("#Filyzon #Pakistan #TikTokReels #Viral") }
    var selectedCategory by remember { mutableStateOf(PostCategory.ENTERTAINMENT) }
    var selectedLocation by remember { mutableStateOf(locations[0]) }
    var selectedFilter by remember { mutableStateOf(filters[0]) }

    var isVipExclusive by remember { mutableStateOf(false) }
    var boostWithAdminFee by remember { mutableStateOf(false) }
    var unlockCoinsPrice by remember { mutableIntStateOf(0) }
    var isUploading by remember { mutableStateOf(false) }
    var uploadProgress by remember { mutableFloatStateOf(0f) }

    // Modern Android Photo Picker (zero broad storage permissions)
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedPhotoUri = uri
            selectedPhotoUrl = uri.toString()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(FilyzonBackgroundDark)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .padding(bottom = 80.dp)
    ) {
        // Studio Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "📸 Photo Upload & Creator Studio",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Upload photos, apply filters, monetize & boost across Pakistan",
                    color = Color(0xFF90A4AE),
                    fontSize = 11.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Main Photo Preview & Device Upload Trigger
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = FilyzonCardDark),
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                AsyncImage(
                    model = selectedPhotoUri ?: selectedPhotoUrl,
                    contentDescription = "Selected photo preview",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Filter Pill overlay
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color.Black.copy(alpha = 0.65f),
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(10.dp)
                ) {
                    Text(
                        text = "✨ Filter: $selectedFilter",
                        color = FilyzonCyan,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                // Pick From Phone Gallery Floating Action Button
                Button(
                    onClick = {
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = FilyzonCyan),
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(12.dp)
                        .testTag("upload_from_device_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.AddPhotoAlternate,
                        contentDescription = "Upload from Phone",
                        tint = Color.Black,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Upload from Phone",
                        color = Color.Black,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Choose From Photo Templates / Studio Shots
        Text(
            text = "Or Select Studio Photography Preset:",
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(6.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(samplePhotos) { photoUrl ->
                val isSelected = selectedPhotoUri == null && photoUrl == selectedPhotoUrl
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .border(
                            2.dp,
                            if (isSelected) FilyzonCyan else Color.Transparent,
                            RoundedCornerShape(10.dp)
                        )
                        .clickable {
                            selectedPhotoUri = null
                            selectedPhotoUrl = photoUrl
                        }
                ) {
                    AsyncImage(
                        model = photoUrl,
                        contentDescription = "Photo preset",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    if (isSelected) {
                        Surface(
                            shape = RoundedCornerShape(bottomStart = 6.dp),
                            color = FilyzonCyan,
                            modifier = Modifier.align(Alignment.TopEnd)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Selected",
                                tint = Color.Black,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Photo Filters Row
        Text(
            text = "Photo Tone & Filter:",
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(6.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(filters) { filter ->
                FilterChip(
                    selected = selectedFilter == filter,
                    onClick = { selectedFilter = filter },
                    label = { Text(filter, fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = FilyzonCyan,
                        selectedLabelColor = Color.Black,
                        containerColor = FilyzonCardDark,
                        labelColor = Color.White
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Caption Input
        Text(
            text = "Post Caption & Description:",
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(4.dp))

        OutlinedTextField(
            value = captionText,
            onValueChange = { captionText = it },
            placeholder = {
                Text(
                    "Share your photoshoot, entertainment vlog, product drop...",
                    color = Color(0xFF78909C),
                    fontSize = 12.sp
                )
            },
            minLines = 3,
            maxLines = 5,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = FilyzonCardDark,
                unfocusedContainerColor = FilyzonCardDark,
                focusedBorderColor = FilyzonCyan,
                unfocusedBorderColor = Color(0xFF263852),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("post_caption_input")
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Location Selector
        Text(
            text = "📍 Location Tag in Pakistan:",
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(6.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            items(locations) { loc ->
                FilterChip(
                    selected = selectedLocation == loc,
                    onClick = { selectedLocation = loc },
                    label = { Text(loc, fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF1E3A5F),
                        selectedLabelColor = FilyzonCyan,
                        containerColor = FilyzonCardDark,
                        labelColor = Color.White
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Category Picker
        Text(
            text = "Select Category:",
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(6.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            items(PostCategory.values()) { cat ->
                FilterChip(
                    selected = selectedCategory == cat,
                    onClick = { selectedCategory = cat },
                    label = { Text("${cat.iconLabel} ${cat.displayName}", fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = FilyzonCyan,
                        selectedLabelColor = Color.Black,
                        containerColor = FilyzonCardDark,
                        labelColor = Color.White
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Monetization & Admin Benefits Hub
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F2642)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "💼 Monetization & Admin Revenue",
                    color = FilyzonCyan,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                // VIP Exclusive Switch
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Lock for VIP Members Only 👑",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Encourages users to subscribe to VIP passes (100% benefits admin).",
                            color = Color(0xFF90A4AE),
                            fontSize = 10.sp
                        )
                    }
                    Switch(
                        checked = isVipExclusive,
                        onCheckedChange = { isVipExclusive = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = GoldVip,
                            checkedTrackColor = Color(0xFF4A3400)
                        )
                    )
                }

                HorizontalDivider(color = Color(0xFF1E3A5F), modifier = Modifier.padding(vertical = 8.dp))

                // Post Boost (Admin Revenue stream)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "🚀 Boost Post to 25,000 Views",
                                color = GoldVipLight,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = EasyPaisaGreen
                            ) {
                                Text(
                                    text = "Rs. 350",
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = "Rs. 350 is paid directly into App Admin earnings for front-page explore feed feature.",
                            color = Color(0xFF81D4FA),
                            fontSize = 10.sp
                        )
                    }
                    Switch(
                        checked = boostWithAdminFee,
                        onCheckedChange = { boostWithAdminFee = it },
                        modifier = Modifier.testTag("boost_post_switch"),
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = FilyzonCyan,
                            checkedTrackColor = Color(0xFF003049)
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Upload progress bar when uploading
        if (isUploading) {
            Column(modifier = Modifier.fillMaxWidth()) {
                LinearProgressIndicator(
                    progress = { uploadProgress },
                    color = FilyzonCyan,
                    trackColor = Color(0xFF1E3A5F),
                    modifier = Modifier.fillMaxWidth().height(6.dp)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Uploading to Filyzon Cloud CDN... ${(uploadProgress * 100).toInt()}%",
                    color = FilyzonCyan,
                    fontSize = 11.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        // Publish Button
        Button(
            onClick = {
                isUploading = true
                coroutineScope.launch {
                    for (i in 1..10) {
                        delay(70)
                        uploadProgress = i / 10f
                    }
                    isUploading = false
                    val tagsList = tagsText.split(" ").filter { it.isNotBlank() }
                    viewModel.createPost(
                        caption = if (captionText.isBlank()) "Enjoying the entertainment vibe on Filyzon Official! 🦅✨" else captionText,
                        imageUrl = selectedPhotoUri?.toString() ?: selectedPhotoUrl,
                        category = selectedCategory,
                        tags = tagsList,
                        isVipExclusive = isVipExclusive,
                        boostWithAdminFee = boostWithAdminFee,
                        location = selectedLocation,
                        filterName = selectedFilter,
                        unlockPriceCoins = unlockCoinsPrice
                    )
                }
            },
            enabled = !isUploading,
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = FilyzonCyan),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("publish_post_button")
        ) {
            Icon(
                imageVector = Icons.Default.CloudUpload,
                contentDescription = "Upload",
                tint = Color(0xFF003049)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (boostWithAdminFee) "Upload & Boost Post (Pay Rs. 350)" else "Upload Photo to Filyzon Feed",
                color = Color(0xFF003049),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
