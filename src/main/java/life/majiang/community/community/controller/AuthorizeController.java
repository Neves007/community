package life.majiang.community.community.controller;

import life.majiang.community.community.dto.AccessTokenDTO;
import life.majiang.community.community.dto.GithubUser;
import life.majiang.community.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("/callback")
    public String callback(@RequestParam(name="code") String code,  //通过@RequestParam(name="code") String code  接受参数，参数名为code，类型为String
                           @RequestParam(name = "state") String state) {
        //拿到这两个参数后，使用okhttp模拟post请求，向git发送：POST https://github.com/login/oauth/access_token  获取tocken
        //我们要发请求不在controller里面发，我们创建package隔离不同的业务

        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();

        accessTokenDTO.setCode(code);
        accessTokenDTO.setState(state);
        accessTokenDTO.setRedirect_uri(redirect_uri);
        accessTokenDTO.setClient_id(client_id);
        accessTokenDTO.setClient_secret(client_secret);
        String accessToken = githubProvider.GetAccessToken(accessTokenDTO);
        GithubUser user = githubProvider.getUser(accessToken);
        System.out.println(user.getId());  //成功拿到user信息
        return "index";  //登录成功返回index页面
    }


}
