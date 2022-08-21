package Jeong.jdbcRefactoring.Join;

import Jeong.jdbcRefactoring.DTO.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * duplicatedKeyException throw
 * SQL -> RUNTIME throw
 */
@Slf4j
@Repository
public class JointRepository {


    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;
    public JointRepository(DataSource dataSource){
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Connection getConnection()throws SQLException{
        Connection con=dataSource.getConnection();
        return con;
    }

    public void save(JoinForm form){
        String sql="insert into MEMBER_HOME(member_id,member_pw,member_email) values(?,?,?)";

        jdbcTemplate.update(sql,form.getMemberId(),form.getMemberPw(),form.getMemberEmail());
    }

    public boolean checkDuplicatedId(String memberId){
        String sql="select*from MEMBER_HOME where member_id=?";

        List<JoinForm> member = jdbcTemplate.query(sql, memberRowMapper(), memberId);
        return !member.isEmpty() ? true : false;
    }

    private RowMapper<JoinForm> memberRowMapper(){
        return (rs,rowNum)->{
            JoinForm form = new JoinForm(
                    rs.getString("member_id"),
                    rs.getString("member_pw"),
                    rs.getString("member_email")
            );
            return form;
        };
    }

}
