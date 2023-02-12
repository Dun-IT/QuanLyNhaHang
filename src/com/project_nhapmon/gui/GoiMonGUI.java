package com.project_nhapmon.gui;

import com.project_nhapmon.connect.BanAnService;
import com.project_nhapmon.connect.GoiMonService;
import com.project_nhapmon.connect.MenuService;
import com.project_nhapmon.model.MonAn;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.Objects;
import java.util.Vector;

public class GoiMonGUI extends JFrame {
    // Các thuộc tính
    private final String maTang;
    private final String maBan;
    private DefaultTableModel dtmDsMenuNhaHang;
    private JTextField txtTimKiem;
    private Vector<MonAn> dsMonAnTrongMenu = new Vector<>();
    private final MenuService menuService = new MenuService();
    private JButton btnThem;
    private final GoiMonService goiMonService = new GoiMonService();
    private final DefaultTableModel dtmThucDonTheoBan;
    private final BanAnService banAnService = new BanAnService();
    private JTable tblDsMenuNhaHang;
    private JTextField txtSoLuong;
    private final JPanel[] pnStatusBanAn;

    // Triển khai phương thức khởi tạo
    public GoiMonGUI(String maTang, String maBan, DefaultTableModel dtmThucDonTheoBan, JPanel[] pnStatusBanAn) {
        super("Gọi món bàn " + maBan + " tầng " + maTang);
        this.maTang = maTang;
        this.maBan = maBan;
        this.dtmThucDonTheoBan = dtmThucDonTheoBan;
        this.pnStatusBanAn = pnStatusBanAn;
        addControls();
        addEvent();
    }

