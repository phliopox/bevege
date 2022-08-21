package Jeong.jdbcRefactoring.interceptor;

import Jeong.jdbcRefactoring.AccountConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@Slf4j
public class AccountPwCheckInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI=request.getRequestURI();

        log.info("계좌 이중 로그인 실행 {}", requestURI);
        HttpSession session = request.getSession(false);
        if(session.getAttribute(AccountConst.ACCOUNT_LOGIN_SESSION)==null){
            log.info("계좌 로그인 요청");

            response.sendRedirect("/check_account?redirectURL="+requestURI);
            return false;
        }
        return true;
    }
}
