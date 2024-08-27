package com.fastcampus.ch4.dao.notice;

import com.fastcampus.ch4.dto.notice.NoticeDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
//@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/root-context.xml"})
//@ContextConfiguration(classes = { SpringTestConfiguration.class })
public class NoticeDaoImplTest {

    @Autowired
    NoticeDao noticeDao;

    // NoticeDto n건 List로 반환하는 메서드
    private List<NoticeDto> createNoticeDto(int n, String dsply_chk) {
        List<NoticeDto> noticeDtoList = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            NoticeDto noticeDto = new NoticeDto("N10", "title", "content", "N", null, dsply_chk, "110111");
            noticeDtoList.add(noticeDto);
        }
        return noticeDtoList;
    }

    // 두 NoticeDto에 저장된 값이 동일한지 확인하는 메서드
    private void compareDto(NoticeDto noticeDto1, NoticeDto noticeDto2) {
        assertTrue(noticeDto2.getNtc_cate_code().equals(noticeDto1.getNtc_cate_code()));
        assertTrue(noticeDto2.getTitle().equals(noticeDto1.getTitle()));
        assertTrue(noticeDto2.getCont().equals(noticeDto1.getCont()));
        assertTrue(noticeDto2.getPin_top_chk().equals(noticeDto1.getPin_top_chk()));
        assertTrue(noticeDto2.getDsply_chk().equals(noticeDto1.getDsply_chk()));
        assertTrue(noticeDto2.getReg_id().equals(noticeDto1.getReg_id()));

        String pin_top_end_date1 = noticeDto1.getPin_top_end_date();
        String pin_top_end_date2 = noticeDto2.getPin_top_end_date();
        assertTrue(pin_top_end_date1 == null ? pin_top_end_date1 == pin_top_end_date2 : pin_top_end_date1.equals(pin_top_end_date2));
    }

    // 제목 기준 정렬 기준
    Comparator<NoticeDto> comparator = new Comparator<NoticeDto>() {
        @Override
        public int compare(NoticeDto o1, NoticeDto o2) {
            return o1.getTitle().compareTo(o2.getTitle());
        }
    };

    // 각 테스트 시작 전 디비에 저장된 값 전부 삭제
    @BeforeEach
    public void init() {
        noticeDao.deleteAll();
        assertTrue(noticeDao.count() == 0);
    }

    // 게시글 전체 개수 세는 기능
    // 게시글 전체 삭제하고 게시글 n개 입력 후 게시글 개수를 세면 게시글이 n개여야 한다.
    @DisplayName("전체 게시글 개수 세기")
    @ParameterizedTest
    @CsvSource(value = {"0,0", "1,1", "100,100"})
    public void count(int input, int expected) {
        String dsply_chk = "Y";
        List<NoticeDto> noticeDtoList = createNoticeDto(input, dsply_chk);

        for (int i = 0; i < noticeDtoList.size(); i++) {
            assertTrue(noticeDao.insert(noticeDtoList.get(i)) == 1);
        }

        assertTrue(noticeDao.count() == expected);
    }

    // 일반사용자에게 표시되는 글 개수 세는 기능
    // 게시글 전체 삭제하고 글 입력 없이 개수 세면 0개
    @DisplayName("일반사용자에게 노출되는 글 개수 세기 0건 - 게시글 없음")
    @Test
    public void countDsply_1() {
        assertTrue(noticeDao.countDsply() == 0);
    }

    /*
    // 게시글 전체 삭제하고 dsply_chk = "N"인 글 1개 입력한 후 개수 세면 0개
    @DisplayName("일반사용자에게 노출되는 글 개수 세기 0건 - N인 글만 입력")
    @Test
    public void countDsply_2() {
        // dsply_chk = "N"으로 글 1개 입력
        String dsply_chk = "N";
        NoticeDto noticeDto = createNoticeDto(1, dsply_chk).get(0);
        assertTrue(noticeDao.insert(noticeDto) == 1);

        // 노출되는 글 개수 세면 결과 0개
        assertTrue(noticeDao.countDsply() == 0);

        // 총 글 개수는 1개
        assertTrue(noticeDao.count() == 1);
    }

    // 게시글 전체 삭제하고 dsply_chk = "Y"인 글 1개 입력한 후 개수 세면 1개
    @DisplayName("일반사용자에게 노출되는 글 개수 세기 1건")
    @Test
    public void countDsply_3() {
        // dsply_chk = "Y"으로 글 1개 입력
        String dsply_chk = "Y";
        NoticeDto noticeDto = createNoticeDto(1, dsply_chk).get(0);
        assertTrue(noticeDao.insert(noticeDto) == 1);

        // 노출되는 글 개수 세면 결과 1개
        assertTrue(noticeDao.countDsply() == 1);

        // 총 글 개수는 1개
        assertTrue(noticeDao.count() == 1);
    }
     */

    // 게시글 전체 삭제하고 dsply_chk = "N"인 글 1개 입력한 후 개수 세면 0개
    // 게시글 전체 삭제하고 dsply_chk = "Y"인 글 1개 입력한 후 개수 세면 1개
    @DisplayName("일반사용자에게 노출되는 글 개수 세기 1건")
    @ParameterizedTest
    @CsvSource(value = {"N,0", "Y,1"})
    public void countDsply_2(String input, int expected) {
        // dsply_chk = "Y"으로 글 1개 입력
        String dsply_chk = input;
        NoticeDto noticeDto = createNoticeDto(1, dsply_chk).get(0);
        assertTrue(noticeDao.insert(noticeDto) == 1);

        // 노출되는 글 개수 세면 결과 1개
        assertTrue(noticeDao.countDsply() == expected);

        // 총 글 개수는 1개
        assertTrue(noticeDao.count() == 1);
    }

    // 게시글 전체 삭제하고 dsply_chk = "N" 50개, "Y" 50개 입력한 후 개수 세면 50개
    @DisplayName("일반사용자에게 노출되는 글 개수 세기 - Y 50건")
    @Test
    public void countDsply_4() {
        int n = 50;
        List<NoticeDto> NoticeDtoY = createNoticeDto(n, "Y");
        List<NoticeDto> NoticeDtoN = createNoticeDto(n, "N");

        for (int i = 0; i < n; i++) {
            assertTrue(noticeDao.insert(NoticeDtoY.get(i)) == 1);
            assertTrue(noticeDao.insert(NoticeDtoN.get(i)) == 1);
        }

        // 총 글 개수는 100개
        assertTrue(noticeDao.count() == n * 2);

        // 노출되는 글 개수 세면 결과 50개
        assertTrue(noticeDao.countDsply() == n);
    }

    // 공지사항 게시글 입력하는 기능
    // 게시글 1건 입력 후 글 개수 조회하면 1개 & 디비에서 게시글을 읽어와 비교하면 입력한 게시글과 동일
    @DisplayName("글 입력하기 1건")
    @Test
    public void insert_1건() {
        // 게시글 1건 입력
        NoticeDto noticeDto = createNoticeDto(1, "Y").get(0);
        assertTrue(noticeDao.insert(noticeDto) == 1);

        // 전체 글 불러와서 notice_seq 획득
        Integer ntcSeq = noticeDao.selectAll().get(0).getNtc_seq();

        // notice_seq로 불러온 게시글과 저장한 게시글(noticeDto) 동일해야 함
        NoticeDto select = noticeDao.select(ntcSeq);

        // 입력한 값이 제대로 저장됐는지 값 비교하여 확인
        compareDto(noticeDto, select);
    }

    // 게시글 10건 입력 후 글 개수 조회하면 10개 & 디비에서 게시글을 읽어와 비교하면 입력한 게시글과 동일
    @DisplayName("글 입력하기 10건")
    @Test
    public void insert_10건() {
        // 게시글 10건 입력
        int N = 10;
        String dsply_chk = "Y";
        List<NoticeDto> noticeList = createNoticeDto(N, dsply_chk);
        for (int i = 0; i < N; i++) {
            assertTrue(noticeDao.insert(noticeList.get(i)) == 1);
        }
        assertTrue(noticeDao.count() == N);

        // 전체 리스트를 불러와
        List<NoticeDto> noticeListSelect = noticeDao.selectAll();

        // 저장한 list와 불러온 List 모두 제목 기준으로 정렬해
        Collections.sort(noticeList, comparator);
        Collections.sort(noticeListSelect, comparator);

        // 두 리스트 순회하면서 동일한지 확인해
        for (int i = 0; i < noticeListSelect.size(); i++) {
            compareDto(noticeListSelect.get(i), noticeList.get(i));
        }
    }

    // 게시글 100건 입력 후 글 개수 조회하면 100개
    @DisplayName("글 입력하기 100건")
    @Test
    public void insert_100건() {
        int N = 100;
        String dsply_chk = "Y";
        List<NoticeDto> noticeList = createNoticeDto(N, dsply_chk);
        for (int i = 0; i < N; i++) {
            assertTrue(noticeDao.insert(noticeList.get(i)) == 1);
        }
        assertTrue(noticeDao.count() == N);
    }

    // 필수 입력 항목에 null 입력 후 글 저장하면 DataIntegrityViolationException 예외 발생
    @DisplayName("글 입력 - 필수 입력 항목에 Null 넣으면 예외 발생")
    @Test
    public void insert_exception_1() {
        NoticeDto noticeDto = createNoticeDto(1, "Y").get(0);
        noticeDto.setTitle(null);
        assertThrows(DataIntegrityViolationException.class, () -> noticeDao.insert(noticeDto));
    }

    // 지정한 값의 범위보다 큰 경우
    @DisplayName("컬럼에 지정한 값의 범위보다 큰 값을 입력 시도할 때 예외 발생")
    @Test
    public void insert_exception_2() {
        NoticeDto noticeDto = createNoticeDto(1, "Y").get(0);
        noticeDto.setDsply_chk("YYY");
        assertThrows(DataIntegrityViolationException.class, () -> noticeDao.insert(noticeDto));
    }

    // 글 읽기 테스트해야 함!!! - 카테고리 이름까지 같이 읽게 되어있음!
    // 디비에 아무런 글도 저장하지 않았을 때 아무런 글도 읽어오지 못해야 한다.
    @DisplayName("글 읽기 - DB에 아무런 값이 없을 때 아무런 글도 읽어오지 못해야 한다.")
    @Test
    public void select_실패() {
        assertTrue(noticeDao.count() == 0);

        int random = (int) (Math.random() * 5000) + 1;
        assertTrue(noticeDao.select(random) == null);
    }

    // 글 번호로 글을 찾으면 글 번호가 동일한 단 하나의 글이 읽어와져야 한다.
    // 글 번호로 글을 찾으면 내가 저장한 내용이 그대로 읽어와져야 한다.
    @DisplayName("글 읽기 - 글 번호로 글을 찾으면 글 번호와 저장한 글 내용이 동일한 단 하나의 글이 읽어와져야 한다.")
    @Test
    public void select_성공1() {
        // 글 입력
        NoticeDto noticeDto = new NoticeDto("N10", "title", "content", "N", null, "Y", "110111");
        assertTrue(noticeDao.insert(noticeDto) == 1);

        // 글 전체읽기로 읽어와서 글 번호 확인
        Integer ntcSeq = noticeDao.selectAll().get(0).getNtc_seq();

        // 글 번호로 글 읽어오면 읽어온 글의 글번호와 찾은 글번호가 동일해야 한다.
        NoticeDto select = noticeDao.select(ntcSeq);
        assertTrue(select.getNtc_seq().equals(ntcSeq));

        // 읽어온 글의 내용이 저장한 값과 동일해야 한다.
        compareDto(noticeDto, select);
    }


    // !!!!!! notice_cate 공지사항 카테고리 테이블 mapper와 dto, dao 만든 후 시행하쟈!!!!!!
    // 글을 읽어올 때 공지사항 분류 이름을 함께 읽어와야 한다. 단, 내가 저장한 코드에 대응하는 이름이어야 한다.
    @DisplayName("글 읽기 - 글을 읽어올 때 공지사항 분류 이름을 함께 읽어와야 한다.")
    @Test
    public void select_성공2() {
        String CATE_CODE = "N10";
        String CATE_NAME = "고객센터";

        // 공지사항 카테고리 테이블 모두 삭제 후 데이터 입력

        // 공지사항 테이블 모두 삭제 후 데이터 1건 입력

        // 전체 글 불러와서 글 번호 확인

        // 확인한 글 번호로 글 읽기

        // 읽은 글의 공지사항 카테고리의 이름이 제대로 읽어와졌는지 확인
    }

    // 없는 글 번호로 글을 찾으면 아무런 글도 읽어오지 못해야 한다.
    // 없는 글 번호? 가장 마지막 글 번호 + 1 => auto increment니까!!!
    @DisplayName("없는 글 번호로 글을 찾으면 아무런 글도 읽어오지 못해야 한다.")
    @Test
    public void select_실패1() {
        // 글 하나 저장
        String dsply_chk = "Y";
        NoticeDto noticeDto = createNoticeDto(1, dsply_chk).get(0);
        assertTrue(noticeDao.insert(noticeDto) == 1);

        // 글 번호 획득
        Integer ntcSeq = noticeDao.selectAll().get(0).getNtc_seq();

        // 글 번호 + 1 (없는 글 번호)로 글 읽으면 Null
        assertTrue(noticeDao.select(ntcSeq + 1) == null);
        assertTrue(noticeDao.count() == 1);
    }

    // 해당 글을 삭제하면 해당 글 번호로 글을 찾아도 아무런 글을 읽어오지 못해야 한다.
    @DisplayName("해당 글을 삭제하면 해당 글 번호로 글을 찾아도 아무런 글을 읽어오지 못해야 한다.")
    @Test
    public void select_실패2() {
        // 글 하나 저장
        String dsply_chk = "Y";
        NoticeDto noticeDto = createNoticeDto(1, dsply_chk).get(0);
        assertTrue(noticeDao.insert(noticeDto) == 1);

        // 글 번호 획득
        Integer ntcSeq = noticeDao.selectAll().get(0).getNtc_seq();

        // 글 번호로 글 삭제
        assertTrue(noticeDao.delete(ntcSeq) == 1);
        assertTrue(noticeDao.count() == 0);

        // 글 번호로 읽어오면 null
        assertTrue(noticeDao.select(ntcSeq) == null);
    }

    // selectAll() 테스트 하기!!!
    // 글 0개 저장 후 selectAll 하면 읽어온 dto List의 사이즈가 0이어야 한다.
    @DisplayName("글 0개 저장 후 selectAll 해서 받아온 List의 size는 0")
    @Test
    public void selectAll_1() {
        List<NoticeDto> noticeDtoList = noticeDao.selectAll();
        assertTrue(noticeDtoList.size() == 0);
    }

    // 글 n개 저장 후 selectAll 하면 읽어온 dto List의 사이즈가 n이어야 하고, 저장한 내용이 같아야 한다.
    @DisplayName("글 n개 저장 후 selectAll 한 List의 size는 n, 내용 동일")
    @ParameterizedTest
    @CsvSource({"1,1", "9,9", "99,99"})
    public void selectAll_2(int input, int expected) {
        // 글 n개 저장
        List<NoticeDto> noticeDtoList = createNoticeDto(input, "Y");
        for (int i = 0; i < noticeDtoList.size(); i++) {
            assertTrue(noticeDao.insert(noticeDtoList.get(i)) == 1);
        }

        // selectAll 해서 사이즈 비교
        List<NoticeDto> selectList = noticeDao.selectAll();
        assertTrue(selectList.size() == expected);

        // 저장한 글의 리스트와 불러온 글의 리스트 각각 정렬
        Collections.sort(noticeDtoList, comparator);
        Collections.sort(selectList, comparator);

        // 정렬된 두 리스트 내 DTO 내용 동일한지 비교
        for(int i = 0; i < selectList.size(); i++) {
            compareDto(selectList.get(i), noticeDtoList.get(i));
        }
    }

    // 일반사용자에게 노출되는 글 전체목록 읽기 : dsply_chk = "Y"인 글만 읽어와야 함
    // 저장된 글이 0건이면 selectDsply로 읽어온 리스트의 사이즈는 0이다.
    @DisplayName("저장된 글이 0건이면 selectDsply로 읽어온 리스트의 사이즈는 0이다.")
    @Test
    public void selectDsply_1() {
        List<NoticeDto> noticeDtoList = noticeDao.selectDsply();
        assertTrue(noticeDtoList.size() == 0);
    }

    // 저장된 글이 모두 dsply_chk = "N"이면 selectDsply로 읽어온 리스트의 사이즈는 0이다.
    @DisplayName("저장된 글이 모두 dsply_chk = \"N\"이면 selectDsply로 읽어온 리스트의 사이즈는 0이다.")
    @Test
    public void selectDsply_2() {
        String dsply_chk = "N";
        List<NoticeDto> noticeDtoList = createNoticeDto(10, dsply_chk);
        for (int i = 0; i < noticeDtoList.size(); i++) {
            assertTrue(noticeDao.insert(noticeDtoList.get(i)) == 1);
        }

        List<NoticeDto> selectList = noticeDao.selectDsply();
        assertTrue(selectList.size() == 0);
    }

    // dsply_chk = "Y"인 글 n개 저장 시, selectDsply로 읽어온 리스트의 사이즈는 n이다.
    // selectDsply로 읽어온 리스트와 dsply_chk = "Y"으로 저장한 리스트가 동일해야 한다.
    @DisplayName("dsply_chk = \"Y\"인 글 n개 저장 시, selectDsply로 읽어온 리스트의 사이즈는 n이다.")
    @ParameterizedTest
    @CsvSource({"0,0", "1,1", "10,10"})
    public void selectDsply_3(int input, int expected) {
        // 노출 예스로 글 저장
        String dsply_chk = "Y";
        List<NoticeDto> noticeDtoList = createNoticeDto(input, dsply_chk);
        for (int i = 0; i < noticeDtoList.size(); i++) {
            assertTrue(noticeDao.insert(noticeDtoList.get(i)) == 1);
        }

        // 노출되는 글 목록 읽어와서 사이즈 확인
        List<NoticeDto> selectList = noticeDao.selectDsply();
        assertTrue(selectList.size() == expected);

        // 저장한 글과 읽어온 글 리스트 각각 정렬 후 동일한지 확인
        Collections.sort(noticeDtoList, comparator);
        Collections.sort(selectList, comparator);
        for(int i = 0; i < selectList.size(); i++) {
            compareDto(selectList.get(i), noticeDtoList.get(i));
        }
    }

    // dsply_chk = "Y"로 게시글 하나 저장 후, "N"으로 바꾸면 selectDsply로 읽어온 리스트의 사이즈는 0이다.
    @DisplayName("노출여부 Y로 저장 후 -> N로 변경 시, 읽어온 리스트 사이즈 0")
    @Test
    public void selectDsply_4() {
        // "Y"로 글 저장
        String dsply_chk = "Y";
        NoticeDto noticeDto = createNoticeDto(1, dsply_chk).get(0);
        assertTrue(noticeDao.insert(noticeDto) == 1);

        // selectDsply로 읽어온 리스트 사이즈 1인지 확인
        List<NoticeDto> selectList = noticeDao.selectDsply();
        assertTrue(selectList.size() == 1);

        // ntc_seq 번호 획득해서 해당 글 노출여부 "N"으로 변경
        Integer ntcSeq = selectList.get(0).getNtc_seq();
        noticeDto.setNtc_seq(ntcSeq);

        dsply_chk = "N";
        noticeDto.setDsply_chk(dsply_chk);
        assertTrue(noticeDao.update(noticeDto) == 1);

        // selectDsply로 읽어온 리스트 사이즈 0인지 확인
        assertTrue(noticeDao.selectDsply().size() == 0);
    }

    // dsply_chk = "N"로 게시글 하나 저장 후, "Y"으로 바꾸면 selectDsply로 읽어온 리스트의 사이즈는 1이며, 내용이 동일해야 한다.
    @DisplayName("노출여부 N로 저장 후 -> Y로 변경 시, 읽어온 리스트 사이즈 1, 내용 동일")
    @Test
    public void selectDsply_5() {
        // "Y"로 글 저장
        String dsply_chk = "N";
        NoticeDto noticeDto = createNoticeDto(1, dsply_chk).get(0);
        assertTrue(noticeDao.insert(noticeDto) == 1);

        // selectDsply로 읽어온 리스트 사이즈 0인지 확인
        assertTrue(noticeDao.selectDsply().size() == 0);

        // ntc_seq 번호 획득해서 해당 글 노출여부 "Y"으로 변경
        Integer ntcSeq = noticeDao.selectAll().get(0).getNtc_seq();
        noticeDto.setNtc_seq(ntcSeq);

        dsply_chk = "Y";
        noticeDto.setDsply_chk(dsply_chk);
        assertTrue(noticeDao.update(noticeDto) == 1);

        // selectDsply로 읽어온 리스트 사이즈 1인지 확인
        List<NoticeDto> selectList = noticeDao.selectDsply();
        assertTrue(selectList.size() == 1);
        compareDto(selectList.get(0), noticeDto);
    }

    // selectPage Test 해야 함!!! -> 아... PageHandler Test 해야 함..

    // selectPageDsply Test 해야 함!!! -> 아... PageHandler Test...

    // 게시글 수정 update


    // 게시글 삭제 delete
    // 게시글 전체삭제 deleteAll
    // 조회수 +1 increaseViewCnt


    // Integer 타입 최대값(2_147_483_647)보다 큰 값 저장 시도하는 경우 -> DataIntegrityViolationException 발생
}