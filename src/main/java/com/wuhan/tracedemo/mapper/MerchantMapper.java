package com.wuhan.tracedemo.mapper;

import com.wuhan.tracedemo.entity.Merchant;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author Labin
 * @date 2021/7/13 18:49
 * @Email:abc3312376711@gmail.com
 * 可以在Mapper的资源文件写sql脚本，也可以使用注解
 */

@Mapper
public interface MerchantMapper {
    @Select("select * from merchant where id=#{id}")
    public Merchant getById(Long id);

    @Select("select * from merchant where name=#{name}")
    public Merchant getByName(String name);

    @Insert("insert into merchant(`name`,`address`, `introduction`, `phone`,`password`) " +
            "values(#{name},#{address},#{introduction}, #{phone},#{password})")
    public Long insert(Merchant merchant);

}
