package com.project_nhapmon.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Objects;

public class AboutGUI extends JDialog {
    // Các thuộc tính

    // Triển khai phương thức khởi tạo

    /**
     * Phương thức AboutGUI được dùng để khởi tạo một của sổ dialog
     *
     * @param parent Frame nơi mà dialog được gọi tới để hiển thị
     * @param title Tiêu đề của của sổ dialog
     */
    public AboutGUI(Frame parent, String title) {
        super(parent, title, true);
        addControls();
    }

    // Triển khai phương thức addControls

    /**
     * Phương thức addControls được dùng để thêm các Component vào cửa sổ dialog chính
     */
    private void addControls() {
        // Tạo khu vực hiển thị tên project
        JPanel pnThongTinProject = new JPanel();
        pnThongTinProject.setLayout(new BorderLayout());
        JLabel lblThongTin = new JLabel("Project Nhập môn Java: Quản lý nhà hàng");
        lblThongTin.setFont(new Font(Font.SERIF, Font.BOLD, 25));
        lblThongTin.setVerticalAlignment(JLabel.CENTER);
        lblThongTin.setHorizontalAlignment(JLabel.CENTER);
        JLabel lblTenNhom = new JLabel("Nhóm 2");
        lblTenNhom.setHorizontalAlignment(JLabel.CENTER);
        lblTenNhom.setVerticalAlignment(JLabel.CENTER);
        lblTenNhom.setFont(new Font(Font.SERIF, Font.ITALIC, 18));
        pnThongTinProject.add(lblThongTin, BorderLayout.CENTER);
        pnThongTinProject.add(lblTenNhom, BorderLayout.SOUTH);

        // Tạo khu vực hiển thị thông tin các thành viên
        JPanel pnThongTinThanhVien = new JPanel();
        pnThongTinThanhVien.setLayout(new BorderLayout());
        DefaultTableModel dtmTableInfoMember = new DefaultTableModel();
        dtmTableInfoMember.addColumn("Họ và tên");
        dtmTableInfoMember.addColumn("MSSV");
        dtmTableInfoMember.addColumn("Lớp");
        dtmTableInfoMember.addColumn("Email");
        JTable tblInfoMember = new JTable(dtmTableInfoMember);
        tblInfoMember.getColumnModel().getColumn(3).setPreferredWidth(180);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tblInfoMember.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        tblInfoMember.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        dtmTableInfoMember.addRow(new Object[]{"Phạm Ngọc Hải", "20207601", "IT-LTU 02-K65", "hai.pn207601@sis.hust.edu.vn"});
        dtmTableInfoMember.addRow(new Object[]{"Nguyễn Hải Phong", "20207624", "IT-LTU 01-K65", "phong.nh207624@sis.hust.edu.vn"});
        dtmTableInfoMember.addRow(new Object[]{"Nguyễn Khoa Đoàn", "20207642", "IT-LTU 01-K65", "doan.nk207642@sis.hust.edu.vn"});
        dtmTableInfoMember.addRow(new Object[]{"Nguyễn Duy Thái", "20207630", "IT-LTU 01-K65", "thai.nd207630@sis.hust.edu.vn"});
        dtmTableInfoMember.addRow(new Object[]{"Đỗ Trung Hiếu", "20207604", "IT-LTU 01-K65", "hieu.dt207604@sis.hust.edu.vn"});
        JScrollPane scrollMember = new JScrollPane(tblInfoMember, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        pnThongTinThanhVien.add(scrollMember, BorderLayout.CENTER);

        // Tạo khu vực hiển thị logo
        JPanel pnLogo = new JPanel();
        pnLogo.setLayout(new BorderLayout());
        JLabel lblLogo = new JLabel();
        lblLogo.setIcon(new ImageIcon(Objects.requireNonNull(AboutGUI.class.getResource("/com/project_nhapmon/images/restaurant.png"))));
        lblLogo.setHorizontalTextPosition(JLabel.CENTER);
        lblLogo.setVerticalTextPosition(JLabel.CENTER);
        lblLogo.setVerticalAlignment(JLabel.CENTER);
        lblLogo.setHorizontalAlignment(JLabel.CENTER);
        pnLogo.add(lblLogo, BorderLayout.CENTER);

        //
        Container con = getContentPane();
        JPanel pnMain = new JPanel();
        pnMain.setLayout(new BorderLayout());
        pnMain.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pnMain.add(pnThongTinProject, BorderLayout.NORTH);
        pnMain.add(pnThongTinThanhVien, BorderLayout.CENTER);
        pnMain.add(pnLogo, BorderLayout.SOUTH);
        con.add(pnMain);
    }

    // Triển khai phương thức showDialog

    /**
     * Phương thức showDialog được dùng để hiển thị dialog lên màn hình
     *
     * @param parent Frame nơi mà hộp thoại sẽ hiển thị
     */
    public void showDialog(Frame parent) {
        this.setIconImage(new ImageIcon(Objects.requireNonNull(AboutGUI.class.getResource("/com/project_nhapmon/images/about.png"))).getImage());
        this.setSize(600, 400);
        this.setLocationRelativeTo(parent);
        this.setResizable(false);
        this.setVisible(true);
    }
}
