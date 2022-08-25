package Jeong.jdbcRefactoring.Store.Review;

import Jeong.jdbcRefactoring.Store.ItemFileStore;
import Jeong.jdbcRefactoring.Store.form.ReviewForm;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;


import static Jeong.jdbcRefactoring.SessionConst.MEMBER_LOGIN_SESSION;

@Slf4j
@Controller
public class ReviewController {
    private final ItemFileStore fileStore;
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService,ItemFileStore fileStore) {
        this.reviewService = reviewService;
        this.fileStore=fileStore;
    }

    @ResponseBody
    @GetMapping("/items/{pageNum}/{itemId}/reviews")
    public List<ReviewForm> getReviewList(@PathVariable Integer itemId) throws JsonProcessingException {
        List<ReviewForm> recentReViewFormList = reviewService.getRecentReViewFormList(itemId);
        if (recentReViewFormList != null) {

            return recentReViewFormList;
        }
        return null;
    }

    @ResponseBody
    @PostMapping("/items/review_enroll/img_enroll")
    public Map<String,Object> uploadImgFile(@RequestParam("upload") MultipartFile img) throws IOException {
       return fileStore.returnTypeMap(img);
    }

    @ResponseBody
    @GetMapping("/reviews/image/{filename}")
    public Resource downloadImage(@PathVariable String filename)throws MalformedURLException {
        return new UrlResource("file:" + fileStore.getFullPathToReviewImg(filename));
    }

    @ResponseBody
    @PostMapping("/items/{pageNum}/{itemId}/review_enroll")
    public Integer saveReview(@RequestBody Map<String,Object> review, HttpServletRequest request,
                              @PathVariable Integer itemId) {
        log.info("title ={}",review.get("title"));
        log.info("ck 에디터 getData={}",String.valueOf(review.get("ckeditor")));
        HttpSession session = request.getSession(false);
        String memberId = (String) session.getAttribute(MEMBER_LOGIN_SESSION);
        ReviewForm reviewForm = new ReviewForm(memberId, itemId, String.valueOf(review.get("title")), String.valueOf(review.get("ckeditor")));
        reviewService.save(reviewForm);
        return 1;
    }
}
