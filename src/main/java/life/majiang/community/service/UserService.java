package life.majiang.community.service;

import life.majiang.community.dto.GithubUser;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.User;
import life.majiang.community.model.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public User createOrUpdate(GithubUser githubUser) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andAccountIdEqualTo(githubUser.getId().toString());
        List<User> users = userMapper.selectByExample(userExample);
        User dbUser = new User();

        if (users.size() == 0) {
            //插入
            dbUser.setAccountId(String.valueOf(githubUser.getId()));
            dbUser.setName(githubUser.getName());
            String token = UUID.randomUUID().toString();
            dbUser.setToken(token);  //随机写个token
            dbUser.setGmtCreate(System.currentTimeMillis());
            dbUser.setGmtModified(dbUser.getGmtCreate());
            dbUser.setAvatarUrl(githubUser.getAvatarUrl());
            System.out.println("user"+dbUser);
            userMapper.insert(dbUser);
        } else {
            dbUser = users.get(0);
            //更新
            dbUser.setName(githubUser.getName());
            String token = UUID.randomUUID().toString();
            dbUser.setGmtCreate(System.currentTimeMillis());
            dbUser.setGmtModified(dbUser.getGmtCreate());
            dbUser.setAvatarUrl(githubUser.getAvatarUrl());
            dbUser.setToken(token);
            userExample.createCriteria()
                    .andIdEqualTo(dbUser.getId());
            userMapper.updateByExampleSelective(dbUser,userExample);
        }
        return dbUser;
    }
}
