package com.web.main.repository;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.web.main.domain.FileVO;

@Mapper
public interface FileMapper {
	@Insert("INSERT INTO files VALUES (#{bno},#{UUID},#{name});")
	public int insert(FileVO file);

	@Select("SELECT * FROM files WHERE bno=#{bno};")
	public List<FileVO> selectListByBno(int bno);

	@Select("SELECT * FROM files WHERE bno=#{bno} AND fileName=#{name};")
	public FileVO select(FileVO file);

	@Delete("DELETE FROM files WHERE bno=#{bno};")
	public int deleteListByBno(int bno);

	@Delete("DELETE FROM files WHERE bno=#{bno} "
			+ "AND uuid NOT IN "
			+ "<foreach collection=\\\"list item=\"item\" open=\"(\" separator=\",\" close=\")\"> #{item.UUID} </foreach>;")
	public void delete(@Param("bno") int bno, @Param("list") List<FileVO> fileList);
}