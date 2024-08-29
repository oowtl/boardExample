package com.fastcampus.ch4.controller;

import com.fastcampus.ch4.domain.*;
import com.fastcampus.ch4.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.*;

import javax.servlet.http.*;
import java.time.*;
import java.util.*;

@Controller
@RequestMapping("/board")
public class BoardController {
    @Autowired
    BoardService boardService;

    @GetMapping("/list")
    public String list(Integer page, Integer pageSize, HttpServletRequest request, Model model) {
        if(!loginCheck(request))
            return "redirect:/login/login?toURL="+request.getRequestURL();  // 로그인을 안했으면 로그인 화면으로 이동

        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }

        try {
            Map<String, Object> map = new HashMap<>();
            map.put("offset", (page - 1) * pageSize);
            map.put("pageSize", pageSize);

            int totalCount = boardService.getCount();
            model.addAttribute("totalCnt", totalCount);

            PageHandler ph = new PageHandler(page, totalCount, pageSize);
            model.addAttribute("ph", ph);

            List<BoardDto>boardList = boardService.getPage(map);
            model.addAttribute("list", boardList);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return "boardList"; // 로그인을 한 상태이면, 게시판 화면으로 이동
    }

    @GetMapping("/read")
    public String read(Integer bno, Integer page, Integer pageSize, HttpServletRequest request, Model model) {
        // login check???

        try {
            // call service
            BoardDto boardDto = boardService.read(bno);
            model.addAttribute(boardDto);

            // set model attr
            model.addAttribute("page", page); // 이건 생략 안됨
            model.addAttribute("pageSize", pageSize); // 이건 생략 안됨
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // return
        return "board";
    }

    private boolean loginCheck(HttpServletRequest request) {
        // 1. 세션을 얻어서
        HttpSession session = request.getSession();
        // 2. 세션에 id가 있는지 확인, 있으면 true를 반환
        return session.getAttribute("id")!=null;
    }
}
