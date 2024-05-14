package com.ezen.www.repository;

import com.ezen.www.domain.FileVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FileMapper {
  int insertFile(FileVO fvo);

  List<FileVO> getFileList(long bno);
}
