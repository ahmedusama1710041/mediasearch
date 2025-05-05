package com.engine.mediasearch.service;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@AllArgsConstructor
public class MediaSearchService {

    private final RestTemplate restTemplate;

    /**
     * Calls the Openverse API to search for media.
     * If mediaType equals "audio", uses the audio endpoint; otherwise, uses images.
     *
     * @param query     The search query.
     * @param license   (Optional) A license filter.
     * @param mediaType Either "images" (default) or "audio".
     * @return The JSON response from Openverse API.
     */
    public Object searchMedia(String query, String license, String mediaType) {
        String baseUrl;
        if ("audio".equalsIgnoreCase(mediaType)) {
            baseUrl = "https://api.openverse.org/v1/audio/";
        } else {
            baseUrl = "https://api.openverse.org/v1/images/";
        }

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("q", query);
        if (license != null && !license.trim().isEmpty()) {
            builder.queryParam("license", license);
        }

        String url = builder.toUriString();
        ResponseEntity<Object> response = restTemplate.getForEntity(url, Object.class);
        return response.getBody();
    }
}
