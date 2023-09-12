package com.lcwd.store.repositories;

import com.lcwd.store.entities.LedgerAccount;
import com.lcwd.store.entities.LedgerAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface LedgerAccountRepository extends JpaRepository<LedgerAccount,Long> {
    @Query("SELECT MAX(la.accountCode) FROM LedgerAccount la")
    String findMaxAccountCode();
    @Query("SELECT f FROM LedgerAccount f WHERE f.isDeleted = false AND f.accountCode = (SELECT MAX(f2.accountCode) FROM LedgerAccount f2 WHERE f2.isDeleted = false AND f2.accountCode < ?1)")
    LedgerAccount findPreviousFormData(String currentId);

    @Query("SELECT f FROM LedgerAccount f WHERE f.isDeleted = false AND f.accountCode = (SELECT MIN(f2.accountCode) FROM LedgerAccount f2 WHERE f2.isDeleted = false AND f2.accountCode > ?1)")
    LedgerAccount findNextFormData(String currentId);

    @Transactional(readOnly = true)
    @Query("SELECT f FROM LedgerAccount f WHERE f.isDeleted = false ORDER BY f.accountCode DESC Limit 1")
    LedgerAccount findTopByOrderByAccountCodeDesc();

    @Transactional(readOnly = true)
    @Query("SELECT f FROM LedgerAccount f WHERE f.isDeleted = false ORDER BY f.accountCode ASC Limit 1")
    LedgerAccount findTopByOrderByAccountCodeAsc();

    Page<LedgerAccount> findAllByIsDeletedFalse(Pageable pageable);

    LedgerAccount findByAccountCode(String accountId);

    Page<LedgerAccount> findByAccountCodeContaining(String accountCode, Pageable pageable);
}
