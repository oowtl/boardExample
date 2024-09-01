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


    /*
    api : /board/write
    method : GET
    params : page, pageSize, request, model
    return : String
    role
    - model.addAttribute("mode", "new");
    - return "board"
     */

    @GetMapping("/write")
    public String write(HttpServletRequest request, Model model) {
        // login check
        if(!loginCheck(request))
            return "redirect:/login/login?toURL="+request.getRequestURL();  // 로그인을 안했으면 로그인 화면으로 이동

        // model에 mode를 new로 설정
        model.addAttribute("mode", "new");

        // return
        return "board";
    }


    /*
    api : /board/write
    method : POST
    params : page, pageSize, boardDto, session, request, model, redirectAttributes
    return : String
    - success
        msg=WRT_OK (redirectAttributes)
        redirect:/board/list
    - fail
        msg=WRT_ERR (model)
        model : boardDto
        redirect:/board/write
     */

    @PostMapping("/write")
    public String write(BoardDto boardDto, HttpSession session, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
        // login check
        if(!loginCheck(request))
            return "redirect:/login/login?toURL="+request.getRequestURL();  // 로그인을 안했으면 로그인 화면으로 이동

        // session에서 id를 얻어서 boardDto에 저장
        String writer = (String) session.getAttribute("id");
        boardDto.setWriter(writer);

        // call service
        try {
            // write
            int result = boardService.write(boardDto);

            // write 성공 여부에 따라 다른 처리
            if (result != 1)
                throw new Exception("WRT_ERR");

            // write 성공 메시지
            redirectAttributes.addFlashAttribute("msg", "WRT_OK");

            if (result == 1)
                throw new Exception();

            return "redirect:/board/list";

        } catch (Exception e) {
            // 작성한 boardDto를 다시 넘겨줌
            // 실패 시에 boardDto 값 유지, 이후 새로고침 시에도 값 유지
            model.addAttribute(boardDto);

            // write 실패 메시지
            model.addAttribute("msg", "WRT_ERR");

            // POST "/board/write" 의 결과로 반환되는 결과
            // model 안에 boardDto 가 존재하기 때문에 새로고침해도 boardDto 값이 유지됨
            return "board";
        }
    }


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
    public String read(Integer bno, Integer page, Integer pageSize, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
        // login check???
        if(!loginCheck(request))
            return "redirect:/login/login?toURL="+request.getRequestURL();  // 로그인을 안했으면 로그인 화면으로 이동

        // set model attr
        model.addAttribute("page", page); // 이건 생략 안됨
        model.addAttribute("pageSize", pageSize); // 이건 생략 안됨

        try {
            // call service
            BoardDto boardDto = boardService.read(bno);

            // 게시물 조회 결과가 없을 때 예외 처리
            if (boardDto == null)
                throw new IllegalArgumentException("해당 게시물이 존재하지 않습니다.");

            model.addAttribute(boardDto);
        } catch (Exception e) {
            // 게시물 조회 실패 메시지
            redirectAttributes.addFlashAttribute("msg", "READ_ERR");

            return "redirect:/board/list?page="+page+"&pageSize="+pageSize;
        }

        // return
        return "board";
    }

    /*
    api : /board
    method : DELETE
    params : bno, writer, page, pageSize, request, model, redirectAttributes
    return : msg
    - success
        msg=DEL_OK
        redirect:/board/list?page=&PageSize=
    - fail
        msg=DEL_ERR
        redirect:/board/read?bno=&page=&pageSize=
        model.addFlashAttribute("msg", "DEL_FAIL");
     */

    @PostMapping("/remove")
    public String remove(Integer page, Integer pageSize, Integer bno, String writer, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        // login check
        if(!loginCheck(request))
            return "redirect:/login/login?toURL="+request.getRequestURL();  // 로그인을 안했으면 로그인 화면으로 이동

        // call service
        try {
            // 되돌아가기 위한 Model attribute 설정
            model.addAttribute("page", page);
            model.addAttribute("pageSize", pageSize);

            // 삭제
            int result = boardService.remove(bno, writer);

            // 삭제 성공 여부에 따라 다른 처리
            if (result != 1)
                throw new IllegalArgumentException("DEL_FAIL");

            // 삭제 성공 메시지
            redirectAttributes.addFlashAttribute("msg", "DEL_OK");

            return "redirect:/board/list?page="+page+"&pageSize="+pageSize;

        } catch (Exception e) {
            // 삭제 실패 메시지
            redirectAttributes.addFlashAttribute("msg", "DEL_FAIL");

            return "redirect:/board/list?page="+page+"&pageSize="+pageSize;
        }
    }

    /*
    api : /board/modify
    method : POST
    params : boardDto, page, pageSize, bno, request, model, redirectAttributes
    return : String
    - success
        msg=MOD_OK
        redirect:/board/list?page=&pageSize=
    - fail
        model
            - msg=MOD_ERR
            - boardDto
        return "board"
    role
    - login check (session)
    - set writer (in session[id])
    - call service
    - modify 성공 시 return success case
    - modify 실패 시
        - Exception 발생
        - return fail case
     */

    @PostMapping("/modify")
    public String modify(BoardDto boardDto, Integer bno, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
        // login check
        if(!loginCheck(request))
            return "redirect:/login/login?toURL="+request.getRequestURL();  // 로그인을 안했으면 로그인 화면으로 이동

        // session에서 id를 얻어서 boardDto에 저장
        String writer = (String) request.getSession().getAttribute("id");
        boardDto.setWriter(writer);

        // call service
        try {
            // modify
            int result = boardService.modify(boardDto);

            // modify 성공 여부에 따라 다른 처리
            if (result != 1)
                throw new Exception("MOD_ERR");

            // modify 성공 메시지
            redirectAttributes.addFlashAttribute("msg", "MOD_OK");

            return "redirect:/board/list";

        } catch (Exception e) {
            // modify 실패 메시지
            model.addAttribute("msg", "MOD_ERR");
            model.addAttribute(boardDto);

            return "board";
        }
    }

    private boolean loginCheck(HttpServletRequest request) {
        // 1. 세션을 얻어서
        HttpSession session = request.getSession();
        // 2. 세션에 id가 있는지 확인, 있으면 true를 반환
        return session.getAttribute("id")!=null;
    }
}
