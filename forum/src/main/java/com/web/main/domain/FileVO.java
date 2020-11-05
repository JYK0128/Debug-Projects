package com.web.main.domain;

import java.io.Serializable;

import lombok.Data;

@Data
public class FileVO implements Serializable{
	private static final long serialVersionUID = 1L;
	private int bno;
	private String name; // 저장할 파일
	private String UUID; // 실제 파일
}
