package com.orfangenes.repo.ws.dto;

import com.orfangenes.repo.ws.entity.Analysis;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class PagedAnalysis {
    private int total;
    private List<Analysis> results;
    int totalPages;
}
