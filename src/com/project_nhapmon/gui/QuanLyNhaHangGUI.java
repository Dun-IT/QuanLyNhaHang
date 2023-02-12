package com.project_nhapmon.gui;

import com.project_nhapmon.connect.BanAnService;
import com.project_nhapmon.connect.GoiMonService;
import com.project_nhapmon.model.MonAn;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DecimalFormat;
import java.util.Objects;
import java.util.Vector;

public class QuanLyNhaHangGUI extends JFrame implements ActionListener, MouseListener {
    // Các thuộc tính
    private JMenuItem menuFileExit, menuAdvancedMenu, menuHelpAbout;
    private final BanAnService banAnService = new BanAnService();
    private final GoiMonService goiMonService = new GoiMonService();
    private JComboBox<String> comboTang;
    private JPanel[] pnStatusBanAn;
    private JButton btnThem, btnXoa, btnTinhTien, btnXuatThongTin, btnXacNhanThanhToan;
    private String banDaChon = "#", tangDaChon = "1";
    private int rowSelected = -1;
    private JLabel[] banAn;
    private JLabel lblVitriBanAn;
    private DefaultTableModel dtmTableThucDon;
    private JTable tblThucDon;

    // Triển khai phương thức khởi tạo
    public QuanLyNhaHangGUI(String title) {
        super(title);
        addControls();
        addEvents();
    }

