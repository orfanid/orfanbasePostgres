package com.orfangenes.repo.ws.dto;

import lombok.Data;

import java.util.List;

@Data
public class PagedResults<T>{
    private long total;
    private List<T> data;
}
