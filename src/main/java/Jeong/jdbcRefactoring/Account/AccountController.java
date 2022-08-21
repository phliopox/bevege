package Jeong.jdbcRefactoring.Account;


import Jeong.jdbcRefactoring.Account.form.AccountForm;
import Jeong.jdbcRefactoring.Account.form.ChangePointForm;
import Jeong.jdbcRefactoring.Account.form.CreateAccountForm;
import Jeong.jdbcRefactoring.Account.form.TransferMoneyForm;
import Jeong.jdbcRefactoring.AccountConst;
import Jeong.jdbcRefactoring.DTO.Member;
import Jeong.jdbcRefactoring.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;


@Slf4j
@Controller
public class AccountController {

    private final AccountService2 accountService;

    public AccountController(AccountService2 accountService) {
        this.accountService = accountService;

    }

    @GetMapping("/check_account")
    public String checkAccountV1( @ModelAttribute("member") AccountForm form,HttpServletRequest request,  Model model) {

        String memberId = getMemberIdBySession(request);
        Member member = hasNotAccountMessage(memberId, model);
        model.addAttribute("member", member);
        return "refactor/account/accountForm";
    }

    @PostMapping("/check_account")
    public String checkAccountPostV1(@Validated @ModelAttribute("member") AccountForm form,
                                     BindingResult bindingResult,
                                     HttpServletRequest request,
                                     @RequestParam(defaultValue="/") String redirectURL) {

        if(bindingResult.hasErrors()){return "refactor/account/accountForm";}

        if(accountService.checkAccountPwCorrect(form)){
            HttpSession session = request.getSession(false);
            session.setAttribute(AccountConst.ACCOUNT_LOGIN_SESSION,form.getAccountId());
            return redirectURL.length()==1?"redirect:/my_account":"redirect:"+redirectURL;
        } else{
            bindingResult.reject("accountPw","계좌 비밀번호가 일치하지 않습니다.");
            return "refactor/account/accountForm";
        }
    }

    @GetMapping("/my_account")
    public String myAccount( @ModelAttribute("member") AccountForm form,HttpServletRequest request,Model model){
        String memberId = getMemberIdBySession(request);
        Member member = hasNotAccountMessage(memberId, model);
        model.addAttribute("member",member);
        return "refactor/account/my_account";
    }

    /* 계좌 생성 */
    @GetMapping("/create_account")
    public String createAccount(@ModelAttribute("member") CreateAccountForm form, HttpServletRequest request, Model model){
        String memberId = getMemberIdBySession(request);
        Member member = accountService.getAccountInfo(memberId);
        if(member!=null) {model.addAttribute("message","계좌는 하나의 계좌만 소유 가능합니다.");
        }else{ model.addAttribute("memberId",memberId); }
        return "refactor/account/create_account";
    }


    @PostMapping("/create_account")
    public String createAccountPost(@Validated @ModelAttribute("member") CreateAccountForm form,
                                    BindingResult bindingResult,
                                    HttpServletRequest request,
                                    Model model) {

        if(bindingResult.hasErrors()){return "refactor/account/create_account";}
        log.info("create Account - memberID={}",form.getMemberId());
        Map<String, String> serviceMessage = accountService.createAccountService(form);
        if(!serviceMessage.isEmpty()){
            serviceMessage.forEach(bindingResult::reject);
            model.addAttribute("memberId",form.getMemberId());
            return "refactor/account/create_account";
        }
        Member member = accountService.getAccountInfo(form.getMemberId());
        model.addAttribute("member", member);
        return "redirect:/my_account";
    }

    /* 포인트 전환 */
    @GetMapping("/my_account/point_to_money")
    public String PointToMoney(HttpServletRequest request, Model model) {

        String memberId = getMemberIdBySession(request);
        Member member = hasNotAccountMessage(memberId, model);
        model.addAttribute("member", member);
        return "refactor/account/my_point";
    }


    @PostMapping("/my_account/point_to_money")
    public String PointToMoneyPost(@Validated @ModelAttribute("member") ChangePointForm form,
                                   BindingResult bindingResult,HttpServletRequest request){
        if (bindingResult.hasErrors()) {return "refactor/account/my_point";}

        String point = form.getPoint();
        String minusComma = point.replace(",", "");
        int intPoint = Integer.parseInt(minusComma);

        if(form.getChangeMoney()>intPoint){
            bindingResult.reject("point","Point 가 부족합니다.");
            return "refactor/account/my_point";
        }
        accountService.PointToMoney(form);
        return"redirect:/my_account";
    }

    /* 이체 */
    @GetMapping("/my_account/transfer")
    public String sendMoney(@ModelAttribute("member") TransferMoneyForm form,
                            HttpServletRequest request,Model model) {

        String memberId = getMemberIdBySession(request);
        Member member = hasNotAccountMessage(memberId, model);
        model.addAttribute("member", member);
        return "refactor/account/transfer_money";
    }

    @PostMapping("/my_account/transfer")
    public String sendMoneyPost(@Validated @ModelAttribute("member") TransferMoneyForm form,
                                BindingResult bindingResult, HttpServletRequest request,
                                RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {return "refactor/account/transfer_money";}

        Map<String, String> transferResult = accountService.transferMoney(form);
        if(!transferResult.isEmpty()){
            transferResult.forEach(bindingResult::reject);
            return "refactor/account/transfer_money";
        }

        redirectAttributes.addFlashAttribute("status","true");
        return "redirect:/my_account";
    }


    private String getMemberIdBySession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return (String) session.getAttribute(SessionConst.MEMBER_LOGIN_SESSION);
    }

    private Member hasNotAccountMessage(String memberId,Model model){
        Member member = accountService.getAccountInfo(memberId);
        if(member==null){
            model.addAttribute("message", "소유 중인 계좌가 없습니다");
        }
        return member;
    }
}
