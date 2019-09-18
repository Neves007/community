package life.majiang.community.community.mapper;

import model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface UserMapper {
    @Insert("insert into user (account_id,name,token,gmt_create,gmt_modified) values (#{accountId},#{name},#{token},#{gmtCreate},#{gmtModified})")
    //把数据库的字段对应user对象属性，并进行插入
    void insert(User user);

    @Select("select * from user where token= #{token}")  //#{value}在mybatis编译时会把下面函数形参里面参数放进去，但只是类的时候才会自动放，不是类的时候，需要加@Param注解
    User findByToken(@Param("token") String token);
}
