package com.Dongo.GodLife.PointBundle.Point.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.Dongo.GodLife.PointBundle.Point.Model.Point;

import java.util.List;

public interface PointRepository extends JpaRepository<Point, Long> {
    List<Point> findByUserId(long userId);
    
    List<Point> findByUserIdAndType(long userId, Point.PointType type);
    
    Page<Point> findByUserIdAndType(long userId, Point.PointType type, Pageable pageable);
    
    @Query("SELECT COALESCE(SUM(CASE WHEN p.type = 'EARN' THEN p.points ELSE -p.points END), 0) FROM Point p WHERE p.userId = :userId")
    Integer calculateTotalPointsByUserId(@Param("userId") long userId);
}
