package Jeong.jdbcRefactoring.Account.form;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
public class AccountForm {


    String memberId;

    @NotBlank
    String accountId;

    @NotNull
    Integer accountPw;


}
