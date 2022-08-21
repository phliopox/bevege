package Jeong.jdbcRefactoring.Join;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
public class JoinForm {

    @NotNull @NotBlank
    String memberId;

    @NotNull @NotBlank
    String memberPw;

    @NotNull @NotBlank @Email
    String memberEmail;


    public JoinForm(String memberId, String memberPw, String memberEmail) {
        this.memberId = memberId;
        this.memberPw = memberPw;
        this.memberEmail = memberEmail;
    }
}
