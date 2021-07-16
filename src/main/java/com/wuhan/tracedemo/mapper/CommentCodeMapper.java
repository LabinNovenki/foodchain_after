package com.wuhan.tracedemo.mapper;

import com.wuhan.tracedemo.entity.CommentCode;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface CommentCodeMapper {
    @Insert("insert into comment_code(`userid`,`commentid`,`is_used`) " +
            "values(#{userid},#{commentid},#{is_used})")
    public void insert(CommentCode commentCode);

    @Select("select * from comment_code where commentid=#{commentid}")
    public CommentCode select(String commentid);

    @Update("update comment_code set is_used=1 where commentid=#{commentid}")
    public boolean update(String commentid);
}
