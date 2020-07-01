package deadlock;

public class DeadLockFix {

	public void method1() {
		synchronized (String.class) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(Thread.currentThread().getName() + "获取String锁");
			synchronized (Integer.class) {
				System.out.println(Thread.currentThread().getName() + "获取Integer锁");
			}
		}
	}

	public void method2() {
		synchronized (String.class) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(Thread.currentThread().getName() + "获取Integer锁");
			synchronized (Integer.class) {
				System.out.println(Thread.currentThread().getName() + "获取String锁");
			}
		}
	}

	public static void main(String[] args) {
		DeadLockFix deadLockFix = new DeadLockFix();
		Thread t1 = new Thread(() -> deadLockFix.method1());
		t1.setName("t1");
		t1.start();
		Thread t2 = new Thread(() -> deadLockFix.method2());
		t2.setName("t2");
		t2.start();
	}
}
