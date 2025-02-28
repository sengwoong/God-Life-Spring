package com.Dongo.GodLife.PointBundle.PointSummary.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Dongo.GodLife.PointBundle.PointSummary.Model.PointSummary;
import com.Dongo.GodLife.PointBundle.PointSummary.Service.PointSummaryService;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/point-summaries")
@RequiredArgsConstructor
public class PointSummaryController {
    
    private final PointSummaryService pointSummaryService;
    private static final Logger logger = LoggerFactory.getLogger(PointSummaryController.class);
    
    @GetMapping("/user/{user_id}")
    public ResponseEntity<PointSummary> getPointSummaryByUserId(@PathVariable(name = "user_id") Long userId) {
        try {
            PointSummary pointSummary = pointSummaryService.getPointSummaryByUserId(userId);
            return ResponseEntity.ok(pointSummary);
        } catch (Exception e) {
            logger.error("Error fetching point summary for user: " + userId, e);
            throw e;
        }
    }

    @PostMapping("/user/{user_id}")
    public ResponseEntity<PointSummary> createPointSummary(@PathVariable(name = "user_id") Long userId) {
        try {
            PointSummary pointSummary = pointSummaryService.createPointSummary(userId);
            return ResponseEntity.ok(pointSummary);
        } catch (Exception e) {
            logger.error("회원 지갑 생성 중 오류 발생: " + userId, e);
            throw e;
        }
    }

} 