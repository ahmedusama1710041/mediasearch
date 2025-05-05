package com.engine.mediasearch.service;

import com.engine.mediasearch.interfaces.dto.SearchHistoryDto;
import com.engine.mediasearch.model.entity.SearchHistory;
import com.engine.mediasearch.model.repository.SearchHistoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SearchHistoryService {

    private final SearchHistoryRepository searchHistoryRepository;

    /**
     * Saves a new search query for a given user.
     */
    public SearchHistory saveSearch(Long userId, SearchHistoryDto dto) {
        SearchHistory searchHistory = new SearchHistory(userId, dto.getQuery(), dto.getFilters());
        return searchHistoryRepository.save(searchHistory);
    }

    /**
     * Retrieves all search history for a given user.
     */
    public List<SearchHistory> getSearchHistory(Long userId) {
        return searchHistoryRepository.findByUserIdOrderByTimestampDesc(userId);
    }

    /**
     * Deletes a specific search query for a given user.
     */
    public void deleteSearch(Long userId, Long searchId) {
        searchHistoryRepository.deleteByIdAndUserId(searchId, userId);
    }
}