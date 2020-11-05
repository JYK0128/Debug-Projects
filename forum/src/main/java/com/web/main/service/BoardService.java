package com.web.main.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.main.domain.BoardVO;
import com.web.main.repository.BoardMapper;

//비즈니스 로직
@Service
public class BoardService {
	@Autowired
	private BoardMapper boardMapper;

	public List<BoardVO> boardListService() {
		return boardMapper.selectList();
	}

	public BoardVO boardDetailService(int bno) {
		return boardMapper.selectByBno(bno);
	}

	public int boardInsertService(BoardVO board) {
		return boardMapper.insert(board);
	}

	public int boardUpdateService(BoardVO board) {
		return boardMapper.update(board);
	}

	public int boardDeleteService(int bno) {
		return boardMapper.delete(bno);
	}

	public int boardViewIncreaseService(BoardVO board) {
		return boardMapper.updateViewCnt(board);
	}
}