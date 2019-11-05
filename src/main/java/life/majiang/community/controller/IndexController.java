package life.majiang.community.controller;


import life.majiang.community.dto.PaginationDTO;
import life.majiang.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {

    @Autowired
    private QuestionService questionService;
    @GetMapping("/")
    public String index(Model model,
                        @RequestParam(name = "page",defaultValue = "1") Integer page,
                        @RequestParam(name = "size", defaultValue = "5") Integer size
    ) {


        //把页面的所有信息全部给pagination，model给前端显示信息
        PaginationDTO pagination=questionService.list(page,size);
        //信息包括问题列表，前一页后一页首页尾页的显示标签，当前页，需要显示的页码列表，总页数。
        model.addAttribute("pagination",pagination);



        return "index";
    }

}
