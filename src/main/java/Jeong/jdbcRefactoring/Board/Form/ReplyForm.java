package Jeong.jdbcRefactoring.Board.Form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
public class ReplyForm {

    Integer board_id;

    Integer reply_id;

    String writer;

    @NotBlank
    String content;

    Date regDate;
}
