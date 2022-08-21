package Jeong.jdbcRefactoring.Account;



import Jeong.jdbcRefactoring.Account.form.CreateAccountForm;
import Jeong.jdbcRefactoring.DTO.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;


/**
 * getAccountIfo() noSuchElementException throw
 */
@Slf4j
@Repository
@Primary
public class AccountRepository2 {

    /* transfer money 로직 , 계좌정보 로직*/

    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;

    public AccountRepository2(DataSource dataSource) {
        this.dataSource=dataSource;
        this.jdbcTemplate=new JdbcTemplate(dataSource);

    }

    public void createAccount(CreateAccountForm member){
        String sql="insert into account(member_id,account_id,account_pw) values(?,?,?)";

        String stringTemp= String.valueOf(Math.random()).substring(2,16);
        String accountId=stringTemp.substring(0,3)+"-"+stringTemp.substring(4,7)+"-"+stringTemp.substring(8,14);

        jdbcTemplate.update(sql, member.getMemberId(), accountId, member.getAccountPw());

    }

    public Member getAccountInfo(String memberId){
        String sql="select*from account where member_id=?";

        List<Member> memberList = jdbcTemplate.query(sql, memberRowMapper(), memberId);
        if (memberList.isEmpty()) {
            log.info("getAccountInfo : member not found"+memberId);
            return null;
        } else {
            return memberList.get(0);
        }
    }

    public String getIdByAccount(String accountId){
        String sql="select*from account where account_id=?";

        List<Member> memberList = jdbcTemplate.query(sql, memberRowMapper(), accountId);
        if (memberList.isEmpty()) {
            log.info("getIdAccount: member not found by accountId"+accountId);
            return null;
        }else{
            Member member = memberList.get(0);
            return member.getMemberId();
        }
    }

    public void transferPoint(Integer resultMoney, Integer resultPoint, String memberId){
        String sql="update account set money=?,point=? where member_id=?";
        jdbcTemplate.update(sql,resultMoney,resultPoint,memberId);
    }

    public void updateMoney(String memberId,Integer sendMoney){
        String sql="update account set money=? where member_id=?";

        Member accountInfo = getAccountInfo(memberId);
        Integer resultMoney = accountInfo.getMoney() + sendMoney;
        int update = jdbcTemplate.update(sql, resultMoney, memberId);
        if(update==0){
            log.info("updateMoney() failed.. memberId={}",memberId);
        }
    }

    public void updateMoneyAndPoint(Integer money,Integer point, String memberId){
        String sql = "update account set money=?,point=? where member_id=?";
        jdbcTemplate.update(sql,money,point, memberId);
    }

    private RowMapper<Member> memberRowMapper(){
        return (rs,rowNum)->{
            Member member=new Member();
            member.setMemberId(rs.getString("member_id"));
            member.setAccountId(rs.getString("account_id"));
            member.setAccountPw(rs.getInt("account_pw"));
            member.setMoney(rs.getInt("money"));
            member.setPoint(rs.getInt("point"));
            return member;
        };
    }
}
