package printoddandeven;

/**
 * 交替输出奇数和偶数
 * 
 * 我们使用 volatile 变量代替 CAS 变量，减轻使用 CAS 的消耗，
 * 注意，这里 num++ 不是原子的，但不妨碍，因为有 flag 变量控制。
 * 而 num 必须是 volatile 的，如果不是，会导致可见性问题。
 * @author lilibo lilibo@agree.com.cn
 */
public class ThreadPrintDemo3 {
	static volatile int num = 0;
	static volatile boolean flag = false;

	public static void main(String[] args) {
		Thread t1 = new Thread(() -> {
			for (; 100 >= num;) {
				if (!flag && (num % 2 == 0)) {
					try {
						Thread.sleep(100);// 防止打印速度过快导致混乱
					} catch (InterruptedException e) {
						// NO
					}
					System.out.println(num++);
					flag = true;
				}
			}
		});

		Thread t2 = new Thread(() -> {
			for (; 100 >= num;) {
				if (flag && (num % 2 != 0)) {
					try {
						Thread.sleep(100);// 防止打印速度过快导致混乱
					} catch (InterruptedException e) {
						// NO
					}
					System.out.println(num++);
					flag = false;
				}
			}
		});
		t1.start();
		t2.start();

	}
}
