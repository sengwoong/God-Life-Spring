package com.Dongo.GodLife.PointBundle.Receipt.Service;

import com.Dongo.GodLife.PointBundle.Receipt.Dto.ReceiptRequest;
import com.Dongo.GodLife.PointBundle.Receipt.Exception.ReceiptNotFoundException;
import com.Dongo.GodLife.PointBundle.Receipt.Model.Receipt;
import com.Dongo.GodLife.PointBundle.Receipt.Repository.ReceiptRepository;
import com.Dongo.GodLife.User.Model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReceiptService {
    
    private final ReceiptRepository receiptRepository;
    
    public Receipt createReceipt(ReceiptRequest request, User user) {
        Receipt receipt = Receipt.builder()
                .user(user)
                .itemName(request.getItemName())
                .itemType(request.getItemType())
                .price(request.getPrice())
                .quantity(request.getQuantity())
                .totalAmount(request.getPrice() * request.getQuantity())
                .transactionId(request.getTransactionId())
                .purchaseDate(request.getPurchaseDate() != null ? request.getPurchaseDate() : LocalDateTime.now())
                .status(request.getStatus())
                .build();
        
        return receiptRepository.save(receipt);
    }
    
    @Transactional(readOnly = true)
    public Receipt findById(Long receiptId) {
        return receiptRepository.findById(receiptId)
                .orElseThrow(() -> new ReceiptNotFoundException(receiptId));
    }
    
    @Transactional(readOnly = true)
    public Receipt findByTransactionId(String transactionId) {
        return receiptRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new ReceiptNotFoundException(transactionId, true));
    }
    
    @Transactional(readOnly = true)
    public Page<Receipt> getReceiptsByUser(User user, Pageable pageable) {
        return receiptRepository.findByUserOrderByCreatedAtDesc(user, pageable);
    }
    
    @Transactional(readOnly = true)
    public Page<Receipt> getReceiptsByUserAndStatus(User user, Receipt.ReceiptStatus status, Pageable pageable) {
        return receiptRepository.findByUserAndStatusOrderByCreatedAtDesc(user, status, pageable);
    }
    
    @Transactional(readOnly = true)
    public List<Receipt> getReceiptsByUserAndDateRange(User user, LocalDateTime startDate, LocalDateTime endDate) {
        return receiptRepository.findByUserAndPurchaseDateBetweenOrderByPurchaseDateDesc(user, startDate, endDate);
    }
    
    @Transactional(readOnly = true)
    public Integer getTotalAmountByUser(User user) {
        Integer totalAmount = receiptRepository.sumTotalAmountByUser(user);
        return totalAmount != null ? totalAmount : 0;
    }
    
    @Transactional(readOnly = true)
    public Long getCompletedReceiptCountByUser(User user) {
        return receiptRepository.countCompletedByUser(user);
    }
    
    public Receipt updateReceipt(Long receiptId, ReceiptRequest request, User user) {
        Receipt receipt = findById(receiptId);
        
        if (!receipt.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("영수증을 수정할 권한이 없습니다");
        }
        
        receipt = Receipt.builder()
                .id(receipt.getId())
                .user(user)
                .itemName(request.getItemName())
                .itemType(request.getItemType())
                .price(request.getPrice())
                .quantity(request.getQuantity())
                .totalAmount(request.getPrice() * request.getQuantity())
                .transactionId(request.getTransactionId())
                .purchaseDate(request.getPurchaseDate() != null ? request.getPurchaseDate() : receipt.getPurchaseDate())
                .status(request.getStatus())
                .createdAt(receipt.getCreatedAt())
                .build();
        
        return receiptRepository.save(receipt);
    }
    
    public void deleteReceipt(Long receiptId, User user) {
        Receipt receipt = findById(receiptId);
        
        if (!receipt.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("영수증을 삭제할 권한이 없습니다");
        }
        
        receiptRepository.delete(receipt);
    }
} 