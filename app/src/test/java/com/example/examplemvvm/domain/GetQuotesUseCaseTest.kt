package com.example.examplemvvm.domain

import com.example.examplemvvm.data.QuoteRepository
import com.example.examplemvvm.data.database.entities.toDatabase
import com.example.examplemvvm.domain.model.Quote
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetQuotesUseCaseTest {

    @RelaxedMockK
    private lateinit var quoteRepository: QuoteRepository // Se declara pero no se inicializa de eso de encarga mockk

    lateinit var getQuotesUseCase: GetQuotesUseCase

    @Before
    fun onBefore() {
        MockKAnnotations.init(this, relaxed = true)
        getQuotesUseCase = GetQuotesUseCase(quoteRepository)
    }

    @Test
    fun `when the api doesnt return anything, then get values from the database`() = runBlocking {
        //Given
        coEvery { quoteRepository.getAllQuotesFromApi() } returns emptyList()

        //When
        getQuotesUseCase()

        //Then
        coVerify (exactly = 1) {
            quoteRepository.getAllQuotesFromDatabase()
        }

    }

    @Test
    fun `when the api return something, then get values from the api`() = runBlocking {
        //Given
        val myList = listOf(Quote("Testeando devolucion de lista de API", "Maxi Rodriguez"))
        coEvery { quoteRepository.getAllQuotesFromApi() } returns myList

        //When
        val result = getQuotesUseCase()

        //Then
        coVerify(exactly = 1) {
            quoteRepository.clearQuotes()
            quoteRepository.insertQuotes(any())
        }
        coVerify(exactly = 0) {
            quoteRepository.getAllQuotesFromDatabase()
        }
        assert(result == myList)
    }
}