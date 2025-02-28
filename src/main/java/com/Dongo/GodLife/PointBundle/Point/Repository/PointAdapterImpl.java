package com.Dongo.GodLife.PointBundle.Point.Repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.Dongo.GodLife.PointBundle.Point.Dto.PointRequest;
import com.Dongo.GodLife.PointBundle.Point.Model.Point;
import com.Dongo.GodLife.PointBundle.Point.Service.PointPersistenceAdapter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class PointAdapterImpl implements PointPersistenceAdapter {

    private final PointRepository pointRepository;

    @Autowired
    public PointAdapterImpl(PointRepository pointRepository) {
        this.pointRepository = pointRepository;
    }

    @Override
    public Point save(PointRequest pointRequest, Point.PointType type, long userId) {
        Point point = new Point();
        point.setTitle(pointRequest.getTitle());
        point.setContent(pointRequest.getContent());
        point.setPoints(pointRequest.getPoints());
        point.setType(type);
        point.setUserId(userId);
        point.setCreatedAt(LocalDateTime.now());
        return pointRepository.save(point);
    }

    @Override
    public Optional<Point> findById(long id) {
        return pointRepository.findById(id);
    }

    @Override
    public Page<Point> getEarnedPointsByUserId(long userId, Pageable pageable) {
        return pointRepository.findByUserIdAndType(userId, Point.PointType.earn, pageable);
    }

    @Override
    public Page<Point> getUsedPointsByUserId(long userId, Pageable pageable) {
        return pointRepository.findByUserIdAndType(userId, Point.PointType.use, pageable);
    }

    @Override
    public List<Point> findByUserIdAndType(long userId, Point.PointType type) {
        return pointRepository.findByUserIdAndType(userId, type);
    }

    @Override
    public Integer calculateTotalPointsByUserId(long userId) {
        return pointRepository.calculateTotalPointsByUserId(userId);
    }

    @Override
    public void delete(long id) {
        pointRepository.findById(id)
                .ifPresent(point -> pointRepository.delete(point));
    }
}