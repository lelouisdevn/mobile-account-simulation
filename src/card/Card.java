package card;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Card {
	private int id;
	private int trangthai;
	private int menhgia;
	private String masothe;
	
	public Card() {
		id = 0;
		trangthai = 0;
		menhgia = 10000;
		masothe = new String();
	}
//	hiển thị danh sách thẻ nạp.
	public void dsTheNap() {
//		thiết lập kết nối.
		Connection conn = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost/htqltbdd?"+"user=root");
			
			
			Statement st = null;
			ResultSet r = null;
			
			int nap = 0;
			int tong = 0;
			
			st = conn.createStatement();
			if (st.execute("SELECT count(idCARD) sl FROM cards WHERE CARDtrangthai = 1")) {
				r = st.getResultSet();
				while (r.next()) {
					nap = r.getInt("sl");
				}
			}
			
//			danh sách tất cả thẻ nạp.
			Statement stmt = null;
			ResultSet rs = null;
			
			stmt = conn.createStatement();
			int i = 1;
			if (stmt.execute("SELECT * FROM cards ORDER by CARDmenhgia ASC")) {
				rs = stmt.getResultSet();
				System.out.println("Danh sách thẻ cào");
				System.out.println("|------------------------------------------------|");
				System.out.format("|  %-5s|     %-10s| %-10s| %-11s|\n", "STT", "Mã số", "Mệnh giá", "Trạng thái");
				System.out.println("|------------------------------------------------|");
				while (rs.next()) {
					System.out.format("| %-6d| %-14s| %-5d đ   | %-11d|\n", 
							i, rs.getString("CARDmaso"), rs.getInt("CARDmenhgia"), rs.getInt("CARDtrangthai"));
					tong ++;
					i ++;
				}
				System.out.println("|------------------------------------------------|");
				System.out.println("Có " + nap + " thẻ đã nạp trong tổng số " + tong + " thẻ.");
			}
		} catch (Exception ex) {
			System.out.println("Noi ket khong thanh cong");
			ex.printStackTrace();
		}
	}
	
//	phương thức thêm thẻ nạp.
	public void themTheNap() {
		Connection conn = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost/htqltbdd?"+"user=root");
		} catch (Exception ex) {
			System.out.println("Noi ket khong thanh cong");
			ex.printStackTrace();
		}
		
		Scanner sc = new Scanner(System.in);
		System.out.println("Nhập mã số thẻ cào:");
		this.masothe = sc.nextLine();
		
		System.out.println("Chọn mệnh giá thẻ cào: ");
		System.out.println("1: 10.000 đồng\n2: 20.000 đồng\n3: 50.000 đồng.");
		this.menhgia = sc.nextInt();
		
		CallableStatement cstmt = null;
		
		Statement pstmt = null;
		ResultSet rs = null;
		try {
			
//			get the last card id;
			pstmt = conn.createStatement();
			if (pstmt.execute("SELECT max(idCARD) maxid FROM cards")) {
				rs = pstmt.getResultSet();
				while (rs.next()) {
					this.id = rs.getInt("maxid");
				}
			}
			
			switch (this.menhgia){
				case 1: this.menhgia = 10000; break;
				case 2: this.menhgia = 20000; break;
				case 3: this.menhgia = 50000; break;
			}
			
//			gọi hàm thêm thẻ nạp. (stored function - themTheCao() );
			cstmt = conn.prepareCall("{? = call themTheCao(?,?,?)}");
			cstmt.registerOutParameter(1, java.sql.Types.BIGINT);
			cstmt.setInt(2, this.id + 1);
			cstmt.setInt(3, this.menhgia);
			cstmt.setString(4, this.masothe);
			
			cstmt.execute();
		}catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	public void suaTheNap() {
		Connection conn = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost/htqltbdd?"+"user=root");
		} catch (Exception ex) {
			System.out.println("Noi ket khong thanh cong");
			ex.printStackTrace();
		}
		
		Scanner sc = new Scanner(System.in);
		System.out.println("Nhập mã số thẻ cào cần sửa: ");
		int c_id = sc.nextInt();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement("SELECT CARDtrangthai, CARDmenhgia, CARDmaso FROM cards WHERE idCARD=?");
			pstmt.setInt(1, c_id);
			
			rs = pstmt.executeQuery();
			rs = pstmt.getResultSet();
			int state = 1;
			int bf_menhgia = 0;
			String bf_maso = new String();
			while (rs.next()) {
				if (rs.getInt("CARDtrangthai") == 0) {
					state = 0;
					bf_maso = rs.getString("CARDmaso");
					bf_menhgia = rs.getInt("CARDmenhgia");
				}
			}
			
			int af_gia = 0;
			String gia = new String();
			String maso = new String();
			if (state == 0) {
				System.out.println("Cập nhật mệnh giá mới: ");
				gia = sc.nextLine();
				sc.nextLine();
				System.out.println("Cập nhật mã số thẻ cào: ");
				maso = sc.nextLine();
				
				if (gia.equals("")) {
					af_gia = bf_menhgia;
				}else {
					af_gia = Integer.parseInt(gia);
				}
				
				if (maso.equals("")) {
					maso = bf_maso;
				}
				
				System.out.println(maso);
				
				pstmt = conn.prepareStatement("UPDATE cards SET CARDmenhgia=?, CARDmaso=? WHERE idCARD=?");
				pstmt.setInt(1, af_gia);
				pstmt.setString(2, maso);
				pstmt.setInt(3, c_id);
				
				pstmt.executeUpdate();
			}
			
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	public void xoaTheNap(){
		Connection conn = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost/htqltbdd?"+"user=root");
		} catch (Exception ex) {
			System.out.println("Noi ket khong thanh cong");
			ex.printStackTrace();
		}
		
		Scanner sc = new Scanner(System.in);
		System.out.println("Nhập mã số thẻ cào cần xóa: ");
		int id = sc.nextInt();
		
		PreparedStatement pstmt = null;
		
		try {
			pstmt = conn.prepareStatement("DELETE FROM cards WHERE idCARD=?");
			pstmt.setInt(1, id);
			
			pstmt.executeUpdate();
			
		}catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
}
