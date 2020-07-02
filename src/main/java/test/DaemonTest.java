package test;

public class DaemonTest {
	
	public static void main(String[] args) throws InterruptedException {
		Thread t1 = new Thread(()->{
			while(true) {
				System.out.println("daemon");
			}
		});	
		t1.setDaemon(true);
		t1.start();
		System.out.println("t1 is Daemon?" + t1.isDaemon());
		Thread t2 = new Thread(()->{
			for(int i=0; i<100; i++) {
				System.out.println("normal");
			}
		});
		t2.start();		
		// t1.join();		
	}
		
}
