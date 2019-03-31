package javanet.l02;

/**
 * 已知有一个类，该类定义如下
 * <code>
 * public class Counter {<br/>
 * 	private int c = 30;<br/>
 * 	public  void decrement() {<br/>
 * 		if(c>0)<br/>
 * 			c--;<br/>
 * 		System.out.println(Thread.currentThread()+"="+c);<br/>
 * 	}<br/>
 * }
 * </code><br/>
 * 请编写一个程序，该程序产生300个线程，
 * 所有线程共用同一个counter实例，
 * 每个线程启动后调用decrement()方法，观察输出结果，
 * 请说明产生该结果的原因是什么。如果要保证c按顺序递减，
 * 则该程序应当如何修改。
 * @author Kio
 *
 */

public class Lab4 extends Thread{
	private Counter counter;
	public static void main(String[] args) {
		System.err.println("No Synchronized");
		Counter counter = new Counter();
		for (int i = 0; i < 300; i++) {
			new Lab4(counter).start();
		}
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.err.println("Synchronized");
		
		Counter counter2 = new Counter();
		for(int i = 0;i<300;i++) {
			new SynThread(counter2).start();
		}
	}
	
	private Lab4(Counter counter) {
		this.counter = counter;
	}
	
	@Override
	public void run() {
		counter.decrement();
	}	
}

class Counter {
    private int c = 30;
    void decrement() {
    	if(c>0)
    		c--;
    	System.out.println(Thread.currentThread()+"="+c);
    }
}

class SynThread extends Thread{
	private final Counter counter;

	SynThread(Counter counter) {
		this.counter = counter;
	}
	
	@Override
	public void run() {
		synchronized(counter){
			counter.decrement();
		}
		
	}
}
