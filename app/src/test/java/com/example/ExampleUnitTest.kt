package com.example

import com.example.data.model.GiftItem
import com.example.data.model.PaymentMethodType
import com.example.data.model.PostCategory
import com.example.data.repository.FilyzonRepository
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ExampleUnitTest {

    private lateinit var repository: FilyzonRepository

    @Before
    fun setUp() {
        repository = FilyzonRepository()
    }

    @Test
    fun testVipSubscriptionFlow_benefitsAdmin() {
        val initialAdminRevenue = repository.adminMetrics.value.vipSubscriptionRevenuePkr
        val plan = repository.vipPlans.first { it.id == "vip_gold" }

        val receipt = repository.subscribeVip(
            plan = plan,
            paymentMethod = PaymentMethodType.EASYPAISA,
            accountIdentifier = "0312-1234567"
        )

        assertNotNull(receipt)
        assertTrue(receipt.receiptNumber.startsWith("FZ-REC-"))
        assertTrue(repository.currentUser.value.isVip)
        assertEquals("GOLD", repository.currentUser.value.vipTier)

        val updatedAdminRevenue = repository.adminMetrics.value.vipSubscriptionRevenuePkr
        assertEquals(initialAdminRevenue + plan.pricePkr, updatedAdminRevenue, 0.01)
    }

    @Test
    fun testPostBoost_chargesUserAndBenefitsAdmin() {
        val initialBoostRevenue = repository.adminMetrics.value.postBoostAdRevenuePkr
        val initialBalance = repository.currentUser.value.walletBalancePkr

        val success = repository.addPost(
            caption = "Test boosted photo post",
            imageUrl = "https://example.com/test.jpg",
            category = PostCategory.ENTERTAINMENT,
            tags = listOf("#Test", "#Boost"),
            isVipExclusive = false,
            boostWithAdminFee = true,
            location = "Islamabad, Pakistan 🇵🇰",
            filterName = "Vivid",
            unlockPriceCoins = 0
        )

        assertTrue(success)
        assertEquals(initialBalance - 350.0, repository.currentUser.value.walletBalancePkr, 0.01)
        assertEquals(initialBoostRevenue + 350.0, repository.adminMetrics.value.postBoostAdRevenuePkr, 0.01)
    }

    @Test
    fun testSendGift_rewardsCreatorAndTakesAdminFee() {
        val initialAdminGiftRevenue = repository.adminMetrics.value.giftsCommissionPkr
        val testPost = repository.posts.value.first()
        val gift = GiftItem("test_gift", "Test Falcon", "🦅", 100, 70.0, 30.0)

        val success = repository.sendGiftToPost(testPost.id, gift)

        assertTrue(success)
        val expectedAdminCut = (100 * 0.5) * 0.30
        assertEquals(initialAdminGiftRevenue + expectedAdminCut, repository.adminMetrics.value.giftsCommissionPkr, 0.01)
    }

    @Test
    fun testProductPurchase_generatesAdminCommissionAndReceipt() {
        val initialCommission = repository.adminMetrics.value.storeCommissionsPkr
        val product = repository.products.value.first()

        val receipt = repository.purchaseProduct(
            product = product,
            paymentMethod = PaymentMethodType.JAZZCASH,
            accountIdentifier = "0300-1234567"
        )

        assertNotNull(receipt)
        assertTrue(receipt.receiptNumber.startsWith("FZ-REC-"))
        val expectedCut = product.pricePkr * (product.adminCommissionPercent / 100.0)
        assertEquals(initialCommission + expectedCut, repository.adminMetrics.value.storeCommissionsPkr, 0.01)
    }
}
