package com.wuhan.tracedemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wuhan.tracedemo.entity.LogisticInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;

/**
 * @author Chris
 * @date 2021/7/12 23:02
 * @Email:gem7991@dingtalk.com
 * 可以在Mapper的资源文件写sql脚本，也可以使用注解
 */
public interface LogisticMapper extends BaseMapper<LogisticInfo> {
    LogisticInfo getLogisticById2(Long id);

    @Insert("insert into logistic_info(`good_id`,`description`,`status`,`create_time`) values(#{goodId},#{description},#{status},#{createTime})")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    void insertData(LogisticInfo logisticInfo);
}
