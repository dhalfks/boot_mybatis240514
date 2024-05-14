package com.ezen.www.domain;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class FileVO {
  private String uuid;
  private String saveDir;
  private String fileName;
  private int fileType;
  private long bno;
  private long fileSize;
  private String regAt;
}
