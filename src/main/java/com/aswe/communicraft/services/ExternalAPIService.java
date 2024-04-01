package com.aswe.communicraft.services;

import com.aswe.communicraft.models.dto.QuoteResponse;

public interface ExternalAPIService {
    QuoteResponse getRandomQuote();
}
