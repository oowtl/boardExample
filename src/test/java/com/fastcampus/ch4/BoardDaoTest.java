package com.fastcampus.ch4;

import com.fastcampus.ch4.dao.BoardDao;
import com.fastcampus.ch4.domain.BoardDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

/*
C, R, U, D

1. INSERT
- not null 컬럼에 null 넣기
- insert 하고 데이터 개수 체크
- insert 후 데이터 잘 들어갔는지 체크하기

2. UPDATE
- not null 컬럼에 null 로 바꾸기
- 없는 ROW 변경하기
- update 후 변경내용이 잘 적용되었는지 확인하기

3. DELETE
- 이미 삭제한 bno 로 삭제해보기
- 삭제

4. SELECT
- 삭제한 row select
- update row select
- select

 */


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"file:src/main/webapp/WEB-INF/spring/root-context.xml"})
public class BoardDaoTest {

    @Autowired
    BoardDao boardDao;

    private final String TEST_TITLE = "dao_test_title";
    private final String TEST_USER = "dao_tester";
    private final String TEST_CONTENTS = "contents";
    private final int SUCCESS = 1;

    private final String UPDATE_TITLE = "update_title";
    private final String UPDATE_USER = "updater";
    private final String UPDATE_CONTENTS = "update_contents";

    @Test(expected = DataIntegrityViolationException.class)
    public void notnull에null넣기 () {
        BoardDto boardDto = BoardDto.create();
        boardDto.setTitle(null);
        boardDto.setContent(TEST_CONTENTS);
        boardDto.setWriter(TEST_USER);

        int result = boardDao.insert(boardDto);
        assertEquals(SUCCESS, result);

        fail();
    }

    @Test
    public void insert () {
        // insert 후 개수 체크
        BoardDto boardDto = BoardDto.create();
        boardDto.setTitle(TEST_TITLE);
        boardDto.setContent(TEST_CONTENTS);
        boardDto.setWriter(TEST_USER);

        int result = boardDao.insert(boardDto);
        assertEquals(SUCCESS, result);
    }


    // insert 후 데이터 잘 들어갔는지 체크하기
    @Test
    public void insertCheckData () {
        BoardDto boardDto = BoardDto.create();
        boardDto.setTitle(TEST_TITLE);
        boardDto.setContent(TEST_CONTENTS);
        boardDto.setWriter(TEST_USER);

        int result = boardDao.insert(boardDto);
        assertEquals(SUCCESS, result);

        // empty list
        Integer insertedBno = boardDto.getBno();
        List<BoardDto> selectedList = boardDao.select(insertedBno);
        assertFalse(selectedList.isEmpty());

        // check
        BoardDto selected = selectedList.get(0);
        assertTrue(boardDto.equals(selected));
    }

    // not null 컬럼에 null 로 바꾸기
    @Test(expected = DataIntegrityViolationException.class)
    public void notnull컬럼에null로업데이트 () {
        BoardDto boardDto = BoardDto.create();
        boardDto.setTitle(TEST_USER);
        boardDto.setContent(TEST_CONTENTS);
        boardDto.setWriter(TEST_USER);

        int insertResult = boardDao.insert(boardDto);
        assertEquals(SUCCESS, insertResult);

        List<BoardDto> selectedList = boardDao.select(boardDto.getBno());
        assertFalse(selectedList.isEmpty());

        BoardDto updating = selectedList.get(0);
        assertTrue(boardDto.equals(updating));

        updating.setTitle(null);
        updating.setContent(UPDATE_CONTENTS);
        updating.setWriter(UPDATE_USER);

        int updateResult = boardDao.update(updating);
        assertEquals(SUCCESS, updateResult);
    }

    // 없는 ROW 변경하기
    @Test
    public void 없는컬럼update () {
        BoardDto boardDto = BoardDto.create();
        boardDto.setTitle(TEST_TITLE);
        boardDto.setContent(TEST_CONTENTS);
        boardDto.setWriter(TEST_USER);

        int intsertResult = boardDao.insert(boardDto);
        assertEquals(SUCCESS, intsertResult);

        // 삭제
        Integer bno = boardDto.getBno();
        int deleteResult = boardDao.delete(bno);
        assertEquals(SUCCESS, deleteResult);

        boardDto.setTitle(UPDATE_TITLE);
        boardDto.setContent(UPDATE_CONTENTS);
        boardDto.setWriter(UPDATE_USER);

        int updateResult = boardDao.update(boardDto);
        assertNotEquals(SUCCESS, updateResult);
    }

