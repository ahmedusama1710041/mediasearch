package com.engine.mediasearch.interfaces.rest;

import com.engine.mediasearch.interfaces.dto.SearchHistoryDto;
import com.engine.mediasearch.model.entity.SearchHistory;
import com.engine.mediasearch.service.SearchHistoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/search")
@AllArgsConstructor
public class SearchHistoryController {

    private final SearchHistoryService searchHistoryService;

    /**
     * (c.a) Save a new search query for the authenticated user.
     * POST /api/search/save
     */
    @PostMapping("/save")
    public ResponseEntity<SearchHistory> saveSearch(@RequestBody SearchHistoryDto dto, Principal principal) {
        Long userId = getUserIdFromPrincipal(principal);
        SearchHistory saved = searchHistoryService.saveSearch(userId, dto);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    /**
     * (c.b) Retrieve saved searches for the authenticated user.
     * GET /api/search/history
     */
    @GetMapping("/history")
    public ResponseEntity<List<SearchHistory>> getSearchHistory(Principal principal) {
        Long userId = getUserIdFromPrincipal(principal);
        List<SearchHistory> history = searchHistoryService.getSearchHistory(userId);
        return new ResponseEntity<>(history, HttpStatus.OK);
    }

    /**
     * (c.c) Delete a specific search query for the authenticated user.
     * DELETE /api/search/history/{id}
     */
    @DeleteMapping("/history/{id}")
    public ResponseEntity<Void> deleteSearch(@PathVariable("id") Long searchId, Principal principal) {
        Long userId = getUserIdFromPrincipal(principal);
        searchHistoryService.deleteSearch(userId, searchId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Helper method to convert Principal to userId.
     * In a real application, convert the username (principal.getName()) into the actual user ID.
     * For demonstration, we assume the principal name is numeric.
     */
    private Long getUserIdFromPrincipal(Principal principal) {
        try {
            return Long.parseLong(principal.getName());
        } catch (NumberFormatException e) {
            // If not numeric, you might need to look up the user from the database.
            // For demonstration, return a dummy user id.
            return 1L;
        }
    }
}
