package com.engine.mediasearch.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "search_history")
@Getter
@Setter
@NoArgsConstructor
public class SearchHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // For simplicity, we store the userId as a Long.
    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String query;

    // Optional filters as a JSON string or plain text.
    private String filters;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    public SearchHistory(Long userId, String query, String filters) {
        this.userId = userId;
        this.query = query;
        this.filters = filters;
        this.timestamp = LocalDateTime.now();
    }
}