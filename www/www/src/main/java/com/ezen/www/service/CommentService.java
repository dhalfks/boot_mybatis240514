package com.ezen.www.service;

import com.ezen.www.domain.CommentVO;
import com.ezen.www.domain.PagingVO;
import com.ezen.www.handler.PagingHandler;

public interface CommentService {
  int post(CommentVO cvo);

  PagingHandler getList(long bno, PagingVO pgvo);

  int modify(CommentVO cvo);

  int delete(long cno);
}
