package user;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class User {
	private int mand;
	private String tennd;
	private String nsinh;
	private String cmnd;
	private String sdt;
	private int sodu;
	
	public User() {
		mand = 0;
		tennd = new String();
		nsinh = new String();
		cmnd = new String();
		sdt = new String();
		sodu = 0;
	}
	
	public int userMenu() {
		Scanner sc = new Scanner(System.in);
		
		System.out.println("1: Kiểm tra tài khoản.");
		System.out.println("2: Nạp tiền điện thoại.");
		System.out.println("3. Đăng ký gói cước.");
		System.out.println("4. Hủy gói cước");
		System.out.println("5. Chỉnh sửa thông tin cá nhân.");
		
		int index = sc.nextInt();
		
		return index;
	}
	
	public void action(int index) {
		switch (index) {
		case 1:
			this.ktTaiKhoan();
			break;
		case 2: 
			this.napTienDT();
			break;
		}
	}
//	kiểm tra tài khoản:
	
	public void ktTaiKhoan() {
		this.mand = 1;
		
		Connection conn = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost/htqltbdd?"+"user=root");

			System.out.println("Noi ket thanh cong");
		} catch (Exception ex) {
			System.out.println("Noi ket khong thanh cong");
			ex.printStackTrace();
		}
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement("SELECT * FROM users WHERE idUSER=?");
			pstmt.setInt(1, this.mand);
			
			rs = pstmt.executeQuery();
			
			rs = pstmt.getResultSet();
			
			while(rs.next()) {
				System.out.println("Tài khoản của bạn là: " + rs.getInt("USERsodu"));
			}
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void napTienDT() {
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Nhập mã số thẻ cào: ");
		String maso = sc.nextLine();
		
//		lấy id người dùng, chương trình này sử dụng user có id = 1.
		
//		lấy số dư hiện tại của người dùng.
		Connection conn = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost/htqltbdd?"+"user=root");

			System.out.println("Noi ket thanh cong");
		} catch (Exception ex) {
			System.out.println("Noi ket khong thanh cong");
			ex.printStackTrace();
		}
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = conn.prepareStatement("SELECT * FROM cards WHERE CARDmaso = ?");
			pstmt.setString(1, maso);
			
			rs = pstmt.executeQuery();
			
			rs = pstmt.getResultSet();
			
			boolean status = false;
			int id = 0;
			int state = 1;
			while (rs.next()) {
				id = rs.getInt("idCARD");
				state = rs.getInt("CARDtrangthai");
				if (id != 0 && state == 0) {
					status = true;
					
//					mệnh giá thẻ cào.
					int menhgia = rs.getInt("CARDmenhgia");
//					System.out.println(rs.getString("CARDmenhgia"));
					
//					update card status and isuser;S
					PreparedStatement pst = null;
					pst = conn.prepareStatement("UPDATE cards SET idUSER=?, CARDtrangthai=?");
					
					this.mand = 1;
					pst.setInt(1, mand);
					pst.setInt(2, 1);
					
					pst.executeUpdate();
					
//					lấy số dư cũ tài khoản người dùng.
					pst = null;
					ResultSet r = null;
					
					pst = conn.prepareStatement("SELECT * FROM users WHERE idUSER=?");
					pst.setInt(1, this.mand);
					
					r = pst.executeQuery();
					r = pst.getResultSet();
					
					int sodu = 0;
					while (r.next()) {
						sodu = r.getInt("USERsodu");
					}
					
//					số dư mới = số dư cũ + mệnh giá thẻ nạp.
					int tong = sodu + menhgia;
					pst = conn.prepareStatement("UPDATE users SET USERsodu=? WHERE idUSER=?");
					pst.setInt(1, tong);
					pst.setInt(2, this.mand);
					
					pst.executeUpdate();
					
//					In ra số dư tài khoản sau khi nạp tiền.
					this.ktTaiKhoan();
				}
				else {
//					in ra thông báo nếu mã số thẻ cào không hợp lệ
//					hoặc hết hạn sử dụng (CARDtrangthai = 1).
					System.out.println("Mã thẻ đã được sử dụng hoặc hết hạn.");
				}
			}
		}catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void dkNguoiDung() {
		Connection conn = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost/htqltbdd?"+"user=root");

			System.out.println("Noi ket thanh cong");
		} catch (Exception ex) {
			System.out.println("Noi ket khong thanh cong");
			ex.printStackTrace();
		}
		
		
		Scanner sc = new Scanner(System.in);		
		System.out.println("Nhập tên người dùng: ");
		String tennd = sc.nextLine();
		
		System.out.println("Nhập ngày sinh: ");
		String nsinh = sc.nextLine();
		
		System.out.println("Nhập số CMND: ");
		String cmnd = sc.nextLine();
		
		System.out.println("Nhập số điện thoại: ");
		String sdt = sc.nextLine();
		
		this.tennd = tennd;
		this.nsinh = nsinh;
		this.cmnd = cmnd;
		this.sdt = sdt;

