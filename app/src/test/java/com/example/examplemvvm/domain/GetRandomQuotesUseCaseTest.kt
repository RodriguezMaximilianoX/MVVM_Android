package com.example.examplemvvm.domain

import com.example.examplemvvm.data.QuoteRepository
import com.example.examplemvvm.domain.model.Quote
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetRandomQuotesUseCaseTest {

    @RelaxedMockK
    private lateinit var quoteRepository: QuoteRepository

    lateinit var getRandomQuotesUseCase: GetRandomQuotesUseCase

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        getRandomQuotesUseCase = GetRandomQuotesUseCase(quoteRepository)
    }

    @Test
    fun `when database is empty return null`() = runBlocking{
        //Given
        coEvery { quoteRepository.getAllQuotesFromDatabase() } returns emptyList()

        //When
        val result = getRandomQuotesUseCase()

        //Then
        assert(result == null)
    }

    @Test
    fun `when database is not null return quote`() = runBlocking {
        //Given
        val quoteResult = listOf(Quote("Prueba de cita en base de datos", "Maxi Rodriguez"))
        coEvery { quoteRepository.getAllQuotesFromDatabase() } returns quoteResult

        //When
        val result = getRandomQuotesUseCase()

        //Then
        assert(result == quoteResult.first())
    }

}