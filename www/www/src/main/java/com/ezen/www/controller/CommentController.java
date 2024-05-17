package com.ezen.www.controller;

import com.ezen.www.domain.CommentVO;
import com.ezen.www.domain.PagingVO;
import com.ezen.www.handler.PagingHandler;
import com.ezen.www.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RequestMapping("/comment/*")
@RequiredArgsConstructor
@RestController
public class CommentController {
  private final CommentService csv;

  @PostMapping("/post")
  @ResponseBody
  public String post(@RequestBody CommentVO cvo){
    log.info(">>>> cvo >> {}", cvo);
    int isOk = csv.post(cvo);
    return isOk > 0 ? "1" : "0";
  }

  /* select * from comment order by cno desc limit 0,5 */
  @GetMapping("/list/{bno}/{page}")
  @ResponseBody
  public PagingHandler list(@PathVariable("bno")long bno, @PathVariable("page") int page){
    log.info(">>> bno >> page >> {}"+ bno + " / " +page);
    PagingVO pgvo = new PagingVO(page, 5);
    //Comment List
    //totalCount
    PagingHandler ph = csv.getList(bno, pgvo);
    return ph;
  }

  @ResponseBody
  @PutMapping("/edit")
  public String modify(@RequestBody CommentVO cvo){
    log.info(">>>> cvo >> {}", cvo);
    int isOk = csv.modify(cvo);
    return isOk >0 ? "1" : "0";
  }

  @ResponseBody
  @DeleteMapping("/remove/{cno}")
  public String remove(@PathVariable("cno")long cno){
    int isOk = csv.delete(cno);
    return isOk > 0? "1":"0";
  }


}