//		mysql query;
		Statement st = null;
		ResultSet rs = null;
		PreparedStatement pst = null;
		
		CallableStatement cstmt = null;
		
		try {
			st = conn.createStatement();
			
			if (st.execute("SELECT max(idUSER) idUSER FROM users")) {
				rs = st.getResultSet();
				
				int lastID = 0;
				while (rs.next()) {
					lastID = rs.getInt("idUSER");
					System.out.println(lastID);
				}
				
//				pst = conn.prepareStatement("INSERT INTO users VALUES (?,?,?,?,?,?)");
//				
//				pst.setInt(1, lastID + 1);
//				pst.setString(2, this.tennd);
//				pst.setString(3, this.nsinh);
//				pst.setString(4, this.sdt);
//				pst.setString(5, this.cmnd);
//				pst.setInt(6, this.sodu);
//				
//				pst.executeUpdate();
				
//				gọi thủ tục (procedure) thêm người dùng.
				cstmt = conn.prepareCall("{call themND(?,?,?,?,?)}");
				cstmt.setInt(1, lastID + 1);
				cstmt.setString(2, this.tennd);
				cstmt.setString(3, this.nsinh);
				cstmt.setString(4, this.sdt);
				cstmt.setString(5, this.cmnd);
				
				cstmt.executeQuery();
			}else {
				System.out.println("error");
			}
		}catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
		}
	}
	
	public void dsNguoiDung() {
		Connection conn = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost/htqltbdd?"+"user=root");

//			System.out.println("Noi ket thanh cong");
		} catch (Exception ex) {
			System.out.println("Noi ket khong thanh cong");
			ex.printStackTrace();
		}
		
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			stmt = conn.createStatement();
			
			if (stmt.execute("SELECT * FROM users")) {
				rs = stmt.getResultSet();
				System.out.println("DANH SÁCH NGƯỜI DÙNG");
				System.out.println("|------------------------------------------------------------------------------------------------------|");
				System.out.format("|  %4s |          %9s           |   %9s   |      %3s      |     %4s      |     %5s     |\n", 
									"MAND", "Họ và tên", "Ngày sinh", "SĐT", "CMND", "Số dư");
				System.out.println("|------------------------------------------------------------------------------------------------------|");
				while (rs.next()) {
					System.out.format("| %-6d| %-29s| %-14s| %-14s| %-14s| %-14d|\n", rs.getInt("idUSER"),
							rs.getString("USERname"), rs.getString("USERdob"), rs.getString("USERsdt"),
							rs.getString("USERcmnd"), rs.getInt("USERsodu"));
				}
				System.out.print("|------------------------------------------------------------------------------------------------------|");
			}
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	public void hienthi() {
		System.out.println("[" + mand + "," + nsinh + "," + cmnd + "," + sdt + "," + sodu + "]");
	}

//	admin
	public void suaNguoiDung() {
		// TODO Auto-generated method stub
		Connection conn = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost/htqltbdd?"+"user=root");

			System.out.println("Noi ket thanh cong");
		} catch (Exception ex) {
			System.out.println("Noi ket khong thanh cong");
			ex.printStackTrace();
		}
		
		Scanner sc = new Scanner (System.in);
		System.out.println("Nhập mã số người dùng cần sửa: ");
		int maso = sc.nextInt();
		sc.nextLine();
		
		PreparedStatement pst = null;
		try {
			pst = conn.prepareStatement("UPDATE users SET USERname=?, "
					+ "USERdob=?, USERcmnd=? WHERE idUSER=?");
			
			System.out.println("Nhập tên người dùng mới: ");
			String ten = sc.nextLine();
			
			System.out.println("Nhập ngày sinh mới: ");
			String nsinh = sc.nextLine();
			
			System.out.println("Nhập cmnd mới: ");
			String cmnd = sc.nextLine();
			
			pst.setString(1, ten);
			pst.setString(2, nsinh);
			pst.setString(3, cmnd);
			pst.setInt(4, maso);
			
			pst.executeUpdate();
			
			System.out.println("Cập nhật thông tin người dùng thành công.");
		}catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
//	admin
	public void xoaNguoiDung() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Nhập mã số người dùng cần xóa: ");
		int maso = sc.nextInt();
		
		Connection conn = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost/htqltbdd?"+"user=root");

			System.out.println("Noi ket thanh cong");
		} catch (Exception ex) {
			System.out.println("Noi ket khong thanh cong");
			ex.printStackTrace();
		}
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			stmt = conn.prepareStatement("SELECT * FROM users WHERE idUSER=?");
			
			stmt.setInt(1,  maso);
			stmt.executeQuery();
			
			rs = stmt.getResultSet();
//			System.out.println(rs);
			
			System.out.println("Bạn có chắc muốn xóa người dùng này?");
			System.out.println("|------------------------------------------------------------------------------------------------------|");
			System.out.format("|  %4s |          %9s           |   %9s   |      %3s      |     %4s      |     %5s     |\n", 
									"MAND", "Họ và tên", "Ngày sinh", "SĐT", "CMND", "Số dư");
			System.out.println("|------------------------------------------------------------------------------------------------------|");
			while (rs.next()) {
				System.out.format("| %-6d| %-29s| %-14s| %-14s| %-14s| %-14d|\n", rs.getInt("idUSER"),
						rs.getString("USERname"), rs.getString("USERdob"), rs.getString("USERsdt"),
						rs.getString("USERcmnd"), rs.getInt("USERsodu"));
				}
			System.out.println("|------------------------------------------------------------------------------------------------------|");
				
//			xác nhận xóa
			sc.nextLine();
			System.out.println("Gõ \"yes\" để xóa.");
			String ver = sc.nextLine();
			String ver2 = ver.trim();
			
			PreparedStatement pstmt = null;
			if (ver2.equals("yes")) {
				try {
					pstmt = conn.prepareStatement("DELETE FROM users WHERE idUSER=?");
					pstmt.setInt(1, maso);
					
					pstmt.executeUpdate();
				}catch (SQLException ex) {
					System.out.println(ex.getMessage());
				}
				this.dsNguoiDung();
			}else {
				System.out.println("Xác nhận xóa người dùng không thành công.");
			}
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
	}
}
