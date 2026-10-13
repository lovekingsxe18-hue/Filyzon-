package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Filyzon", appName)
  }

  @Test
  fun `test granting green tick with authorized admin UUID succeeds`() {
    val repo = com.example.data.repository.FilyzonRepository()
    val targetUser = repo.registeredProfiles.value.first { it.id == "usr_001" }
    assertEquals(false, targetUser.isVerified)

    val (success, message) = repo.verifyUserWithEdgeFunction("usr_001", grant = true)
    assertEquals(true, success)

    val updatedUser = repo.registeredProfiles.value.first { it.id == "usr_001" }
    assertEquals(true, updatedUser.isVerified)
    assertEquals(true, repo.currentUser.value.isVerified)
  }

  @Test
  fun `test admin access required check when admin mode is disabled`() {
    val repo = com.example.data.repository.FilyzonRepository()
    repo.setAdminSessionActive(false)

    val (success, message) = repo.verifyUserWithEdgeFunction("usr_004", grant = true)
    assertEquals(false, success)
    assert(message.contains("Admin access required"))

    val targetUser = repo.registeredProfiles.value.first { it.id == "usr_004" }
    assertEquals(false, targetUser.isVerified)
  }

  @Test
  fun `test revoking green tick resets badge status`() {
    val repo = com.example.data.repository.FilyzonRepository()
    // First verify
    repo.verifyUserWithEdgeFunction("usr_004", grant = true)
    assertEquals(true, repo.registeredProfiles.value.first { it.id == "usr_004" }.isVerified)

    // Now revoke
    val (success, message) = repo.verifyUserWithEdgeFunction("usr_004", grant = false)
    assertEquals(true, success)
    assertEquals(false, repo.registeredProfiles.value.first { it.id == "usr_004" }.isVerified)
  }
}

