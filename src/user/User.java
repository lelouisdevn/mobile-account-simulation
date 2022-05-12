package user;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import goicuoc.Goicuoc;
import goicuoc.dkGoicuoc;

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
	public User(int mand) {
		this.mand = mand;
	}
	
//	hiển thị menu chức năng phía người dùng.
	public String userMenu() {
		Scanner sc = new Scanner(System.in);
		
		System.out.println("1: Kiểm tra tài khoản.");
		System.out.println("2: Nạp tiền điện thoại.");
		System.out.println("3. Đăng ký gói cước.");
		System.out.println("4. Chỉnh sửa thông tin cá nhân.");
		
		String index = sc.nextLine();
		
		String data[];
		data = new String[2];
		if (index.equals("1") || index.equals("2") || index.equals("3") || index.equals("4") || index.equals("5")) {
			return index;
		}else if (index.equals("*101#")) {
			return index = "1";
		}
		
		return index;
	}
	
//	action thực thi khi người dùng chọn số;
	public void action(String i) {
		int index = Integer.parseInt(i);
		switch (index) {
		case 1:
			this.ktTaiKhoan();
			break;
		case 2: 
			this.napTienDT();
			break;
		case 3:
			Goicuoc gc = new Goicuoc();
			gc.dsGoiCuoc();
			this.dkGoiCuoc();
			break;
		case 4:
			this.capNhatTT();
			break;
		}
	}
	
	public int getID() {
		return this.mand;
	}
	
//	user updates his information.
	public void capNhatTT() {
		Connection conn = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost/htqltbdd?"+"user=root");
		} catch (Exception ex) {
			System.out.println("Noi ket khong thanh cong");
			ex.printStackTrace();
		}
		CallableStatement cstmt = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement("SELECT USERname, USERdob, USERcmnd FROM users WHERE idUSER=?");
			pstmt.setInt(1, this.mand);
			rs = pstmt.executeQuery();
			rs = pstmt.getResultSet();
			
			String bf_uname = new String();
			String bf_dob = new String();
			String bf_cmnd = new String();
			while (rs.next()) {
				bf_uname = rs.getString("USERname");
				bf_dob = rs.getString("USERdob");
				bf_cmnd = rs.getString("USERcmnd");
			}
			
			Scanner sc = new Scanner(System.in);
			System.out.println("Cập nhật tên người dùng: ");
			String uname = sc.nextLine();
			if (uname.equals("")) {
				uname = bf_uname;
			}
			
			System.out.println("Cập nhật ngày sinh: ");
			String dob = sc.nextLine();
			if (dob.equals("")) {
				dob = bf_dob;
			}
			
			System.out.println("Cập nhật số CMND: ");
			String cmnd = sc.nextLine();
			if (cmnd.equals("")) {
				cmnd = bf_cmnd;
			}
			
//			System.out.println(dob);
			
			cstmt = conn.prepareCall("{? = call capNhatTT(?,?,?,?)}");
			cstmt.registerOutParameter(1, java.sql.Types.BIGINT);
			cstmt.setInt(2, this.mand);
			cstmt.setString(3, uname);
			cstmt.setString(4, dob);
			cstmt.setString(5, cmnd);

			cstmt.executeUpdate();
		}catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
	}
	public int getSodu() {
		Connection conn = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost/htqltbdd?"+"user=root");
		} catch (Exception ex) {
			System.out.println("Noi ket khong thanh cong");
			ex.printStackTrace();
		}
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int sodu = 0;
		try {
			pstmt = conn.prepareStatement("SELECT * FROM users WHERE idUSER=?");
			pstmt.setInt(1, this.mand);
			
			rs = pstmt.executeQuery();
			rs = pstmt.getResultSet();
			
			while (rs.next()) {
				sodu = rs.getInt("USERsodu");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return sodu;
	}
	
	private void dkGoiCuoc() {
		// TODO Auto-generated method stub
		dkGoicuoc dk = new dkGoicuoc();
		dk.dkGoicuoc(this);
	}
	
//	kiểm tra tài khoản:
	public void ktTaiKhoan() {
		
		Connection conn = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost/htqltbdd?"+"user=root");
		} catch (Exception ex) {
//			System.out.println("Noi ket khong thanh cong");
			ex.printStackTrace();
		}
		System.out.println("Tài khoản của bạn là: " + this.getSodu() + " đồng.");
	}
	
//	phương thức nạp tiền điện thoại.
	public void napTienDT() {
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Nhập mã số thẻ cào: ");
		String maso = sc.nextLine();
		
//		lấy id người dùng.		
//		lấy số dư hiện tại của người dùng.
		Connection conn = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost/htqltbdd?"+"user=root");
		} catch (Exception ex) {
			System.out.println("Noi ket khong thanh cong");
			ex.printStackTrace();
		}
		
		int cid = 0;
		try {
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			pstmt = conn.prepareStatement("SELECT USERsodu FROM users WHERE idUSER = ?");
			pstmt.setInt(1, this.mand);
			
			rs = pstmt.executeQuery();
			rs = pstmt.getResultSet();
			
			int oldsodu = 0;
			while (rs.next()) {
				oldsodu = rs.getInt("USERsodu");
			}
			
			String sql = "SELECT * FROM cards WHERE CARDmaso = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,  maso);
			
			rs = pstmt.executeQuery();
			rs = pstmt.getResultSet();
			
//			System.out.println();
			while (rs.next()) {
				cid = rs.getInt("idCARD");
				System.out.println(cid);
				if (rs.getInt("CARDtrangthai") == 0) {
					CallableStatement cstmt = null;
					
					cstmt = conn.prepareCall("{call naptienDT(?,?,?,?)}");
					cstmt.setInt(1, this.mand);
					cstmt.setInt(2, cid);
					cstmt.setInt(3, oldsodu);
					cstmt.setInt(4, rs.getInt("CARDmenhgia"));
					
					cstmt.executeUpdate();
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

//			System.out.println("Noi ket thanh cong");
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

//			System.out.println("Noi ket thanh cong");
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
