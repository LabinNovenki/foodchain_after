package com.wuhan.tracedemo.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import com.wuhan.tracedemo.entity.Good;

/**
 * @author Labin
 * @date 2021/7/13 18:49
 * @Email:abc3312376711@gmail.com
 * 可以在Mapper的资源文件写sql脚本，也可以使用注解
 */

@Mapper
public interface GoodMapper {
    @Select("select * from good where id=#{id}")
    public Good getById(Long id);

    @Select("select * from good where name=#{name}")
    public Good getByName(String name);

    @Insert("insert into good(`id`, `name`,`weight`,`price`) values(#{id},#{name},#{weight},#{price})")
    public Long insert(Good good);

}
