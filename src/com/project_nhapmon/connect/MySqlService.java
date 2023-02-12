package com.project_nhapmon.connect;

import com.mysql.cj.jdbc.Driver;

import java.sql.Connection;
import java.util.Properties;

public class MySqlService {
    // Các thuộc tính
    protected Connection conn;

    // Triển khai phương thức khởi tạo
    public MySqlService(){
        try{
            String strConn = "jdbc:mysql://localhost/database_quanlynhahang?useUnicode=true&characterEncoding=utf-8";
            Properties pro = new Properties();
            pro.put("user", "root");
            pro.put("password", "");
            Driver driver = new Driver();
            conn = driver.connect(strConn, pro);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
