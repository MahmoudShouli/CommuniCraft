package com.aswe.communicraft.annotations;

import com.aswe.communicraft.models.dto.UserDto;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Aspect
@Component
public class CheckRoleAspect {

    @Around("@annotation(com.aswe.communicraft.annotations.HidePasswordIfNotAdmin)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().iterator().next().getAuthority();
        if (!role.equalsIgnoreCase("ADMIN")) {

            Object result = joinPoint.proceed();

            if (result instanceof UserDto) {
                UserDto user = (UserDto) result;
                user.setPassword("***********");
                return user;
            } else if (result instanceof Collection) {
                List<UserDto> modifiedUsers = new ArrayList<>();
                for (Object obj : (Collection) result) {
                    if (obj instanceof UserDto) {
                        UserDto user = (UserDto) obj;
                        user.setPassword("***********");
                        modifiedUsers.add(user);
                    }
                }
                return modifiedUsers;
            } else {
                return result;
            }
        }

        return joinPoint.proceed();
    }
}
