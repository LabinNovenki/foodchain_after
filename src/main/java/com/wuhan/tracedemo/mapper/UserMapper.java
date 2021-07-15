package com.wuhan.tracedemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wuhan.tracedemo.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Chris
 * @date 2021/7/12 21:44
 * @Email:gem7991@dingtalk.com
 */
public interface UserMapper extends BaseMapper<User>{
    User getUserByName(String userName);

    List<String> queryAllPerms(Long userId);

    /**
     * 修改密码
     */
    void updatePassword(@Param("username") String username, @Param("newPassword") String newPassword,@Param("salt") String salt);
}