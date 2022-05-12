package goicuoc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import user.User;

public class dkGoicuoc {
	private String ngaydk;
	private User user;
	private String ngayhethan;
	
	public dkGoicuoc() {
		ngaydk = new String();
		ngayhethan = new String();
	}
//	lấy thông tin gói cước.
	public int[] ttGoicuoc(String gc) {
		Connection conn = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost/htqltbdd?"+"user=root");
		} catch (Exception ex) {
			System.out.println("Noi ket khong thanh cong");
			ex.printStackTrace();
		}
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int idgc = 0;
		String ten = null;
		String mota = null;
		int hieuluc = 0;
		int gia = 0;
		try {
			stmt = conn.prepareStatement("SELECT * FROM goicuoc WHERE GOICUOCten=?");
			stmt.setString(1, gc);
			rs = stmt.executeQuery();
			
			rs = stmt.getResultSet();
			
			while (rs.next()) {
				idgc = rs.getInt("idGOICUOC");
				ten = rs.getString("GOICUOCten");
				mota = rs.getString("GOICUOCmota");
				hieuluc = rs.getInt("GOICUOCthoihieu");
				gia = rs.getInt("GOICUOCgia");
			}
			
			System.out.println("Đăng ký gói cước " + ten + ", có ngay " 
								+ mota + ", hiệu lực trong " + hieuluc 
								+ " ngày với giá chỉ " + gia + " đồng.");
		}catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		int data[] = new int[4];
		data[0] = idgc;
		data[1] = hieuluc;
		data[2] = gia;
		return data;
	}
	
//	người dùng đăng ký gói cước.
	public void dkGoicuoc(User user) {
		System.out.println("Nhập tên gói cước: ");
		Scanner sc = new Scanner(System.in);
		String tengc = sc.nextLine();
		
//		int gcid = this.ttGoicuoc(tengc);
		
		int data[] = new int[4];
		data = this.ttGoicuoc(tengc);
		
		int gcid = data[0];
		int hieuluc = data[1];
		int gia = data[2];
//		System.out.println(hieuluc);
		
//		iduser.
		int id = user.getID();
		
		Connection conn = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost/htqltbdd?"+"user=root");
		} catch (Exception ex) {
			System.out.println("Noi ket khong thanh cong");
			ex.printStackTrace();
		}
		
		int sodu = user.getSodu();
		if (sodu >= gia) {
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
//			chuyển đổi ngày hiện tại sang chuỗi.
			LocalDate localDate = LocalDate.now();//For reference
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			String formattedString = localDate.format(formatter);
			
			try {
				pstmt = conn.prepareStatement("INSERT INTO gcdk VALUES (?,?,?,?)");
				
				pstmt.setInt(1, id);
				pstmt.setInt(2, gcid);
				pstmt.setString(3, formattedString);
				
				int index = formattedString.indexOf("/");
				int day = Integer.parseInt(formattedString.substring(0, index));
				System.out.println(day);
				int month = Integer.parseInt(formattedString.substring(index + 1, index + 3));
				
				int index1 = formattedString.lastIndexOf("/");
				int year = Integer.parseInt(formattedString.substring(index1+1));
				
				int day_temp = day + hieuluc;
				int months[] = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
				if (day_temp > months[month]) {
					day = day_temp - months[month];
					int m = month ++;
					if (m > 12) {
						month = 1;
						year ++;
					}
				}else {
					day = day_temp;
				}
				
				String hethan = Integer.toString(day).concat("/").
								concat(Integer.toString(month)).
								concat("/").concat(Integer.toString(year));

				pstmt.setString(4, hethan);
				
				pstmt.executeUpdate();
			}catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}else {
			System.out.println("Tài khoản của bạn không đủ để đăng ký gói cước này.\n"
					+ "Vui lòng nạp thêm tiền vào tài khoản.");
			System.out.println("Bạn có muốn nạp thêm tiền không? Yes/No?");
			String yn = sc.nextLine();
			if (yn.equalsIgnoreCase("yes")) {
				user.napTienDT();
			}
		}
	}
}
