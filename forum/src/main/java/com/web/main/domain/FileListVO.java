package com.web.main.domain;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class FileListVO implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<FileVO> fileList;

	public boolean isEmpty() {
		return (fileList == null);
	}
}
