package com.project_nhapmon.connect;

import com.project_nhapmon.model.MonAn;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

public class MenuService extends MySqlService {
    /**
     * Phương thức layDanhSachMenu được dùng để lấy danh sách menu của nhà hàng từ bảng menu_nhahang trong
     * cơ sở dữ liệu
     *
     * @return Danh sách món ăn trong Menu nhà hàng
     */
    public Vector<MonAn> layDanhSachMenu() {
        Vector<MonAn> dsMonAn = new Vector<>();
        try {
            String sql = "select * from menu_nhahang";
            PreparedStatement preStatement = conn.prepareStatement(sql);
            ResultSet result = preStatement.executeQuery();
            while (result.next()) {
                MonAn monAn = new MonAn();
                monAn.setMaMonAn(result.getString(1));
                monAn.setTenMonAn(result.getString(2));
                monAn.setDonGia(result.getInt(3));
                dsMonAn.add(monAn);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dsMonAn;
    }

    /**
     * Phương thức xoaMonAn được dùng để xóa món ăn theo maMonAn trong danh sách thực đơn nhà hàng từ
     * bảng menu_nhahang trong cơ sở dũ liệu
     *
     * @param maMonAn Mã món ăn cần xóa
     * @return true nếu xóa thành công hoặc false nếu xóa không thành công
     */
    public boolean xoaMonAn(String maMonAn) {
        try {
            String sql = "delete from menu_nhahang where MaMonAn=?";
            PreparedStatement preStatement = conn.prepareStatement(sql);
            preStatement.setString(1, maMonAn);
            int result = preStatement.executeUpdate();
            if (result > 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Phương thức themMonAn được dùng để thêm món ăn vào danh sách Menu nhà hàng trong bảng menu_nhahang
     * trong cơ sở dữ liệu
     *
     * @param monAn Món ăn cần thêm
     * @return true nếu thêm món ăn thành công hoặc false nếu thêm món ăn không thành công
     */
    public boolean themMonAn(MonAn monAn) {
        try {
            String sql = "insert into menu_nhahang values (?,?,?)";
            PreparedStatement preStatement = conn.prepareStatement(sql);
            preStatement.setString(1, monAn.getMaMonAn());
            preStatement.setString(2, monAn.getTenMonAn());
            preStatement.setInt(3, monAn.getDonGia());
            int result = preStatement.executeUpdate();
            if (result > 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Phương thức chinhSuaThongTinMonAn được dùng để cập nhật lại thông tin món ăn
     *
     * @param monAn Món ăn cần chỉnh sửa thông tin
     * @return true nếu cập nhật thành công hoặc false nếu không thành công
     */
    public boolean chinhSuaThongTinMonAn(MonAn monAn) {
        try {
            String sql = "update menu_nhahang set TenMonAn=?, DonGia=? where MaMonAn=?";
            PreparedStatement preStatement = conn.prepareStatement(sql);
            preStatement.setString(1, monAn.getTenMonAn());
            preStatement.setInt(2, monAn.getDonGia());
            preStatement.setString(3, monAn.getMaMonAn());
            int result = preStatement.executeUpdate();
            if (result > 0) {
                return  true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  false;
    }
}
