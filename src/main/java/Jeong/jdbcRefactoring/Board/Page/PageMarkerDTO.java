package Jeong.jdbcRefactoring.Board.Page;

import lombok.Data;

@Data
public class PageMarkerDTO {
    private int startPage;
    private int endPage;
    private boolean prev,next;
    private int total;

    private int pageNum;
    private Criteria cri;

    public PageMarkerDTO(Criteria cri,int total){
        this.cri=cri;
        this.total=total;
        this.pageNum=cri.getPageNum();

        this.endPage=(int)(Math.ceil(cri.getPageNum()/10.0))*10;
        this.startPage=this.endPage-9;
        int realEnd=(int)(Math.ceil(total*1.0/cri.getAmount()));
        if(realEnd<this.endPage){
            this.endPage=realEnd;
        }

        this.prev=this.startPage>1;
        this.next=this.endPage<realEnd;
    }
}
