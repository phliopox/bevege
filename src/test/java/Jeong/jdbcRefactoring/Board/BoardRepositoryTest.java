package Jeong.jdbcRefactoring.Board;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class BoardRepositoryTest {
    BoardRepository boardRepository;


static long sequence =0L;

    @Test
    void sequence() {
        //Integer integer = ++BoardRepository.sequence;
        log.info("sequence 출력 테스트 ={}", ++sequence);
    }

    @Test
    void savePost() {
    }

    @Test
    void findByBoardId() {
    }
}