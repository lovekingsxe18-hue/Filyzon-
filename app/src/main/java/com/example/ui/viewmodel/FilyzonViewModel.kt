package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.*
import com.example.data.repository.FilyzonRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class FilyzonTab {
    FEED,
    ENTERTAINMENT,
    CREATE,
    SHOP,
    VIP_WALLET,
    ADMIN_HUB
}

sealed class CheckoutTarget {
    data class ProductCheckout(val product: Product) : CheckoutTarget()
    data class VipCheckout(val plan: VipPlan) : CheckoutTarget()
    data class CoinReloadCheckout(val coins: Int, val pricePkr: Double) : CheckoutTarget()
    data class WalletDepositCheckout(val amountPkr: Double) : CheckoutTarget()
}

class FilyzonViewModel(
    private val repository: FilyzonRepository = FilyzonRepository()
) : ViewModel() {

    private val _currentTab = MutableStateFlow(FilyzonTab.FEED)
    val currentTab: StateFlow<FilyzonTab> = _currentTab.asStateFlow()

    private val _selectedCategory = MutableStateFlow<PostCategory?>(null)
    val selectedCategory: StateFlow<PostCategory?> = _selectedCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _activeCheckout = MutableStateFlow<CheckoutTarget?>(null)
    val activeCheckout: StateFlow<CheckoutTarget?> = _activeCheckout.asStateFlow()

    private val _activeReceipt = MutableStateFlow<PaymentReceipt?>(null)
    val activeReceipt: StateFlow<PaymentReceipt?> = _activeReceipt.asStateFlow()

    private val _giftDialogPost = MutableStateFlow<Post?>(null)
    val giftDialogPost: StateFlow<Post?> = _giftDialogPost.asStateFlow()

    private val _statusMessage = MutableStateFlow<String?>(null)
    val statusMessage: StateFlow<String?> = _statusMessage.asStateFlow()

    private val _selectedPublicProfile = MutableStateFlow<UserProfile?>(null)
    val selectedPublicProfile: StateFlow<UserProfile?> = _selectedPublicProfile.asStateFlow()

    private val _adminUserSearchQuery = MutableStateFlow("")
    val adminUserSearchQuery: StateFlow<String> = _adminUserSearchQuery.asStateFlow()

    private val _isUserListLoaded = MutableStateFlow(true)
    val isUserListLoaded: StateFlow<Boolean> = _isUserListLoaded.asStateFlow()

    val currentUser: StateFlow<UserProfile> = repository.currentUser
    val allPosts: StateFlow<List<Post>> = repository.posts
    val products: StateFlow<List<Product>> = repository.products
    val vipPlans: List<VipPlan> = repository.vipPlans
    val availableGifts: List<GiftItem> = repository.availableGifts
    val transactions: StateFlow<List<WalletTransaction>> = repository.transactions
    val adminMetrics: StateFlow<AdminRevenueMetrics> = repository.adminMetrics
    val officialBanks: List<PakistaniBankInfo> = repository.officialPakistaniBanks
    val receipts: StateFlow<Map<String, PaymentReceipt>> = repository.receipts

    val registeredProfiles: StateFlow<List<UserProfile>> = repository.registeredProfiles
    val verificationLogs: StateFlow<List<VerificationLog>> = repository.verificationLogs
    val adminUsers: StateFlow<Set<String>> = repository.adminUsers
    val currentAdminUuid: StateFlow<String> = repository.currentAdminUuid
    val isAdminSessionActive: StateFlow<Boolean> = repository.isAdminSessionActive

    val filteredAdminProfiles: StateFlow<List<UserProfile>> = combine(
        repository.registeredProfiles,
        _adminUserSearchQuery
    ) { profiles, query ->
        if (query.isBlank()) {
            profiles
        } else {
            profiles.filter {
                it.name.contains(query, ignoreCase = true) ||
                        it.handle.contains(query, ignoreCase = true) ||
                        it.authUuid.contains(query, ignoreCase = true)
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val filteredPosts: StateFlow<List<Post>> = combine(
        repository.posts,
        _selectedCategory,
        _searchQuery
    ) { posts, category, query ->
        posts.filter { post ->
            val matchCategory = category == null || post.category == category
            val matchQuery = query.isBlank() ||
                    post.caption.contains(query, ignoreCase = true) ||
                    post.authorName.contains(query, ignoreCase = true) ||
                    post.location.contains(query, ignoreCase = true) ||
                    post.tags.any { it.contains(query, ignoreCase = true) }
            matchCategory && matchQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun selectTab(tab: FilyzonTab) {
        _currentTab.value = tab
    }

    fun selectCategory(category: PostCategory?) {
        _selectedCategory.value = category
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun toggleLike(postId: String) {
        repository.toggleLikePost(postId)
    }

    fun openGiftDialog(post: Post) {
        _giftDialogPost.value = post
    }

    fun closeGiftDialog() {
        _giftDialogPost.value = null
    }

    fun sendGift(post: Post, gift: GiftItem) {
        val success = repository.sendGiftToPost(post.id, gift)
        if (success) {
            _statusMessage.value = "Gift ${gift.emoji} sent! Creator rewarded & platform fee added."
            closeGiftDialog()
        } else {
            _statusMessage.value = "Insufficient Filyzon Coins! Please reload your wallet."
        }
    }

    fun createPost(
        caption: String,
        imageUrl: String,
        category: PostCategory,
        tags: List<String>,
        isVipExclusive: Boolean,
        boostWithAdminFee: Boolean,
        location: String = "Lahore, Pakistan 🇵🇰",
        filterName: String = "Normal",
        unlockPriceCoins: Int = 0
    ) {
        val success = repository.addPost(
            caption = caption,
            imageUrl = imageUrl,
            category = category,
            tags = tags,
            isVipExclusive = isVipExclusive,
            boostWithAdminFee = boostWithAdminFee,
            location = location,
            filterName = filterName,
            unlockPriceCoins = unlockPriceCoins
        )
        if (success) {
            _statusMessage.value = if (boostWithAdminFee) {
                "Photo published & BOOSTED to 25k views! Rs. 350 paid to Admin."
            } else {
                "Photo successfully uploaded to Filyzon Feed!"
            }
            _currentTab.value = FilyzonTab.FEED
        } else {
            _statusMessage.value = "Insufficient balance for Post Boost! Please top up Rs. 350."
        }
    }

    fun openCheckout(target: CheckoutTarget) {
        _activeCheckout.value = target
    }

    fun closeCheckout() {
        _activeCheckout.value = null
    }

    fun processComprehensivePayment(
        paymentMethod: PaymentMethodType,
        accountIdentifier: String,
        referenceTid: String
    ) {
        val target = _activeCheckout.value ?: return

        val receipt: PaymentReceipt = when (target) {
            is CheckoutTarget.ProductCheckout -> {
                repository.purchaseProduct(target.product, paymentMethod, accountIdentifier, referenceTid)
            }
            is CheckoutTarget.VipCheckout -> {
                repository.subscribeVip(target.plan, paymentMethod, accountIdentifier, referenceTid)
            }
            is CheckoutTarget.CoinReloadCheckout -> {
                repository.buyCoins(target.coins, target.pricePkr, paymentMethod, accountIdentifier, referenceTid)
            }
            is CheckoutTarget.WalletDepositCheckout -> {
                repository.depositWalletFunds(target.amountPkr, paymentMethod, accountIdentifier, referenceTid)
            }
        }

        closeCheckout()
        _activeReceipt.value = receipt
        _statusMessage.value = "Payment Successful via ${paymentMethod.brandTag}! Receipt generated."
    }

    fun openReceiptByNumber(receiptId: String) {
        val receipt = repository.receipts.value[receiptId]
        if (receipt != null) {
            _activeReceipt.value = receipt
        }
    }

    fun closeReceipt() {
        _activeReceipt.value = null
    }

    fun withdrawAdminEarnings(amountPkr: Double, destinationBank: String) {
        val success = repository.withdrawAdminProfits(amountPkr, destinationBank)
        if (success) {
            _statusMessage.value = "Payout of Rs. ${amountPkr.toInt()} transferred to $destinationBank successfully!"
        } else {
            _statusMessage.value = "Requested amount exceeds total available admin revenue."
        }
    }

    // --- VERIFIED TICK & ADMIN PANEL ACTIONS ---

    fun refreshAdminUserList() {
        _isUserListLoaded.value = true
        _statusMessage.value = "Admin user list loaded successfully (via public.profiles & verified_users)."
    }

    fun updateAdminSearchQuery(query: String) {
        _adminUserSearchQuery.value = query
    }

    fun grantGreenTick(userId: String) {
        val (success, message) = repository.verifyUserWithEdgeFunction(userId, grant = true)
        _statusMessage.value = message
    }

    fun revokeGreenTick(userId: String) {
        val (success, message) = repository.verifyUserWithEdgeFunction(userId, grant = false)
        _statusMessage.value = message
    }

    fun toggleAdminSession(active: Boolean) {
        repository.setAdminSessionActive(active)
        _statusMessage.value = if (active) {
            "Admin session active (UUID verified in admin_users)."
        } else {
            "Switched to standard user mode. Edge Function will reject verification attempts."
        }
    }

    fun updateAdminUuid(uuid: String) {
        repository.setCurrentAdminUuid(uuid)
        _statusMessage.value = "Admin UUID set to: $uuid"
    }

    fun addAdminUuidToTable(uuid: String) {
        repository.addAdminUserUuid(uuid)
        _statusMessage.value = "UUID $uuid inserted into public.admin_users."
    }

    fun createNewProfile(username: String, name: String) {
        val profile = repository.addNewProfile(username, name)
        _statusMessage.value = "Profile created: ${profile.handle} (UUID: ${profile.authUuid})"
    }

    fun openPublicProfile(user: UserProfile) {
        _selectedPublicProfile.value = user
    }

    fun openPublicProfileByHandle(handle: String) {
        val profile = repository.registeredProfiles.value.find {
            it.handle.equals(handle, ignoreCase = true)
        } ?: UserProfile(
            id = "gen_${(100..999).random()}",
            name = handle.removePrefix("@").replace("_", " ").replaceFirstChar { it.uppercase() },
            handle = handle,
            avatarUrl = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=150&auto=format&fit=crop&q=80",
            bio = "Filyzon Creator 🦅",
            isVerified = true
        )
        _selectedPublicProfile.value = profile
    }

    fun closePublicProfile() {
        _selectedPublicProfile.value = null
    }

    fun clearStatusMessage() {
        _statusMessage.value = null
    }
}
