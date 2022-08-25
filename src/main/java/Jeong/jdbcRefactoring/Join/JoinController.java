package Jeong.jdbcRefactoring.Join;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;



@Controller
public class JoinController {

    private final JointRepository joinRepository;

    public JoinController(JointRepository joinRepository) {
        this.joinRepository = joinRepository;
    }

    @GetMapping("/join-us")
    public String joinPage(@ModelAttribute("member") JoinForm form) {
        return "refactor/joinForm";
    }


    @PostMapping("/join-us")
    public String joinPagePost(@Validated @ModelAttribute("member") JoinForm form, BindingResult bindingResult){
        if(bindingResult.hasErrors()){return "refactor/joinForm";}

        if(joinRepository.checkDuplicatedId(form.memberId)){
            bindingResult.reject("joinFail", "이미 존재하는 아이디입니다.");
            return "refactor/joinForm";
        }

        joinRepository.save(form);
        return "redirect:/";
    }

}
