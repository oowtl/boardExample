package com.fastcampus.ch4.dao.notice;

import com.fastcampus.ch4.dto.notice.NoticeDto;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class NoticeDaoImpl implements NoticeDao {

    @Autowired
    SqlSession sqlSession;

    String namespace = "com.fastcampus.ch4.dao.notice.NoticeMapper.";

    @Override
    public int count() {
        return sqlSession.selectOne(namespace + "count");
    }

    @Override
    public int countDsply() {
        return sqlSession.selectOne(namespace + "countDsply");
    }

    @Override
    public int insert(NoticeDto noticeDto) {
        return sqlSession.insert(namespace + "insert", noticeDto);
    }

    @Override
    public NoticeDto select(Integer ntc_seq) {
        return sqlSession.selectOne(namespace + "select", ntc_seq);
    }

    @Override
    public List<NoticeDto> selectAll() {
        return sqlSession.selectList(namespace + "selectAll");
    }

    @Override
    public List<NoticeDto> selectDsply() {
        return sqlSession.selectList(namespace + "selectDsply");
    }

    @Override
    public List<NoticeDto> selectPage(Map map) {
        return sqlSession.selectList(namespace + selectPage(map));
    }

    @Override
    public List<NoticeDto> selectPageDsply(Map map) {
        return sqlSession.selectList(namespace + selectPageDsply(map));
    }

    @Override
    public int update(NoticeDto noticeDto) {
        return sqlSession.update(namespace + "update", noticeDto);
    }

    @Override
    public int delete(Integer ntc_seq) {
        return sqlSession.delete(namespace + "delete", ntc_seq);
    }

    @Override
    public int deleteAll() {
        return sqlSession.delete(namespace + "deleteAll");
    }

    @Override
    public int increaseViewCnt(Integer ntc_seq) {
        return sqlSession.update(namespace + "increaseViewCnt", ntc_seq);
    }
}