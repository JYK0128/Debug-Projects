package com.web.main.web;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.web.main.domain.BoardVO;
import com.web.main.domain.CommentVO;
import com.web.main.domain.FileListVO;
import com.web.main.domain.FileVO;
import com.web.main.service.BoardService;
import com.web.main.service.CommentService;
import com.web.main.service.FileService;
import com.web.main.util.FileUtil;

//뷰 전달
@Controller
public class BoardController {

	@Autowired
	private BoardService mBoardService;
	@Autowired
	private FileService mFileService;
	@Autowired
	private CommentService mCommentService;

	@GetMapping("list")
	private String boardList(Model model) {
		model.addAttribute("list", mBoardService.boardListService());
		return "list";
	}

	@GetMapping("detail/{bno}")
	private String boardDetail(@PathVariable int bno, Model model) {
		BoardVO boardVO = mBoardService.boardDetailService(bno);
		List<FileVO> fileListVO = mFileService.fileSearchService(bno);
		model.addAttribute("board", boardVO);
		model.addAttribute("list", fileListVO);
		mBoardService.boardViewIncreaseService(boardVO);
		return "detail";
	}

	@GetMapping("insert")
	private String boardInsertForm() throws Exception {
		return "insert";
	}

	@PostMapping("insert")
	private String boardInsertProc(@ModelAttribute BoardVO boardVO, @RequestPart List<MultipartFile> files)
			throws Exception {
		mBoardService.boardInsertService(boardVO);

		for (MultipartFile file : files) {
			if(!file.isEmpty()) {
				Map<String, String> fileInfo = FileUtil.upload(file);
				mFileService.fileUploadService(boardVO, fileInfo);				
			}
		}
		return "redirect:/list";
	}

	// update
	@GetMapping("update/{bno}")
	private String boardUpdateForm(@PathVariable int bno, Model model) {
		model.addAttribute("board", mBoardService.boardDetailService(bno));
		model.addAttribute("list", mFileService.fileSearchService(bno));
		return "update";
	}

	@PostMapping("update/{bno}")
	private String boardUpdateProc(@PathVariable int bno, @ModelAttribute BoardVO boardVO,
			@ModelAttribute FileListVO fileListVO, @RequestPart List<MultipartFile> files) throws Exception {
		mBoardService.boardUpdateService(boardVO);

		mFileService.fileRemoveService(bno, fileListVO);
		for (MultipartFile file : files) {
			if(!file.isEmpty()) {
				Map<String, String> fileInfo = FileUtil.upload(file);
				mFileService.fileUploadService(boardVO, fileInfo);				
			}
		}
		return "redirect:/detail/{bno}";
	}

	// delete
	@GetMapping("delete/{bno}")
	private String boardDelete(@PathVariable int bno) {
		mFileService.fileRemoveAllService(bno);
		mBoardService.boardDeleteService(bno);
		return "redirect:/list";
	}

	// file
	@GetMapping("download/{bno}/{UUID}/{name}")
	private void fileDownload(@ModelAttribute FileVO fileVO, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String fileName = fileVO.getName();
		String fileUUID = fileVO.getUUID();

		Map<String, String> fileInfo = FileUtil.makeFileInfo(fileName, fileUUID);
		long size = FileUtil.getSize(fileInfo);

		// 파일 다운로드 헤더 구현
		response.reset();
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Description", "JSP Generated Data");
		response.setHeader("Content-Length", "" + size);

		// 파일 다운로드 헤더의 브라우저 셋팅 (IE, Non-IE)
		request.setCharacterEncoding("UTF-8");
		String client = request.getHeader("User-Agent");

		if (client.indexOf("MSIE") != -1 || client.indexOf("Trident") != -1) {
			response.setHeader("Content-Disposition", "attachment; filename=\""
					+ java.net.URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "\\ ") + "\"");
		} else {
			response.setHeader("Content-Disposition",
					"attachment; filename=\"" + new String(fileName.getBytes("UTF-8"), "ISO8859_1") + "\"");
			response.setHeader("Content-Type", "application/octet-stream; charset=utf-8");
		}

		OutputStream out = response.getOutputStream();
		FileUtil.download(fileInfo, out);
	}

	// comment
	@PostMapping("comment/list") // 댓글 리스트
	@ResponseBody
	private List<CommentVO> mCommentServiceList(@RequestParam int bno) throws Exception {
		return mCommentService.commentListService(bno);
	}

	@PostMapping("comment/insert") // 댓글 작성
	@ResponseBody
	private int mCommentServiceInsert(@RequestParam int bno, @RequestParam String content, @RequestParam String writer)
			throws Exception {
		CommentVO comment = new CommentVO();
		comment.setBno(bno);
		comment.setContent(content);
		comment.setWriter(writer);

		return mCommentService.commentInsertService(comment);
	}

	@RequestMapping("comment/update") // 댓글 수정
	@ResponseBody
	private int mCommentServiceUpdate(@RequestParam int cno, @RequestParam String content) throws Exception {
		CommentVO comment = new CommentVO();
		comment.setCno(cno);
		comment.setContent(content);

		return mCommentService.commentUpdateService(comment);
	}

	@RequestMapping("comment/delete/{cno}") // 댓글 삭제
	@ResponseBody
	private int mCommentServiceDelete(@PathVariable int cno) throws Exception {
		return mCommentService.commentDeleteService(cno);
	}
}