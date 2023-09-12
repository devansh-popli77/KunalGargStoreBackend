package com.lcwd.store.services.impl;

import com.lcwd.store.dtos.LedgerAccountDto;
import com.lcwd.store.dtos.PageableResponse;
import com.lcwd.store.dtos.LedgerAccountDto;
import com.lcwd.store.entities.LedgerAccount;
import com.lcwd.store.exceptions.ResourceNotFoundException;
import com.lcwd.store.helper.HelperUtils;
import com.lcwd.store.repositories.LedgerAccountRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class LedgerAccountServiceImpl implements com.lcwd.store.services.LedgerAccountService {
    @Autowired
    private LedgerAccountRepository ledgerAccountRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Override
    public LedgerAccountDto saveFormData(LedgerAccountDto LedgerAccountDto) {
        return modelMapper.map(ledgerAccountRepository.save(modelMapper.map(LedgerAccountDto, LedgerAccount.class)),LedgerAccountDto.class);
    }

    @Override
    public String getLastAccountCode() {
        String lastAccountCode = ledgerAccountRepository.findMaxAccountCode();
        return lastAccountCode != null ? lastAccountCode : "mnc-000";
    }
//    public LedgerAccountDto navigateFormData(Long currentId, String action) {
//        if ("last".equals(action)) {
//            return getLastFormData();
//        } else if ("first".equals(action)) {
//            return getFirstFormData();
//        } else if ("previous".equals(action)) {
//            return getPreviousFormData(currentId);
//        } else if ("next".equals(action)) {
//            return getNextFormData(currentId);
//        } else {
//            throw new IllegalArgumentException("Invalid navigation action: " + action);
//        }
//    }

    @Override
    public LedgerAccountDto getLastFormData() {
        // Find the last record based on the highest ID
        return modelMapper.map(ledgerAccountRepository.findTopByOrderByAccountCodeDesc(),LedgerAccountDto.class);
    }

    @Override
    public LedgerAccountDto getFirstFormData() {
        // Find the first record based on the lowest ID
        return modelMapper.map(ledgerAccountRepository.findTopByOrderByAccountCodeAsc(),LedgerAccountDto.class);
    }

    @Override
    public LedgerAccountDto getPreviousFormData(String currentId) {
        // Find the previous record based on the current ID
        return modelMapper.map(ledgerAccountRepository.findPreviousFormData(currentId),LedgerAccountDto.class);
    }

    @Override
    public LedgerAccountDto getNextFormData(String currentId) {
        // Find the next record based on the current ID
        return modelMapper.map(ledgerAccountRepository.findNextFormData(currentId),LedgerAccountDto.class);
    }

    @Override
    public void softDelete(Long id) {
        LedgerAccount LedgerAccount = ledgerAccountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ledger item not found"));

        LedgerAccount.setDeleted(true);
        ledgerAccountRepository.save(LedgerAccount);
    }

    @Override
    public PageableResponse<LedgerAccountDto> getAllStockItems(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("asc")) ? (Sort.by(sortBy).ascending()) : (Sort.by(sortBy).descending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize,sort);
        Page<LedgerAccount> productPage = ledgerAccountRepository.findAllByIsDeletedFalse(pageable);
        PageableResponse<LedgerAccountDto> response = HelperUtils.getPageableResponse(productPage, LedgerAccountDto.class);
        return response;
    }

    @Override
    public LedgerAccountDto getLedgerAccountByCurrentId(String accountId) {
        return modelMapper.map(ledgerAccountRepository.findByAccountCode(accountId),LedgerAccountDto.class);
    }

    @Override
    public PageableResponse<LedgerAccountDto> searchProducts(String accountCode, int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("asc")) ? (Sort.by(sortBy).ascending()) : (Sort.by(sortBy).descending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize,sort);
        Page<LedgerAccount> productPage = ledgerAccountRepository.findByAccountCodeContaining(accountCode, pageable);
        PageableResponse<LedgerAccountDto> response = HelperUtils.getPageableResponse(productPage, LedgerAccountDto.class);
        return response;
    }
}
