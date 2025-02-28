package com.Dongo.GodLife.PointBundle.PointSummary.Service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Dongo.GodLife.PointBundle.Point.Model.Point;
import com.Dongo.GodLife.PointBundle.Point.Repository.PointRepository;
import com.Dongo.GodLife.PointBundle.PointSummary.Model.PointSummary;
import com.Dongo.GodLife.PointBundle.PointSummary.Repository.PointSummaryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PointSummaryService {
    
    private final PointSummaryRepository pointSummaryRepository;
    private final PointRepository pointRepository;
    
    public PointSummary getPointSummaryByUserId(Long userId) {
        return pointSummaryRepository.findByUserId(userId)
                .orElseGet(() -> createEmptyPointSummary(userId));
    }
    
    @Transactional
    public PointSummary updatePointSummary(Long userId) {

        Integer totalPoints = pointRepository.calculateTotalPointsByUserId(userId);
        
      
        PointSummary pointSummary = pointSummaryRepository.findByUserId(userId)
                .orElseGet(() -> createEmptyPointSummary(userId));
        
        pointSummary.setTotalPoints(totalPoints);
        
        return pointSummaryRepository.save(pointSummary);
    }
    
    @Transactional
    public PointSummary incrementEarnedPoints(Long userId, Integer points) {
        PointSummary pointSummary = pointSummaryRepository.findByUserId(userId)
                .orElseGet(() -> createEmptyPointSummary(userId));
        
        pointSummary.setTotalPoints(pointSummary.getTotalPoints() + points);
        pointSummary.setEarnedPoints(pointSummary.getEarnedPoints() + points);
        
        return pointSummaryRepository.save(pointSummary);
    }
    
    @Transactional
    public PointSummary incrementUsedPoints(Long userId, Integer points) {
        PointSummary pointSummary = pointSummaryRepository.findByUserId(userId)
                .orElseGet(() -> createEmptyPointSummary(userId));
        
        pointSummary.setTotalPoints(pointSummary.getTotalPoints() - points);
        pointSummary.setUsedPoints(pointSummary.getUsedPoints() + points);
        
        return pointSummaryRepository.save(pointSummary);
    }
    
    @Transactional
    public PointSummary createPointSummary(Long userId) {
        // 이미 존재하는지 확인
        if (pointSummaryRepository.findByUserId(userId).isPresent()) {
            return pointSummaryRepository.findByUserId(userId).get();
        }
        
        // 새 포인트 요약(지갑) 생성
        PointSummary pointSummary = PointSummary.builder()
                .userId(userId)
                .totalPoints(0)
                .earnedPoints(0)
                .usedPoints(0)
                .build();
        
        return pointSummaryRepository.save(pointSummary);
    }
    
    private PointSummary createEmptyPointSummary(Long userId) {
        return PointSummary.builder()
                .userId(userId)
                .totalPoints(0)
                .earnedPoints(0)
                .usedPoints(0)
                .build();
    }
} 