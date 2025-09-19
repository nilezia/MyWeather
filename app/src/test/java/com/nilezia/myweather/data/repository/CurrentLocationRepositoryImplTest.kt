package com.nilezia.myweather.data.repository


import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import io.mockk.CapturingSlot
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.coroutines.cancellation.CancellationException
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MockKExtension::class)
class CurrentLocationRepositoryImplTest {

    @MockK
    private lateinit var mockFusedLocationProviderClient: FusedLocationProviderClient

    @MockK
    private lateinit var mockLocationTask: Task<Location>

    private lateinit var currentLocationRepository: CurrentLocationRepositoryImpl

    // ประกาศ slot เป็น property ของ class และ initialize ที่นี่
    private val successListenerSlot = slot<OnSuccessListener<Location>>()
    private val failureListenerSlot = slot<OnFailureListener>()

    @BeforeEach
    fun setUp() {
        println("DEBUG: setUp - Start")
        currentLocationRepository = CurrentLocationRepositoryImpl(mockFusedLocationProviderClient)
        println("DEBUG: setUp - mockFusedLocationProviderClient is $mockFusedLocationProviderClient")
        println("DEBUG: setUp - currentLocationRepository is $currentLocationRepository")

        // ไม่ต้อง initialize slot ใหม่ใน setUp ถ้าประกาศและ init เป็น property แล้ว
        // successListenerSlot = slot()
        // failureListenerSlot = slot()

        every { mockFusedLocationProviderClient.lastLocation } returns mockLocationTask
        println("DEBUG: setUp - mockLocationTask is $mockLocationTask (used in fusedClient.lastLocation)")

        // Capture listener ที่นี่
        every { mockLocationTask.addOnSuccessListener(capture(successListenerSlot)) } returns mockLocationTask
        println("DEBUG: setUp - Configured addOnSuccessListener to capture with slot: $successListenerSlot")

        every { mockLocationTask.addOnFailureListener(capture(failureListenerSlot)) } returns mockLocationTask
        println("DEBUG: setUp - Configured addOnFailureListener to capture with slot: $failureListenerSlot")
        println("DEBUG: setUp - End")
    }

    @Nested
    @DisplayName("getCurrentLocation tests")
    inner class GetCurrentLocationTests {

        @Test
        @DisplayName("should return Location when lastLocation is successful and location is not null")
        fun `should return Location on success`() = runTest {
            println("DEBUG: Test Start - should return Location on success (FINAL)")
            val mockLocationResult = mockk<Location>()
            var actualLocation: Location? = null
            var actualException: Throwable? = null

            println("DEBUG: Test - currentLocationRepository is $currentLocationRepository")

            val job = launch {
                println("DEBUG: Test - INSIDE LAUNCH BLOCK - START (FINAL)")
                try {
                    println("DEBUG: Test - Calling currentLocationRepository.getCurrentLocation()")
                    actualLocation = currentLocationRepository.getCurrentLocation()
                    // Log นี้จะแสดงผลหลังจาก coroutine resume แล้ว
                    println("DEBUG: Test - After getCurrentLocation() call, actualLocation (post-resume): $actualLocation")
                } catch (e: Throwable) {
                    // คาดหวังว่า Exception นี้จะไม่เกิดขึ้นแล้ว
                    println("DEBUG: Test - Exception in launch (FINAL - SHOULD NOT HAPPEN): ${e.message}")
                    actualException = e
                }
                println("DEBUG: Test - INSIDE LAUNCH BLOCK - END (FINAL)")
            }

            println("DEBUG: Test - After launch block declared")

            println("DEBUG: Test - BEFORE yield()")
            kotlinx.coroutines.yield()
            println("DEBUG: Test - AFTER yield()")

            println("DEBUG: Test - Verifying mockFusedLocationProviderClient.lastLocation...")
            verify(exactly = 1) { mockFusedLocationProviderClient.lastLocation }
            println("DEBUG: Test - VERIFIED: mockFusedLocationProviderClient.lastLocation")

            println("DEBUG: Test - Verifying mockLocationTask.addOnSuccessListener...")
            verify(exactly = 1) { mockLocationTask.addOnSuccessListener(capture(successListenerSlot)) }
            println("DEBUG: Test - VERIFIED & ATTEMPTED CAPTURE: mockLocationTask.addOnSuccessListener")

            assertTrue(successListenerSlot.isCaptured, "Success listener was not captured.")
            println("DEBUG: Test - ASSERTION PASSED: successListenerSlot IS captured.")

            // --- เอาคอมเมนต์ออก: จำลอง Callback ---
            println("DEBUG: Test - Simulating FusedLocationProvider success callback...")
            successListenerSlot.captured.onSuccess(mockLocationResult)
            println("DEBUG: Test - Called successListener.captured.onSuccess() with $mockLocationResult")
            // --- สิ้นสุดส่วนที่เอาคอมเมนต์ออก ---

            println("DEBUG: Test - BEFORE job.join()")
            job.join() // <<--- **ไม่ควรจะ Timeout แล้ว!**
            println("DEBUG: Test - AFTER job.join()")

            // --- เอาคอมเมนต์ออก: Assertions สุดท้าย ---
            println("DEBUG: Test - Performing final assertions...")
            assertNull(actualException, "Exception should be null on successful location retrieval. Got: ${actualException?.message}")
            assertNotNull(actualLocation, "Location should not be null on success")
            assertEquals(mockLocationResult, actualLocation, "Returned location does not match expected location")
            println("DEBUG: Test - FINAL Assertions PASSED")
            // --- สิ้นสุดส่วนที่เอาคอมเมนต์ออก ---

            println("DEBUG: Test - (FINAL) finished.")
        }

        @Test
        @DisplayName("should throw Exception when lastLocation is successful but location is null")
        fun `should throw Exception when location is null`() = runTest {
            println("DEBUG: Test Start - should throw Exception when location is null")
            var actualException: Throwable? = null

            val job = launch {
                println("DEBUG: Test - Inside launch (null location) - START")
                try {
                    currentLocationRepository.getCurrentLocation()
                } catch (e: Throwable) {
                    println("DEBUG: Test - Exception in launch (null location): ${e.message}")
                    actualException = e
                }
                println("DEBUG: Test - Inside launch (null location) - END")
            }
            println("DEBUG: Test - After launch (null location) declared")
            kotlinx.coroutines.yield()
            verify(exactly = 1) { mockFusedLocationProviderClient.lastLocation }
            verify(exactly = 1) { mockLocationTask.addOnSuccessListener(capture(successListenerSlot)) }
            assertTrue(successListenerSlot.isCaptured, "Success listener (for null location) was not captured.")

            successListenerSlot.captured.onSuccess(null) // Simulate location is null
            println("DEBUG: Test - Called successListener.captured.onSuccess(null)")

            job.join()
            println("DEBUG: Test - After job.join (null location)")

            assertNotNull(actualException, "Exception should have been thrown when location is null")
            assertInstanceOf(Exception::class.java, actualException)
            assertEquals("Location not available", actualException?.message)
            println("DEBUG: Test - Assertions PASSED (null location)")
        }

    }
}