package com.Dongo.GodLife.PointBundle.Point.Controller;

import com.Dongo.GodLife.PointBundle.Point.Dto.PointRequest;
import com.Dongo.GodLife.PointBundle.Point.Dto.PointSummaryResponse;
import com.Dongo.GodLife.PointBundle.Point.Model.Point;
import com.Dongo.GodLife.PointBundle.Point.Service.PointService;
import com.Dongo.GodLife.PointBundle.Receipt.Dto.ReceiptRequest;
import com.Dongo.GodLife.PointBundle.Receipt.Model.Receipt;
import com.Dongo.GodLife.PointBundle.Receipt.Service.ReceiptService;
import com.Dongo.GodLife.User.Model.User;
import com.Dongo.GodLife.User.Service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/points")
@RequiredArgsConstructor
public class PointController {
    
    private final PointService pointService;
    private final ReceiptService receiptService;
    private final UserService userService;
    
    // ==================== Point 관련 API ====================
    
    @PostMapping("/user/{user_id}")
    public ResponseEntity<Point> createPoint(
            @PathVariable(name = "user_id") Long userId,
            @RequestBody @Valid PointRequest pointRequest) {
        User user = userService.CheckUserAndGetUser(userId);
        Point createdPoint = pointService.createPoint(pointRequest, user);
        return ResponseEntity.ok(createdPoint);
    }
    
    @GetMapping("/{point_id}")
    public ResponseEntity<Point> getPointById(@PathVariable(name = "point_id") Long pointId) {
        Point point = pointService.findById(pointId);
        return ResponseEntity.ok(point);
    }
    
    @GetMapping("/user/{user_id}")
    public ResponseEntity<Page<Point>> getPointsByUser(
            @PathVariable(name = "user_id") Long userId,
            @RequestParam(name = "type", required = false) Point.PointType type,
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
        User user = userService.CheckUserAndGetUser(userId);
        Page<Point> points;
        
        if (type != null) {
            points = pointService.getPointsByUserAndType(user, type, pageable);
        } else {
            points = pointService.getPointsByUser(user, pageable);
        }
        
        return ResponseEntity.ok(points);
    }
    
    @GetMapping("/summary/user/{user_id}")
    public ResponseEntity<PointSummaryResponse> getPointSummary(@PathVariable(name = "user_id") Long userId) {
        User user = userService.CheckUserAndGetUser(userId);
        PointSummaryResponse summary = pointService.getPointSummary(user);
        return ResponseEntity.ok(summary);
    }
    
    @GetMapping("/count/user/{user_id}")
    public ResponseEntity<Long> getPointCountByUser(@PathVariable(name = "user_id") Long userId) {
        User user = userService.CheckUserAndGetUser(userId);
        Long count = pointService.getPointCountByUser(user);
        return ResponseEntity.ok(count);
    }
    
    @PutMapping("/{point_id}/user/{user_id}")
    public ResponseEntity<Point> updatePoint(
            @PathVariable(name = "point_id") Long pointId,
            @PathVariable(name = "user_id") Long userId,
            @RequestBody @Valid PointRequest pointRequest) {
        User user = userService.CheckUserAndGetUser(userId);
        Point updatedPoint = pointService.updatePoint(pointId, pointRequest, user);
        return ResponseEntity.ok(updatedPoint);
    }
    
    @DeleteMapping("/{point_id}/user/{user_id}")
    public ResponseEntity<Void> deletePoint(
            @PathVariable(name = "point_id") Long pointId,
            @PathVariable(name = "user_id") Long userId) {
        User user = userService.CheckUserAndGetUser(userId);
        pointService.deletePoint(pointId, user);
        return ResponseEntity.noContent().build();
    }
    
    // ==================== Receipt 관련 API ====================
    
    @PostMapping("/receipts/user/{user_id}")
    public ResponseEntity<Receipt> createReceipt(
            @PathVariable(name = "user_id") Long userId,
            @RequestBody @Valid ReceiptRequest receiptRequest) {
        User user = userService.CheckUserAndGetUser(userId);
        Receipt createdReceipt = receiptService.createReceipt(receiptRequest, user);
        return ResponseEntity.ok(createdReceipt);
    }
    
    @GetMapping("/receipts/{receipt_id}")
    public ResponseEntity<Receipt> getReceiptById(@PathVariable(name = "receipt_id") Long receiptId) {
        Receipt receipt = receiptService.findById(receiptId);
        return ResponseEntity.ok(receipt);
    }
    
    @GetMapping("/receipts/transaction/{transaction_id}")
    public ResponseEntity<Receipt> getReceiptByTransactionId(@PathVariable(name = "transaction_id") String transactionId) {
        Receipt receipt = receiptService.findByTransactionId(transactionId);
        return ResponseEntity.ok(receipt);
    }
    
    @GetMapping("/receipts/user/{user_id}")
    public ResponseEntity<Page<Receipt>> getReceiptsByUser(
            @PathVariable(name = "user_id") Long userId,
            @RequestParam(name = "status", required = false) Receipt.ReceiptStatus status,
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
        User user = userService.CheckUserAndGetUser(userId);
        Page<Receipt> receipts;
        
        if (status != null) {
            receipts = receiptService.getReceiptsByUserAndStatus(user, status, pageable);
        } else {
            receipts = receiptService.getReceiptsByUser(user, pageable);
        }
        
        return ResponseEntity.ok(receipts);
    }
    
    @GetMapping("/receipts/user/{user_id}/date-range")
    public ResponseEntity<List<Receipt>> getReceiptsByDateRange(
            @PathVariable(name = "user_id") Long userId,
            @RequestParam(name = "start_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(name = "end_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        User user = userService.CheckUserAndGetUser(userId);
        List<Receipt> receipts = receiptService.getReceiptsByUserAndDateRange(user, startDate, endDate);
        return ResponseEntity.ok(receipts);
    }
    
    @GetMapping("/receipts/total-amount/user/{user_id}")
    public ResponseEntity<Integer> getTotalAmountByUser(@PathVariable(name = "user_id") Long userId) {
        User user = userService.CheckUserAndGetUser(userId);
        Integer totalAmount = receiptService.getTotalAmountByUser(user);
        return ResponseEntity.ok(totalAmount);
    }
    
    @GetMapping("/receipts/count/user/{user_id}")
    public ResponseEntity<Long> getCompletedReceiptCountByUser(@PathVariable(name = "user_id") Long userId) {
        User user = userService.CheckUserAndGetUser(userId);
        Long count = receiptService.getCompletedReceiptCountByUser(user);
        return ResponseEntity.ok(count);
    }
    
    @PutMapping("/receipts/{receipt_id}/user/{user_id}")
    public ResponseEntity<Receipt> updateReceipt(
            @PathVariable(name = "receipt_id") Long receiptId,
            @PathVariable(name = "user_id") Long userId,
            @RequestBody @Valid ReceiptRequest receiptRequest) {
        User user = userService.CheckUserAndGetUser(userId);
        Receipt updatedReceipt = receiptService.updateReceipt(receiptId, receiptRequest, user);
        return ResponseEntity.ok(updatedReceipt);
    }
    
    @DeleteMapping("/receipts/{receipt_id}/user/{user_id}")
    public ResponseEntity<Void> deleteReceipt(
            @PathVariable(name = "receipt_id") Long receiptId,
            @PathVariable(name = "user_id") Long userId) {
        User user = userService.CheckUserAndGetUser(userId);
        receiptService.deleteReceipt(receiptId, user);
        return ResponseEntity.noContent().build();
    }
} 