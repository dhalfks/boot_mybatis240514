package com.ezen.www.controller;

import com.ezen.www.domain.BoardDTO;
import com.ezen.www.domain.BoardVO;
import com.ezen.www.domain.FileVO;
import com.ezen.www.domain.PagingVO;
import com.ezen.www.handler.FileHandler;
import com.ezen.www.handler.PagingHandler;
import com.ezen.www.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/board/*")
@Controller
public class BoardController {
  private final BoardService bsv;
  private final FileHandler fh;

  @GetMapping("/register")
  public void register(){}

  @PostMapping("/register")
  public String register(BoardVO bvo, @RequestParam(name="files", required = false)MultipartFile[] files){
    log.info(">>>> bvo >> {}", bvo);
    List<FileVO> flist = null;
    if(files[0].getSize() > 0 || files != null){
      flist = fh.uploadFiles(files);
    }
    int isOk = bsv.register(new BoardDTO(bvo, flist));
    return "index";
  }

  @GetMapping("/list")
  public void list(Model m, PagingVO pgvo){
    log.info(">>>> pgvo >> {}", pgvo);
    //totalCount DB에서 가져오기
    int totalCount = bsv.getTotalCount(pgvo); //검색어 같이.
    //PagingHandler 객체 생성
    PagingHandler ph = new PagingHandler(pgvo, totalCount);
    m.addAttribute("list", bsv.getList(pgvo));
    m.addAttribute("ph", ph);
  }

  @GetMapping("/detail")
  public void detail(Model m, @RequestParam("bno") long bno){
    m.addAttribute("bdto", bsv.getDetail(bno));
  }

  @PostMapping("/modify")
  public String modify(BoardVO bvo){
    bsv.modify(bvo);
    return "redirect:/board/detail?bno="+bvo.getBno();
  }

  @PostMapping("/remove")
  public String remove(@RequestParam("bno")long bno){
    bsv.remove(bno);
    return "redirect:/board/list";
  }

}
