package com.lcwd.store.controller;

import com.lcwd.store.dtos.PageableResponse;
import com.lcwd.store.dtos.StockItemMenuDto;
import com.lcwd.store.services.StockItemMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class StockItemMenuController {

    @Autowired
    private StockItemMenuService stockItemMenuService;

    @PostMapping("/stockItemMenu")
    public ResponseEntity<StockItemMenuDto> saveFormData(@RequestBody StockItemMenuDto formData) {
        StockItemMenuDto result = stockItemMenuService.saveFormData(formData);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/lastAccountCode")
    public String getLastAccountCode() {
        String lastAccountCode = stockItemMenuService.getLastAccountCode();
        return lastAccountCode;
    }


    @GetMapping("/get/stock-item-menu")
    public ResponseEntity<StockItemMenuDto> navigateFormData(
            @RequestParam String accountId) {
        StockItemMenuDto result = stockItemMenuService.getStockItemMenuByCurrentId(accountId);

        if (result != null) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/navigate")
    public ResponseEntity<StockItemMenuDto> navigateFormData(
            @RequestParam String currentId,
            @RequestParam String action) {
        StockItemMenuDto result = null;

        if ("last".equals(action)) {
            result = stockItemMenuService.getLastFormData();
        } else if ("first".equals(action)) {
            result = stockItemMenuService.getFirstFormData();
        } else if ("previous".equals(action)) {
            result = stockItemMenuService.getPreviousFormData(currentId);
        } else if ("next".equals(action)) {
            result = stockItemMenuService.getNextFormData(currentId);
        }

        if (result != null) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/stockItemMenu/{id}")
    public ResponseEntity<String> softDeleteStockItem(@PathVariable Long id) {
        stockItemMenuService.softDelete(id);
        return ResponseEntity.ok("Stock item soft deleted successfully");
    }

    @GetMapping("/stock-item-menu/all")
    public ResponseEntity<PageableResponse<StockItemMenuDto>> getAllStockItems(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
                                                                               @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,

                                                                               @RequestParam(value = "sortBy", defaultValue = "name", required = false)
                                                                               String sortBy,
                                                                               @RequestParam(value = "sortDir", defaultValue = "asc", required = false)
                                                                               String sortDir) {
        PageableResponse<StockItemMenuDto> stockItems = stockItemMenuService.getAllStockItems(pageNumber, pageSize, sortBy, sortDir);
        return ResponseEntity.ok(stockItems);
    }

    @GetMapping("/stock-item-menu/all/{accountCode}")
    public ResponseEntity<PageableResponse<StockItemMenuDto>> getAllStockItems(
            @PathVariable String accountCode,
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,

            @RequestParam(value = "sortBy", defaultValue = "name", required = false)
            String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false)
            String sortDir) {
        PageableResponse<StockItemMenuDto> stockItems = stockItemMenuService.searchProducts(accountCode, pageNumber, pageSize, sortBy, sortDir);
        return ResponseEntity.ok(stockItems);
    }
}
