package com.web.main.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.main.domain.BoardVO;
import com.web.main.domain.FileListVO;
import com.web.main.domain.FileVO;
import com.web.main.repository.FileMapper;
import com.web.main.util.FileUtil;

//비즈니스 로직
@Service
public class FileService {
	@Autowired
	private FileMapper fileMapper;

	public void fileUploadService(BoardVO boardVO, Map<String, String> fileInfo) throws Exception {
		FileVO fileVO = new FileVO();
		fileVO.setBno(boardVO.getBno());
		fileVO.setName(fileInfo.get("name"));
		fileVO.setUUID(fileInfo.get("UUID"));

		fileMapper.insert(fileVO);
	}

	public List<FileVO> fileSearchService(int bno) {
		return fileMapper.selectListByBno(bno);
	}

	public FileVO fileDownloadService(FileVO fileVO) {
		return fileMapper.select(fileVO);
	}

	public void fileRemoveService(int bno, FileListVO fileListVO) {
		fileMapper.delete(bno, fileListVO.getFileList());

		for (FileVO fileVO : fileListVO.getFileList()) {
			if (fileVO != null) {
				String fileName = fileVO.getName();
				String fileUUID = fileVO.getUUID();
				Map<String, String> fileInfo = FileUtil.makeFileInfo(fileName, fileUUID);
				FileUtil.delete(fileInfo);
			}
		}
	}

	public void fileRemoveAllService(int bno) {
		List<FileVO> files = fileMapper.selectListByBno(bno);
		fileMapper.deleteListByBno(bno);
		files.stream().forEach(file -> FileUtil.delete(FileUtil.makeFileInfo(file.getName(), file.getUUID())));
	}
}