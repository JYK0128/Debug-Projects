package com.web.main.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.web.multipart.MultipartFile;

public class FileUtil {
	private static String projDir = System.getProperty("user.dir");
	private static String fileDir = FilenameUtils.separatorsToSystem(projDir + "/src/main/webapp/FILE-SYS/");

	public static Map<String, String> upload(MultipartFile uploadFile) throws Exception {
		String fileName = FilenameUtils.getName(uploadFile.getOriginalFilename());
		String fileUUID = UUID.randomUUID().toString();
		File file = new File(fileUUID);
		uploadFile.transferTo(file);
		Map<String, String> fileInfo = makeFileInfo(fileName, fileUUID);
		return fileInfo;
	}
	
	public static Map<String, String> makeFileInfo(String fileName, String fileUUID){
		Map<String, String> fileInfo = new HashMap<String, String>();
		fileInfo.put("name", fileName);	
		fileInfo.put("UUID", fileUUID);	
		
		return fileInfo;
	}
	
	public static void delete(Map<String, String> fileInfo){
		String fileUUID = fileInfo.get("UUID");
		File file = new File(fileDir, fileUUID);
		file.delete();
	}
	
	
	public static void download(Map<String, String> fileInfo, OutputStream out) throws Exception {
		String fileUUID = fileInfo.get("UUID");
		File file = new File(fileDir, fileUUID);

		// 스트림 생성, 연결, 종료
		InputStream in = new FileInputStream(file);
		IOUtils.copy(in, out);
		in.close();
		out.close();
	}

	public static long getSize(Map<String, String> fileInfo) {
		String fileUUID = fileInfo.get("UUID");
		File file = new File(fileDir, fileUUID);
		return file.length();
	}

	public static String getFileDir() {
		return fileDir;
	}
}
