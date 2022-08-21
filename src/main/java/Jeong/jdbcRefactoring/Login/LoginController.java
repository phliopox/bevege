package Jeong.jdbcRefactoring.Login;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.NoSuchElementException;

import static Jeong.jdbcRefactoring.SessionConst.*;

@Slf4j
@Controller
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("member") LoginForm form) {
        return "refactor/login/loginForm";
    }

    @PostMapping("/login")
    public String loginFormPost(@Validated @ModelAttribute("member") LoginForm form, BindingResult bindingResult,
                                @RequestParam(defaultValue = "/") String redirectURL, HttpServletRequest request){
        if (bindingResult.hasErrors()) {
            return "refactor/login/loginForm";
        }
        Map<String, String> invalid = loginService.login(form);
        if (!invalid.isEmpty()) {
            invalid.forEach(bindingResult::reject);
            return "refactor/login/loginForm";
        }else{
            HttpSession session=request.getSession();
            session.setAttribute(MEMBER_LOGIN_SESSION,form.getMemberId());
            return "redirect:"+redirectURL;
        }

    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(session!=null){
            session.invalidate();
        }
        return "redirect:/";
    }
}
