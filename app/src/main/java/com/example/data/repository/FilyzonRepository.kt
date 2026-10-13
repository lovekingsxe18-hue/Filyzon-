package com.example.data.repository

import com.example.data.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class FilyzonRepository {

    private val dateFormat = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())

    val officialPakistaniBanks: List<PakistaniBankInfo> = listOf(
        PakistaniBankInfo(
            bankName = "Meezan Bank Limited",
            accountTitle = "FILYZON TECHNOLOGIES PVT LTD",
            iban = "PK64MEZN0001234567890101",
            branchCode = "0101 (Gulberg Lahore)",
            isRaastEnabled = true,
            note = "Preferred for instant Islamic banking & Raast transfers"
        ),
        PakistaniBankInfo(
            bankName = "Habib Bank Limited (HBL)",
            accountTitle = "FILYZON DIGITAL HUB",
            iban = "PK29HABB0009876543210101",
            branchCode = "0245 (Blue Area Islamabad)",
            isRaastEnabled = true,
            note = "Supports 1Link, Konnect & HBL Mobile"
        ),
        PakistaniBankInfo(
            bankName = "Bank Alfalah Islamic",
            accountTitle = "FILYZON CREATOR REVENUE",
            iban = "PK81ALFH0003456789010101",
            branchCode = "0320 (Clifton Karachi)",
            isRaastEnabled = true,
            note = "Instant Alfa app verification"
        ),
        PakistaniBankInfo(
            bankName = "United Bank Limited (UBL)",
            accountTitle = "FILYZON OFFICIAL",
            iban = "PK55UNIL0001122334455667",
            branchCode = "0112 (Faisalabad Main)",
            isRaastEnabled = true,
            note = "Direct UBL Digital & Omni support"
        ),
        PakistaniBankInfo(
            bankName = "SadaPay Business",
            accountTitle = "FILYZON OFFICIAL REVENUE",
            iban = "PK52SADA0000001234567890",
            branchCode = "0001 (SadaPay HQ)",
            isRaastEnabled = true,
            note = "Instant zero-fee transfer via SadaPay app"
        ),
        PakistaniBankInfo(
            bankName = "NayaPay Commercial",
            accountTitle = "FILYZON ENTERTAINMENT",
            iban = "PK19NAYA0000009876543210",
            branchCode = "0001 (NayaPay HQ)",
            isRaastEnabled = true,
            note = "Instant transfer via NayaPay Chat ID or IBAN"
        )
    )

    private val _currentUser = MutableStateFlow(
        UserProfile(
            id = "usr_001",
            authUuid = "09a12b34-8c7e-4f12-91ef-filyzon001",
            name = "Hamza Abbasi",
            handle = "@hamza_creator",
            avatarUrl = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=150&auto=format&fit=crop&q=80",
            bio = "Official Filyzon Creator 🦅 | Entertainment & Tech Reviews | Lahore, Pakistan 🇵🇰",
            isVip = false,
            vipTier = null,
            isVerified = false,
            followersCount = 28500,
            followingCount = 190,
            totalEarningsPkr = 48200.0,
            walletBalancePkr = 6500.0,
            walletCoins = 2400
        )
    )
    val currentUser: StateFlow<UserProfile> = _currentUser.asStateFlow()

    private val _adminUsers = MutableStateFlow(
        setOf(
            "0a9e71b2-admin-super",
            "usr_admin_001",
            "filyzon-owner-uuid-2026"
        )
    )
    val adminUsers: StateFlow<Set<String>> = _adminUsers.asStateFlow()

    private val _currentAdminUuid = MutableStateFlow("0a9e71b2-admin-super")
    val currentAdminUuid: StateFlow<String> = _currentAdminUuid.asStateFlow()

    private val _isAdminSessionActive = MutableStateFlow(true)
    val isAdminSessionActive: StateFlow<Boolean> = _isAdminSessionActive.asStateFlow()

    private val _registeredProfiles = MutableStateFlow<List<UserProfile>>(
        listOf(
            UserProfile(
                id = "usr_001",
                authUuid = "09a12b34-8c7e-4f12-91ef-filyzon001",
                name = "Hamza Abbasi",
                handle = "@hamza_creator",
                avatarUrl = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=150&auto=format&fit=crop&q=80",
                bio = "Official Filyzon Creator 🦅 | Entertainment & Tech Reviews | Lahore, Pakistan 🇵🇰",
                isVip = false,
                isVerified = false,
                followersCount = 28500,
                followingCount = 190,
                totalEarningsPkr = 48200.0,
                walletBalancePkr = 6500.0,
                walletCoins = 2400
            ),
            UserProfile(
                id = "usr_002",
                authUuid = "11c43d56-9d8f-4a21-98bc-filyzon002",
                name = "Ayesha Khan Official",
                handle = "@ayesha_vlogs",
                avatarUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=150&auto=format&fit=crop&q=80",
                bio = "Travel & Lifestyle Vlogger 🇵🇰 Islamabad | FZ Top Creator",
                isVip = true,
                vipTier = "VIP_GOLD",
                isVerified = true,
                verifiedDate = "10 Oct 2026, 04:30 PM",
                verifiedByAdminUuid = "0a9e71b2-admin-super",
                followersCount = 64200,
                followingCount = 210,
                totalEarningsPkr = 95400.0,
                walletBalancePkr = 18200.0,
                walletCoins = 8500
            ),
            UserProfile(
                id = "usr_003",
                authUuid = "22d54e67-0e9a-4b32-87cd-filyzon003",
                name = "Bilal Tariq Gaming",
                handle = "@bilal_plays",
                avatarUrl = "https://images.unsplash.com/photo-1570295999919-56ceb5ecca61?w=150&auto=format&fit=crop&q=80",
                bio = "Pro Gamer & Streamer | Esports Pakistan 🎮 | PC setups & reviews",
                isVip = true,
                vipTier = "VIP_PLATINUM",
                isVerified = true,
                verifiedDate = "11 Oct 2026, 09:15 PM",
                verifiedByAdminUuid = "0a9e71b2-admin-super",
                followersCount = 89000,
                followingCount = 145,
                totalEarningsPkr = 142000.0,
                walletBalancePkr = 25400.0,
                walletCoins = 14200
            ),
            UserProfile(
                id = "usr_004",
                authUuid = "33e65f78-1f0b-4c43-76de-filyzon004",
                name = "Zainab Kitchen Express",
                handle = "@zainab_cooks",
                avatarUrl = "https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=150&auto=format&fit=crop&q=80",
                bio = "Authentic Pakistani Biryani, Karahi & BBQ Recipes 🍲 Karachi Food Street",
                isVip = false,
                isVerified = false,
                followersCount = 18200,
                followingCount = 98,
                totalEarningsPkr = 22000.0,
                walletBalancePkr = 3400.0,
                walletCoins = 1100
            ),
            UserProfile(
                id = "usr_005",
                authUuid = "44f76a89-2a1c-4d54-65ef-filyzon005",
                name = "Fahad Comedy Hub",
                handle = "@fahad_reels",
                avatarUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150&auto=format&fit=crop&q=80",
                bio = "Daily memes, funny TikTok shorts & Urdu comedy sketches 😂 Lahore",
                isVip = false,
                isVerified = false,
                followersCount = 31400,
                followingCount = 310,
                totalEarningsPkr = 38000.0,
                walletBalancePkr = 4900.0,
                walletCoins = 1750
            ),
            UserProfile(
                id = "usr_006",
                authUuid = "55a87b90-3b2d-4e65-54fa-filyzon006",
                name = "Sara Style & Trends",
                handle = "@sara_fashion",
                avatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150&auto=format&fit=crop&q=80",
                bio = "Bridal & Western Fashion Lookbooks | Pakistani Couture 👗",
                isVip = true,
                vipTier = "VIP_GOLD",
                isVerified = true,
                verifiedDate = "08 Oct 2026, 02:00 PM",
                verifiedByAdminUuid = "0a9e71b2-admin-super",
                followersCount = 47800,
                followingCount = 180,
                totalEarningsPkr = 62000.0,
                walletBalancePkr = 9800.0,
                walletCoins = 3900
            )
        )
    )
    val registeredProfiles: StateFlow<List<UserProfile>> = _registeredProfiles.asStateFlow()

    private val _verificationLogs = MutableStateFlow<List<VerificationLog>>(
        listOf(
            VerificationLog(
                id = "log_001",
                targetUserId = "usr_002",
                targetUsername = "@ayesha_vlogs",
                adminUserId = "0a9e71b2-admin-super",
                action = VerificationAction.GRANT_GREEN_TICK,
                timestamp = "10 Oct, 04:30 PM",
                executionMs = 138,
                responseStatus = 200,
                message = "Edge Function 'admin-verify': Green tick granted to @ayesha_vlogs"
            ),
            VerificationLog(
                id = "log_002",
                targetUserId = "usr_003",
                targetUsername = "@bilal_plays",
                adminUserId = "0a9e71b2-admin-super",
                action = VerificationAction.GRANT_GREEN_TICK,
                timestamp = "11 Oct, 09:15 PM",
                executionMs = 142,
                responseStatus = 200,
                message = "Edge Function 'admin-verify': Green tick granted to @bilal_plays"
            ),
            VerificationLog(
                id = "log_003",
                targetUserId = "usr_006",
                targetUsername = "@sara_fashion",
                adminUserId = "0a9e71b2-admin-super",
                action = VerificationAction.GRANT_GREEN_TICK,
                timestamp = "08 Oct, 02:00 PM",
                executionMs = 129,
                responseStatus = 200,
                message = "Edge Function 'admin-verify': Green tick granted to @sara_fashion"
            )
        )
    )
    val verificationLogs: StateFlow<List<VerificationLog>> = _verificationLogs.asStateFlow()

    private val _posts = MutableStateFlow<List<Post>>(
        listOf(
            Post(
                id = "post_1",
                authorName = "Ayesha Khan Official",
                authorHandle = "@ayesha_vlogs",
                authorAvatarUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=150&auto=format&fit=crop&q=80",
                isVipCreator = true,
                isVerified = true,
                caption = "Night photoshoot in Islamabad! 📸 Which frame is your favorite? Drop a falcon in the comments 🦅✨ #Filyzon #IslamabadVibes #Photography #Pakistan",
                tags = listOf("#Filyzon", "#IslamabadVibes", "#Photography", "#VIPCreator"),
                imageUrl = "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=800&auto=format&fit=crop&q=80",
                likesCount = 3840,
                commentsCount = 248,
                sharesCount = 120,
                giftsCount = 45,
                category = PostCategory.ENTERTAINMENT,
                isVipExclusive = false,
                isMonetized = true,
                isBoosted = true,
                location = "Islamabad, Pakistan 🇵🇰",
                filterName = "Vivid",
                timeAgo = "1h ago"
            ),
            Post(
                id = "post_2",
                authorName = "Bilal Tariq Gaming",
                authorHandle = "@bilal_plays",
                authorAvatarUrl = "https://images.unsplash.com/photo-1570295999919-56ceb5ecca61?w=150&auto=format&fit=crop&q=80",
                isVipCreator = true,
                isVerified = true,
                caption = "Setup update 2026! 🚀 Mechanical keyboard & stream lighting test. New streaming shorts coming tonight on Filyzon Entertainment channel!",
                tags = listOf("#GamingSetup", "#TechPakistan", "#TikTokReels", "#Gamer"),
                imageUrl = "https://images.unsplash.com/photo-1542751371-adc38448a05e?w=800&auto=format&fit=crop&q=80",
                likesCount = 5920,
                commentsCount = 412,
                sharesCount = 310,
                giftsCount = 89,
                category = PostCategory.TECH,
                isVipExclusive = false,
                isMonetized = true,
                isBoosted = true,
                location = "Lahore, Pakistan 🇵🇰",
                filterName = "Cyberpunk",
                timeAgo = "3h ago"
            ),
            Post(
                id = "post_3",
                authorName = "Zara Noor Fashion",
                authorHandle = "@zara_style",
                authorAvatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150&auto=format&fit=crop&q=80",
                isVipCreator = true,
                isVerified = true,
                caption = "Exclusive Eid Collection Sneak Peek! 👑 VIP members get early discount coupon for the Filyzon Shop. Check product tag below! 🛍️",
                tags = listOf("#FashionTrends", "#FilyzonShop", "#VIPExclusive", "#KarachiFashion"),
                imageUrl = "https://images.unsplash.com/photo-1490481651871-ab68de25d43d?w=800&auto=format&fit=crop&q=80",
                likesCount = 8120,
                commentsCount = 670,
                sharesCount = 450,
                giftsCount = 142,
                category = PostCategory.FASHION,
                isVipExclusive = true,
                isMonetized = true,
                isBoosted = false,
                location = "Karachi, Pakistan 🇵🇰",
                filterName = "Golden",
                timeAgo = "5h ago"
            ),
            Post(
                id = "post_4",
                authorName = "Ali Raza Comedy Hub",
                authorHandle = "@ali_roasts",
                authorAvatarUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150&auto=format&fit=crop&q=80",
                isVipCreator = false,
                isVerified = false,
                caption = "Pakistani cricket fans during last over tension 😂 Tag that one friend who cannot watch TV in the final over! #Humor #TikTokPakistan #CricketFever",
                tags = listOf("#TikTokPakistan", "#Comedy", "#MemePakistan", "#Cricket"),
                imageUrl = "https://images.unsplash.com/photo-1531427186611-ecfd6d936c79?w=800&auto=format&fit=crop&q=80",
                likesCount = 12400,
                commentsCount = 930,
                sharesCount = 1800,
                giftsCount = 68,
                category = PostCategory.COMEDY,
                isVipExclusive = false,
                isMonetized = true,
                isBoosted = false,
                location = "Rawalpindi, Pakistan 🇵🇰",
                filterName = "Normal",
                timeAgo = "8h ago"
            )
        )
    )
    val posts: StateFlow<List<Post>> = _posts.asStateFlow()

    private val _products = MutableStateFlow<List<Product>>(
        listOf(
            Product(
                id = "prod_1",
                title = "FZ Pro 4K Creator Vlogging Ring Light & Tripod",
                description = "Professional 18-inch RGB ring light with bluetooth remote, phone holder, and 2.1m aluminum stand. Perfect for TikTok, Reels & photography.",
                pricePkr = 3499.0,
                originalPricePkr = 4999.0,
                discountPercent = 30,
                rating = 4.9,
                reviewCount = 380,
                salesCount = 1450,
                stock = 45,
                imageUrl = "https://images.unsplash.com/photo-1598327105666-5b89351aff97?w=500&auto=format&fit=crop&q=80",
                sellerName = "Filyzon Official Store 🦅",
                isOfficialFilyzon = true,
                category = "Creator Gear",
                adminCommissionPercent = 10.0
            ),
            Product(
                id = "prod_2",
                title = "Wireless Noise-Cancelling Earbuds ANC Pro",
                description = "Ultra low latency gaming & music earbuds with touch controls, 36hr battery backup, and IPX5 water resistance.",
                pricePkr = 2799.0,
                originalPricePkr = 3999.0,
                discountPercent = 30,
                rating = 4.7,
                reviewCount = 612,
                salesCount = 2890,
                stock = 80,
                imageUrl = "https://images.unsplash.com/photo-1590658268037-6bf12165a8df?w=500&auto=format&fit=crop&q=80",
                sellerName = "SoundBeat Pakistan",
                isOfficialFilyzon = false,
                category = "Electronics",
                adminCommissionPercent = 8.0
            ),
            Product(
                id = "prod_3",
                title = "Filyzon VIP Embroidered Falcon Streetwear Hoodie",
                description = "Heavyweight 380 GSM fleece hoodie with high-definition embroidered FZ Falcon crest and royal blue drawstrings.",
                pricePkr = 3850.0,
                originalPricePkr = 5500.0,
                discountPercent = 30,
                rating = 4.8,
                reviewCount = 188,
                salesCount = 890,
                stock = 25,
                imageUrl = "https://images.unsplash.com/photo-1556905055-8f358a7a47b2?w=500&auto=format&fit=crop&q=80",
                sellerName = "FZ Official Merch",
                isOfficialFilyzon = true,
                category = "Fashion",
                adminCommissionPercent = 15.0
            ),
            Product(
                id = "prod_4",
                title = "Smart Fitness Watch Ultra 2 with AMOLED Display",
                description = "Bluetooth calling, heart rate & sleep tracker, 100+ sports modes, waterproof with interchangeable strap.",
                pricePkr = 4999.0,
                originalPricePkr = 6999.0,
                discountPercent = 28,
                rating = 4.6,
                reviewCount = 420,
                salesCount = 1200,
                stock = 34,
                imageUrl = "https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=500&auto=format&fit=crop&q=80",
                sellerName = "TechZone Lahore",
                isOfficialFilyzon = false,
                category = "Gadgets",
                adminCommissionPercent = 8.0
            )
        )
    )
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    val vipPlans: List<VipPlan> = listOf(
        VipPlan(
            id = "vip_silver",
            title = "Silver Creator Pass",
            tierCode = "SILVER",
            pricePkr = 499.0,
            durationLabel = "1 Month",
            coinBonus = 500,
            badgeColorHex = 0xFFB0BEC5,
            perks = listOf(
                "Verified Silver Creator Badge 🥈",
                "500 Free Filyzon Coins reload",
                "Unlock Photo Monetization & Tips",
                "3% lower store purchase fee"
            ),
            isPopular = false
        ),
        VipPlan(
            id = "vip_gold",
            title = "Gold VIP Creator & Partner",
            tierCode = "GOLD",
            pricePkr = 1199.0,
            durationLabel = "1 Month",
            coinBonus = 1500,
            badgeColorHex = 0xFFFFB300,
            perks = listOf(
                "Golden Crown & Falcon Verified Badge 👑",
                "1,500 Bonus Filyzon Coins",
                "Top Explore & TikTok Feed Boost",
                "Direct Easypaisa & JazzCash payout access",
                "Exclusive VIP Creator Live Audio Stage"
            ),
            isPopular = true
        ),
        VipPlan(
            id = "vip_diamond",
            title = "Diamond Falcon Elite",
            tierCode = "DIAMOND",
            pricePkr = 2499.0,
            durationLabel = "1 Month",
            coinBonus = 4000,
            badgeColorHex = 0xFF00E5FF,
            perks = listOf(
                "Diamond Royal Badge & Animated Border 💎",
                "4,000 Filyzon Gift Coins",
                "0% Platform fee on creator payouts",
                "Post Boost voucher worth Rs. 1000",
                "Official Filyzon Partner verification"
            ),
            isPopular = false
        )
    )

    val availableGifts: List<GiftItem> = listOf(
        GiftItem("gift_rose", "Red Rose", "🌹", 20, 70.0, 30.0),
        GiftItem("gift_heart", "Love Heart", "💖", 50, 70.0, 30.0),
        GiftItem("gift_falcon", "FZ Falcon Emblem", "🦅", 150, 75.0, 25.0),
        GiftItem("gift_crown", "Royal VIP Crown", "👑", 500, 75.0, 25.0),
        GiftItem("gift_supercar", "Golden Supercar", "🏎️", 1200, 80.0, 20.0)
    )

    private val _transactions = MutableStateFlow<List<WalletTransaction>>(
        listOf(
            WalletTransaction(
                id = "TXN_78491",
                title = "Gold VIP Creator Subscription",
                type = TransactionType.VIP_SUBSCRIPTION,
                amountPkr = 1199.0,
                coinsDelta = 1500,
                paymentMethod = "Easypaisa (0312-9988112)",
                status = "Completed",
                date = "Today, 10:15 AM",
                adminSharePkr = 1199.0,
                receiptId = "FZ-REC-78491"
            ),
            WalletTransaction(
                id = "TXN_78420",
                title = "Creator Gift Earnings & Tips",
                type = TransactionType.CREATOR_GIFT,
                amountPkr = 450.0,
                coinsDelta = 200,
                paymentMethod = "Filyzon Coins",
                status = "Completed",
                date = "Yesterday, 04:30 PM",
                adminSharePkr = 135.0,
                receiptId = "FZ-REC-78420"
            ),
            WalletTransaction(
                id = "TXN_78310",
                title = "Wallet Reload (Meezan Bank)",
                type = TransactionType.DEPOSIT,
                amountPkr = 2500.0,
                coinsDelta = 0,
                paymentMethod = "Meezan Bank (PK64MEZN0001234567890101)",
                status = "Completed",
                date = "11 Oct, 02:10 PM",
                adminSharePkr = 0.0,
                receiptId = "FZ-REC-78310"
            )
        )
    )
    val transactions: StateFlow<List<WalletTransaction>> = _transactions.asStateFlow()

    // Recent receipts
    private val _receipts = MutableStateFlow<Map<String, PaymentReceipt>>(emptyMap())
    val receipts: StateFlow<Map<String, PaymentReceipt>> = _receipts.asStateFlow()

    // Admin Revenue Tracking ("Jo app admin ko fayda pahuncha sake")
    private val _adminMetrics = MutableStateFlow(
        AdminRevenueMetrics(
            totalNetRevenuePkr = 148650.0,
            vipSubscriptionRevenuePkr = 84900.0,
            storeCommissionsPkr = 38450.0,
            giftsCommissionPkr = 14800.0,
            postBoostAdRevenuePkr = 10500.0,
            totalActiveVipMembers = 78,
            totalStoreOrders = 214,
            boostedPostsCount = 35,
            commissionRatePercent = 10.0
        )
    )
    val adminMetrics: StateFlow<AdminRevenueMetrics> = _adminMetrics.asStateFlow()

    // User actions
    fun toggleLikePost(postId: String) {
        val updated = _posts.value.map { post ->
            if (post.id == postId) {
                val newLiked = !post.isLiked
                post.copy(
                    isLiked = newLiked,
                    likesCount = if (newLiked) post.likesCount + 1 else post.likesCount - 1
                )
            } else post
        }
        _posts.value = updated
    }

    fun addPost(
        caption: String,
        imageUrl: String,
        category: PostCategory,
        tags: List<String>,
        isVipExclusive: Boolean,
        boostWithAdminFee: Boolean,
        location: String = "Lahore, Pakistan 🇵🇰",
        filterName: String = "Normal",
        unlockPriceCoins: Int = 0
    ): Boolean {
        val currentUserVal = _currentUser.value
        val boostCostPkr = 350.0

        if (boostWithAdminFee && currentUserVal.walletBalancePkr < boostCostPkr) {
            return false // insufficient balance for boosting
        }

        if (boostWithAdminFee) {
            _currentUser.value = currentUserVal.copy(
                walletBalancePkr = currentUserVal.walletBalancePkr - boostCostPkr
            )
            // Admin receives post boost revenue
            val m = _adminMetrics.value
            _adminMetrics.value = m.copy(
                totalNetRevenuePkr = m.totalNetRevenuePkr + boostCostPkr,
                postBoostAdRevenuePkr = m.postBoostAdRevenuePkr + boostCostPkr,
                boostedPostsCount = m.boostedPostsCount + 1
            )
            addTransaction(
                title = "Post Boost to 25k Views",
                type = TransactionType.POST_BOOST_AD,
                amountPkr = boostCostPkr,
                coinsDelta = 0,
                method = "Filyzon Wallet Balance",
                adminShare = boostCostPkr
            )
        }

        val newPost = Post(
            id = "post_${UUID.randomUUID()}",
            authorName = currentUserVal.name,
            authorHandle = currentUserVal.handle,
            authorAvatarUrl = currentUserVal.avatarUrl,
            isVipCreator = currentUserVal.isVip,
            isVerified = currentUserVal.isVip,
            caption = caption,
            tags = tags,
            imageUrl = imageUrl,
            likesCount = 1,
            commentsCount = 0,
            sharesCount = 0,
            giftsCount = 0,
            category = category,
            isVipExclusive = isVipExclusive,
            isMonetized = true,
            isBoosted = boostWithAdminFee,
            location = location,
            filterName = filterName,
            unlockPriceCoins = unlockPriceCoins,
            timeAgo = "Just now"
        )

        _posts.value = listOf(newPost) + _posts.value
        return true
    }

    fun sendGiftToPost(postId: String, gift: GiftItem): Boolean {
        val user = _currentUser.value
        if (user.walletCoins < gift.costCoins) return false

        _currentUser.value = user.copy(walletCoins = user.walletCoins - gift.costCoins)

        val giftValuePkr = gift.costCoins * 0.5
        val adminProfit = giftValuePkr * (gift.adminSharePercent / 100.0)

        val m = _adminMetrics.value
        _adminMetrics.value = m.copy(
            totalNetRevenuePkr = m.totalNetRevenuePkr + adminProfit,
            giftsCommissionPkr = m.giftsCommissionPkr + adminProfit
        )

        _posts.value = _posts.value.map {
            if (it.id == postId) it.copy(giftsCount = it.giftsCount + 1) else it
        }

        addTransaction(
            title = "Sent Gift: ${gift.emoji} ${gift.name}",
            type = TransactionType.CREATOR_GIFT,
            amountPkr = giftValuePkr,
            coinsDelta = -gift.costCoins,
            method = "Filyzon Coins",
            adminShare = adminProfit
        )
        return true
    }

    fun subscribeVip(
        plan: VipPlan,
        paymentMethod: PaymentMethodType,
        accountIdentifier: String,
        referenceTid: String = "FZ-TID-${(100000..999999).random()}"
    ): PaymentReceipt {
        val user = _currentUser.value
        _currentUser.value = user.copy(
            isVip = true,
            vipTier = plan.tierCode,
            walletCoins = user.walletCoins + plan.coinBonus
        )

        // 100% of VIP subscription goes straight to App Admin Profit!
        val m = _adminMetrics.value
        _adminMetrics.value = m.copy(
            totalNetRevenuePkr = m.totalNetRevenuePkr + plan.pricePkr,
            vipSubscriptionRevenuePkr = m.vipSubscriptionRevenuePkr + plan.pricePkr,
            totalActiveVipMembers = m.totalActiveVipMembers + 1
        )

        val receipt = createReceipt(
            orderTitle = "VIP Subscription: ${plan.title}",
            amountPkr = plan.pricePkr,
            paymentMethod = paymentMethod,
            accountUsed = accountIdentifier,
            referenceTid = referenceTid
        )

        addTransaction(
            title = "VIP Subscription: ${plan.title}",
            type = TransactionType.VIP_SUBSCRIPTION,
            amountPkr = plan.pricePkr,
            coinsDelta = plan.coinBonus,
            method = "${paymentMethod.brandTag} ($accountIdentifier)",
            adminShare = plan.pricePkr,
            receiptId = receipt.receiptNumber
        )

        return receipt
    }

    fun purchaseProduct(
        product: Product,
        paymentMethod: PaymentMethodType,
        accountIdentifier: String,
        referenceTid: String = "FZ-TID-${(100000..999999).random()}"
    ): PaymentReceipt {
        val adminCommission = product.pricePkr * (product.adminCommissionPercent / 100.0)

        val m = _adminMetrics.value
        _adminMetrics.value = m.copy(
            totalNetRevenuePkr = m.totalNetRevenuePkr + adminCommission,
            storeCommissionsPkr = m.storeCommissionsPkr + adminCommission,
            totalStoreOrders = m.totalStoreOrders + 1
        )

        _products.value = _products.value.map {
            if (it.id == product.id) it.copy(salesCount = it.salesCount + 1, stock = maxOf(0, it.stock - 1)) else it
        }

        val receipt = createReceipt(
            orderTitle = "Shop Order: ${product.title}",
            amountPkr = product.pricePkr,
            paymentMethod = paymentMethod,
            accountUsed = accountIdentifier,
            referenceTid = referenceTid
        )

        addTransaction(
            title = "Shop Order: ${product.title.take(24)}...",
            type = TransactionType.SHOP_PURCHASE,
            amountPkr = product.pricePkr,
            coinsDelta = (product.pricePkr * 0.1).toInt(),
            method = "${paymentMethod.brandTag} ($accountIdentifier)",
            adminShare = adminCommission,
            receiptId = receipt.receiptNumber
        )

        return receipt
    }

    fun depositWalletFunds(
        amountPkr: Double,
        paymentMethod: PaymentMethodType,
        accountIdentifier: String,
        referenceTid: String = "FZ-TID-${(100000..999999).random()}"
    ): PaymentReceipt {
        val user = _currentUser.value
        _currentUser.value = user.copy(
            walletBalancePkr = user.walletBalancePkr + amountPkr
        )

        val receipt = createReceipt(
            orderTitle = "Wallet Credits Deposit (Rs. ${amountPkr.toInt()})",
            amountPkr = amountPkr,
            paymentMethod = paymentMethod,
            accountUsed = accountIdentifier,
            referenceTid = referenceTid
        )

        addTransaction(
            title = "Wallet Deposit (${paymentMethod.brandTag})",
            type = TransactionType.DEPOSIT,
            amountPkr = amountPkr,
            coinsDelta = 0,
            method = "${paymentMethod.brandTag} ($accountIdentifier)",
            adminShare = 0.0,
            receiptId = receipt.receiptNumber
        )

        return receipt
    }

    fun buyCoins(
        coinsCount: Int,
        costPkr: Double,
        paymentMethod: PaymentMethodType,
        accountIdentifier: String,
        referenceTid: String = "FZ-TID-${(100000..999999).random()}"
    ): PaymentReceipt {
        val user = _currentUser.value
        _currentUser.value = user.copy(
            walletCoins = user.walletCoins + coinsCount
        )

        val adminCut = costPkr * 0.35
        val m = _adminMetrics.value
        _adminMetrics.value = m.copy(
            totalNetRevenuePkr = m.totalNetRevenuePkr + adminCut,
            giftsCommissionPkr = m.giftsCommissionPkr + adminCut
        )

        val receipt = createReceipt(
            orderTitle = "Filyzon In-App Credits: $coinsCount Coins",
            amountPkr = costPkr,
            paymentMethod = paymentMethod,
            accountUsed = accountIdentifier,
            referenceTid = referenceTid
        )

        addTransaction(
            title = "Purchased $coinsCount Filyzon Coins",
            type = TransactionType.DEPOSIT,
            amountPkr = costPkr,
            coinsDelta = coinsCount,
            method = "${paymentMethod.brandTag} ($accountIdentifier)",
            adminShare = adminCut,
            receiptId = receipt.receiptNumber
        )

        return receipt
    }

    fun withdrawAdminProfits(amountPkr: Double, destinationBank: String): Boolean {
        val m = _adminMetrics.value
        if (amountPkr > m.totalNetRevenuePkr) return false
        _adminMetrics.value = m.copy(
            totalNetRevenuePkr = m.totalNetRevenuePkr - amountPkr
        )
        addTransaction(
            title = "Admin Profit Payout to $destinationBank",
            type = TransactionType.ADMIN_PROFIT_COLLECTION,
            amountPkr = amountPkr,
            coinsDelta = 0,
            method = destinationBank,
            adminShare = -amountPkr
        )
        return true
    }

    private fun createReceipt(
        orderTitle: String,
        amountPkr: Double,
        paymentMethod: PaymentMethodType,
        accountUsed: String,
        referenceTid: String
    ): PaymentReceipt {
        val recNum = "FZ-REC-${(10000..99999).random()}"
        val receipt = PaymentReceipt(
            receiptNumber = recNum,
            orderTitle = orderTitle,
            amountPkr = amountPkr,
            feePkr = 0.0,
            paymentMethod = paymentMethod,
            accountUsed = accountUsed,
            referenceTid = referenceTid,
            timestamp = dateFormat.format(Date()),
            verificationHash = "SHA256-${UUID.randomUUID().toString().take(12).uppercase()}"
        )
        _receipts.value = _receipts.value + (recNum to receipt)
        return receipt
    }

    private fun addTransaction(
        title: String,
        type: TransactionType,
        amountPkr: Double,
        coinsDelta: Int,
        method: String,
        adminShare: Double,
        receiptId: String? = null
    ) {
        val newTx = WalletTransaction(
            id = "TXN_${(10000..99999).random()}",
            title = title,
            type = type,
            amountPkr = amountPkr,
            coinsDelta = coinsDelta,
            paymentMethod = method,
            status = "Completed",
            date = dateFormat.format(Date()),
            adminSharePkr = adminShare,
            receiptId = receiptId
        )
        _transactions.value = listOf(newTx) + _transactions.value
    }

    /**
     * Executes the Supabase Edge Function 'admin-verify'.
     * Validates that the caller's auth UUID is in 'public.admin_users'.
     * If valid, updates 'verified_users' badge status, syncs feed posts, and records audit log.
     */
    fun verifyUserWithEdgeFunction(targetUserId: String, grant: Boolean): Pair<Boolean, String> {
        val currentAdmin = _currentAdminUuid.value
        val isAdminSession = _isAdminSessionActive.value

        // Security check: must have active admin role and UUID in admin_users
        if (!isAdminSession || !_adminUsers.value.contains(currentAdmin)) {
            val errorMsg = "Admin access required: Current session UUID ($currentAdmin) is not in admin_users table!"
            val failedLog = VerificationLog(
                id = "log_${UUID.randomUUID().toString().take(6)}",
                targetUserId = targetUserId,
                targetUsername = _registeredProfiles.value.find { it.id == targetUserId }?.handle ?: targetUserId,
                adminUserId = currentAdmin,
                action = if (grant) VerificationAction.GRANT_GREEN_TICK else VerificationAction.REVOKE_GREEN_TICK,
                timestamp = dateFormat.format(Date()),
                executionMs = 45,
                responseStatus = 403,
                message = "HTTP 403 Forbidden: Caller lacks admin privilege in admin_users table."
            )
            _verificationLogs.value = listOf(failedLog) + _verificationLogs.value
            return Pair(false, errorMsg)
        }

        val target = _registeredProfiles.value.find { it.id == targetUserId }
            ?: return Pair(false, "User profile with ID $targetUserId not found in public.profiles.")

        val dateStamp = dateFormat.format(Date())

        // 1. Update registered profile
        val updatedProfile = target.copy(
            isVerified = grant,
            verifiedDate = if (grant) dateStamp else null,
            verifiedByAdminUuid = if (grant) currentAdmin else null
        )

        _registeredProfiles.value = _registeredProfiles.value.map { profile ->
            if (profile.id == targetUserId) updatedProfile else profile
        }

        // 2. If target is current logged-in user, sync current user state
        if (_currentUser.value.id == targetUserId || _currentUser.value.handle == target.handle) {
            _currentUser.value = updatedProfile
        }

        // 3. Update author's posts across feed in real time
        _posts.value = _posts.value.map { post ->
            if (post.authorHandle.equals(target.handle, ignoreCase = true) ||
                post.authorName.equals(target.name, ignoreCase = true)
            ) {
                post.copy(isVerified = grant)
            } else {
                post
            }
        }

        // 4. Record successful Edge Function invocation audit log
        val action = if (grant) VerificationAction.GRANT_GREEN_TICK else VerificationAction.REVOKE_GREEN_TICK
        val log = VerificationLog(
            id = "log_${UUID.randomUUID().toString().take(6)}",
            targetUserId = targetUserId,
            targetUsername = target.handle,
            adminUserId = currentAdmin,
            action = action,
            timestamp = dateStamp,
            executionMs = (110..180).random().toLong(),
            responseStatus = 200,
            message = "HTTP 200 OK: 'admin-verify' Edge Function updated public.verified_users"
        )
        _verificationLogs.value = listOf(log) + _verificationLogs.value

        val successMsg = if (grant) {
            "Green tick granted to ${target.handle}! Edge Function updated verified_users."
        } else {
            "Green tick revoked from ${target.handle}. Profile status updated."
        }
        return Pair(true, successMsg)
    }

    fun setAdminSessionActive(active: Boolean) {
        _isAdminSessionActive.value = active
    }

    fun setCurrentAdminUuid(uuid: String) {
        _currentAdminUuid.value = uuid
    }

    fun addAdminUserUuid(uuid: String) {
        _adminUsers.value = _adminUsers.value + uuid
    }

    fun removeAdminUserUuid(uuid: String) {
        _adminUsers.value = _adminUsers.value - uuid
    }

    fun addNewProfile(username: String, name: String, bio: String = "Filyzon Creator 🇵🇰"): UserProfile {
        val cleanHandle = if (username.startsWith("@")) username else "@$username"
        val newUuid = "usr_auth_${UUID.randomUUID().toString().take(8)}"
        val avatar = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150&auto=format&fit=crop&q=80"
        val profile = UserProfile(
            id = "usr_${(100..999).random()}",
            authUuid = newUuid,
            name = name,
            handle = cleanHandle,
            avatarUrl = avatar,
            bio = bio,
            isVip = false,
            isVerified = false,
            followersCount = 120,
            followingCount = 15,
            totalEarningsPkr = 0.0,
            walletBalancePkr = 500.0,
            walletCoins = 100
        )
        _registeredProfiles.value = listOf(profile) + _registeredProfiles.value
        return profile
    }
}

