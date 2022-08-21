package Jeong.jdbcRefactoring.Account.form;

import lombok.Data;
import javax.validation.constraints.*;

@Data
public class TransferMoneyForm {


    private String memberId;

    @NotBlank
    private String accountId;

    @NotNull
    private Integer accountPw;

    @NotBlank
    private String money;

    @NotBlank
    private String receiverAccountId;

    @NotNull
    @Max(5000000)
    private Integer sendMoney;


}
