package com.lcwd.store.services;

import com.lcwd.store.dtos.PageableResponse;
import com.lcwd.store.dtos.StockItemMenuDto;

public interface StockItemMenuService {

    public StockItemMenuDto saveFormData(StockItemMenuDto stockItemMenuDto);

    String getLastAccountCode();

    StockItemMenuDto getPreviousFormData(String currentId);

    StockItemMenuDto getLastFormData();

    StockItemMenuDto getFirstFormData();

    StockItemMenuDto getNextFormData(String currentId);

    void softDelete(Long id);

    PageableResponse<StockItemMenuDto> getAllStockItems(int pageNumber, int pageSize, String sortBy, String sortDir);

    StockItemMenuDto getStockItemMenuByCurrentId(String accountId);

    public PageableResponse<StockItemMenuDto> searchProducts(String accountCode, int pageNumber, int pageSize, String sortBy, String sortDir);
}
