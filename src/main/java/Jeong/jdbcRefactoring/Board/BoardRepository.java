package Jeong.jdbcRefactoring.Board;

import Jeong.jdbcRefactoring.Board.Form.BoardForm;
import Jeong.jdbcRefactoring.Board.Form.EditPostForm;
import Jeong.jdbcRefactoring.Board.Page.Criteria;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.util.List;


@Slf4j
@Repository
public class BoardRepository {

    private final JdbcTemplate jdbcTemplate;


    public BoardRepository(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Integer CountPost(){
        String sql="select count(board_id) from board";
        Integer totalCount = jdbcTemplate.queryForObject(sql, Integer.class);
        return totalCount;
    }

    public List<BoardForm> AllPost(Criteria criteria){
        /*String sql="select * from board order by board_id DESC";*/
        String sql="select R1.*from(select*from board order by board_id desc)R1 limit ?,?";
        List<BoardForm> allPosts = jdbcTemplate.query(sql, memberRowMapper(),criteria.getSkip(),criteria.getAmount());
        log.info("getSkip={},getAmount={}",criteria.getSkip(),criteria.getAmount());
        Integer integer = CountPost();
        log.info("all={}",integer);

        return allPosts.isEmpty()?null:allPosts;
    }

    public void savePost(BoardForm form){
        String sql = "insert into board values(ifnull(((select max(board_id) from board N)+1),1),?,?,?,now());";
        jdbcTemplate.update(sql,form.getMemberId(), form.getPostTitle(), form.getContent());
    }
    public void editPost(EditPostForm form){
        String sql="update board set title=?,content=?,regdate=now() where board_id=? ";
        jdbcTemplate.update(sql, form.getPostTitle(), form.getContent(), form.getBoard_id());

    }
    public void deletePost(Integer board_id){
        String sql="delete from board where board_id=?";
        jdbcTemplate.update(sql,board_id);

    }
    public BoardForm findByBoardId(Integer board_id) {
        String sql="select *from board where board_id=?";
        List<BoardForm> formList = jdbcTemplate.query(sql, memberRowMapper(), board_id);
        BoardForm boardForm = formList.get(0);
        return boardForm;
    }
    public List<BoardForm> findById(String memberId){
        String sql=" select *from board where member_id=?";
        List<BoardForm> results=jdbcTemplate.query(sql,memberRowMapper(),memberId);
        return results.isEmpty()?null:results;
    }

   /* private Integer countPost(){
        return (rs,rowNum)
    }*/
    private RowMapper<BoardForm> memberRowMapper(){
        return (rs,rowNum)->{
            BoardForm form=new BoardForm(
            rs.getInt("board_id"),
            rs.getString("member_id"),
            rs.getString("title"),
            rs.getString("content"),
            rs.getDate("regdate"));
            return form;
        };
    }

}
