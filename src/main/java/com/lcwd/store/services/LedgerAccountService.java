package com.lcwd.store.services;

import com.lcwd.store.dtos.LedgerAccountDto;
import com.lcwd.store.dtos.PageableResponse;

public interface LedgerAccountService {
    LedgerAccountDto saveFormData(LedgerAccountDto LedgerAccountDto);

    String getLastAccountCode();

    LedgerAccountDto getLastFormData();

    LedgerAccountDto getFirstFormData();

    LedgerAccountDto getPreviousFormData(String currentId);

    LedgerAccountDto getNextFormData(String currentId);

    void softDelete(Long id);

    PageableResponse<LedgerAccountDto> getAllStockItems(int pageNumber, int pageSize, String sortBy, String sortDir);

    LedgerAccountDto getLedgerAccountByCurrentId(String accountId);

    PageableResponse<LedgerAccountDto> searchProducts(String accountCode, int pageNumber, int pageSize, String sortBy, String sortDir);
}
