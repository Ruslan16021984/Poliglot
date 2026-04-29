package com.carbit3333333.oiiglot_bulgary.data.billing

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesResponseListener
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import com.android.billingclient.api.PendingPurchasesParams
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface BillingFacade {
    val hasFullCourseAccessFlow: Flow<Boolean>
    val isPurchaseFlowAvailable: Boolean

    suspend fun launchFullCoursePurchase(activity: Activity): Boolean
    suspend fun restorePurchases()
    suspend fun revokeFullCourseAccess()
}

class LocalBillingFacade(
    context: Context
) : BillingFacade, PurchasesUpdatedListener {

    private val appContext = context.applicationContext
    private val purchaseAccessStore = PurchaseAccessStore(appContext)
    private val billingScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val billingClient: BillingClient by lazy {
        BillingClient.newBuilder(appContext)
            .setListener(this)
            .enablePendingPurchases(
                PendingPurchasesParams.newBuilder()
                    .enableOneTimeProducts()
                    .build()
            )
            .build()
    }

    override val hasFullCourseAccessFlow: Flow<Boolean> =
        purchaseAccessStore.hasFullCourseAccessFlow

    override val isPurchaseFlowAvailable: Boolean = true

    override suspend fun launchFullCoursePurchase(activity: Activity): Boolean {
        if (!ensureConnected()) return false

        val productDetails = loadFullCourseProductDetails() ?: return false
        val productDetailsParams = BillingFlowParams.ProductDetailsParams.newBuilder()
            .setProductDetails(productDetails)
            .build()
        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(listOf(productDetailsParams))
            .build()

        val billingResult = withContext(Dispatchers.Main) {
            billingClient.launchBillingFlow(activity, billingFlowParams)
        }

        return billingResult.responseCode == BillingClient.BillingResponseCode.OK
    }

    override suspend fun restorePurchases() {
        if (!ensureConnected()) return

        val purchases = queryInAppPurchases()
        val matchingPurchases = purchases.filter { purchase ->
            purchase.products.contains(FULL_COURSE_PRODUCT_ID) &&
                purchase.purchaseState == Purchase.PurchaseState.PURCHASED
        }

        if (matchingPurchases.isEmpty()) {
            purchaseAccessStore.setFullCourseAccess(false)
            return
        }

        matchingPurchases.forEach { purchase ->
            handleCompletedPurchase(purchase)
        }
    }

    override suspend fun revokeFullCourseAccess() {
        purchaseAccessStore.setFullCourseAccess(false)
    }

    override fun onPurchasesUpdated(
        billingResult: BillingResult,
        purchases: MutableList<Purchase>?
    ) {
        if (billingResult.responseCode != BillingClient.BillingResponseCode.OK || purchases == null) {
            return
        }

        purchases.forEach { purchase ->
            billingScope.launch {
                handleCompletedPurchase(purchase)
            }
        }
    }

    private suspend fun handleCompletedPurchase(purchase: Purchase) {
        if (!purchase.products.contains(FULL_COURSE_PRODUCT_ID)) return
        if (purchase.purchaseState != Purchase.PurchaseState.PURCHASED) return

        if (!purchase.isAcknowledged) {
            val acknowledged = acknowledgePurchase(purchase)
            if (!acknowledged) return
        }

        purchaseAccessStore.setFullCourseAccess(true)
    }

    private suspend fun ensureConnected(): Boolean {
        if (billingClient.isReady) return true

        val connectionReady = CompletableDeferred<Boolean>()
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                connectionReady.complete(
                    billingResult.responseCode == BillingClient.BillingResponseCode.OK
                )
            }

            override fun onBillingServiceDisconnected() {
                if (!connectionReady.isCompleted) {
                    connectionReady.complete(false)
                }
            }
        })

        return connectionReady.await()
    }

    private suspend fun loadFullCourseProductDetails(): ProductDetails? {
        val deferred = CompletableDeferred<ProductDetails?>()
        val product = QueryProductDetailsParams.Product.newBuilder()
            .setProductId(FULL_COURSE_PRODUCT_ID)
            .setProductType(BillingClient.ProductType.INAPP)
            .build()
        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(listOf(product))
            .build()

        billingClient.queryProductDetailsAsync(params) { billingResult, productDetailsResult ->
            val productDetails = if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                productDetailsResult.productDetailsList.firstOrNull()
            } else {
                null
            }
            deferred.complete(productDetails)
        }

        return deferred.await()
    }

    private suspend fun queryInAppPurchases(): List<Purchase> {
        val deferred = CompletableDeferred<List<Purchase>>()
        val params = QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.INAPP)
            .build()

        billingClient.queryPurchasesAsync(params, PurchasesResponseListener { billingResult, purchases ->
            deferred.complete(
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    purchases
                } else {
                    emptyList()
                }
            )
        })

        return deferred.await()
    }

    private suspend fun acknowledgePurchase(purchase: Purchase): Boolean {
        val deferred = CompletableDeferred<Boolean>()
        val params = AcknowledgePurchaseParams.newBuilder()
            .setPurchaseToken(purchase.purchaseToken)
            .build()

        billingClient.acknowledgePurchase(params) { billingResult ->
            deferred.complete(
                billingResult.responseCode == BillingClient.BillingResponseCode.OK
            )
        }

        return deferred.await()
    }

    private companion object {
        // Replace this with the real product id from Google Play Console.
        private const val FULL_COURSE_PRODUCT_ID = "full_course_access"
    }
}
