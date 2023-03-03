package com.ramsoft.poc

import android.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.ramsoft.poc.data.RetrofitClient
import com.ramsoft.poc.presentation.MainActivityViewModel
import com.ramsoft.poc.repository.DogRepository
import com.ramsoft.poc.response.RandomDogResponse
import junit.framework.TestCase
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.util.concurrent.CountDownLatch

@RunWith(JUnit4::class)
class MainViewModelTest {

    @Mock
    var apiClient: RetrofitClient? = null

    @get:Rule
    var instantExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: MainActivityViewModel

    @Mock
    private lateinit var repository: DogRepository

    @Mock
    private lateinit var observer: Observer<RandomDogResponse>

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = MainActivityViewModel(repository)
        viewModel.dogImageResponse.observeForever(observer)
    }

    @Test
    fun testNull() {
        `when`(apiClient?.getRandomDogImage()).thenReturn(null)
        TestCase.assertNotNull(viewModel.dogImageResponse)
        viewModel.dogImageResponse.hasObservers().let { TestCase.assertTrue(it) }
    }

//    @Test
//    fun testApiFetchDataSuccess() {
//        val response = RandomDogResponse(dogImage = "", status = "")
//        `when`(apiClient?.getRandomDogImage()).thenReturn(Call<RandomDogResponse>)
//        viewModel.getRandomDogImage()
////        verify(observer).onChanged(NewsListViewState.LOADING_STATE)
////        verify(observer).onChanged(NewsListViewState.SUCCESS_STATE)
//    }

    @Test
    fun testApiResponse() {
        val latch = CountDownLatch(1)
        apiClient?.getRandomDogImage()
        try {
            latch.await()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

//    @Test
//    fun testApiFetchDataError() {
//        `when`(apiClient.fetchNews()).thenReturn(Single.error(Throwable("Api error")))
//        viewModel.fetchNews()
//        verify(observer).onChanged(NewsListViewState.LOADING_STATE)
//        verify(observer).onChanged(NewsListViewState.ERROR_STATE)
//    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        apiClient = null
    }
}