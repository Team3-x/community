package com.team3.community.mapper;

import com.team3.community.model.Comment;

public interface CommentExtMapper {
    int incCommentCount(Comment record);
}