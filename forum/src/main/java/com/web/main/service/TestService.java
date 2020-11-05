package com.web.main.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.web.main.domain.TestVO;
import com.web.main.repository.TestMapper;

public class TestService {
	@Autowired
	TestMapper testMapper;
	private List<TestVO> testFunc() {
		return testMapper.getList();
	}
}