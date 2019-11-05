package life.majiang.community.advice;

import life.majiang.community.exception.CustomizeException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;


@ControllerAdvice    //实现通用异常处理
public class CustomizeExceptionHandler {
    @ExceptionHandler(Exception.class)
    ModelAndView handle( Throwable e, Model model) {
        if(e instanceof CustomizeException){
            model.addAttribute("message", e.getMessage());
            System.out.println(e);
        }else{
            model.addAttribute("message","服务太热了，返回主页吧~");
            System.out.println(e);
        }
        return new ModelAndView("error");  //访问憨憨页面会报异常，需要返回错误异常到页面去。
    }
}
