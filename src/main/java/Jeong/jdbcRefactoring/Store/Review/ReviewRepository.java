package Jeong.jdbcRefactoring.Store.Review;

import Jeong.jdbcRefactoring.Store.form.ReviewForm;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ReviewRepository {

    private final JdbcTemplate jdbcTemplate;

    public ReviewRepository(JdbcTemplate jdbcTemplate, DataSource dataSource){
        this.jdbcTemplate=new JdbcTemplate(dataSource);
    }

    public void saveReview(ReviewForm reviewForm){
        String sql="insert into review_board values(?,ifnull(((select max(review_id) from review_board N)+1),1),?,?,?)";
        jdbcTemplate.update(sql, reviewForm.getMemberId(), reviewForm.getItemId(), reviewForm.getReview(), reviewForm.getTitle());


    }

    public List<ReviewForm> getRecentReviewList(Integer itemId){
            String sql="select*from review_board where item_id=? order by review_id DESC limit 0,5";
        List<ReviewForm> ReviewFormList = jdbcTemplate.query(sql, new RowMapper<ReviewForm>() {
            @Override
            public ReviewForm mapRow(ResultSet rs, int rowNum) throws SQLException {
                ReviewForm reviewForm = new ReviewForm(
                        rs.getString("member_id"),
                        rs.getInt("review_id"),
                        rs.getInt("item_id"),
                        rs.getString("title"),
                        rs.getString("review")

                );
                return reviewForm;
            }
        },itemId);
        return ReviewFormList.isEmpty()?null:ReviewFormList;

    }


}
