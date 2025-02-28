package com.Dongo.GodLife.PointBundle.Point.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Dongo.GodLife.PointBundle.Point.Dto.PointRequest;
import com.Dongo.GodLife.PointBundle.Point.Model.Point;
import com.Dongo.GodLife.PointBundle.PointSummary.Service.PointSummaryService;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PointService {
    private final PointPersistenceAdapter pointAdapter;
    private final PointSummaryService pointSummaryService;

    @Transactional
    public Point createAddPoint(PointRequest pointRequest, long userId) {
        Point point = pointAdapter.save(pointRequest, Point.PointType.earn, userId);
        pointSummaryService.incrementEarnedPoints(userId, point.getPoints());
        return point;
    }

    @Transactional
    public Point createEarnPoint(PointRequest pointRequest, long userId) {
        Point point = pointAdapter.save(pointRequest, Point.PointType.use, userId);
        pointSummaryService.incrementUsedPoints(userId, point.getPoints());
        return point;
    }

    public Optional<Point> getPointById(long id) {
        return pointAdapter.findById(id);
    }

    public Page<Point> getEarnedPointsByUserId(long userId, Pageable pageable) {
        return pointAdapter.getEarnedPointsByUserId(userId, pageable);
    }

    public Page<Point> getUsedPointsByUserId(long userId, Pageable pageable) {
        return pointAdapter.getUsedPointsByUserId(userId, pageable);
    }

    @Transactional
    public void deletePoint(long id) {
        pointAdapter.findById(id).ifPresent(point -> {
            Long userId = point.getUserId();
            Integer points = point.getPoints();
            
            if (point.getType() == Point.PointType.earn) {
                pointSummaryService.incrementEarnedPoints(userId, -points);
            } else {
                pointSummaryService.incrementUsedPoints(userId, -points);
            }
            
            pointAdapter.delete(id);
        });
    }

    public Integer calculateTotalPointsByUserId(long userId) {
        return pointAdapter.calculateTotalPointsByUserId(userId);
    }
}
