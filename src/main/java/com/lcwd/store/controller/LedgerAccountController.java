package com.lcwd.store.controller;

import com.lcwd.store.dtos.PageableResponse;
import com.lcwd.store.dtos.LedgerAccountDto;
import com.lcwd.store.entities.LedgerAccount;
import com.lcwd.store.repositories.LedgerAccountRepository;
import com.lcwd.store.services.impl.LedgerAccountServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ledger-accounts")
public class LedgerAccountController {

    @Autowired
    private LedgerAccountServiceImpl ledgerAccountService;

    // API to save ledger account data
//    @PostMapping("/save")
//    public ResponseEntity<LedgerAccount> saveLedgerAccount(@RequestBody LedgerAccount ledgerAccount) {
//        LedgerAccount savedAccount = ledgerAccountRepository.save(ledgerAccount);
//        return ResponseEntity.ok(savedAccount);
//    }
//
//    // API to get the next available account code
//    @GetMapping("/next-account-code")
//    public ResponseEntity<String> getNextAccountCode() {
//        String maxAccountCode = ledgerAccountRepository.findMaxAccountCode();
//        int nextAccountNumber = Integer.parseInt(maxAccountCode) + 1;
//        String nextAccountCode = String.format("%04d", nextAccountNumber); // Assuming a 4-digit account code
//        return ResponseEntity.ok(nextAccountCode);
//    }


    @PostMapping("/save")
    public ResponseEntity<LedgerAccountDto> saveFormData(@RequestBody LedgerAccountDto formData) {
        LedgerAccountDto result = ledgerAccountService.saveFormData(formData);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/lastAccountCode")
    public String getLastAccountCode() {
        String lastAccountCode = ledgerAccountService.getLastAccountCode();
        return lastAccountCode;
    }


    @GetMapping("/get")
    public ResponseEntity<LedgerAccountDto> navigateFormData(
            @RequestParam String accountId) {
        LedgerAccountDto result = ledgerAccountService.getLedgerAccountByCurrentId(accountId);

        if (result != null) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/navigate/action")
    public ResponseEntity<LedgerAccountDto> navigateFormData(
            @RequestParam String currentId,
            @RequestParam String action) {
        LedgerAccountDto result = null;

        if ("last".equals(action)) {
            result = ledgerAccountService.getLastFormData();
        } else if ("first".equals(action)) {
            result = ledgerAccountService.getFirstFormData();
        } else if ("previous".equals(action)) {
            result = ledgerAccountService.getPreviousFormData(currentId);
        } else if ("next".equals(action)) {
            result = ledgerAccountService.getNextFormData(currentId);
        }

        if (result != null) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> softDeleteStockItem(@PathVariable Long id) {
        ledgerAccountService.softDelete(id);
        return ResponseEntity.ok("Stock item soft deleted successfully");
    }

    @GetMapping("/stock-item-menu/all")
    public ResponseEntity<PageableResponse<LedgerAccountDto>> getAllStockItems(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
                                                                               @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,

                                                                               @RequestParam(value = "sortBy", defaultValue = "name", required = false)
                                                                               String sortBy,
                                                                               @RequestParam(value = "sortDir", defaultValue = "asc", required = false)
                                                                               String sortDir) {
        PageableResponse<LedgerAccountDto> stockItems = ledgerAccountService.getAllStockItems(pageNumber, pageSize, sortBy, sortDir);
        return ResponseEntity.ok(stockItems);
    }

    @GetMapping("/stock-item-menu/all/{accountCode}")
    public ResponseEntity<PageableResponse<LedgerAccountDto>> getAllStockItems(
            @PathVariable String accountCode,
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,

            @RequestParam(value = "sortBy", defaultValue = "name", required = false)
            String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false)
            String sortDir) {
        PageableResponse<LedgerAccountDto> stockItems = ledgerAccountService.searchProducts(accountCode, pageNumber, pageSize, sortBy, sortDir);
        return ResponseEntity.ok(stockItems);
    }
}