    // Triển khai phương thức addControls
    private void addControls() {
        // Tạo khu vực tìm kiếm
        JPanel pnTimKiem = new JPanel();
        pnTimKiem.setLayout(new FlowLayout());
        JLabel lblTimKiem = new JLabel();
        lblTimKiem.setIcon(new ImageIcon(Objects.requireNonNull(GoiMonGUI.class.getResource("/com/project_nhapmon/images/search.png"))));
        lblTimKiem.setVerticalTextPosition(JLabel.CENTER);
        lblTimKiem.setHorizontalTextPosition(JLabel.CENTER);
        txtTimKiem = new JTextField(15);
        pnTimKiem.add(lblTimKiem);
        pnTimKiem.add(txtTimKiem);

        // Tạo khu vực hiển thị danh sách menu
        JPanel pnDsMenuNhaHang = new JPanel();
        pnDsMenuNhaHang.setLayout(new BorderLayout());
        dtmDsMenuNhaHang = new DefaultTableModel();
        dtmDsMenuNhaHang.addColumn("Mã món ăn");
        dtmDsMenuNhaHang.addColumn("Tên món ăn");
        dtmDsMenuNhaHang.addColumn("Đơn giá");
        tblDsMenuNhaHang = new JTable(dtmDsMenuNhaHang);
        tblDsMenuNhaHang.setDefaultEditor(Object.class, null);
        hienThiMenuNhaHang();
        JScrollPane crollDsMenuNhaHang = new JScrollPane(tblDsMenuNhaHang, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        pnDsMenuNhaHang.add(crollDsMenuNhaHang, BorderLayout.CENTER);

        // Tạo khu vực hiển thị chức năng thêm số lượng
        JPanel pnThemMonAn = new JPanel();
        pnThemMonAn.setLayout(new FlowLayout());
        Border border = BorderFactory.createLineBorder(Color.BLUE);
        TitledBorder titledBorder = new TitledBorder(border, "Thêm số lượng");
        titledBorder.setTitleFont(new Font(Font.SERIF, Font.BOLD, 15));
        pnThemMonAn.setBorder(titledBorder);
        JLabel lblSoLuong = new JLabel("Số lượng: ");
        txtSoLuong = new JTextField(15);
        txtSoLuong.setText("1");
        btnThem = new JButton("Thêm món ăn");
        pnThemMonAn.add(lblSoLuong);
        pnThemMonAn.add(txtSoLuong);
        pnThemMonAn.add(btnThem);

        //
        Container con = getContentPane();
        JPanel pnMain = new JPanel();
        pnMain.setLayout(new BorderLayout());
        pnMain.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pnMain.add(pnTimKiem, BorderLayout.NORTH);
        pnMain.add(pnDsMenuNhaHang, BorderLayout.CENTER);
        pnMain.add(pnThemMonAn, BorderLayout.SOUTH);
        con.add(pnMain);
    }

    // Triển khai phương thức addEvents
    private void addEvent() {
        txtTimKiem.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                locThongTin();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                locThongTin();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                locThongTin();
            }
        });

        btnThem.addActionListener(e -> {
            int rowSelected = tblDsMenuNhaHang.getSelectedRow();
            if (rowSelected != -1) {
                MonAn monAn = new MonAn();
                monAn.setMaMonAn((String) dtmDsMenuNhaHang.getValueAt(rowSelected, 0));
                monAn.setTenMonAn((String) dtmDsMenuNhaHang.getValueAt(rowSelected, 1));
                monAn.setDonGia((Integer) dtmDsMenuNhaHang.getValueAt(rowSelected, 2));
                String strSoLuong = txtSoLuong.getText();
                if (strSoLuong != null && strSoLuong.trim().length() > 0 && strSoLuong.matches("^\\d+$")) {
                    int soLuong = Integer.parseInt(strSoLuong);
                    goiMonService.themMonAn(maTang, maBan, monAn, soLuong);
                    hienThiThucDonBanAn(maTang, maBan);
                    if (goiMonService.laySoLuongMonAn(maTang, maBan) > 0) {
                        banAnService.setTrangThaiBanAn(maTang, maBan, "busy");
                    } else {
                        banAnService.setTrangThaiBanAn(maTang, maBan, "active");
                    }
                    capNhatTrangThaiBanAn(maTang);
                } else {
                    JOptionPane.showMessageDialog(GoiMonGUI.this, "Số lượng không hợp lệ!");
                }
            }
        });
    }

    // Triển khai phương thức showWindow
    public void showWindow(Frame parent) {
        this.setIconImage(new ImageIcon(Objects.requireNonNull(GoiMonGUI.class.getResource("/com/project_nhapmon/images/icon.png"))).getImage());
        this.setSize(500, 300);
        this.setLocationRelativeTo(parent);
        this.setVisible(true);
    }

    private void docDuLieu() {
        dsMonAnTrongMenu = menuService.layDanhSachMenu();
    }

    public void locThongTin() {
        docDuLieu();
        String duLieuLoc = txtTimKiem.getText();
        Vector<MonAn> dsMonAnDaLoc = new Vector<>();
        for (MonAn monAn : dsMonAnTrongMenu) {
            if (monAn.getMaMonAn().toUpperCase().contains(duLieuLoc.toUpperCase())
                    || monAn.getTenMonAn().toUpperCase().contains(duLieuLoc.toUpperCase())) {
                dsMonAnDaLoc.add(monAn);
            }
        }
        if (duLieuLoc.trim().length() > 0) {
            hienThiMenuNhaHang(dsMonAnDaLoc);
        } else {
            hienThiMenuNhaHang();
        }
    }

    private void hienThiMenuNhaHang() {
        dtmDsMenuNhaHang.setRowCount(0);
        dsMonAnTrongMenu = menuService.layDanhSachMenu();
        for (MonAn monAn : dsMonAnTrongMenu) {
            Vector<Object> vec = new Vector<>();
            vec.add(monAn.getMaMonAn());
            vec.add(monAn.getTenMonAn());
            vec.add(monAn.getDonGia());
            dtmDsMenuNhaHang.addRow(vec);
        }
    }

    private void hienThiMenuNhaHang(Vector<MonAn> dsMonAn) {
        dtmDsMenuNhaHang.setRowCount(0);
        for (MonAn monAn : dsMonAn) {
            Vector<Object> vec = new Vector<>();
            vec.add(monAn.getMaMonAn());
            vec.add(monAn.getTenMonAn());
            vec.add(monAn.getDonGia());
            dtmDsMenuNhaHang.addRow(vec);
        }
    }

    private void hienThiThucDonBanAn(String maTang, String maBan) {
        Vector<MonAn> dsThucDonTheoBan = goiMonService.layDsThucDonTheoBan(maTang, maBan);
        dtmThucDonTheoBan.setRowCount(0);
        DecimalFormat df = new DecimalFormat("#,###");
        for (MonAn monAn : dsThucDonTheoBan) {
            Vector<Object> vec = new Vector<>();
            vec.add(monAn.getMaMonAn());
            vec.add(monAn.getTenMonAn());
            vec.add(df.format(monAn.getDonGia()));
            vec.add(monAn.getSoLuong());
            vec.add(df.format((long) monAn.getDonGia() * monAn.getSoLuong()));
            dtmThucDonTheoBan.addRow(vec);
        }
    }

    private void capNhatTrangThaiBanAn(String maTang) {
        for (int j = 0; j < 12; j++) {
            if (banAnService.getTrangThaiBanAn(maTang, String.valueOf(j + 1)).equalsIgnoreCase("active")) {
                pnStatusBanAn[j].setBackground(Color.GREEN);
            } else {
                pnStatusBanAn[j].setBackground(Color.RED);
            }
        }
    }
}
