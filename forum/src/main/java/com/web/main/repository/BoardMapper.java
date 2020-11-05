package com.web.main.repository;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.web.main.domain.BoardVO;

//DB 조회
@Mapper
@Repository
public interface BoardMapper {
	@Insert("INSERT INTO board VALUES(DEFAULT, #{title}, #{contents}, #{writer}, NOW(), DEFAULT)")
	@Options(useGeneratedKeys = true, keyProperty = "bno")
	public int insert(BoardVO board);

	@Select("SELECT * FROM board WHERE bno = #{bno}")
	public BoardVO selectByBno(int bno);

	@Select("SELECT * FROM board")
	public List<BoardVO> selectList();

	@Update("UPDATE board SET title = #{title}, contents = #{contents}, writer = #{writer}, reg_date = NOW() WHERE bno = #{bno}")
	public int update(BoardVO board);

	@Update("UPDATE board SET view_cnt = view_cnt + 1 WHERE bno = #{bno}")
	public int updateViewCnt(BoardVO board);

	@Delete("DELETE FROM board WHERE bno=#{bno}")
	public int delete(int bno);


}
