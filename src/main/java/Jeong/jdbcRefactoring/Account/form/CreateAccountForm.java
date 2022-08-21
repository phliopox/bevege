package Jeong.jdbcRefactoring.Account.form;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
public class CreateAccountForm {

    @NotBlank
    String memberId;

    @NotNull
    String memberPw;

    @NotNull
    Integer accountPw;

}
