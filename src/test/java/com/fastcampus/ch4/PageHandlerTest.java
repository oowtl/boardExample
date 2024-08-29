package com.fastcampus.ch4;

import com.fastcampus.ch4.domain.PageHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;


// TODO : 2024.08.26 page redirect 처리

/*
PageHandler
=== 기능
1. 한 페이지 당 보여줄 게시글의 개수
2. 한 목록에 최대로 나타낼 수 있는 페이지 개수
3. 이전 페이지 목록으로 이동하기 기능
4. 이후 페이지 목록으로 이동하기 기능
5. 현재 페이지 나타내는 기능 : 현재 페이지를 저장하고 있어야 한다.
- 현재 페이지를 받으면 현재 페이지에 해당하는 페이지 목록, 이전 페이지, 이후 페이지 기능을 나타낸다.
6. 시작 페이지 : 현재의 페이지 목록에서의 시작 페이지
7. 끝 페이지 : 현재의 페이지 목록에서 가장 큰 페이지 (끝 페이지)

=== 테스트
1. 한 페이지 당 보여줄 게시글의 개수 테스트
- 조회한 결과의 개수 비교?
-- 조회한 결과의 개수가 보여줘야 하는 게시글의 개수보다 크면?
-- 조회한 결과의 개수가 보여줘야 하는 게시글의 개수보다 작으면?

1. 이전 페이지 기능 테스트
- 시작페이지가 1페이지 이면 이전 페이지 이동 기능이 비활성화된다.
2. 이후 페이지 기능 테스트
- 마지막 페이지가 나오는 경우에는 이후 페이지 이동 기능이 비활성화된다.
3. 현재 페이지 표현 기능 테스트
- 현재 페이지를 반환한다.
4. 페이지 목록의 마지막 페이지보다 실제 마지막 페이지가 작을 때는 실제 마지막 페이지를 페이지 목록의 마지막 페이지로 한다.

5. 주어진 총 페이지 수와 현재 페이지 수를 바탕으로 pageHandler 를 생성해서 값을 비교한다.
- 현재 페이지
- 총 게시글 개수
- 시작 페이지, 끝 페이지

 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"file:src/main/webapp/WEB-INF/spring/root-context.xml"})
public class PageHandlerTest {
    private final int DEFAULT_PAGE_SIZE = 10;

    @Test
    public void 한페이지당보여줄게시글의개수테스트() {
        PageHandler ph = new PageHandler(1, 150, 10);
        assertEquals(DEFAULT_PAGE_SIZE, ph.getPageSize());
    }


    @Test
    public void pageHandler값Test() {
        // 1 : 1페이지 테스트
        PageHandler ph = new PageHandler
                (1, 150, 10);
        assertEquals(1, ph.getPage());
        assertEquals(15, ph.getTotalPage());
        assertEquals(false, ph.isShowPrev());
        assertEquals(true, ph.isShowNext());
        assertEquals(1, ph.getStartPage());
        assertEquals(10,ph.getEndPage());

        // 2 : showPrev == false
        ph = new PageHandler(10, 150, 10);
        assertEquals(10, ph.getPage());
        assertEquals(15, ph.getTotalPage());
        assertEquals(false, ph.isShowPrev());
        assertEquals(true, ph.isShowNext());
        assertEquals(1, ph.getStartPage());
        assertEquals(10,ph.getEndPage());

        // 3 : showNext == false
        ph = new PageHandler(4, 90, 10);
        assertEquals(4, ph.getPage());
        assertEquals(9, ph.getTotalPage());
        assertEquals(false, ph.isShowPrev());
        assertEquals(false, ph.isShowNext());
        assertEquals(1, ph.getStartPage());
        assertEquals(9, ph.getEndPage());

        // 4 : showNext == false, totalPage == 20
        ph = new PageHandler(15, 200, 10);
        assertEquals(15, ph.getPage());
        assertEquals(20, ph.getTotalPage());
        assertEquals(true, ph.isShowPrev());
        assertEquals(false, ph.isShowNext());
        assertEquals(11, ph.getStartPage());
        assertEquals(20, ph.getEndPage());

        // 5 showPrev == true, showNext == true
        ph = new PageHandler(15, 210, 10);
        assertEquals(15, ph.getPage());
        assertEquals(21, ph.getTotalPage());
        assertEquals(true, ph.isShowPrev());
        assertEquals(true, ph.isShowNext());
        assertEquals(11, ph.getStartPage());
        assertEquals(20, ph.getEndPage());


    }

}
