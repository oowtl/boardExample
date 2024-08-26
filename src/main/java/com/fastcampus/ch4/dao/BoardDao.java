package com.fastcampus.ch4.dao;

import com.fastcampus.ch4.domain.BoardDto;

import java.util.List;

public interface BoardDao {
    int insert(BoardDto boardDto);
    List<BoardDto> select(Integer bno);
    int update(BoardDto boardDto);
    int delete(Integer bno);
}
