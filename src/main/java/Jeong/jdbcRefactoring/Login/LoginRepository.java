package Jeong.jdbcRefactoring.Login;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;



@Repository
public class LoginRepository {

    private final JdbcTemplate jdbcTemplate;
    private final DataSource dataSource;

    public LoginRepository( DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate=new JdbcTemplate(dataSource);
    }


    public String getMemberPwById(String formId){
        String sql = "select*from MEMBER_HOME where member_id=?";
        List<LoginForm> member = jdbcTemplate.query(sql, loginFormRowMapper(), formId);
        return member.isEmpty()?null:member.get(0).getMemberPw();
    }
    public LoginForm getLoginInfo(LoginForm form) {
        String sql = "select member_id,member_pw from MEMBER_HOME where member_id=?";
        List<LoginForm> member = jdbcTemplate.query(sql, loginFormRowMapper(), form.getMemberId());
        return member.isEmpty() ? null : member.get(0);
    }

    private RowMapper<LoginForm> loginFormRowMapper(){
        return (rs,rowNum)->{
            LoginForm form =new LoginForm();
            form.setMemberId(rs.getString("member_id"));
            form.setMemberPw(rs.getString("member_pw"));
            return form;
        };
    }
}
