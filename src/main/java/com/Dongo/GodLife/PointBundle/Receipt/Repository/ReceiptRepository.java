package com.Dongo.GodLife.PointBundle.Receipt.Repository;

import com.Dongo.GodLife.PointBundle.Receipt.Model.Receipt;
import com.Dongo.GodLife.User.Model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
    
    Page<Receipt> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);
    
    Page<Receipt> findByUserAndStatusOrderByCreatedAtDesc(User user, Receipt.ReceiptStatus status, Pageable pageable);
    
    Optional<Receipt> findByTransactionId(String transactionId);
    
    @Query("SELECT SUM(r.totalAmount) FROM Receipt r WHERE r.user = :user AND r.status = 'COMPLETED'")
    Integer sumTotalAmountByUser(@Param("user") User user);
    
    @Query("SELECT COUNT(r) FROM Receipt r WHERE r.user = :user AND r.status = 'COMPLETED'")
    Long countCompletedByUser(@Param("user") User user);
    
    List<Receipt> findByUserAndPurchaseDateBetweenOrderByPurchaseDateDesc(
            User user, LocalDateTime startDate, LocalDateTime endDate);
} 