package com.lcwd.store.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockItemMenuDto {
    private Long StockItemId;
    private String accountCode;
    private String name;
    private String openingStockQty;
    private String openingStockRs;
    private String groupName;
    private String purchaseRate;
    private String mrp;
    private String saleRate;
    private String totalGST;
    private String cgst;
    private String sgst;
    private String purchaseAccount;
    private String saleAccount;
    private String size;
    private String hsnCode;
    private String scheme;
    private String rateCalculate;
    private String clsStockIn;
    private String qtyInUnits;
    private String portalUOM;
    private String stockCalculate;
    private String typeOfGoods;
    private String stockValuation;
    private String qtyPerPcCase;
    private String minStockLevel;
    private String taxType;
    private String gstType;
}
