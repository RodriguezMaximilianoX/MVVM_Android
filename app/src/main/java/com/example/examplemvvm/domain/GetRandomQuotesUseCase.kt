package com.example.examplemvvm.domain

import com.example.examplemvvm.data.model.QuoteModel
import com.example.examplemvvm.data.model.QuoteProvider
import javax.inject.Inject

class GetRandomQuotesUseCase @Inject constructor(private val quoteProvider : QuoteProvider) {

    operator fun invoke(): QuoteModel? {
        val quotes = quoteProvider.quotes
        if (!quotes.isNullOrEmpty()) {
            val randomNumber = (0..quotes.size - 1).random()
            return quotes[randomNumber]
        }
        return null
    }
}