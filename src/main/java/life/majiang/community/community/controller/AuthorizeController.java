package life.majiang.community.community.controller;

import com.sun.deploy.net.HttpResponse;
import life.majiang.community.community.dto.AccessTokenDTO;
import life.majiang.community.community.dto.GithubUser;
import life.majiang.community.community.mapper.UserMapper;
import life.majiang.community.community.provider.GithubProvider;
import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class AuthorizeController {

    @Autowired   //把spring实例化好的实例加载到当前使用的上下文
    private GithubProvider githubProvider;

    @Value("${github.client.id}")    //从application.properties找出github.client.id得value并赋值给client_id
    private String client_id;
    @Value("${github.client.secret}")
    private String client_secret;
    @Value("${github.redirect.uri}")
    private String redirect_uri;

    @Autowired
    private UserMapper userMapper;  //为什么爆红，因为idea不认为@Mapper可以生成bean，加一个@Component就可以了

    @GetMapping("/callback")
    public String callback(@RequestParam(name="code") String code,  //通过@RequestParam(name="code") String code  接受参数，参数名为code，类型为String
                           @RequestParam(name = "state") String state,
                           HttpServletRequest request,
                           HttpServletResponse response) {  //当我们把 HttpServletRequest request写到方法里，spring就会自动把上文中的request放进来供我们使用。
        //拿到这两个参数后，使用okhttp模拟post请求，向git发送：POST https://github.com/login/oauth/access_token  获取tocken
        //我们要发请求不在controller里面发，我们创建package隔离不同的业务

        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();

        accessTokenDTO.setCode(code);
        accessTokenDTO.setState(state);
        accessTokenDTO.setRedirect_uri(redirect_uri);
        accessTokenDTO.setClient_id(client_id);
        accessTokenDTO.setClient_secret(client_secret);

        String accessToken = githubProvider.GetAccessToken(accessTokenDTO);
        GithubUser githubUser = githubProvider.getUser(accessToken);
        System.out.println("accesstoken "+accessToken);  //成功拿到user信息，把user存到数据库
        if(githubUser!=null){

            User user = new User();
            String token = UUID.randomUUID().toString();
            user.setToken(token);  //随机写个token
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
            //登录成功，写cokie和session
            response.addCookie(new Cookie("token",token));
            return "redirect:/";
        }
        else{
            System.out.println("艹！GitHubuser没拿到");
            //登录失败，重新登录
            return "redirect:/";
        }
    }


}
