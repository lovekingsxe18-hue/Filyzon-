package com.example.data.model

data class UserProfile(
    val id: String,
    val name: String,
    val handle: String,
    val avatarUrl: String,
    val bio: String,
    val isVip: Boolean = false,
    val vipTier: String? = null,
    val isVerified: Boolean = false,
    val verifiedDate: String? = null,
    val verifiedByAdminUuid: String? = null,
    val isAdmin: Boolean = false,
    val authUuid: String = id,
    val followersCount: Int = 12400,
    val followingCount: Int = 340,
    val totalEarningsPkr: Double = 35400.0,
    val walletBalancePkr: Double = 5250.0,
    val walletCoins: Int = 1800
)

data class VerificationLog(
    val id: String,
    val targetUserId: String,
    val targetUsername: String,
    val adminUserId: String,
    val action: VerificationAction,
    val timestamp: String,
    val executionMs: Long = 135,
    val responseStatus: Int = 200,
    val message: String = "Edge Function executed: verified_users record updated"
)

enum class VerificationAction(val displayName: String, val badgeLabel: String) {
    GRANT_GREEN_TICK("Green Tick Granted", "Verified 🟢"),
    REVOKE_GREEN_TICK("Green Tick Revoked", "Revoked ⚪")
}

data class Post(
    val id: String,
    val authorName: String,
    val authorHandle: String,
    val authorAvatarUrl: String,
    val isVipCreator: Boolean,
    val isVerified: Boolean = true,
    val caption: String,
    val tags: List<String>,
    val imageUrl: String,
    val likesCount: Int,
    val commentsCount: Int,
    val sharesCount: Int,
    val giftsCount: Int,
    val category: PostCategory,
    val isVipExclusive: Boolean = false,
    val isMonetized: Boolean = true,
    val isBoosted: Boolean = false,
    val priceToUnlockPkr: Int = 0,
    val unlockPriceCoins: Int = 0,
    val location: String = "Lahore, Pakistan 🇵🇰",
    val filterName: String = "Normal",
    val isLiked: Boolean = false,
    val isSaved: Boolean = false,
    val timeAgo: String = "2h ago"
)

enum class PostCategory(val displayName: String, val iconLabel: String) {
    ENTERTAINMENT("Entertainment", "🎭"),
    TIKTOK_REELS("TikTok Reels", "🎬"),
    COMEDY("Comedy & Fun", "😂"),
    MUSIC("Music & Trends", "🎵"),
    FASHION("Fashion & Style", "👗"),
    STORE_SHOWCASE("Shop & Business", "🛍️"),
    TECH("Tech & Gaming", "⚡")
}

data class PostComment(
    val id: String,
    val userName: String,
    val userAvatar: String,
    val text: String,
    val timeAgo: String,
    val isVip: Boolean = false,
    val isVerified: Boolean = false
)

data class Product(
    val id: String,
    val title: String,
    val description: String,
    val pricePkr: Double,
    val originalPricePkr: Double,
    val discountPercent: Int,
    val rating: Double,
    val reviewCount: Int,
    val salesCount: Int,
    val stock: Int,
    val imageUrl: String,
    val sellerName: String,
    val isOfficialFilyzon: Boolean,
    val category: String,
    val adminCommissionPercent: Double = 8.0 // Admin profit percentage
)

data class VipPlan(
    val id: String,
    val title: String,
    val tierCode: String,
    val pricePkr: Double,
    val durationLabel: String,
    val coinBonus: Int,
    val badgeColorHex: Long,
    val perks: List<String>,
    val isPopular: Boolean = false
)

enum class PaymentCategory(val title: String) {
    MOBILE_WALLET("Mobile Wallets (EasyPaisa & JazzCash)"),
    BANK_TRANSFER("Direct Bank Transfer & Raast"),
    DIGITAL_WALLET("Digital Banks (SadaPay & NayaPay)"),
    IN_APP_WALLET("Filyzon Wallet Balance")
}

