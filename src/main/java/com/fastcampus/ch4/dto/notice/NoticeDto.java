package com.fastcampus.ch4.dto.notice;

import java.util.Date;
import java.util.Objects;

public class NoticeDto {
    Integer ntc_seq;
    String ntc_cate_code;
    String title;
    String cont;
    String pin_top_chk;
    String pin_top_end_date;
    String dsply_chk;
    Integer view_cnt;
    Date reg_date;
    String reg_id;
    Date up_date;
    String up_id;
    String cate_name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NoticeDto noticeDto = (NoticeDto) o;
        return Objects.equals(ntc_seq, noticeDto.ntc_seq) && Objects.equals(title, noticeDto.title) && Objects.equals(cont, noticeDto.cont);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ntc_seq, title, cont);
    }

    public NoticeDto() {
    }

    public NoticeDto(String ntc_cate_code, String title, String cont, String pin_top_chk, String pin_top_end_date, String dsply_chk, String reg_id) {
        this.ntc_cate_code = ntc_cate_code;
        this.title = title;
        this.cont = cont;
        this.pin_top_chk = pin_top_chk;
        this.pin_top_end_date = pin_top_end_date;
        this.dsply_chk = dsply_chk;
        this.reg_id = reg_id;
    }

    public Integer getNtc_seq() {
        return ntc_seq;
    }

    public void setNtc_seq(Integer ntc_seq) {
        this.ntc_seq = ntc_seq;
    }

    public String getNtc_cate_code() {
        return ntc_cate_code;
    }

    public void setNtc_cate_code(String ntc_cate_code) {
        this.ntc_cate_code = ntc_cate_code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCont() {
        return cont;
    }

    public void setCont(String cont) {
        this.cont = cont;
    }

    public String getPin_top_chk() {
        return pin_top_chk;
    }

    public void setPin_top_chk(String pin_top_chk) {
        this.pin_top_chk = pin_top_chk;
    }

    public String getPin_top_end_date() {
        return pin_top_end_date;
    }

    public void setPin_top_end_date(String pin_top_end_date) {
        this.pin_top_end_date = pin_top_end_date;
    }

    public String getDsply_chk() {
        return dsply_chk;
    }

    public void setDsply_chk(String dsply_chk) {
        this.dsply_chk = dsply_chk;
    }

    public Integer getView_cnt() {
        return view_cnt;
    }

    public void setView_cnt(Integer view_cnt) {
        this.view_cnt = view_cnt;
    }

    public Date getReg_date() {
        return reg_date;
    }

    public void setReg_date(Date reg_date) {
        this.reg_date = reg_date;
    }

    public String getReg_id() {
        return reg_id;
    }

    public void setReg_id(String reg_id) {
        this.reg_id = reg_id;
    }

    public Date getUp_date() {
        return up_date;
    }

    public void setUp_date(Date up_date) {
        this.up_date = up_date;
    }

    public String getUp_id() {
        return up_id;
    }

    public void setUp_id(String up_id) {
        this.up_id = up_id;
    }

    public String getCate_name() {
        return cate_name;
    }

    public void setCate_name(String cate_name) {
        this.cate_name = cate_name;
    }

    @Override
    public String toString() {
        return "NoticeDto{" +
                "ntc_seq=" + ntc_seq +
                ", ntc_cate_code='" + ntc_cate_code + '\'' +
                ", title='" + title + '\'' +
                ", cont='" + cont + '\'' +
                ", pin_top_chk='" + pin_top_chk + '\'' +
                ", pin_top_end_date='" + pin_top_end_date + '\'' +
                ", dsply_chk='" + dsply_chk + '\'' +
                ", view_cnt=" + view_cnt +
                ", reg_date=" + reg_date +
                ", reg_id='" + reg_id + '\'' +
                ", up_date=" + up_date +
                ", up_id='" + up_id + '\'' +
                ", cate_name='" + cate_name + '\'' +
                '}';
    }
}