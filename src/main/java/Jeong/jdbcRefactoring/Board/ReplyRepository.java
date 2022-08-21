package Jeong.jdbcRefactoring.Board;

import Jeong.jdbcRefactoring.Board.Form.ReplyForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Slf4j
@Repository
public class ReplyRepository {

    private final DataSource dataSource;
    private final SQLExceptionTranslator exTranslator;
    private final JdbcTemplate jdbcTemplate;
    public ReplyRepository(DataSource dataSource) {
        this.dataSource = dataSource;
        this.exTranslator = new SQLErrorCodeSQLExceptionTranslator(dataSource);
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<ReplyForm> allReply(Integer board_id){
        String sql="select*from reply where board_id=?";
        List<ReplyForm> replyList = jdbcTemplate.query(sql, replyFormRowMapper(), board_id);
        return replyList.isEmpty()?null:replyList;
    }

    public void saveReply(Integer board_id,String memberId,String content){
        String sql = "insert into reply values(?,ifnull(((select max(reply_id) from reply N)+1),1),?,?,now())";
        jdbcTemplate.update(sql,board_id,memberId,content);
    }

    public void deleteReply(Integer replyId){
        String sql="delete from reply where reply_id=?";
        jdbcTemplate.update(sql,replyId);
    }

    public void updateReply(Integer replyId, String updateContent) {
        String sql="update reply set content=?,regdate=now() where reply_id=?";
        jdbcTemplate.update(sql,updateContent,replyId);
    }
    private RowMapper<ReplyForm> replyFormRowMapper(){
        return (rs,RowNum)->{
            ReplyForm form=new ReplyForm();
            form.setBoard_id(rs.getInt("board_id"));
            form.setReply_id(rs.getInt("reply_id"));
            form.setWriter(rs.getString("writer"));
            form.setContent(rs.getString("content"));
            form.setRegDate(rs.getDate("regdate"));
            return form;
        };
    }

     /*  public ReplyForm getCommentByReplyId(Integer replyId){
        String sql="select * from reply where reply_id=?";
        List<ReplyForm> replyFormList = jdbcTemplate.query(sql, replyFormRowMapper(), replyId);
        if(!replyFormList.isEmpty()) {
            ReplyForm replyForm = replyFormList.get(0);
            return replyForm;
        }
        return null;
    }*/

    /* public List<ReplyForm> getReply(Integer board_id,String memberId){
        String sql="select*from reply where board_id=? and writer=?";
        List<ReplyForm> replyList = jdbcTemplate.query(sql, replyFormRowMapper(), board_id, memberId);
        return replyList.isEmpty()?null:replyList;

    }*/
}
