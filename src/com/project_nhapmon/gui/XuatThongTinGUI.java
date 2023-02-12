package com.project_nhapmon.gui;

import com.project_nhapmon.model.MonAn;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

public class XuatThongTinGUI extends JDialog {
    // Các thuộc tính
    private final Vector<MonAn> dsThucDon;

    // Triển khai phương thức khởi tạo
    public XuatThongTinGUI(Frame parent, Vector<MonAn> dsThucDon){
        super(parent, "Thông tin thanh toán", true);
        this.dsThucDon = dsThucDon;
        addControls();
        addEvents();
    }

    // Triển khai phương thức addControls
    private void addControls() {
        // Tạo khu vực hiển thị thông báo
        JPanel pnThongBao = new JPanel();
        pnThongBao.setLayout(new BorderLayout());
        JLabel lblThongBao = new JLabel("Thông tin thanh toán");
        lblThongBao.setFont(new Font(Font.SERIF, Font.BOLD, 28));
        lblThongBao.setHorizontalAlignment(JLabel.CENTER);
        lblThongBao.setVerticalAlignment(JLabel.CENTER);
        JLabel lblThoiGian = new JLabel();
        lblThoiGian.setFont(new Font(Font.SERIF, Font.BOLD, 13));
        lblThoiGian.setVerticalAlignment(JLabel.CENTER);
        lblThoiGian.setHorizontalAlignment(JLabel.CENTER);
        Calendar calendar = Calendar.getInstance();
        Date dateNow = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        lblThoiGian.setText("Thời gian xuất: " + sdf.format(dateNow));
        pnThongBao.add(lblThongBao, BorderLayout.CENTER);
        pnThongBao.add(lblThoiGian, BorderLayout.SOUTH);

        // Tạo khu vực hiển thị mặt hàng trong hóa đơn
        JPanel pnDsHoaDon = new JPanel();
        pnDsHoaDon.setLayout(new BorderLayout());
        pnDsHoaDon.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));
        DefaultTableModel dtmDsHoaDon = new DefaultTableModel();
        dtmDsHoaDon.addColumn("Mã món ăn");
        dtmDsHoaDon.addColumn("Tên món ăn");
        dtmDsHoaDon.addColumn("Đơn giá");
        dtmDsHoaDon.addColumn("Số lượng");
        dtmDsHoaDon.addColumn("Thành tiền");
        JTable tblDsHoaDon = new JTable(dtmDsHoaDon);
        tblDsHoaDon.getColumnModel().getColumn(1).setPreferredWidth(150);
        tblDsHoaDon.setDefaultEditor(Object.class, null);
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        tblDsHoaDon.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
        tblDsHoaDon.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
        tblDsHoaDon.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);

        JScrollPane scrollDsHoaDon = new JScrollPane(tblDsHoaDon, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        pnDsHoaDon.add(scrollDsHoaDon, BorderLayout.CENTER);
        int tongTien = 0;
        DecimalFormat df = new DecimalFormat("#,###");
        for(MonAn monAn:dsThucDon){
            Vector<Object> vec = new Vector<>();
            vec.add(monAn.getMaMonAn());
            vec.add(monAn.getTenMonAn());
            vec.add(df.format(monAn.getDonGia()));
            vec.add(monAn.getSoLuong());
            int thanhTien = monAn.getDonGia()*monAn.getSoLuong();
            tongTien += thanhTien;
            vec.add(df.format(thanhTien));
            dtmDsHoaDon.addRow(vec);
        }
        String[] tong = {"", "", "", "Tổng tiền: ", df.format(tongTien)};
        dtmDsHoaDon.addRow(tong);
        dtmDsHoaDon.setRowCount(dsThucDon.size()+1);

        //
        Container con = getContentPane();
        JPanel pnMain = new JPanel();
        pnMain.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        pnMain.setLayout(new BorderLayout());
        pnMain.add(pnThongBao, BorderLayout.NORTH);
        pnMain.add(pnDsHoaDon, BorderLayout.CENTER);
        con.add(pnMain);
    }

    // Triển khai phương thức addEvent
    private void addEvents() {
    }

    public void showDialog(Frame parent){
        this.setSize(600, 300);
        this.setLocationRelativeTo(parent);
        this.setVisible(true);
    }
}
