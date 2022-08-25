package Jeong.jdbcRefactoring.Account;

import Jeong.jdbcRefactoring.Account.form.AccountForm;
import Jeong.jdbcRefactoring.Account.form.ChangePointForm;
import Jeong.jdbcRefactoring.Account.form.CreateAccountForm;
import Jeong.jdbcRefactoring.Account.form.TransferMoneyForm;
import Jeong.jdbcRefactoring.Member;
import Jeong.jdbcRefactoring.Login.LoginRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@Transactional
public class AccountService {

    private final AccountRepository accountRepository;
    private final LoginRepository loginRepository;

    public AccountService(AccountRepository accountRepository, LoginRepository loginRepository) {
        this.accountRepository = accountRepository;
        this.loginRepository=loginRepository;
    }

    public boolean checkAccountPwCorrect(AccountForm form){
        Member member = accountRepository.getAccountInfo(form.getMemberId());
        return member.getAccountPw().equals(form.getAccountPw());
    }

    public Map<String,String> createAccountService(CreateAccountForm form) {
        Map<String,String> valid=new HashMap<>();

        String memberPwInfo = loginRepository.getMemberPwById(form.getMemberId());

        if (form.getMemberPw().equals(memberPwInfo)) {
            if (String.valueOf(form.getAccountPw()).length() == 4) {
                accountRepository.createAccount(form);
            }else{valid.put("accountPw", "계좌 비밀번호는 4자리 숫자여야합니다.");}
        } else {valid.put("memberPW","로그인 비밀번호가 일치하지 않습니다.");}
        return valid;
    }


    public Map<String,String> transferMoney(TransferMoneyForm form) {
        Map<String,String> valid=new HashMap<>();
        String sender=form.getMemberId();
        Integer sendMoney=form.getSendMoney();

        String receiver = accountRepository.getIdByAccount(form.getReceiverAccountId());
        if(receiver==null){ valid.put("receiverAccountID","받는 분의 계좌 정보가 정확하지 않습니다."); return valid;}

        Member senderInfo = accountRepository.getAccountInfo(sender);
        if(!senderInfo.getAccountPw().equals(form.getAccountPw())){
            valid.put("accountPw","계좌 비밀번호가 일치하지 않습니다."); return valid;}

        if(sendMoney>senderInfo.getMoney()){valid.put("sendMoney","금액이 부족합니다"); return valid;}

        accountRepository.updateMoney(sender,-sendMoney);
        accountRepository.updateMoney(receiver,+sendMoney);
        return valid;
    }


    public void PointToMoney(ChangePointForm form) {
        String memberId = form.getMemberId();
        Integer amount = form.getChangeMoney();

        Integer money = getAccountInfo(memberId).getMoney();
        int resultMoney = money + amount;

        Integer point = getAccountInfo(memberId).getPoint();
        int resultPoint = point - amount;

        log.info("point ={}",point);

        accountRepository.transferPoint(resultMoney,resultPoint,memberId);
    }

    public Member getAccountInfo(String memberId){
        Member accountInfo = accountRepository.getAccountInfo(memberId);
        return accountInfo;
    }

    public void PayMoneyAndPoint(Integer usedMoney, Integer usedPoint ,String memberId){
        Member accountInfo = accountRepository.getAccountInfo(memberId);
        Integer money = accountInfo.getMoney()-usedMoney;
        Integer point = accountInfo.getPoint()-usedPoint;
        accountRepository.updateMoneyAndPoint(money,point,memberId);


    }
}
