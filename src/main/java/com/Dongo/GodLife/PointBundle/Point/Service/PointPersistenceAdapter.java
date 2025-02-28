package com.Dongo.GodLife.PointBundle.Point.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.Dongo.GodLife.PointBundle.Point.Dto.PointRequest;
import com.Dongo.GodLife.PointBundle.Point.Model.Point;

import java.util.List;
import java.util.Optional;

public interface PointPersistenceAdapter {

    Point save(PointRequest pointRequest, Point.PointType type, long userId);

    Optional<Point> findById(long id);
    
    Page<Point> getEarnedPointsByUserId(long userId, Pageable pageable);
    
    Page<Point> getUsedPointsByUserId(long userId, Pageable pageable);
    
    List<Point> findByUserIdAndType(long userId, Point.PointType type);
    
    Integer calculateTotalPointsByUserId(long userId);

    void delete(long id);
}