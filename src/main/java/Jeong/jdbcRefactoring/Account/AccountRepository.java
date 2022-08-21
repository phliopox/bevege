package Jeong.jdbcRefactoring.Account;


import Jeong.jdbcRefactoring.Account.form.CreateAccountForm;
import Jeong.jdbcRefactoring.DTO.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.NoSuchElementException;


/**
 * getAccountIfo() noSuchElementException throw
 */
@Slf4j
public class AccountRepository {

    // transfer money 로직 , 계좌정보 로직

    private final DataSource dataSource;
    private final SQLExceptionTranslator exTranslator;

    public AccountRepository(DataSource dataSource) {
        this.exTranslator = new SQLErrorCodeSQLExceptionTranslator(dataSource);
        this.dataSource=dataSource;
    }
    private Connection getConnection()throws SQLException {
        Connection con = dataSource.getConnection();
        return con;
    }

    public void createAccount(CreateAccountForm member){
        String sql="insert into account(member_id,account_id,account_pw) values(?,?,?)";
        String stringTemp= String.valueOf(Math.random()).substring(2,16);
        String accountId=stringTemp.substring(0,3)+"-"+stringTemp.substring(4,7)+"-"+stringTemp.substring(8,14);


        Connection con=null;
        PreparedStatement pstmt =null;
        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, member.getMemberId());
            pstmt.setString(2,accountId);
            pstmt.setInt(3,member.getAccountPw());
            int i = pstmt.executeUpdate();
            if(i==0){
                log.info("create account failed");
            }
        }catch (SQLException e){
            throw exTranslator.translate("createAccount()", sql, e);
        }finally {
            close(con,pstmt,null);
        }
    }

    public Member getAccountInfo(String memberId){
        String sql="select*from account where member_id=?";

        Connection con=null;
        PreparedStatement pstmt=null;
        ResultSet rs=null;
        try{
            con=getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);
            rs = pstmt.executeQuery();

            if(rs.next()){
                Member member=Member.AccountRepositoryMember(
                        memberId,
                        rs.getString("account_id"),
                        rs.getInt("account_pw"),
                        rs.getInt("money"),
                        rs.getInt("point")
                );

                return member;
            }else{
                throw new NoSuchElementException("member not found memberId="+memberId);
            }
        }catch (SQLException e){
            throw exTranslator.translate("getAccountInfo()", sql, e);
        }finally {
            close(con,pstmt,rs);
        }
    }

    public String getIdByAccount(String accountId){
        String sql="select member_id from account where account_id=?";

        Connection con=null;
        PreparedStatement pstmt=null;
        ResultSet rs=null;
        try{
            con=getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, accountId);
            rs = pstmt.executeQuery();

            if(rs.next()){

                String memberId = rs.getString("member_id");
                return memberId;
            }else{
                throw new NoSuchElementException("member_id not found by accountId="+accountId);
            }
        }catch (SQLException e){
            throw exTranslator.translate("getIdByAccount()", sql, e);
        }finally {
            close(con,pstmt,rs);
        }
    }




    public void PointToMoney(String memberId,int amount){
        String sql="update account set money=?,point=? where member_id=?";

        Integer money = getAccountInfo(memberId).getMoney();
        int resultMoney=money+amount;

        Integer point = getAccountInfo(memberId).getPoint();
        int resultPoint=point-amount;


        Connection con=null;
        PreparedStatement pstmt=null;
        try{
            con=getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1,resultMoney);
            pstmt.setInt(2, resultPoint);
            pstmt.setString(3,memberId);
            int i = pstmt.executeUpdate();
            if(i==0){
                log.info("PointToMoney()_update money failed");
            }

        }catch (SQLException e){
            throw exTranslator.translate("", sql, e);
        }finally {
            close(con,pstmt,null);
        }
    }

    public void updateMoney(String memberId,Integer sendMoney){
        String sql="update account set money=? where member_id=?";

        Member accountInfo = getAccountInfo(memberId);
        Integer money = accountInfo.getMoney();

        Connection con=null;
        PreparedStatement pstmt=null;

        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, money+sendMoney);
            pstmt.setString(2, memberId);
            int i = pstmt.executeUpdate();
            if(i==0){
                log.info("sendMoney() failed");
            }
        }catch(SQLException e){
            throw exTranslator.translate("sendMoney", sql, e);
        }

    }


    public void receiveMoney(String memberId, Integer receiveMoney){
        String sql = "update account set money=? where member_id=?";
    }



    private void close(Connection con, Statement stmt, ResultSet rs){
        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(stmt);
        DataSourceUtils.releaseConnection(con,dataSource);
    }
}
