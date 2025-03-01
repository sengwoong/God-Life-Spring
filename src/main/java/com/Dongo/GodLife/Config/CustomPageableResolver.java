package com.Dongo.GodLife.Config;

import org.springframework.core.MethodParameter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class CustomPageableResolver extends PageableHandlerMethodArgumentResolver {

    @Override
    public Pageable resolveArgument(MethodParameter methodParameter, 
                                   ModelAndViewContainer mavContainer,
                                   NativeWebRequest webRequest, 
                                   WebDataBinderFactory binderFactory) {
        try {
            return super.resolveArgument(methodParameter, mavContainer, webRequest, binderFactory);
        } catch (Exception e) {
            // 오류 발생 시 기본 페이지네이션 반환
            return PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        }
    }
} 