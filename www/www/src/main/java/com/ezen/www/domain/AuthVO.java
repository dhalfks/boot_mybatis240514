package com.ezen.www.domain;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AuthVO {
  private String email;
  private String auth;
}
