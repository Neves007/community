package life.majiang.community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PaginationDTO {
    private List<QuestionDTO> questions;
    private boolean showPrevious;
    private boolean showFirstPage;
    private boolean showNext;
    private boolean showEndPage;
    private Integer page;
    private List<Integer> pages=new ArrayList<>();
    private Integer totalPage;

    //总页数，是否展示上一页，下一页，首页，尾页。
    public void setPagination(Integer page, Integer size) {
        //当前页面
        this.page=page;
        //pages 页码组的设定,
        pages.add(page);
        for (int i = 1; i < 3; i++) {
            if (page-i>0){
                pages.add(0,page-i);
            }
            if (page+ i <=totalPage){
                pages.add(page+i);
            }
        }
        //第一页的时候没有上一页
        if(page == 1){
            showPrevious = false;
        }else{
            showPrevious =true;
        }
        //最后一页的时候没有下一页
        if(page == totalPage){
            showNext = false;
        }else{
            showNext = true;
        }


        //当页码组包含第一页就不展示返回首页
        if(pages.contains(1)){
            showFirstPage = false;
        }else{
            showFirstPage=true;
        }
        if(pages.contains(totalPage)){
            showEndPage = false;
        }else{
            showEndPage=true;
        }
    }
}
