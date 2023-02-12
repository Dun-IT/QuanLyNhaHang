package com.project_nhapmon.model;

import java.io.Serializable;

public class MonAn implements Serializable {
    // Các thuộc tính
    private String maMonAn;
    private String tenMonAn;
    private int donGia;
    private int soLuong;

    // Triển khai phương thức khởi tạo
    public MonAn() {
        super();
    }

    public MonAn(String maMonAn, String tenMonAn, int donGia) {
        this.maMonAn = maMonAn;
        this.tenMonAn = tenMonAn;
        this.donGia = donGia;
    }

    // Triển khai các phương thức truy xuất và sửa đổi

    public String getMaMonAn() {
        return maMonAn;
    }

    public void setMaMonAn(String maMonAn) {
        this.maMonAn = maMonAn;
    }

    public String getTenMonAn() {
        return tenMonAn;
    }

    public void setTenMonAn(String tenMonAn) {
        this.tenMonAn = tenMonAn;
    }

    public int getDonGia() {
        return donGia;
    }

    public void setDonGia(int donGia) {
        this.donGia = donGia;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }
}
