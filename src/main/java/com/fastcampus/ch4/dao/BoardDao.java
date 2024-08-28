package com.fastcampus.ch4.dao;

import com.fastcampus.ch4.domain.BoardDto;

import java.util.List;
import java.util.Map;

public interface BoardDao {
    int insert(BoardDto boardDto);
    List<BoardDto> selectPage(Integer bno);
    List<BoardDto> selectPage(Map map);
    List<BoardDto> selectAll();
    int update(BoardDto boardDto);
    int increaseViewCnt(Integer bno);
    int delete(Integer bno, String writer);
    int deleteForAdmin(Integer bno);
    int count();

}
