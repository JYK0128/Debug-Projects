package com.web.main.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.web.main.repository.TestMapper;


@SpringBootTest
class TestMapperTest {
	
	@Autowired
	TestMapper testMapper;
	
	@Test
	void test1() {
		System.out.println(testMapper.now());
	}
	
	@Test
	void test2() {
		System.out.println(testMapper.getName());
	}

	@Test
	void test3() {
		System.out.println(testMapper.getUid());
	}
	
	@Test
	void test4() {
		System.out.println(testMapper.getObj1());
	}
	
	@Test
	void test5() {
		System.out.println(testMapper.getObj2());
	}
	
	@Test
	void test6() {
		System.out.println(testMapper.getList());
	}
}