package com.fastcampus.ch4.dao;

import com.fastcampus.ch4.domain.BoardDto;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BoardDaoImpl implements BoardDao {
    @Autowired
    SqlSession sqlSession;

    private final String namespace = "com.fastcampus.ch4.mapper.BoardMapper.";

    @Override
    public int insert(BoardDto boardDto) {
        return sqlSession.insert(namespace + "insert", boardDto);
    }

    @Override
    public List<BoardDto> select(Integer bno) {
        return sqlSession.selectList(namespace + "select", bno);
    }

    @Override
    public int update(BoardDto boardDto) {
        return sqlSession.update(namespace + "update", boardDto);
    }

    @Override
    public int delete(Integer bno) {
        return sqlSession.delete(namespace + "delete", bno);
    }
}
