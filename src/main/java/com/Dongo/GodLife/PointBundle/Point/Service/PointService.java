package com.Dongo.GodLife.PointBundle.Point.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.Dongo.GodLife.PointBundle.Point.Dto.PointRequest;
import com.Dongo.GodLife.PointBundle.Point.Model.Point;
import com.Dongo.GodLife.PointBundle.Point.Service.PointPersistenceAdapter;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PointService {
    private final PointPersistenceAdapter pointAdapter;

    public Point createAddPoint(PointRequest pointRequest, long userId) {
        return pointAdapter.save(pointRequest, Point.PointType.earn, userId);
    }

    public Point createEarnPoint(PointRequest pointRequest, long userId) {
        return pointAdapter.save(pointRequest, Point.PointType.use, userId);
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

    public void deletePoint(long id) {
        pointAdapter.delete(id);
    }

    public Integer calculateTotalPointsByUserId(long userId) {
        return pointAdapter.calculateTotalPointsByUserId(userId);
    }
}
