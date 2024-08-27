package com.fastcampus.ch4.dao.notice;

import com.fastcampus.ch4.dto.notice.NoticeDto;

import java.util.List;
import java.util.Map;

public interface NoticeDao {
    int count();

    int countDsply();

    int insert(NoticeDto noticeDto);

    NoticeDto select(Integer ntc_seq);

    List<NoticeDto> selectAll();

    List<NoticeDto> selectDsply();

    List<NoticeDto> selectPage(Map map);

    List<NoticeDto> selectPageDsply(Map map);

    int update(NoticeDto noticeDto);

    int delete(Integer ntc_seq);

    int deleteAll();

    int increaseViewCnt(Integer ntc_seq);
}
