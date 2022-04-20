package main;

import java.util.Scanner;

import admin.Admin;
import user.User;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner sc = new Scanner(System.in);
		
		int state;
		do {
			System.out.println("HỆ THỐNG QUẢN LÝ THUÊ BAO DI ĐỘNG\n---------------------------------");
			System.out.println("Chọn 1 vai trò: \n1: Quản trị\n2: Khách hàng");
			int role = sc.nextInt();
			if (role == 1 || role == 2) {
//				valid
				state = 1;
			}else {
//				invalid
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
					User usr = new User();
					int c2_key;
					do {
						int index = usr.userMenu();
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
