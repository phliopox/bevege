package Jeong.jdbcRefactoring.Board.Form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class EditPostForm {

    Integer board_id;

    @NotBlank
    String postTitle;

    @NotBlank
    String content;

}
