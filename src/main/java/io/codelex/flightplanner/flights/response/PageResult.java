package io.codelex.flightplanner.flights.response;

import lombok.Data;

import java.util.List;

@Data
public class PageResult<T> {

    private int page;
    private int totalItems;
    private List<T> items;

    public PageResult(int page, int totalItems, List<T> items) {
        this.page = page;
        this.totalItems = totalItems;
        this.items = items;
    }
}
