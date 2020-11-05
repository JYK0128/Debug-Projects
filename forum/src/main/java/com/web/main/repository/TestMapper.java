package com.web.main.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.web.main.domain.TestVO;

@Mapper
public interface TestMapper {
	public String now();
	public String getName();
	public int getUid();
	public TestVO getObj1();
	public TestVO getObj2();
	public List<TestVO> getList();
}
