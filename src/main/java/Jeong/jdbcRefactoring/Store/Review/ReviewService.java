package Jeong.jdbcRefactoring.Store.Review;

import Jeong.jdbcRefactoring.Store.form.ReviewForm;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public List<ReviewForm> getRecentReViewFormList(Integer itemId){
        List<ReviewForm> ReviewList = reviewRepository.getRecentReviewList(itemId);
        return ReviewList;
    }

    public void save (ReviewForm reviewForm){
        reviewRepository.saveReview(reviewForm);
    }

}
