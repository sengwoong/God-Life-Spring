package util;

import jakarta.validation.ConstraintViolationException;
import org.springframework.util.StringUtils;

public class Validator {

    public static void validateNickName(String nickName) {
        if (nickName == null || nickName.length() < 3 || nickName.length() > 8) {
            throw new ConstraintViolationException("닉네임은 3글자 이상 8글자 이하여야 합니다.",null);
        }
    }

    public static void validateNotEmpty(String value, String message) {
        if (!StringUtils.hasText(value)) {
            throw new ConstraintViolationException(message,null);
        }
    }

    public static void validateNotNegative(long value, String message) {
        if (value <= 0 || value % 10 != 0) {
            throw new IllegalArgumentException(message);
        }
    }

}