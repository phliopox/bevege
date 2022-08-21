package Jeong.jdbcRefactoring.interceptor;

import Jeong.jdbcRefactoring.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
public class AdminInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();

        log.info("관리자 인증 인터셉터 실행",requestURI);
        HttpSession session = request.getSession();

        if(session==null||session.getAttribute(SessionConst.MEMBER_LOGIN_SESSION)==null||
                !((String)session.getAttribute(SessionConst.MEMBER_LOGIN_SESSION)).equals("admin")){
            log.info("미인증 사용자 요청");

            response.sendRedirect("/not_admin");
            return false;
        }
        return true;

    }
}
