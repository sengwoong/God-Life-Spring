package com.Dongo.GodLife.PointBundle.PointSummary.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Dongo.GodLife.PointBundle.PointSummary.Model.PointSummary;

import java.util.Optional;

@Repository
public interface PointSummaryRepository extends JpaRepository<PointSummary, Long> {
    Optional<PointSummary> findByUserId(Long userId);
} 