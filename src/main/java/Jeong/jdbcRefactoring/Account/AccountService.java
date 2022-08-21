package Jeong.jdbcRefactoring.Account;

import Jeong.jdbcRefactoring.Account.form.CreateAccountForm;
import Jeong.jdbcRefactoring.Account.form.TransferMoneyForm;
import Jeong.jdbcRefactoring.DTO.Member;
import Jeong.jdbcRefactoring.Login.LoginRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.NoSuchElementException;

@Slf4j
@Service
public class AccountService {

    private final AccountRepository2 accountRepository;
    private final LoginRepository loginRepository;

    public AccountService(AccountRepository2 accountRepository,LoginRepository loginRepository) {
        this.accountRepository = accountRepository;
        this.loginRepository=loginRepository;
    }

    /*public Member createAccountPwCheck(String memberId, CreateAccountForm form){

        String memberPwInfo = loginRepository.getMemberPwInfo(memberId);
        if (form.getMemberPw().equals(memberPwInfo)) {

            *//*if((form.getAccountPw().length() > 4) || (form.getAccountPw().length() < 4)){
                throw new IndexOutOfBoundsException("계좌 비밀번호는 4자리 숫자여야합니다");
            }*//*

            accountRepository.createAccount(form);
            Member memberAccountInfo = accountRepository.getAccountInfo(memberId);
            return memberAccountInfo;
        }
        else{
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다");
        }



    }*/

    @Transactional
    public void transferMoney(TransferMoneyForm form) {

        try {
            String senderId = form.getMemberId();
            Integer sendMoney = form.getSendMoney();

            Member senderInfo = accountRepository.getAccountInfo(senderId);
            if(senderInfo.getAccountPw().equals(String.valueOf(form.getAccountPw()))) {
                String receiverId = accountRepository.getIdByAccount(form.getReceiverAccountId());

                if (sendMoney > senderInfo.getMoney()) {
                    throw new IllegalStateException("금액이 부족합니다.");
                } else {
                    accountRepository.updateMoney(senderId, -sendMoney);
                    accountRepository.updateMoney(receiverId, +sendMoney);
                }
            }else{

                throw new IllegalArgumentException("계좌 비밀번호가 일치하지 않습니다");
            }

        }catch (NoSuchElementException e){
            throw e;


    }
    }
}
