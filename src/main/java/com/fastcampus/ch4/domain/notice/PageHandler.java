package com.fastcampus.ch4.domain.notice;

public class PageHandler {
    static final int DEFAULT_PAGE_SIZE = 10;
    static final int DEFAULT_PAGE_NUMBER = 1;

    private int page = DEFAULT_PAGE_NUMBER;
    private int pageSize = DEFAULT_PAGE_SIZE;

    private int totalCnt;
    private int totalPage;

    private final int NAVI_SIZE = 10;
    private int beginPage;
    private int endPage;
    private boolean showPrev = false;
    private boolean showNext = false;

    public PageHandler(int page, int totalCnt) {
        this(page, DEFAULT_PAGE_SIZE, totalCnt);
    }

    public PageHandler(int page, int pageSize, int totalCnt) {
        // 입력값으로 유효한 값이 들어왔을 때만 값 지정(아니면 디폴트값)
        if (isValidPage(page)) {
            this.page = page;
        }
        if (isValidPageSize(pageSize)) {
            this.pageSize = pageSize;
        }
        this.totalCnt = totalCnt;
        this.totalPage = calculateTotalPages(totalCnt);
        this.beginPage = calculateBeginPage();
        this.endPage = Math.min((beginPage + NAVI_SIZE - 1), totalPage);
        this.showPrev = beginPage > 1;
        this.showNext = endPage < totalPage;
    }

    // 메서드명 : calculateTotalPages
    // 매개변수 : int totalCnt
    // 반환값 : int totalPage
    // 기능 : 총 게시글의 건수를 받아서 게시글이 총 몇 페이지인지 계산하는 기능
    private int calculateTotalPages(int totalCnt) {
        // 전체 게시글 건수를 한 페이지 사이즈로 나눈다.
        // 나눈 나머지가 0이면 그대로, 1 이상이면 한 페이지를 더한다!
        return totalCnt / pageSize + (totalCnt % pageSize == 0 ? 0 : 1);
    }

    // 한 줄이면 그냥 생성자 내부에 쓰는 게 낫나?
    private int calculateBeginPage() {
        return (page-1) / NAVI_SIZE * NAVI_SIZE + 1;
    }

    /*
    // page나 pageSize가 유효하지 않은 값이 들어왔을 때 디폴트 값으로 보여주는 게 사용자 편의성에서 낫겠다.
    // 입력값 유효성 검사
    private void validate(int page, int pageSize, int totalCnt) {
        if (!isValidPage(page)) {
            throw new IllegalArgumentException("Invalid page number: " + page);
        }
        if (!isValidPageSize(pageSize)) {
            throw new IllegalArgumentException("Invalid page size: " + pageSize);
        }
        if (!isValidTotalCnt(totalCnt)) {
            throw new IllegalArgumentException("Invalid total count: " + totalCnt);
        }
    }
     */

    // 유효한 페이지 번호인지 검사하는 기능
    private boolean isValidPage(int page) {
        // page가 0보다 커야 한다.
        // page가 토탈페이지와 같거나 작아야 한다.
        return page > 0 && page <= totalPage;
    }

    // 유효한 페이지 사이즈인지 검사하는 기능
    private boolean isValidPageSize(int pageSize) {
        // pageSize가 0보다 커야 한다.
        return pageSize > 0;
    }

    // 총 게시글 개수 유효성 검사..도 필요한가?
    private boolean isValidTotalCnt(int totalCnt) {
        // 0과 같거나 0보다 커야 한다.
        return totalCnt >= 0;
    }


    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        if (!isValidPage(page)) {
            throw new IllegalArgumentException("Invalid page number : " + page);
        }
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        if (!isValidPageSize(pageSize)) {
            throw new IllegalArgumentException("Invalid page size : " + pageSize);
        }
        this.pageSize = pageSize;
    }

    public int getTotalCnt() {
        return totalCnt;
    }

    public void setTotalCnt(int totalCnt) {
        this.totalCnt = totalCnt;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getNAVI_SIZE() {
        return NAVI_SIZE;
    }

    public int getBeginPage() {
        return beginPage;
    }

    public void setBeginPage(int beginPage) {
        this.beginPage = beginPage;
    }

    public int getEndPage() {
        return endPage;
    }

    public void setEndPage(int endPage) {
        this.endPage = endPage;
    }

    public boolean isShowPrev() {
        return showPrev;
    }

    public void setShowPrev(boolean showPrev) {
        this.showPrev = showPrev;
    }

    public boolean isShowNext() {
        return showNext;
    }

    public void setShowNext(boolean showNext) {
        this.showNext = showNext;
    }
}
