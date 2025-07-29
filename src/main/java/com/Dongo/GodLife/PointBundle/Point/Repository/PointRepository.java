package com.Dongo.GodLife.PointBundle.Point.Repository;

import com.Dongo.GodLife.PointBundle.Point.Model.Point;
import com.Dongo.GodLife.User.Model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PointRepository extends JpaRepository<Point, Long> {
    
    Page<Point> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);
    
    Page<Point> findByUserAndTypeOrderByCreatedAtDesc(User user, Point.PointType type, Pageable pageable);
    
    @Query("SELECT SUM(p.points) FROM Point p WHERE p.user = :user AND p.type = 'EARN'")
    Integer sumEarnedPointsByUser(@Param("user") User user);
    
    @Query("SELECT SUM(ABS(p.points)) FROM Point p WHERE p.user = :user AND p.type = 'USE'")
    Integer sumUsedPointsByUser(@Param("user") User user);
    
    @Query("SELECT COUNT(p) FROM Point p WHERE p.user = :user")
    Long countByUser(@Param("user") User user);
} 