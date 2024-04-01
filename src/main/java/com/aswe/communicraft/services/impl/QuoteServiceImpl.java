package com.aswe.communicraft.services.impl;

import com.aswe.communicraft.models.dto.QuoteResponse;
import com.aswe.communicraft.services.ExternalAPIService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
/*
 * The QuoteService class provides Quote-related services.
 */
public class QuoteServiceImpl implements ExternalAPIService {
    private static final String API_URL = "https://zenquotes.io/api/random/";
    public QuoteResponse getRandomQuote() {
        RestTemplate restTemplate = new RestTemplate();
        QuoteResponse[] quoteResponses = restTemplate.getForObject(API_URL, QuoteResponse[].class);
        if (quoteResponses != null && quoteResponses.length > 0) {
            return quoteResponses[0];
        } else {
            return null;
        }
    }
}
