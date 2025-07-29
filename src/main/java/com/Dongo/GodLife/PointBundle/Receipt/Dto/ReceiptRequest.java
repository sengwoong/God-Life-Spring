package com.Dongo.GodLife.PointBundle.Receipt.Dto;

import com.Dongo.GodLife.PointBundle.Receipt.Model.Receipt;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptRequest {
    
    @NotBlank(message = "상품명은 필수입니다")
    private String itemName;
    
    @NotBlank(message = "상품 타입은 필수입니다")
    private String itemType;
    
    @NotNull(message = "가격은 필수입니다")
    @Positive(message = "가격은 양수여야 합니다")
    private Integer price;
    
    @NotNull(message = "수량은 필수입니다")
    @Positive(message = "수량은 양수여야 합니다")
    private Integer quantity;
    
    @NotBlank(message = "거래 ID는 필수입니다")
    private String transactionId;
    
    private LocalDateTime purchaseDate;
    
    @NotNull(message = "상태는 필수입니다")
    private Receipt.ReceiptStatus status;
} 