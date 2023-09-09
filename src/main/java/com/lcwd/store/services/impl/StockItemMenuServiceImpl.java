package com.lcwd.store.services.impl;

import com.lcwd.store.dtos.PageableResponse;
import com.lcwd.store.dtos.ProductDto;
import com.lcwd.store.dtos.StockItemMenuDto;
import com.lcwd.store.entities.Product;
import com.lcwd.store.entities.StockItemMenu;
import com.lcwd.store.exceptions.ResourceNotFoundException;
import com.lcwd.store.helper.HelperUtils;
import com.lcwd.store.repositories.StockItemMenuRepository;
import com.lcwd.store.services.StockItemMenuService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class StockItemMenuServiceImpl implements StockItemMenuService {
    @Autowired
    private StockItemMenuRepository stockItemMenuRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Override
    public StockItemMenuDto saveFormData(StockItemMenuDto stockItemMenuDto) {
        return modelMapper.map(stockItemMenuRepository.save(modelMapper.map(stockItemMenuDto, StockItemMenu.class)),StockItemMenuDto.class);
    }

    @Override
    public String getLastAccountCode() {
        String lastAccountCode = stockItemMenuRepository.findLastAccountCode();
        return lastAccountCode != null ? lastAccountCode : "abc-000";
    }
//    public StockItemMenuDto navigateFormData(Long currentId, String action) {
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

    public StockItemMenuDto getLastFormData() {
        // Find the last record based on the highest ID
        return modelMapper.map(stockItemMenuRepository.findTopByOrderByAccountCodeDesc(),StockItemMenuDto.class);
    }

    public StockItemMenuDto getFirstFormData() {
        // Find the first record based on the lowest ID
        return modelMapper.map(stockItemMenuRepository.findTopByOrderByAccountCodeAsc(),StockItemMenuDto.class);
    }

    public StockItemMenuDto getPreviousFormData(String currentId) {
        // Find the previous record based on the current ID
        return modelMapper.map(stockItemMenuRepository.findPreviousFormData(currentId),StockItemMenuDto.class);
    }

    public StockItemMenuDto getNextFormData(String currentId) {
        // Find the next record based on the current ID
        return modelMapper.map(stockItemMenuRepository.findNextFormData(currentId),StockItemMenuDto.class);
    }

    public void softDelete(Long id) {
        StockItemMenu stockItemMenu = stockItemMenuRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stock item not found"));

        stockItemMenu.setDeleted(true);
        stockItemMenuRepository.save(stockItemMenu);
    }

    @Override
    public PageableResponse<StockItemMenuDto> getAllStockItems(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("asc")) ? (Sort.by(sortBy).ascending()) : (Sort.by(sortBy).descending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize,sort);
        Page<StockItemMenu> productPage = stockItemMenuRepository.findAllByIsDeletedFalse(pageable);
        PageableResponse<StockItemMenuDto> response = HelperUtils.getPageableResponse(productPage, StockItemMenuDto.class);
        return response;
    }

    @Override
    public StockItemMenuDto getStockItemMenuByCurrentId(String accountId) {
        return modelMapper.map(stockItemMenuRepository.findByAccountCode(accountId),StockItemMenuDto.class);
    }

    @Override
    public PageableResponse<StockItemMenuDto> searchProducts(String accountCode, int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("asc")) ? (Sort.by(sortBy).ascending()) : (Sort.by(sortBy).descending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize,sort);
        Page<StockItemMenu> productPage = stockItemMenuRepository.findByAccountCodeContaining(accountCode, pageable);
        PageableResponse<StockItemMenuDto> response = HelperUtils.getPageableResponse(productPage, StockItemMenuDto.class);
        return response;
    }
}
