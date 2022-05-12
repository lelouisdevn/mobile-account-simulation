package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import admin.Admin;
import user.User;

public class Main {
	public int[] signIn() {
		Connection conn = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost/htqltbdd?"+"user=root");

		} catch (Exception ex) {
			System.out.println("Noi ket khong thanh cong");
			ex.printStackTrace();
		}
		
		Scanner sc = new Scanner(System.in);
		System.out.println("Nhập số điện thoại để đăng nhập:");
		String sdt = sc.nextLine();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int data[];
		data = new int[2];
		try {
			pstmt = conn.prepareStatement("SELECT * FROM users WHERE USERsdt = ?");
			pstmt.setString(1,  sdt);
			rs = pstmt.executeQuery();
			
			rs = pstmt.getResultSet();
			
			while (rs.next()) {
				data[0] = rs.getInt("UserRole");
				data[1] = rs.getInt("idUSER");
			}
		}catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return data;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner sc = new Scanner(System.in);
		
		int state;
		do {
			System.out.println("HỆ THỐNG QUẢN LÝ THUÊ BAO DI ĐỘNG\n---------------------------------");
			Main m = new Main();
			int data[] = m.signIn();
			int role = 0;
			if (data[0] == 0) {
				role = 2;
				state = 1;
			}else if (data[0] == 1){
				role = 1;
				state = 1;
			}else {
				state = 0;
			}
			switch (role) {
				case 1:
					System.out.println("Bạn đang thực hiện chức năng với vai trò Admin.");
					Admin qtv = new Admin();
					int c1_key;
					do {
						int index = qtv.adminMenu();
						qtv.action(index);
						System.out.println();
						System.out.println("Chọn 1 để tiếp tục, 0 để dừng.");
						c1_key = sc.nextInt();
						state = 0;
					}while (c1_key == 1);
				break;
				
				case 2: 
					System.out.println("Bạn đang thực hiện chức năng với vai trò khách hàng.");
					User usr = new User(data[1]);
					int c2_key;
					do {
						String index = usr.userMenu();
						usr.action(index);
						
						System.out.println("Chọn 1 để tiếp tục, 0 để dừng.");
						c2_key = sc.nextInt();
						state = 0;
						
					}while (c2_key == 1);
				break;
				
				default: 
					System.out.println("Đã có lỗi xãy ra, vui lòng chọn 1 hoặc 2.");
			}
		}while(state == 0);
	}

}
