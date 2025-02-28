package com.Dongo.GodLife.PointBundle.Point.Controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.Dongo.GodLife.PointBundle.Point.Dto.PointRequest;
import com.Dongo.GodLife.PointBundle.Point.Model.Point;
import com.Dongo.GodLife.PointBundle.Point.Service.PointService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/points")
@RequiredArgsConstructor
public class PointController {
    private final PointService pointService;
    private static final Logger logger = LoggerFactory.getLogger(PointController.class);

    @PostMapping("add/user/{user_id}")
    public ResponseEntity<Point> createAddPoint(
            @PathVariable(name = "user_id") long userId,
            @RequestBody PointRequest pointRequest) {

        Point point = pointService.createAddPoint(pointRequest, userId);

        return ResponseEntity.ok(point);
    }

    @PostMapping("earn/user/{user_id}")
    public ResponseEntity<Point> createEarnPoint(
            @PathVariable(name = "user_id") long userId,
            @RequestBody PointRequest pointRequest) {

        Point point = pointService.createEarnPoint(pointRequest, userId);

        return ResponseEntity.ok(point);
    }


    @GetMapping("/{point_id}")
    public ResponseEntity<Point> getPointById(@PathVariable(name = "point_id") long pointId) {
        return pointService.getPointById(pointId)
                .map(point -> ResponseEntity.ok(point))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{user_id}/earned")
    public ResponseEntity<Page<Point>> getEarnedPointsByUserId(
            @PathVariable(name = "user_id") long userId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        
        logger.info("Fetching earned points for user: {}, page: {}, size: {}", userId, page, size);
        
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Point> points = pointService.getEarnedPointsByUserId(userId, pageable);
            logger.info("Found {} earned points", points.getTotalElements());
            
            return ResponseEntity.ok(points);
        } catch (Exception e) {
            logger.error("Error fetching earned points", e);
            throw e;
        }
    }

    @GetMapping("/user/{user_id}/used")
    public ResponseEntity<Page<Point>> getUsedPointsByUserId(
            @PathVariable(name = "user_id") long userId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Point> points = pointService.getUsedPointsByUserId(userId, pageable);
            
            return ResponseEntity.ok(points);
        } catch (Exception e) {
            logger.error("Error fetching used points", e);
            throw e;
        }
    }

    @DeleteMapping("/{point_id}")
    public ResponseEntity<Void> deletePoint(@PathVariable(name = "point_id") long pointId) {
        pointService.deletePoint(pointId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{user_id}/total")
    public ResponseEntity<Integer> calculateTotalPointsByUserId(@PathVariable(name = "user_id") long userId) {
        Integer totalPoints = pointService.calculateTotalPointsByUserId(userId);
        return ResponseEntity.ok(totalPoints);
    }
}
