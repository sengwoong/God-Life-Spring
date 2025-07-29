package com.Dongo.GodLife.PointBundle.Receipt.Exception;

public class ReceiptNotFoundException extends RuntimeException {
    public ReceiptNotFoundException(String message) {
        super(message);
    }
    
    public ReceiptNotFoundException(Long receiptId) {
        super("영수증을 찾을 수 없습니다: " + receiptId);
    }
    
    public ReceiptNotFoundException(String transactionId, boolean isTransactionId) {
        super("거래 ID로 영수증을 찾을 수 없습니다: " + transactionId);
    }
} 