package Jeong.jdbcRefactoring.Login;

import Jeong.jdbcRefactoring.DTO.Member;
import lombok.Data;

import javax.validation.constraints.NotBlank;



@Data
public class LoginForm {

    @NotBlank @NotBlank
    String memberId;

    @NotBlank @NotBlank
    String memberPw;


}
