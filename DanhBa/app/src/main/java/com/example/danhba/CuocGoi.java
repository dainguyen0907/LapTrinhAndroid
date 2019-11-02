package com.example.danhba;

public class CuocGoi {
    private byte[] hinh;
    private String ten;
    private String sodienthoai;
    private  String icon;
    private String ngaygoi;

    public CuocGoi(byte[] hinh, String ten,String Sodienthoai, String icon, String ngaygoi) {
        this.hinh = hinh;
        this.ten = ten;
        this.sodienthoai=Sodienthoai;
        this.icon = icon;
        this.ngaygoi = ngaygoi;
    }

    public byte[] getHinh() {
        return hinh;
    }

    public String getSodienthoai() {
        return sodienthoai;
    }

    public void setSodienthoai(String sodienthoai) {
        this.sodienthoai = sodienthoai;
    }

    public void setHinh(byte[] hinh) {
        this.hinh = hinh;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getNgaygoi() {
        return ngaygoi;
    }

    public void setNgaygoi(String ngaygoi) {
        this.ngaygoi = ngaygoi;
    }
}
