package Jeong.jdbcRefactoring.Board;

import Jeong.jdbcRefactoring.Board.Form.BoardForm;
import Jeong.jdbcRefactoring.Board.Form.EditPostForm;
import Jeong.jdbcRefactoring.Board.Form.ReplyForm;
import Jeong.jdbcRefactoring.Board.Page.Criteria;
import Jeong.jdbcRefactoring.Board.Page.PageMarkerDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@Transactional
public class BoardService {

    private final ReplyRepository replyRepository;
    private final BoardRepository boardRepository;

    public BoardService(ReplyRepository replyRepository,BoardRepository boardRepository){
        this.boardRepository=boardRepository;
        this.replyRepository=replyRepository;
    }



    public List<BoardForm> PostList(Criteria criteria){
        return boardRepository.AllPost(criteria);
    }

    public PageMarkerDTO Page(Criteria criteria){
        Integer total = boardRepository.CountPost();
        PageMarkerDTO Marker = new PageMarkerDTO(criteria, total);

        return Marker;
    }

    public void saveNewPost(BoardForm form){
        boardRepository.savePost(form);
    }
    public void editPost(EditPostForm form){
        boardRepository.editPost(form);
    }
    public void deletePost(Integer board_id){
        replyRepository.deleteReply(board_id);
        boardRepository.deletePost(board_id);
    }
    public Map<String,Object> contentAndReply(Integer board_id){
        BoardForm byBoardId = boardRepository.findByBoardId(board_id);
        List<ReplyForm> replyForms = replyRepository.allReply(board_id);

        Map<String,Object> postMap =new HashMap<>();

        postMap.put("post",byBoardId);
        postMap.put("replies",replyForms);

        return postMap;
    }

    public BoardForm BoardFormByBoardId(int board_id){
        return boardRepository.findByBoardId(board_id);
    }
    public String getPostWriter(int board_id){
        return boardRepository.findByBoardId(board_id).getMemberId();
    }

    public void saveNewReply(Integer board_id,String memberId,String content){
        replyRepository.saveReply(board_id,memberId,content);
    }

    public void deleteReplyByReplyId(Integer replyId){
        replyRepository.deleteReply(replyId);
    }

    public void updateReply(Integer replyId, String updateContent){
        replyRepository.updateReply(replyId,updateContent);
    }

   /* public Integer getBoardIdByReply(Integer replyId){
        ReplyForm replyForm = replyRepository.getCommentByReplyId(replyId);
        Integer board_id = replyForm.getBoard_id();
        return board_id;
    }*/
}
