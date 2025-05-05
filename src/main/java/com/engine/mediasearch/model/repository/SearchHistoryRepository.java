package com.engine.mediasearch.model.repository;

import com.engine.mediasearch.model.entity.SearchHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {
    List<SearchHistory> findByUserIdOrderByTimestampDesc(Long userId);
    void deleteByIdAndUserId(Long id, Long userId);
}