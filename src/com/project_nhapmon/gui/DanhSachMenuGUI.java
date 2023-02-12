package com.project_nhapmon.gui;

import com.project_nhapmon.connect.MenuService;
import com.project_nhapmon.model.MonAn;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Objects;
import java.util.Vector;

public class DanhSachMenuGUI extends JDialog implements ActionListener, MouseListener {
    // Các thuộc tính
    private JButton btnThem, btnXoa, btnChinhSua, btnThoat;
    private Vector<MonAn> dsMonAn;
    private final MenuService menuService = new MenuService();
    private DefaultTableModel dtmDsMenu;
    private JTable tblMenu;
    private int rowSelected = -1;
    private JTextField txtTimKiem;

    // Triển khai phương thức khởi tạo
    /**
     * Phương thức DanhSachMenuGUI được dùng để khởi tạo hộp thoại
     *
     * @param parent Frame nơi mà hộp thoại hiển thị
     * @param title Tiêu đề của hộp thoại
     */
    public DanhSachMenuGUI(Frame parent, String title) {
        super(parent, title, true);
        addControls();
        addEvents();
    }

    // Triển khai phương thức addControls
    /**
     * Phương thức addControls được dùng để thêm các Component vào Dialog
     */
    private void addControls() {
        // Tạo khu vực tìm kiếm
        JPanel pnTimKiem = new JPanel();
        pnTimKiem.setLayout(new FlowLayout());
        JLabel lblTimKiem = new JLabel();
        lblTimKiem.setIcon(new ImageIcon(Objects.requireNonNull(DanhSachMenuGUI.class.getResource("/com/project_nhapmon/images/search.png"))));
        txtTimKiem = new JTextField(15);
        pnTimKiem.add(lblTimKiem);
        pnTimKiem.add(txtTimKiem);

        // Tạo khu vực hiển thị danh sách thực đơn nhà hàng
        JPanel pnDsMenu = new JPanel();
        pnDsMenu.setLayout(new BorderLayout());
        dtmDsMenu = new DefaultTableModel();
        dtmDsMenu.addColumn("Mã món ăn");
        dtmDsMenu.addColumn("Tên món ăn");
        dtmDsMenu.addColumn("Đơn giá");
        tblMenu = new JTable(dtmDsMenu);
        tblMenu.getColumnModel().getColumn(1).setPreferredWidth(150);
        tblMenu.setDefaultEditor(Object.class, null);
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        tblMenu.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
        hienThiMenuMonAn();
        JScrollPane scrollMenu = new JScrollPane(tblMenu, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        pnDsMenu.add(scrollMenu, BorderLayout.CENTER);

        // Tạo khu vực các công cụ thêm, bớt, chỉnh sửa thực đơn
        JPanel pnCongCu = new JPanel();
        pnCongCu.setLayout(new FlowLayout(FlowLayout.RIGHT));
        btnThem = new JButton("Thêm");
        btnXoa = new JButton("Xóa");
        btnChinhSua = new JButton("Chỉnh sửa");
        btnThoat = new JButton("Thoát");
        pnCongCu.add(btnThem);
        pnCongCu.add(btnXoa);
        pnCongCu.add(btnChinhSua);
        pnCongCu.add(btnThoat);

        //
        Container con = getContentPane();
        JPanel pnMain = new JPanel();
        pnMain.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pnMain.setLayout(new BorderLayout());
        pnMain.add(pnTimKiem, BorderLayout.NORTH);
        pnMain.add(pnDsMenu, BorderLayout.CENTER);
        pnMain.add(pnCongCu, BorderLayout.SOUTH);
        con.add(pnMain);
    }

    // Triển khai phương thức addEvents
    /**
     * Phương thức addEvents được dùng để thêm các sự kiện cho các Component
     */
    private void addEvents() {
        btnThem.addActionListener(this);
        btnXoa.addActionListener(this);
        btnChinhSua.addActionListener(this);
        btnThoat.addActionListener(this);
        tblMenu.addMouseListener(this);
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
    }

    /**
     * Phương thức locThongTin được dùng để lọc các món ăn trong danh sách menu nhà hàng mà trong mã món ăn hay tên
     * món ăn có chứa dữ liệu nhập vào từ ô tìm kiếm
     */
    public void locThongTin() {
        docDuLieu();
        String duLieuLoc = txtTimKiem.getText();
        Vector<MonAn> dsMonAnDaLoc = new Vector<>();
        for (MonAn monAn : dsMonAn) {
            if (monAn.getMaMonAn().toUpperCase().contains(duLieuLoc.toUpperCase())
                    || monAn.getTenMonAn().toUpperCase().contains(duLieuLoc.toUpperCase())) {
                dsMonAnDaLoc.add(monAn);
            }
        }
        if (duLieuLoc.trim().length() > 0) {
            hienThiMenuMonAn(dsMonAnDaLoc);
        } else {
            hienThiMenuMonAn();
        }
    }

    // Triển khai phương thức showDialog
    /**
     * Phương thức showDialog được dùng để hiển thị dialog lên màn hình
     *
     * @param parent Frame nơi mà hộp thoại sẽ hiển thị
     */
    public void showDialog(Frame parent) {
        this.setSize(500, 350);
        this.setLocationRelativeTo(parent);
        this.setVisible(true);
    }

    /**
     * Phương thức docDuLieu được dùng để lấy toàn bộ danh sách menu nhà hàng
     */
    private void docDuLieu() {
        dsMonAn = menuService.layDanhSachMenu();
    }

    /**
     * Phương thức hienThiMenuMonAn được dùng để hiển thị toàn bộ món ăn trong danh sách menu nhà hàng
     * ra bảng danh sách
     */
    private void hienThiMenuMonAn() {
        docDuLieu();
        dtmDsMenu.setRowCount(0);
        for (MonAn monAn : dsMonAn) {
            Vector<Object> vec = new Vector<>();
            vec.add(monAn.getMaMonAn());
            vec.add(monAn.getTenMonAn());
            vec.add(monAn.getDonGia());
            dtmDsMenu.addRow(vec);
        }
    }

    /**
     * Phương thức hienThiMenuMonAn được dùng để hiển thị dsMonAnDaloc ra bảng danh sách
     *
     * @param dsMonAnDaLoc Danh sách menu món ăn cần hiển thị
     */
    private void hienThiMenuMonAn(Vector<MonAn> dsMonAnDaLoc) {
        dtmDsMenu.setRowCount(0);
        for (MonAn monAn : dsMonAnDaLoc) {
            Vector<Object> vec = new Vector<>();
            vec.add(monAn.getMaMonAn());
            vec.add(monAn.getTenMonAn());
            vec.add(monAn.getDonGia());
            dtmDsMenu.addRow(vec);
        }
    }

    /**
     * Phương thức kiemTraTonTaiMaMonAn được dùng để kiểm tra xem maMonAn đã tồn tại trong danh sách
     * Menu nhà hàng hay chưa
     *
     * @param maMonAn Mã món ăn cần kiểm tra
     * @return true nếu tồn tại hoặc false nếu không tồn tại
     */
    private boolean kiemTraTonTaiMaMonAn(String maMonAn) {
        for (MonAn monAn : dsMonAn) {
            if (monAn.getMaMonAn().equalsIgnoreCase(maMonAn)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnThoat) {
            this.setVisible(false);
        }

        if (e.getSource() == btnThem) {
            String maMonAn = JOptionPane.showInputDialog("Mã món ăn: ");
            if (maMonAn != null && maMonAn.trim().length() > 0) {
                if (!kiemTraTonTaiMaMonAn(maMonAn.trim())) {
                    String tenMonAn = JOptionPane.showInputDialog("Tên món ăn: ");
                    if (tenMonAn != null && tenMonAn.trim().length() > 0) {
                        String strDonGia = JOptionPane.showInputDialog("Đơn giá: ");
                        if (strDonGia != null && strDonGia.trim().length() > 0 && strDonGia.matches("^\\d+$")) {
                            int donGia = Integer.parseInt(strDonGia);
                            MonAn monAn = new MonAn(maMonAn, tenMonAn, donGia);
                            boolean status = menuService.themMonAn(monAn);
                            if (status) {
                                locThongTin();
                                JOptionPane.showMessageDialog(this, "Thêm món ăn thành công!");
                            }
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Mã món ăn đã tồn tại!");
                }
            }

        }

        if (e.getSource() == btnXoa) {
            rowSelected = tblMenu.getSelectedRow();
            if (rowSelected != -1) {
                String maMonAn = (String) tblMenu.getValueAt(rowSelected, 0);
                int chon = JOptionPane.showConfirmDialog(this, "Xác nhận xóa?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (chon == JOptionPane.YES_OPTION) {
                    boolean status = menuService.xoaMonAn(maMonAn);
                    if (status) {
                        locThongTin();
                        JOptionPane.showMessageDialog(this, "Xóa món ăn thành công!");
                    }
                }
            }
        }

        if (e.getSource() == btnChinhSua) {
            rowSelected = tblMenu.getSelectedRow();
            if (rowSelected != -1) {
                int colSelected = tblMenu.getSelectedColumn();
                MonAn monAn = new MonAn();
                monAn.setMaMonAn((String) tblMenu.getValueAt(rowSelected, 0));
                monAn.setTenMonAn((String) tblMenu.getValueAt(rowSelected, 1));
                monAn.setDonGia((Integer) tblMenu.getValueAt(rowSelected, 2));
                switch (colSelected) {
                    case 0 -> JOptionPane.showMessageDialog(this, "Không thể sửa đổi mã!");
                    case 1 -> {
                        String tenMonAn = JOptionPane.showInputDialog(this, "Tên món ăn: ", monAn.getTenMonAn());
                        if (tenMonAn != null && tenMonAn.trim().length() > 0) {
                            monAn.setTenMonAn(tenMonAn);
                            boolean status = menuService.chinhSuaThongTinMonAn(monAn);
                            if (status) {
                                locThongTin();
                                JOptionPane.showMessageDialog(this, "Sửa tên món ăn thành công!");
                            }
                        }
                    }
                    case 2 -> {
                        String strDonGia = JOptionPane.showInputDialog(this, "Đơn giá: ", monAn.getDonGia());
                        if (strDonGia != null && strDonGia.trim().length() > 0 && strDonGia.matches("^\\d+$")) {
                            int donGia = Integer.parseInt(strDonGia);
                            monAn.setDonGia(donGia);
                            boolean status = menuService.chinhSuaThongTinMonAn(monAn);
                            if (status) {
                                locThongTin();
                                JOptionPane.showMessageDialog(this, "Sửa đơn giá món ăn thành công!");
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == tblMenu) {
            rowSelected = tblMenu.getSelectedRow();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