    // update 후 변경내용이 잘 적용되었는지 확인하기
    @Test
    public void update () {
        BoardDto boardDto = BoardDto.create();
        boardDto.setTitle(TEST_TITLE);
        boardDto.setContent(TEST_CONTENTS);
        boardDto.setWriter(TEST_USER);

        int intsertResult = boardDao.insert(boardDto);
        assertEquals(SUCCESS, intsertResult);

        boardDto.setTitle(UPDATE_TITLE);
        boardDto.setContent(UPDATE_CONTENTS);
        boardDto.setWriter(UPDATE_USER);

        int updateResult = boardDao.update(boardDto);
        assertEquals(SUCCESS, updateResult);

        Integer bno = boardDto.getBno();
        List<BoardDto> selectedList = boardDao.select(bno);
        assertFalse(selectedList.isEmpty());

        BoardDto selected = selectedList.get(0);
        assertTrue(boardDto.equals(selected));
    }

    // 이미 삭제한 bno 로 삭제해보기
    @Test
    public void 이미삭제한row삭제하기 () {
        BoardDto boardDto = BoardDto.create();
        boardDto.setTitle(TEST_TITLE);
        boardDto.setContent(TEST_CONTENTS);
        boardDto.setWriter(TEST_USER);

        int insertResult = boardDao.insert(boardDto);
        assertEquals(SUCCESS, insertResult);

        Integer bno = boardDto.getBno();
        int deleteResult = boardDao.delete(bno);
        assertEquals(SUCCESS, deleteResult);

        deleteResult = boardDao.delete(bno);
        assertNotEquals(SUCCESS, deleteResult);
    }

    // 삭제
    @Test
    public void delete () {
        BoardDto boardDto = BoardDto.create();
        boardDto.setTitle(TEST_TITLE);
        boardDto.setContent(TEST_CONTENTS);
        boardDto.setWriter(TEST_USER);

        int insertResult = boardDao.insert(boardDto);
        assertEquals(SUCCESS, insertResult);

        Integer bno = boardDto.getBno();
        int deleteResult = boardDao.delete(bno);
        assertEquals(SUCCESS, deleteResult);

        List<BoardDto> selected = boardDao.select(bno);
        assertTrue(selected.isEmpty());
    }

    // 삭제한 row select
    @Test
    public void 삭제한rowSelect () {
        BoardDto boardDto = BoardDto.create();
        boardDto.setTitle(TEST_TITLE);
        boardDto.setContent(TEST_CONTENTS);
        boardDto.setWriter(TEST_USER);

        int insertResult = boardDao.insert(boardDto);
        assertEquals(SUCCESS, insertResult);

        Integer bno = boardDto.getBno();

        int deleteResult = boardDao.delete(bno);
        assertEquals(SUCCESS, deleteResult);

        List<BoardDto> selectedList = boardDao.select(bno);
        assertTrue(selectedList.isEmpty());
    }

    // update row select
    @Test
    public void updateRowSelect () {
        BoardDto boardDto = BoardDto.create();
        boardDto.setTitle(TEST_TITLE);
        boardDto.setContent(TEST_CONTENTS);
        boardDto.setWriter(TEST_USER);

        int insertResult = boardDao.insert(boardDto);
        assertEquals(SUCCESS, insertResult);

        boardDto.setTitle(UPDATE_TITLE);
        boardDto.setContent(UPDATE_CONTENTS);
        boardDto.setWriter(UPDATE_USER);

        int updateResult = boardDao.update(boardDto);
        assertEquals(SUCCESS, updateResult);

        Integer bno = boardDto.getBno();
        List<BoardDto> selectedList = boardDao.select(bno);
        assertFalse(selectedList.isEmpty());

        BoardDto selected = selectedList.get(0);
        assertTrue(boardDto.equals(selected));
    }

    // select
    @Test
    public void select () {
        BoardDto boardDto = BoardDto.create();
        boardDto.setTitle(TEST_TITLE);
        boardDto.setContent(TEST_CONTENTS);
        boardDto.setWriter(TEST_USER);

        int insertResult = boardDao.insert(boardDto);
        assertEquals(SUCCESS, insertResult);

        Integer bno = boardDto.getBno();
        List<BoardDto> selectedList = boardDao.select(bno);
        assertFalse(selectedList.isEmpty());

        BoardDto selected = selectedList.get(0);
        assertTrue(boardDto.equals(selected));
    }

}
