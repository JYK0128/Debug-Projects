package com.web.main.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.main.domain.CommentVO;
import com.web.main.repository.CommentMapper;

@Service
public class CommentService {
	@Autowired
	CommentMapper mCommentMapper;

	public List<CommentVO> commentListService(int bno) throws Exception {
		return mCommentMapper.selectListByBno(bno);
	}

	public int commentInsertService(CommentVO comment) throws Exception {
		return mCommentMapper.insert(comment);
	}

	public int commentUpdateService(CommentVO comment) throws Exception {
		return mCommentMapper.update(comment);
	}

	public int commentDeleteService(int cno) throws Exception {
		return mCommentMapper.delete(cno);
	}
}
