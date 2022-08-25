package Jeong.jdbcRefactoring.Login;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class LoginService {

    private final LoginRepository loginRepository;

    public LoginService(LoginRepository loginRepository){
        this.loginRepository=loginRepository;
    }

    public Map<String,String> login(LoginForm form){
        Map<String,String> invalid=new HashMap<>();

        LoginForm memberInfo = loginRepository.getLoginInfo(form);
        if(memberInfo==null){
            invalid.put("memberId","아이디가 존재하지않습니다");
        }else{
            if(!form.getMemberPw().equals(memberInfo.memberPw)){
            invalid.put("memberPw","비밀번호가 일치하지않습니다.");
            }
        }
        return invalid;
    }
}
