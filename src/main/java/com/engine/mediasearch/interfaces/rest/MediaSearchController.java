package com.engine.mediasearch.interfaces.rest;

import com.engine.mediasearch.interfaces.dto.SearchHistoryDto;
import com.engine.mediasearch.service.MediaSearchService;
import com.engine.mediasearch.service.SearchHistoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/media")
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class MediaSearchController {

    private final MediaSearchService mediaSearchService;
    private final SearchHistoryService searchHistoryService;

    /**
     * Endpoint for searching media via Openverse.
     * Saves the search query (with filters) in search history and returns the Openverse API response.
     *
     * URL: GET /api/media/search?q={query}&license={license}&mediaType={mediaType}
     */
    @GetMapping("/search")
    public ResponseEntity<?> searchMedia(
            @RequestParam("q") String query,
            @RequestParam(value = "license", required = false) String license,
            @RequestParam(value = "mediaType", required = false, defaultValue = "audio") String mediaType,
            Principal principal) {

        // Retrieve user ID from Principal (update this helper as per your authentication system)
        Long userId = getUserIdFromPrincipal(principal);

        // Prepare filters string (for storage purposes)
        String filters = "mediaType=" + mediaType;
        if (license != null && !license.trim().isEmpty()) {
            filters += ",license=" + license;
        }

        // Save search history entry
        searchHistoryService.saveSearch(userId, new SearchHistoryDto(query, filters));

        // Call the Openverse API
        Object results = mediaSearchService.searchMedia(query, license, mediaType);
        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    /**
     * Helper method to extract userId from Principal.
     * For demonstration, assumes the principal name is numeric.
     * Replace with proper user lookup in production.
     */
    private Long getUserIdFromPrincipal(Principal principal) {
        try {
            return Long.parseLong(principal.getName());
        } catch (NumberFormatException e) {
            // In a real scenario, look up the user by username.
            // For demonstration, return a dummy user ID.
            return 1L;
        }
    }
}
