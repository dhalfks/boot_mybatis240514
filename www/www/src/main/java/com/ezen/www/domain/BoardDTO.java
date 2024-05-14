package com.ezen.www.domain;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BoardDTO {
  private BoardVO bvo;
  private List<FileVO> flist;
}
