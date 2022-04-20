package admin;

import java.util.Scanner;

import goicuoc.Goicuoc;
import user.User;

public class Admin {
	private int maso;
	private String tenqtv;
	
	public Admin() {
		maso = 0;
		tenqtv = new String();
	}
	
	public int adminMenu() {
		Scanner sc = new Scanner(System.in);
		
//		1,2,3,4,5,6
		System.out.println("1: Hiển thị danh sách người dùng.");
		System.out.println("2: Hiển thị danh sách gói cước.");
		System.out.println("3: Thêm người dùng mới.");
		System.out.println("4: Sửa thông tin người dùng.");
		System.out.println("5: Xóa người dùng.");
		System.out.println("6: Thêm gói cước.");
		System.out.println("7: Sửa gói cước.");
		System.out.println("8: Xóa gói cước.");
		System.out.println("9: Thêm thẻ nạp.");
		System.out.println("10: Sửa thẻ nạp: ");
		System.out.println("11: Xóa thẻ nạp.");
		
		
		int key = sc.nextInt();
		return key;
	}
	
	public void action(int index) {
		switch (index) {
		case 1: 
			this.dsNguoiDung();
			break;
		case 2: 
			this.dsGoiCuoc();
			break;
		case 3:
			this.themNguoiDung();
			break;
		case 4: 
			this.suaNguoiDung();
			break;
		case 5:
			this.xoaNguoiDung();
			break;
		case 6: 
			this.themGoiCuoc();
			break;
		case 7: 
			this.suaGoiCuoc();
			break;
		case 8:
			this.xoaGoiCuoc();
			break;
		case 9:
			this.themTheNap();
			break;
		case 10:
			this.suaTheNap();
			break;
		case 11:
			this.xoaTheNap();
			break;
		default: 
			System.out.println("ERROR");
		}
	}
//	chức năng 1
	public void dsNguoiDung() {
		User usr = new User();
		usr.dsNguoiDung();
	}
//	chức năng 2
	public void dsGoiCuoc() {
		Goicuoc gc = new Goicuoc();
		gc.dsGoiCuoc();
	}
//	chức năng 3
	public void themNguoiDung() {
		User usr = new User();
		usr.dkNguoiDung();
	}
//	chức năng 4.
	public void suaNguoiDung() {
		User usr = new User();
		usr.suaNguoiDung();
	}
//	chức năng 5: xóa người dùng.
	public void xoaNguoiDung() {
		User usr = new User();
		usr.xoaNguoiDung();
	}
//	Chức năng 6: thêm gói cước.
	public void themGoiCuoc() {
		Goicuoc gc = new Goicuoc();
		gc.themGoiCuoc();
	}
//	Chức năng 7: sửa gói cước
	public void suaGoiCuoc() {
		
	}
//	Chức năng 8: xóa gói cước
	public void xoaGoiCuoc() {
		
	}
//	Chức năng 9: thêm thẻ nạp
	public void themTheNap() {
		
	}
//	Chức năng 10: sửa thẻ nạp.
	public void suaTheNap() {
		
	}
//	Chức năng 11: xóa thẻ nạp.
	public void xoaTheNap() {
		
	}
}
