package life.majiang.community.community.provider;

import com.alibaba.fastjson.JSON;
import life.majiang.community.community.dto.AccessTokenDTO;
import life.majiang.community.community.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;



/*
@Controller:把当前的类作为路由Api的承载者
@Component：把当前的类初始化到spring容器的上下文，效果就是被注解的类就会自动实例化放到一个池子里面，我们直接拿来用不再实例化了（IOC）
*/

@Component
public class GithubProvider {
    //拿code换token，返回token
    public String GetAccessToken(AccessTokenDTO accessTokenDTO) {   //属性超过3个就应该创建类来封装
        System.out.println("code"+accessTokenDTO.getCode());
        System.out.println("state"+accessTokenDTO.getState());
        System.out.println("redirect_uri"+accessTokenDTO.getRedirect_uri());
        System.out.println("client_id"+accessTokenDTO.getClient_id());
        System.out.println("client_secret"+accessTokenDTO.getClient_secret());
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
        System.out.println("jason accesstoken "+JSON.toJSONString(accessTokenDTO));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            String token = string.split("&")[0].split("=")[1];
            return token;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //拿token换user信息，返回user对象
    public GithubUser getUser(String accessToken){
        OkHttpClient client = new OkHttpClient();
        System.out.println(accessToken);
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token="+accessToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            GithubUser githubUser = JSON.parseObject(string, GithubUser.class);   //把string的json对象解析成类对象
            System.out.println("username"+ githubUser.getName());
            System.out.println("userid"+ githubUser.getId());
            return githubUser;
        } catch (IOException e) {
            System.out.println("大哥错了");
            e.printStackTrace();
        }
        return null;
    }
}
