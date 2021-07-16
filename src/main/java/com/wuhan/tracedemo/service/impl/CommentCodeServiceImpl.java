package com.wuhan.tracedemo.service.impl;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import com.wuhan.tracedemo.entity.CommentCode;
import com.wuhan.tracedemo.mapper.CommentCodeMapper;
import com.wuhan.tracedemo.service.CommentCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
public class CommentCodeServiceImpl implements CommentCodeService {
    public CommentCode createCommentCode(String userid) {
        CommentCode commentCode= new CommentCode();
        commentCode.setUserid(userid);

        try {
            String str = userid + new Date();
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(str.getBytes());
            byte hash[]  = digest.digest();
            StringBuffer result = new StringBuffer(hash.length * 2);
            for (int i = 0; i < hash.length; i++) {
                if (((int) hash[i] & 0xff) < 0x10) {
                    result.append("0");
                }
                result.append(Long.toString((int) hash[i] & 0xff, 16));
            }

            System.out.print(result.toString());
            commentCode.setCommentid(result.toString());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return commentCode;
    }

    @Autowired
    CommentCodeMapper commentCodeMapper;
    public void saveCommentCode(CommentCode commentCode) {
        this.commentCodeMapper.insert(commentCode);
    }

    public CommentCode getCommentCode(String commentid) {
        return this.commentCodeMapper.select(commentid);
    }

    public boolean updateCommentCode(String commentid) {
        return this.commentCodeMapper.update(commentid);
    }
}
