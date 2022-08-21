package Jeong.jdbcRefactoring.DTO;

import Jeong.jdbcRefactoring.Join.JoinForm;
import Jeong.jdbcRefactoring.Login.LoginForm;
import lombok.Data;


@Data
public class Member {

    String memberId;
    String memberPw;
    String memberEmail;
    String accountId;
    Integer accountPw;
    Integer money;
    Integer point;

    /*포인트 전환 객체*/
    Integer changeMoney;

    /*계좌이체 객체*/
    String receiverAccountId;
    Integer sendMoney;

    public Member(){}
    public Member(String memberId, String memberPw, String memberEmail,
                  String accountId,Integer accountPw, Integer money,Integer point) {
        this.memberId = memberId;
        this.memberPw = memberPw;
        this.memberEmail = memberEmail;
        this.accountId=accountId;
        this.accountPw = accountPw;
        this.money = money;
        this.point=point;
    }


    public static Member LoginRepositoryMember(String memberId,String memberPw,String memberEmail) {
        return new Member(memberId,memberPw,memberEmail,null,null,null,null);
    }

    public static Member AccountRepositoryMember(String memberId,String account_id,Integer account_pw,Integer money,Integer point){
        return new Member(memberId,null,null,account_id, account_pw, money, point);
    }



}
