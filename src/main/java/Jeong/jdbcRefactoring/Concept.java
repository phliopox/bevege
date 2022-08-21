package Jeong.jdbcRefactoring;

public class Concept {
    /*
     *
     * Login 과 계좌를 분리하고   -login , join, account 를 다 분리함 ..
     * db 테이블도 분리하고 join  -ok
     * 회원가입시 검증 단계 추가 -ok
     * 오류페이지 생성
     * 부트스트랩 적용-ok?
     *
     * 추가해야할 기능
     * LoginRepository &controller-ok
     * 로그아웃-ok
     * 아이디 찾기
     * 비밀번호 찾기
     *
     * 리팩토링해야할 것
     * 서비스로직에서 컨트롤러로 메세지 던지는 거 어캐할까
     * (현재 모두 에러를 던져서 에러 종류에따라 리턴 매핑을 다르게함)
     * -> 컨트롤러에 try catch 가 너무 많아 졋다 ...
     *
     * 컨트롤러 repository 의존 안하게끔 서비스로 다 빼야한당 ..
     *
     *
     *
     * */
    /*
     * if null 체크보다는 차라리 예외를 던지고, controlleradviser 공부해서 해보기 .... ?
     * */

    /*
     * 게시물 index에 관하여
     * db seq 가 가장 쉬운 방법인데 -> h2 seq 만의 오류가 발생한다 (컴터 다시시작시 seq 값이 33씩 증가되어잇음 항상 초기화가 필요 )
     * 언니의 조언 -> for 문으로 index 넣기
     * 지금 board 가 table로 구성되어잇어서 index를 어떻게 넣어야할지 고민 ...
     *
     *
     * */
}
