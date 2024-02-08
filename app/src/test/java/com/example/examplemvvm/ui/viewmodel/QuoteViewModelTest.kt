package com.example.examplemvvm.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.examplemvvm.domain.GetQuotesUseCase
import com.example.examplemvvm.domain.GetRandomQuotesUseCase
import com.example.examplemvvm.domain.model.Quote
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class QuoteViewModelTest {

    @RelaxedMockK
    private lateinit var getQuotesUseCase: GetQuotesUseCase

    @RelaxedMockK
    private lateinit var getRandomQuotesUseCase: GetRandomQuotesUseCase

    private lateinit var quoteViewModel: QuoteViewModel

    @get:Rule
    var rule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        quoteViewModel = QuoteViewModel(getQuotesUseCase, getRandomQuotesUseCase)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun onAfter() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when viewmodel is created at the first time, get all quotes and set the first value`() =
        runTest {
            //Given
            val quoteList = listOf(
                Quote("Prueba de cita 1", "Maxi Rodriguez"),
                Quote("Prueba de cita 2", "Maxi Rodriguez")
            )
            coEvery { getQuotesUseCase() } returns quoteList

            //When
            quoteViewModel.onCreate()

            //Then
            assert(quoteViewModel.quoteModel.value == quoteList.first())

        }

    @Test
    fun `when randomQuoteUseCase return a quote set on the livedata`() = runTest {
        //Given
        val quote = Quote("Nueva pruaba de cita", "Maxi")
        coEvery { getRandomQuotesUseCase() } returns quote

        //When
        quoteViewModel.randomQuote()

        //Then
        assert(quoteViewModel.quoteModel.value == quote)
    }

    @Test
    fun `if randomQuoteUseCase return null keep the last value`() = runTest {
        //Given
        val quote = Quote("Ya no se que poner en los ejemplos de citas", "Maxi")
        quoteViewModel.quoteModel.value = quote
        coEvery { getRandomQuotesUseCase() } returns null

        //When
        quoteViewModel.randomQuote()

        //Then
        assert(quoteViewModel.quoteModel.value == quote)
    }
}