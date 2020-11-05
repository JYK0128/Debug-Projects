package com.web.main.repository;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.web.main.domain.CommentVO;

@Mapper
public interface CommentMapper {
    // 댓글 작성
    @Insert ("Insert INTO comment VALUES(DEFAULT, #{bno}, #{content}, #{writer}, NOW());")
    public int insert(CommentVO comment);

    // 댓글 목록
    @Select("SELECT * FROM comment WHERE bno=#{bno};")
    public List<CommentVO> selectListByBno(int bno);
 
    // 댓글 수정
    @Update("UPDATE comment SET content=#{content} WHERE cno=#{cno};")
    public int update(CommentVO comment);
 
    // 댓글 삭제
    @Delete("DELETE FROM comment WHERE cno=#{cno}")
    public int delete(int cno);
}
