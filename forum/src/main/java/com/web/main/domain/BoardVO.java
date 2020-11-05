package com.web.main.domain;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

// 데이터 구조
@Data
public class BoardVO implements Serializable{
	private static final long serialVersionUID = 1L;
	private int bno;
	private String title;
	private String contents;
	private String writer;
	private Date reg_date;
	private int view_cnt;
}
