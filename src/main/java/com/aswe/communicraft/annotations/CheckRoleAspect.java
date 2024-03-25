package com.aswe.communicraft.annotations;

import com.aswe.communicraft.models.dto.UserDto;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CheckRoleAspect {

    @Around("@annotation(com.aswe.communicraft.annotations.HidePasswordIfNotAdmin)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().iterator().next().getAuthority();
        if (!role.equalsIgnoreCase("ADMIN")) {

            UserDto user = (UserDto) joinPoint.proceed();
            user.setPassword("***********");

            return user;
        }

        return joinPoint.proceed();
    }
}