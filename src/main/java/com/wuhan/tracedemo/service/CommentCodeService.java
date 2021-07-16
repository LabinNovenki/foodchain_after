package com.wuhan.tracedemo.service;

import com.wuhan.tracedemo.entity.CommentCode;

public interface CommentCodeService {
    CommentCode createCommentCode(String userid);
    void saveCommentCode(CommentCode commentCode);
    CommentCode getCommentCode(String commentid);
    boolean updateCommentCode(String commentid);
}
