package com.lcwd.store.repositories;

import com.lcwd.store.entities.StockItemMenu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface StockItemMenuRepository extends JpaRepository<StockItemMenu,Long> {

    @Query("SELECT MAX(f.accountCode) FROM StockItemMenu f")
    String findLastAccountCode();


    @Query("SELECT f FROM StockItemMenu f WHERE f.isDeleted = false AND f.accountCode = (SELECT MAX(f2.accountCode) FROM StockItemMenu f2 WHERE f2.isDeleted = false AND f2.accountCode < ?1)")
    StockItemMenu findPreviousFormData(String currentId);

    @Query("SELECT f FROM StockItemMenu f WHERE f.isDeleted = false AND f.accountCode = (SELECT MIN(f2.accountCode) FROM StockItemMenu f2 WHERE f2.isDeleted = false AND f2.accountCode > ?1)")
    StockItemMenu findNextFormData(String currentId);

    @Transactional(readOnly = true)
    @Query("SELECT f FROM StockItemMenu f WHERE f.isDeleted = false ORDER BY f.accountCode DESC Limit 1")
    StockItemMenu findTopByOrderByAccountCodeDesc();

    @Transactional(readOnly = true)
    @Query("SELECT f FROM StockItemMenu f WHERE f.isDeleted = false ORDER BY f.accountCode ASC Limit 1")
    StockItemMenu findTopByOrderByAccountCodeAsc();

    Page<StockItemMenu> findAllByIsDeletedFalse(Pageable pageable);

    StockItemMenu findByAccountCode(String accountId);

    Page<StockItemMenu> findByAccountCodeContaining(String accountCode, Pageable pageable);

//    @Query("SELECT f FROM StockItemMenu f WHERE f.accountCode = (SELECT MAX(f2.accountCode) FROM StockItemMenu f2 WHERE f2.accountCode < ?1)")
//    StockItemMenu findPreviousFormData(String currentId);
//
//    @Query("SELECT f FROM StockItemMenu f WHERE f.accountCode = (SELECT MIN(f2.accountCode) FROM StockItemMenu f2 WHERE f2.accountCode > ?1)")
//    StockItemMenu findNextFormData(String currentId);
//
//    StockItemMenu findTopByOrderByAccountCodeDesc();
//
//    StockItemMenu findTopByOrderByAccountCodeAsc();
}
