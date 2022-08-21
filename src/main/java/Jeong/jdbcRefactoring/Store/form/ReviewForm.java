package Jeong.jdbcRefactoring.Store.form;

import lombok.Data;

import java.util.List;

@Data
public class ReviewForm {

    private String memberId;
    private Integer reviewId;
    private Integer itemId;

    private String title;
    private String review;

    public ReviewForm(String memberId, Integer reviewId, Integer itemId,String title, String review) {
        this.memberId = memberId;
        this.reviewId = reviewId;
        this.itemId = itemId;
        this.review = review;
        this.title=title;
    }
    public ReviewForm(String memberId, Integer itemId,String title, String review) {
        this.memberId = memberId;
        this.itemId = itemId;
        this.review = review;
        this.title=title;
    }
}
