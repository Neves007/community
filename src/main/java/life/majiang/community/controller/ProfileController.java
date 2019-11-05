package life.majiang.community.controller;

import life.majiang.community.dto.PaginationDTO;
import life.majiang.community.service.QuestionService;
import life.majiang.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {
    @Autowired
    private QuestionService questionService;

    @GetMapping("/profile/{action}")
    //通过切换路径，来动态的调整样式
    public String profile(HttpServletRequest request,
                          @PathVariable(name = "action") String action,
                          @RequestParam(name = "page",defaultValue = "1") Integer page,
                          @RequestParam(name = "size", defaultValue = "5") Integer size,
                          Model model) {
        //我希望在这个路径下用户是登陆的。
        User user = (User) request.getSession().getAttribute("user");
        if(user==null){
            return "redirect:/";
        }

        if ("questions".equals(action)) {  //为什么要用equals
            model.addAttribute("section", "questions");
            model.addAttribute("sectionName", "我的提问");

        } else if ("replies".equals(action)) {  //为什么要用equals
            model.addAttribute("section", "replies");
            model.addAttribute("sectionName", "最新回复");

        }
        //我的问题的分页显示
        PaginationDTO paginationDTO = questionService.list(user.getId(), page, size);
        model.addAttribute("pagination",paginationDTO);
        return "profile";
    }
}
