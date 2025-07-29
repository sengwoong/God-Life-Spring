package com.Dongo.GodLife.PointBundle.Point.Exception;

public class PointNotFoundException extends RuntimeException {
    public PointNotFoundException(String message) {
        super(message);
    }
    
    public PointNotFoundException(Long pointId) {
        super("포인트 기록을 찾을 수 없습니다: " + pointId);
    }
} 