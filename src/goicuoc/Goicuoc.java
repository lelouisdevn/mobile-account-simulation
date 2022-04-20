package goicuoc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Goicuoc {
	private int maso;
	private String tengc;
	private String mota;
	private int giagc;
	private int thoihieu;
	
	public Goicuoc() {
		maso = 0;
		tengc = new String();
		mota = new String();
		thoihieu = 1;
		giagc = 0;
	}
	
//	hiển thị danh sách gói cước.
	public void dsGoiCuoc() {
		Connection conn = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost/htqltbdd?"+"user=root");

			System.out.println("Noi ket thanh cong");
		} catch (Exception ex) {
			System.out.println("Noi ket khong thanh cong");
			ex.printStackTrace();
		}
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			stmt = conn.createStatement();
			
			if (stmt.execute("SELECT * FROM goicuoc")) {
				rs = stmt.getResultSet();
				
				System.out.println("DANH SÁCH GÓI CƯỚC");
				
//				idGOICUOC, GOICUOCten, GOICUOCmota, GOICUOCthoihieu, GOICUOCgia
				System.out.println("|------------------------------------------------------------------------|");
				System.out.format("| %5s |   %-7s|            %-18s| %-9s |   %-7s|\n", "Mã số", "Tên", "Mô tả", "Thời hiệu", "Giá");
				System.out.println("|------------------------------------------------------------------------|");
				while (rs.next()) {
					System.out.format("| %-6d| %-9s| %-29s| %-10d| %-9d|\n", 
							rs.getInt("idGOICUOC"), rs.getString("GOICUOCten"), rs.getString("GOICUOCmota"),
							rs.getInt("GOICUOCthoihieu"), rs.getInt("GOICUOCgia"));
				}
				System.out.println("|------------------------------------------------------------------------|");
			}
		}catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
//	thêm gói cước
	public void themGoiCuoc() {
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Nhập tên gói cước:");
		String tengc = sc.nextLine();
		
		System.out.println("Nhập mô tả:");
		String mota = sc.nextLine();
		
		System.out.println("Nhập thời hiệu gói cước:");
		int thoihieu = sc.nextInt();
		
		System.out.println("Nhập giá gói cước: ");
		int gia = sc.nextInt();
		
		this.tengc = tengc;
		this.mota = mota;
		this.thoihieu = thoihieu;
		this.giagc = gia;
		
		
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
		PreparedStatement p = null;
		ResultSet rs = null;
		
		int lastID = 0;
		
		try {
			pstmt = conn.prepareStatement("SELECT MAX(idGOICUOC) idGOICUOC FROM goicuoc");
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				lastID = rs.getInt("idGOICUOC");
			}
			
			System.out.println(lastID);
			
			p = conn.prepareStatement("INSERT INTO goicuoc values (?,?,?,?,?)");
			p.setInt(1, lastID + 1);
			p.setString(2, this.tengc);
			p.setString(3, this.mota);
			p.setInt(4, this.thoihieu);
			p.setInt(5,  this.giagc);
			
			p.executeUpdate();
			
		}catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
	}
}