enum class PaymentMethodType(
    val displayName: String,
    val brandTag: String,
    val description: String,
    val category: PaymentCategory,
    val iconColor: Long
) {
    EASYPAISA(
        displayName = "Easypaisa Mobile Account",
        brandTag = "Easypaisa",
        description = "03xx-xxxxxxx account with instant OTP & MPIN approval",
        category = PaymentCategory.MOBILE_WALLET,
        iconColor = 0xFF00A859
    ),
    JAZZCASH(
        displayName = "JazzCash Mobile Account",
        brandTag = "JazzCash",
        description = "Instant debit via JazzCash App prompt or OTC voucher",
        category = PaymentCategory.MOBILE_WALLET,
        iconColor = 0xFFE65100
    ),
    PAKISTANI_BANK(
        displayName = "Direct Pakistani Bank Transfer",
        brandTag = "Meezan / HBL / Alfalah / UBL",
        description = "Instant 1Link / IBFT transfer to official Filyzon IBAN",
        category = PaymentCategory.BANK_TRANSFER,
        iconColor = 0xFF1565C0
    ),
    RAAST_INSTANT(
        displayName = "Raast Instant Payment (State Bank of Pakistan)",
        brandTag = "Raast ID",
        description = "Zero-fee instant transfer via official Raast Directory",
        category = PaymentCategory.BANK_TRANSFER,
        iconColor = 0xFF2E7D32
    ),
    SADAPAY(
        displayName = "SadaPay Personal / Business",
        brandTag = "SadaPay",
        description = "Modern numberless debit card / SadaPay account",
        category = PaymentCategory.DIGITAL_WALLET,
        iconColor = 0xFF00897B
    ),
    NAYAPAY(
        displayName = "NayaPay Digital Wallet",
        brandTag = "NayaPay",
        description = "Visa Debit / NayaPay Chat ID instant transfer",
        category = PaymentCategory.DIGITAL_WALLET,
        iconColor = 0xFFFF6D00
    ),
    FILYZON_COINS(
        displayName = "Filyzon Wallet Credits & Coins",
        brandTag = "Filyzon Balance",
        description = "Deduct directly from accumulated in-app credits",
        category = PaymentCategory.IN_APP_WALLET,
        iconColor = 0xFFFFB300
    )
}

data class PakistaniBankInfo(
    val bankName: String,
    val accountTitle: String,
    val iban: String,
    val branchCode: String,
    val isRaastEnabled: Boolean = true,
    val note: String
)

data class PaymentReceipt(
    val receiptNumber: String,
    val orderTitle: String,
    val amountPkr: Double,
    val feePkr: Double = 0.0,
    val paymentMethod: PaymentMethodType,
    val accountUsed: String,
    val referenceTid: String,
    val timestamp: String,
    val verificationHash: String,
    val status: String = "VERIFIED & CONFIRMED"
)

data class WalletTransaction(
    val id: String,
    val title: String,
    val type: TransactionType,
    val amountPkr: Double,
    val coinsDelta: Int = 0,
    val paymentMethod: String,
    val status: String = "Completed",
    val date: String,
    val adminSharePkr: Double = 0.0,
    val receiptId: String? = null
)

enum class TransactionType {
    DEPOSIT,
    WITHDRAWAL,
    VIP_SUBSCRIPTION,
    SHOP_PURCHASE,
    CREATOR_GIFT,
    POST_BOOST_AD,
    ADMIN_PROFIT_COLLECTION
}

data class GiftItem(
    val id: String,
    val name: String,
    val emoji: String,
    val costCoins: Int,
    val creatorSharePercent: Double = 70.0,
    val adminSharePercent: Double = 30.0 // Admin profit cut
)

data class AdminRevenueMetrics(
    val totalNetRevenuePkr: Double,
    val vipSubscriptionRevenuePkr: Double,
    val storeCommissionsPkr: Double,
    val giftsCommissionPkr: Double,
    val postBoostAdRevenuePkr: Double,
    val totalActiveVipMembers: Int,
    val totalStoreOrders: Int,
    val boostedPostsCount: Int,
    val commissionRatePercent: Double = 10.0
)
