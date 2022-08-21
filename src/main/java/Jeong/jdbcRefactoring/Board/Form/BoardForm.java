package Jeong.jdbcRefactoring.Board.Form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;


@Data
public class BoardForm {

    Integer board_id;

    String memberId;

    @NotBlank
    String postTitle;

    @NotBlank
    String content;

    Date regDate;

//    Map<Integer,BoardForm> posts = new HashMap<>();
    public BoardForm(){}

    public BoardForm(Integer board_id, String memberId, String postTitle, String content, Date regDate) {
        this.board_id = board_id;
        this.memberId = memberId;
        this.postTitle = postTitle;
        this.content = content;
        this.regDate = regDate;
    }

    public static BoardForm byMemberId(String memberId){
    return new BoardForm(null,memberId,null,null,null);
    }



}
