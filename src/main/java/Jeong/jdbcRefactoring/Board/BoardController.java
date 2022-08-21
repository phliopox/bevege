package Jeong.jdbcRefactoring.Board;

import Jeong.jdbcRefactoring.Board.Form.BoardForm;
import Jeong.jdbcRefactoring.Board.Form.EditPostForm;
import Jeong.jdbcRefactoring.Board.Form.ReplyForm;
import Jeong.jdbcRefactoring.Board.Page.Criteria;
import Jeong.jdbcRefactoring.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/board")
public class BoardController {


    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService=boardService;
    }


    @GetMapping
    public String board(@RequestParam(required = false, value="pageIndex") String param, Model model) {
        //List<BoardForm> boardForms = boardRepository.AllPost();

        Integer pageNum;
        Criteria criteria;
        if(param==null){
            pageNum=1;
            criteria=Criteria.changePageNum(pageNum,10);
        }else{
            pageNum=Integer.parseInt(param);
            criteria = Criteria.changePageNum(pageNum,10);
        }

        model.addAttribute("pageList",boardService.Page(criteria));
        model.addAttribute("posts", boardService.PostList(criteria));

        return "refactor/board/board";
    }


    @GetMapping("/{pageNum}/{board_id}")
    public String postDetail(@ModelAttribute("reply") ReplyForm form,
                             @PathVariable Integer board_id, @PathVariable Integer pageNum,HttpServletRequest request, Model model) {

        String memberId = getIdBySession(request);
        if(checkPostWriter(memberId,board_id)){
            model.addAttribute("canEdit","canEdit");
        }

        /*타인이 쓴 게시물 볼 경우*/
        postAndReply(board_id, model);
        model.addAttribute("userId",memberId);
        model.addAttribute("pageNum",pageNum);
        return "refactor/board/postDetail";
    }


    /*타인이 쓴 게시물 볼 경우->댓글 입력*/
    @PostMapping("/{pageNum}/{board_id}")
    public String postDetailPost(@Validated @ModelAttribute("reply") ReplyForm form, BindingResult bindingResult,
                                 HttpServletRequest request, @PathVariable Integer board_id, @PathVariable Integer pageNum, Model model){

        String memberId = getIdBySession(request);
        if(memberId==null||bindingResult.hasErrors()){
            postAndReply(board_id, model);

            if(memberId==null){bindingResult.reject("content", "로그인이 필요합니다.");}
            return "refactor/board/postDetail";
        }

        boardService.saveNewReply(board_id,memberId,form.getContent());
        return"redirect:/board/"+pageNum+"/"+board_id;
    }


    @GetMapping("/editPost/{pageNum}/{board_id}")
    public String myPostGet(@ModelAttribute("member") EditPostForm form,
                         @PathVariable Integer board_id,@PathVariable Integer pageNum,Model model,HttpServletRequest request){
        String memberId = getIdBySession(request);
        if(!checkPostWriter(memberId,board_id)){return "redirect:/login";}

        BoardForm postInfo = boardService.BoardFormByBoardId(board_id);
        model.addAttribute("post", postInfo);
        model.addAttribute("pageNum",pageNum);
        return "refactor/board/editPost";
    }

    @PostMapping("/editPost/{pageNum}/{board_id}")
    public String myPost(@Validated @ModelAttribute("member") EditPostForm form,
                         BindingResult bindingResult,
                         @PathVariable Integer board_id,@PathVariable Integer pageNum) {

        if(bindingResult.hasErrors()){return "refactor/board/editPost";}
        boardService.editPost(form);
        return "redirect:/board/{pageNum}/{board_id}";
    }

    @GetMapping("/delete/{board_id}")
    public String deletePost(@PathVariable Integer board_id){
        boardService.deletePost(board_id);
        return "redirect:/board";
    }


    @GetMapping("/new_post")
    public String newPostGet(@ModelAttribute("member") BoardForm form,HttpServletRequest request,Model model){
        String memberId = getIdBySession(request);
        BoardForm boardForm = BoardForm.byMemberId(memberId);
        model.addAttribute("member",boardForm);
        return "refactor/board/newPost";
    }

    @PostMapping("/new_post")
    public String newPost(@Validated @ModelAttribute("member") BoardForm form,
                          BindingResult bindingResult,
                          HttpServletRequest request){

        if(bindingResult.hasErrors()){return "refactor/board/newPost";}


        boardService.saveNewPost(form);
        return "redirect:/board/";
    }

    @ResponseBody
    @PostMapping("/replyDelete")
    public Integer replyDelete(@RequestBody Map<String,Object> param){
        int replyId =Integer.parseInt(String.valueOf(param.get("replyId")));
        boardService.deleteReplyByReplyId(replyId);
        return 1;
    }

    @ResponseBody
    @PostMapping("/replyUpdate")
    public Integer replyUpdate(@RequestBody Map<String,Object> param){
        int replyId = Integer.parseInt(String.valueOf(param.get("replyId")));
        String updateContent = (String) param.get("updateContent");
        boardService.updateReply(replyId,updateContent);
        return 1;

    }
    private void postAndReply(Integer board_id, Model model) {
        Map<String, Object> stringObjectMap = boardService.contentAndReply(board_id);
        List replies = (List)stringObjectMap.get("replies");

        model.addAttribute("post", stringObjectMap.get("post"));
        if(replies!=null&&replies.size()>0){
        model.addAttribute("replies", replies);
        model.addAttribute("reply_count",replies.size());}
    }

    private boolean checkPostWriter(String memberId,int board_id) {
        String postWriter = boardService.getPostWriter(board_id);
        return memberId != null && memberId.equals(postWriter);
    }


    private String getIdBySession(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        return session!=null?(String) session.getAttribute(SessionConst.MEMBER_LOGIN_SESSION):null;
    }

}
