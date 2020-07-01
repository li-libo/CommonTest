package test;

public class DaemonThreadTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Thread threadNormalThread = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				System.out.println(Thread.currentThread().getName() + " running ... ");
				try {
					Thread.sleep(100 * 1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}, "threadNormalThread");

		Thread threadDaemonThread = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (true) {
					System.out.println(Thread.currentThread().getName() + " running ... ");
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}, "threadDaemonThread");
		threadDaemonThread.setDaemon(true);
		threadDaemonThread.start();
		threadNormalThread.start();
	}

}
