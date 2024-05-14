package com.ezen.www.service;

import com.ezen.www.domain.BoardVO;

import java.util.List;

public interface BoardService {
  int register(BoardVO bvo);

  List<BoardVO> getList();

  BoardVO getDetail(long bno);

  void modify(BoardVO bvo);

  void remove(long bno);
}
