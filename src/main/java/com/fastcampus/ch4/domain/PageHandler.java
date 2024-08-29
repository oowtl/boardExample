package com.fastcampus.ch4.domain;

public class PageHandler {
    int pageSize; // 한 페이지 당 최대로 표현할 수 있는 게시글의 개수
    int navigationSize = 10; // 한 목록에 최대로 나타낼 수 있는 페이지 개수

    boolean showPrev; // 이전 페이지 목록으로 이동 가능 여부
    boolean showNext; // 이후 페이지 목록으로 이동 가능 여부

    int page; // 현재 페이지
    int startPage; // 현재 페이지 목록의 시작 페이지
    int endPage; // 현재 페이지 목록에서 가장 큰 페이지 (끝 페이지)
    int totalPage; // 전체 페이지 개수 (가장 큰 페이지)

    int totalCount; // 총 게시글 개수

    public PageHandler(int page, int totalCount, int pageSize) {
        this.totalCount = totalCount;
        this.page = page;
        this.pageSize = pageSize;

        /*
            _ 1, 2, 3, 4, 5, 6, 7, 8
            _ 1, 2, 3, 4, 5, 6, 7, 8, 9, 10
            _ 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 >
            < 11, 12, 13, 14, 15, 16, 17, 18, 19, 20
            < 11, 12, 13, 14, 15, 16, 17, 18, 19, 20 >
            < 21, 22, 23, 24
         */

        this.totalPage = (int) Math.ceil(this.totalCount / (double)this.pageSize);

        /*
        page  startPage  endPage
        1           1           10 || totalPage
        4           1           10 || totalPage
        14          11          20 || totalPage
        16          11          20 || totalPage
         */
        this.startPage = (page - 1) / this.navigationSize * this.navigationSize + 1;
        this.endPage = Math.min(this.startPage + this.navigationSize - 1, this.totalPage);

        this.showPrev = this.startPage != 1;
        this.showNext = this.endPage != this.totalPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getNavigationSize() {
        return navigationSize;
    }

    public boolean isShowPrev() {
        return showPrev;
    }

    public boolean isShowNext() {
        return showNext;
    }

    public int getPage() {
        return page;
    }

    public int getStartPage() {
        return startPage;
    }

    public int getEndPage() {
        return endPage;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public int getTotalCount() {
        return totalCount;
    }
}