    // Triển khai phương thức addControls
    private void addControls() {
        // Tạo thanh Menubar
        JMenuBar menuBar = new JMenuBar();

        JMenu menuFile = new JMenu("File");
        menuFileExit = new JMenuItem("Exit");
        menuFileExit.setIcon(new ImageIcon(Objects.requireNonNull(QuanLyNhaHangGUI.class.getResource("/com/project_nhapmon/images/exit.png"))));
        menuFileExit.setAccelerator(KeyStroke.getKeyStroke('Q', Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        menuFile.add(menuFileExit);

        JMenu menuAdvanced = new JMenu("Advanced");
        menuAdvancedMenu = new JMenuItem("Thực đơn nhà hàng");
        menuAdvancedMenu.setIcon(new ImageIcon(Objects.requireNonNull(QuanLyNhaHangGUI.class.getResource("/com/project_nhapmon/images/catalog.png"))));
        menuAdvanced.add(menuAdvancedMenu);

        JMenu menuHelp = new JMenu("Help");
        menuHelpAbout = new JMenuItem("About");
        menuHelpAbout.setIcon(new ImageIcon(Objects.requireNonNull(QuanLyNhaHangGUI.class.getResource("/com/project_nhapmon/images/about.png"))));
        menuHelpAbout.setAccelerator(KeyStroke.getKeyStroke('I', Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        menuHelp.add(menuHelpAbout);

        menuBar.add(menuFile);
        menuBar.add(menuAdvanced);
        menuBar.add(menuHelp);
        setJMenuBar(menuBar);

        // Tạo khu vực tầng và bàn ăn
        JPanel pnTangVaBanAn = new JPanel();
        pnTangVaBanAn.setLayout(new BorderLayout());
        pnTangVaBanAn.setBorder(BorderFactory.createRaisedBevelBorder());
        pnTangVaBanAn.setPreferredSize(new Dimension(250, 0));

        JPanel pnTang = new JPanel();
        pnTang.setLayout(new FlowLayout());
        JLabel lblTang = new JLabel("Tầng: ");
        lblTang.setFont(new Font("Serif", Font.BOLD, 15));
        String[] tenTang = {"1", "2", "3"};
        comboTang = new JComboBox<>(tenTang);
        pnTang.add(lblTang);
        pnTang.add(comboTang);

        JPanel pnDsBanAn = new JPanel();
        pnDsBanAn.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JPanel[] pnBanAn = new JPanel[12];
        pnStatusBanAn = new JPanel[12];
        banAn = new JLabel[12];
        for (int i = 0; i < 12; i++) {
            banAn[i] = new JLabel("Bàn " + (i + 1));
            banAn[i].setIcon(new ImageIcon(Objects.requireNonNull(QuanLyNhaHangGUI.class.getResource("/com/project_nhapmon/images/table.png"))));
            banAn[i].setHorizontalTextPosition(JLabel.CENTER);
            banAn[i].setVerticalTextPosition(JLabel.BOTTOM);
            pnBanAn[i] = new JPanel();
            pnBanAn[i].setLayout(new BorderLayout());
            pnBanAn[i].setBorder(BorderFactory.createRaisedBevelBorder());
            pnStatusBanAn[i] = new JPanel();
            pnStatusBanAn[i].setPreferredSize(new Dimension(0, 10));
            pnBanAn[i].add(pnStatusBanAn[i], BorderLayout.NORTH);
            pnBanAn[i].add(banAn[i], BorderLayout.CENTER);
            pnDsBanAn.add(pnBanAn[i]);
        }
        capNhatTrangThaiBanAn();

        JPanel pnLogo = new JPanel();
        pnLogo.setLayout(new BorderLayout());
        JLabel lblLogo = new JLabel();
        lblLogo.setIcon(new ImageIcon(Objects.requireNonNull(QuanLyNhaHangGUI.class.getResource("/com/project_nhapmon/images/restaurant.png"))));
        lblLogo.setHorizontalTextPosition(JLabel.CENTER);
        lblLogo.setVerticalTextPosition(JLabel.CENTER);
        pnLogo.add(lblLogo, BorderLayout.CENTER);

        pnTangVaBanAn.add(pnTang, BorderLayout.NORTH);
        pnTangVaBanAn.add(pnLogo, BorderLayout.SOUTH);
        pnTangVaBanAn.add(pnDsBanAn, BorderLayout.CENTER);

        // Tạo khu vực danh sách thực đơn và chức năng
        JPanel pnDanhSachVaChucNang = new JPanel();
        pnDanhSachVaChucNang.setLayout(new BorderLayout());
        pnDanhSachVaChucNang.setBorder(BorderFactory.createRaisedBevelBorder());

        JPanel pnThongTin = new JPanel();
        pnThongTin.setLayout(new BorderLayout());
        pnThongTin.setBorder(BorderFactory.createRaisedBevelBorder());
        JLabel lblTitle = new JLabel("Thông tin chi tiết");
        lblTitle.setFont(new Font("Serif", Font.BOLD, 25));
        lblTitle.setHorizontalAlignment(JLabel.CENTER);
        lblTitle.setVerticalAlignment(JLabel.CENTER);
        lblVitriBanAn = new JLabel();
        lblVitriBanAn.setText("Bàn " + banDaChon + " Tầng " + tangDaChon);
        lblVitriBanAn.setHorizontalAlignment(JLabel.CENTER);
        lblVitriBanAn.setVerticalAlignment(JLabel.CENTER);
        lblVitriBanAn.setFont(new Font("Serif", Font.BOLD, 15));
        pnThongTin.add(lblVitriBanAn, BorderLayout.SOUTH);
        pnThongTin.add(lblTitle, BorderLayout.CENTER);

        JPanel pnDsThucDon = new JPanel();
        pnDsThucDon.setLayout(new BorderLayout());
        pnDsThucDon.setBorder(BorderFactory.createRaisedBevelBorder());
        dtmTableThucDon = new DefaultTableModel();
        dtmTableThucDon.addColumn("Mã món ăn");
        dtmTableThucDon.addColumn("Tên món ăn");
        dtmTableThucDon.addColumn("Đơn giá");
        dtmTableThucDon.addColumn("Số lượng");
        dtmTableThucDon.addColumn("Thành tiền");
        tblThucDon = new JTable(dtmTableThucDon);
        tblThucDon.getColumnModel().getColumn(1).setPreferredWidth(180);
        tblThucDon.setDefaultEditor(Object.class, null);
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        tblThucDon.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
        tblThucDon.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
        tblThucDon.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
        JScrollPane scrollThucDon = new JScrollPane(tblThucDon, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        pnDsThucDon.add(scrollThucDon, BorderLayout.CENTER);

        JPanel pnChucNang = new JPanel();
        pnChucNang.setLayout(new FlowLayout());
        Border borderChucNang = BorderFactory.createLineBorder(Color.BLUE, 2);
        TitledBorder titleBorderChucNang = new TitledBorder(borderChucNang, "Chức năng");
        titleBorderChucNang.setTitleFont(new Font(Font.SERIF, Font.BOLD, 18));
        titleBorderChucNang.setTitleJustification(TitledBorder.CENTER);
        pnChucNang.setBorder(titleBorderChucNang);
        btnThem = new JButton("Thêm");
        btnXoa = new JButton("Xóa");
        btnTinhTien = new JButton("Tính tiền");
        btnXuatThongTin = new JButton("Xuất thông tin");
        btnXacNhanThanhToan = new JButton("Xác nhận thanh toán");
        pnChucNang.add(btnThem);
        pnChucNang.add(btnXoa);
        pnChucNang.add(btnTinhTien);
        pnChucNang.add(btnXuatThongTin);
        pnChucNang.add(btnXacNhanThanhToan);

        pnDanhSachVaChucNang.add(pnThongTin, BorderLayout.NORTH);
        pnDanhSachVaChucNang.add(pnDsThucDon, BorderLayout.CENTER);
        pnDanhSachVaChucNang.add(pnChucNang, BorderLayout.SOUTH);

        //
        Container con = getContentPane();
        JPanel pnMain = new JPanel();
        pnMain.setLayout(new BorderLayout(5, 0));
        pnMain.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pnMain.add(pnTangVaBanAn, BorderLayout.EAST);
        pnMain.add(pnDanhSachVaChucNang, BorderLayout.CENTER);
        con.add(pnMain);
    }

    // Triển khai phương thức addEvents
    private void addEvents() {
        menuFileExit.addActionListener(this);
        menuAdvancedMenu.addActionListener(this);
        menuHelpAbout.addActionListener(this);
        btnThem.addActionListener(this);
        for (int i = 0; i < 12; i++) {
            banAn[i].addMouseListener(this);
        }
        comboTang.addActionListener(this);
        btnXoa.addActionListener(this);
        btnTinhTien.addActionListener(this);
        btnXuatThongTin.addActionListener(this);
        btnXacNhanThanhToan.addActionListener(this);
        tblThucDon.addMouseListener(this);

    }

    // Triển khai phương thức showWindow
    public void showWindow() {
        this.setIconImage(new ImageIcon(Objects.requireNonNull(QuanLyNhaHangGUI.class.getResource("/com/project_nhapmon/images/icon.png"))).getImage());
        this.setSize(900, 600);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void capNhatTrangThaiBanAn() {
        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 12; j++) {
                if (goiMonService.laySoLuongMonAn(String.valueOf(i), String.valueOf(j)) > 0) {
                    banAnService.setTrangThaiBanAn(String.valueOf(i), String.valueOf(j), "busy");
                } else {
                    banAnService.setTrangThaiBanAn(String.valueOf(i), String.valueOf(j), "active");
                }
            }
        }

        if (comboTang.getSelectedIndex() != -1) {
            String maTang = (String) comboTang.getSelectedItem();
            for (int i = 0; i < 12; i++) {
                if (banAnService.getTrangThaiBanAn(maTang, String.valueOf(i + 1)).equalsIgnoreCase("active")) {
                    pnStatusBanAn[i].setBackground(Color.GREEN);
                } else {
                    pnStatusBanAn[i].setBackground(Color.RED);
                }
            }

        }
    }

    private void hienThiThucDonBanAn(String maTang, String maBan) {
        Vector<MonAn> dsThucDonTheoBan = goiMonService.layDsThucDonTheoBan(maTang, maBan);
        dtmTableThucDon.setRowCount(0);
        DecimalFormat df = new DecimalFormat("#,###");
        for (MonAn monAn : dsThucDonTheoBan) {
            Vector<Object> vec = new Vector<>();
            vec.add(monAn.getMaMonAn());
            vec.add(monAn.getTenMonAn());
            vec.add(df.format(monAn.getDonGia()));
            vec.add(monAn.getSoLuong());
            vec.add(df.format((long) monAn.getDonGia() * monAn.getSoLuong()));
            dtmTableThucDon.addRow(vec);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == menuFileExit) {
            System.exit(0);
        }
        if (e.getSource() == menuAdvancedMenu) {
            DanhSachMenuGUI dsMenu = new DanhSachMenuGUI(this, "Danh sách thực đơn nhà hàng");
            dsMenu.showDialog(this);
        }
        if (e.getSource() == menuHelpAbout) {
            AboutGUI aboutGUI = new AboutGUI(this, "Information");
            aboutGUI.showDialog(this);
        }

        if (e.getSource() == btnThem) {
            if (!banDaChon.equals("#")) {
                GoiMonGUI goiMonGUI = new GoiMonGUI(tangDaChon, banDaChon, dtmTableThucDon, pnStatusBanAn);
                goiMonGUI.showWindow(this);
            }
        }

        if (e.getSource() == comboTang) {
            tangDaChon = (String) comboTang.getSelectedItem();
            lblVitriBanAn.setText("Bàn " + banDaChon + " Tầng " + tangDaChon);
            hienThiThucDonBanAn(tangDaChon, banDaChon);
            capNhatTrangThaiBanAn();
        }

        if (e.getSource() == btnXoa) {
            rowSelected = tblThucDon.getSelectedRow();
            if (rowSelected != -1) {
                goiMonService.xoaMonAn(tangDaChon, banDaChon, (String) dtmTableThucDon.getValueAt(rowSelected, 0));
            }
            hienThiThucDonBanAn(tangDaChon, banDaChon);
            capNhatTrangThaiBanAn();
        }

        if (e.getSource() == btnXacNhanThanhToan) {
            if (!banDaChon.equals("#") && tblThucDon.getRowCount() > 0) {
                int chon = JOptionPane.showConfirmDialog(this, "Xác nhận đã thanh toán", "Confirm", JOptionPane.OK_CANCEL_OPTION);
                if (chon == JOptionPane.OK_OPTION) {
                    goiMonService.xoaMonAn(tangDaChon, banDaChon);
                    hienThiThucDonBanAn(tangDaChon, banDaChon);
                    capNhatTrangThaiBanAn();
                }
            }
        }

        if (e.getSource() == btnTinhTien) {
            Vector<MonAn> dsMonAnTheoBan = goiMonService.layDsThucDonTheoBan(tangDaChon, banDaChon);
            if (dsMonAnTheoBan.size() > 0) {
                int tongTien = 0;
                DecimalFormat df = new DecimalFormat("#,###,##0.00");
                for (MonAn monAn : dsMonAnTheoBan) {
                    tongTien = tongTien + monAn.getSoLuong() * monAn.getDonGia();
                }
                JOptionPane.showMessageDialog(this, "Tổng số tiền là: " + df.format(tongTien) + " đồng.");
            }
        }

        if (e.getSource() == btnXuatThongTin) {
            int rowCount = tblThucDon.getRowCount();
            if (rowCount > 0) {
                Vector<MonAn> dsThucDon = goiMonService.layDsThucDonTheoBan(tangDaChon, banDaChon);
                XuatThongTinGUI xuatThongTinGUI = new XuatThongTinGUI(this, dsThucDon);
                xuatThongTinGUI.showDialog(this);
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        for (int i = 0; i < 12; i++) {
            if (e.getSource() == banAn[i]) {
                banDaChon = String.valueOf(i + 1);
                lblVitriBanAn.setText("Bàn " + banDaChon + " Tầng " + tangDaChon);
                hienThiThucDonBanAn(tangDaChon, banDaChon);
            }
        }

        if (e.getSource() == tblThucDon && e.getClickCount() == 2 && tblThucDon.getRowCount() > 0) {
            rowSelected = tblThucDon.getSelectedRow();
            int soLuong = (int) dtmTableThucDon.getValueAt(rowSelected, 3);
            String strSoLuong = JOptionPane.showInputDialog("Số lượng: ", soLuong);
            if (strSoLuong != null && strSoLuong.trim().length() > 0 && strSoLuong.matches("^\\d+$")) {
                soLuong = Integer.parseInt(strSoLuong);
                if (soLuong == 0) {
                    goiMonService.xoaMonAn(tangDaChon, banDaChon, (String) dtmTableThucDon.getValueAt(rowSelected, 0));
                } else {
                    goiMonService.thayDoiSoLuongMonAn(tangDaChon, banDaChon, (String) dtmTableThucDon.getValueAt(rowSelected, 0), soLuong);
                }
                hienThiThucDonBanAn(tangDaChon, banDaChon);
                capNhatTrangThaiBanAn();
            }
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
