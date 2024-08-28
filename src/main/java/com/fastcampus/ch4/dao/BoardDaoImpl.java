package com.fastcampus.ch4.dao;

import com.fastcampus.ch4.domain.BoardDto;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public List<BoardDto> selectPage(Integer bno) {
        return sqlSession.selectList(namespace + "select", bno);
    }

    @Override
    public List<BoardDto> selectPage(Map map) {
        return sqlSession.selectList(namespace + "selectPage", map);
    }

    @Override
    public List<BoardDto> selectAll() {
        return sqlSession.selectList(namespace + "selectAll");
    }

    @Override
    public int update(BoardDto boardDto) {
        return sqlSession.update(namespace + "update", boardDto);
    }

    @Override
    public int increaseViewCnt(Integer bno) {
        return sqlSession.update(namespace + "increaseViewCnt", bno);
    }

    @Override
    public int delete(Integer bno, String writer) {
        Map<String, Object> map = new HashMap<>();
        map.put("bno", bno);
        map.put("writer", writer);

        return sqlSession.delete(namespace + "delete", map);
    }

    @Override
    public int deleteForAdmin(Integer bno) {
        Map<String, Object> map = new HashMap<>();
        map.put("bno", bno);
        return sqlSession.delete(namespace + "delete", map);
    }

    @Override
    public int count() {
        return sqlSession.selectOne(namespace + "count");
    }
}
