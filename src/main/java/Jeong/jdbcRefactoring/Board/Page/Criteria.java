package Jeong.jdbcRefactoring.Board.Page;

import lombok.Data;

@Data
public class Criteria {
    private int pageNum;
    private int amount;
    private int skip;
    private int boardAmount=10;
    private int itemAmount=8;
    /*public Criteria BoardCriteria(){
        Criteria criteria = new Criteria(1, 10);
        return criteria;
    }
    public Criteria() {
        this(1,10);
        this.skip=0;
    }*/
    public Criteria(int pageNum, int amount) {
        this.pageNum = pageNum;
        this.amount = amount;
        this.skip=((pageNum-1)*amount);
    }
    public static Criteria changePageNum(int pageNum,int amount){
        return new Criteria(pageNum,amount);
    }


 /*   public void setAmount(int amount) {
        this.skip=(this.pageNum-1)*amount;
        this.amount = amount;
    }*/

    @Override
    public String toString() {
        return "Criteria{" +
                "pageNum=" + pageNum +
                ", amount=" + amount +
                '}';
    }
}
