package javanet.l02;

import java.util.Scanner;

/**
 * <Strong>编写一个程序，能够根据用户的输入的数值生成相应数目的线程，每个线程运行1秒后结束。</Strong>
 * @author Kio
 * 
 */

public class Lab1 extends Thread{



	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		System.out.println("Please input the number of threads you want to create:");
		try {
			int num = input.nextInt();
			for (int i = 0; i < num; i++) {
				new Lab1("Thread "+i).start();
			}
		}catch(Exception e) {
			System.err.println("Illegal Input!");
		}
		input.close();
	}
	
	private Lab1(String name) {
		this.setName(name);
	}
	
	@Override
	public void run() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(this.getName() +" over!");
	}
	
}
