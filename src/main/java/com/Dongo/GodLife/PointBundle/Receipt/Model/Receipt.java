package com.Dongo.GodLife.PointBundle.Receipt.Model;

import com.Dongo.GodLife.User.Model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "receipts")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Receipt {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(nullable = false)
    private String itemName;
    
    @Column(nullable = false)
    private String itemType;
    
    @Column(nullable = false)
    private Integer price;
    
    @Column(nullable = false)
    private Integer quantity;
    
    @Column(nullable = false)
    private Integer totalAmount;
    
    @Column(nullable = false)
    private LocalDateTime purchaseDate;
    
    @Column(nullable = false)
    private String transactionId;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReceiptStatus status;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (purchaseDate == null) {
            purchaseDate = LocalDateTime.now();
        }
    }
    
    public enum ReceiptStatus {
        PENDING, COMPLETED, CANCELLED, REFUNDED
    }
} 