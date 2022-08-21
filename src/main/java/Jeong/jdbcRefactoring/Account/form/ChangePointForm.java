package Jeong.jdbcRefactoring.Account.form;

import lombok.Data;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ChangePointForm {

    @NotBlank
    String memberId;

    @NotBlank
    String accountId;

    @NotNull
    String point;

    @NotNull
    @Min(value = 1000,message = "1000point 이상만 전환 가능합니다.")
    Integer changeMoney;

}
