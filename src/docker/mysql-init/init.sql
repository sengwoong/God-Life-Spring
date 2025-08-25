-- Ensure UTF-8 for this session and required seed data
SET NAMES utf8mb4;
SET character_set_client = utf8mb4;
SET character_set_connection = utf8mb4;
SET character_set_results = utf8mb4;

-- Ensure required users exist in table `user` (snake_case columns)
INSERT INTO `user` (user_id, email, password, nick_name, phone_number, address, profile_image, bio, level, followers, following, sales, created_at)
VALUES
  (1, 'admin@example.com', '$2a$10$abcdefghijk1234567890', 'admin', NULL, NULL, NULL, NULL, 1, 0, 0, 0, NOW()),
  (2, 'user1@example.com', '$2a$10$abcdefghijk1234567890', 'user1', NULL, NULL, NULL, NULL, 1, 0, 0, 0, NOW())
ON DUPLICATE KEY UPDATE email = VALUES(email);

-- Create posts table (minimal columns used by Logstash pipeline)
CREATE TABLE IF NOT EXISTS posts (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(255) NOT NULL,
  content TEXT NULL,
  image_url VARCHAR(255) NULL,
  type VARCHAR(20) NOT NULL,
  category VARCHAR(20) NOT NULL,
  is_shared TINYINT(1) NOT NULL DEFAULT 0,
  is_ad TINYINT(1) NOT NULL DEFAULT 0,
  like_count INT NOT NULL DEFAULT 0,
  comment_count INT NOT NULL DEFAULT 0,
  user_id BIGINT NOT NULL,
  created_at DATETIME NOT NULL,
  updated_at DATETIME NULL,
  INDEX idx_posts_updated_at (updated_at),
  INDEX idx_posts_is_shared (is_shared),
  CONSTRAINT fk_posts_user_id FOREIGN KEY (user_id) REFERENCES `user`(user_id)
) DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- Reset and seed a few shared posts
DELETE FROM posts;
INSERT INTO posts
  (title, content, image_url, type, category, is_shared, is_ad, like_count, comment_count, user_id, created_at, updated_at)
VALUES
  ('공유 글 1', '첫 번째 테스트 포스트입니다.', NULL, 'NORMAL', 'SHARED', 1, 0, 3, 0, 1, NOW(), NOW()),
  ('공유 글 2', '두 번째 테스트 포스트입니다.', NULL, 'NORMAL', 'SHARED', 1, 0, 0, 1, 2, NOW(), NOW()),
  ('공유 글 3', '세 번째 테스트 포스트입니다.', NULL, 'VOCA',   'SHARED', 1, 0, 1, 2, 1, NOW(), NOW());


