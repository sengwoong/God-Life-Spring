package com.Dongo.GodLife.PointBundle.Point.Service;

import com.Dongo.GodLife.PointBundle.Point.Dto.PointRequest;
import com.Dongo.GodLife.PointBundle.Point.Dto.PointSummaryResponse;
import com.Dongo.GodLife.PointBundle.Point.Exception.PointNotFoundException;
import com.Dongo.GodLife.PointBundle.Point.Model.Point;
import com.Dongo.GodLife.PointBundle.Point.Repository.PointRepository;
import com.Dongo.GodLife.User.Model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PointService {
    
    private final PointRepository pointRepository;
    
    public Point createPoint(PointRequest request, User user) {
        Point point = Point.builder()
                .user(user)
                .title(request.getTitle())
                .content(request.getContent())
                .points(request.getPoints())
                .type(request.getType())
                .build();
        
        return pointRepository.save(point);
    }
    
    @Transactional(readOnly = true)
    public Point findById(Long pointId) {
        return pointRepository.findById(pointId)
                .orElseThrow(() -> new PointNotFoundException(pointId));
    }
    
    @Transactional(readOnly = true)
    public Page<Point> getPointsByUser(User user, Pageable pageable) {
        return pointRepository.findByUserOrderByCreatedAtDesc(user, pageable);
    }
    
    @Transactional(readOnly = true)
    public Page<Point> getPointsByUserAndType(User user, Point.PointType type, Pageable pageable) {
        return pointRepository.findByUserAndTypeOrderByCreatedAtDesc(user, type, pageable);
    }
    
    @Transactional(readOnly = true)
    public PointSummaryResponse getPointSummary(User user) {
        Integer earnedPoints = pointRepository.sumEarnedPointsByUser(user);
        Integer usedPoints = pointRepository.sumUsedPointsByUser(user);
        
        if (earnedPoints == null) earnedPoints = 0;
        if (usedPoints == null) usedPoints = 0;
        
        Integer totalPoints = earnedPoints - usedPoints;
        
        return PointSummaryResponse.builder()
                .userId(user.getId())
                .totalPoints(totalPoints)
                .earnedPoints(earnedPoints)
                .usedPoints(usedPoints)
                .build();
    }
    
    @Transactional(readOnly = true)
    public Long getPointCountByUser(User user) {
        return pointRepository.countByUser(user);
    }
    
    public Point updatePoint(Long pointId, PointRequest request, User user) {
        Point point = findById(pointId);
        
        if (!point.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("포인트 기록을 수정할 권한이 없습니다");
        }
        
        point = Point.builder()
                .id(point.getId())
                .user(user)
                .title(request.getTitle())
                .content(request.getContent())
                .points(request.getPoints())
                .type(request.getType())
                .createdAt(point.getCreatedAt())
                .build();
        
        return pointRepository.save(point);
    }
    
    public void deletePoint(Long pointId, User user) {
        Point point = findById(pointId);
        
        if (!point.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("포인트 기록을 삭제할 권한이 없습니다");
        }
        
        pointRepository.delete(point);
    }
} 