package com.ezen.www.domain;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CommentVO {
  private long cno;
  private long bno;
  private String writer;
  private String content;
  private String regAt;
  private String modAt;
}
