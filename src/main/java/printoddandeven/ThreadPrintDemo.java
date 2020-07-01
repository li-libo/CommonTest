package printoddandeven;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 利用CAS交替输出奇数和偶数
 * @author lilibo lilibo@agree.com.cn
 */
public class ThreadPrintDemo {
	static AtomicInteger cxsNum = new AtomicInteger(0);
	static volatile boolean flag = false;

	public static void main(String[] args) {
		Thread t1 = new Thread(() -> {
			for (; 100 >= cxsNum.get();) {
				if (!flag && (cxsNum.get() % 2 == 0)) {
					try {
						Thread.sleep(100);// 防止打印速度过快导致混乱
					} catch (InterruptedException e) {
						// NO
					}
					System.out.println(cxsNum.getAndIncrement());
					flag = true;
				}
			}
		});

		Thread t2 = new Thread(() -> {
			for (; 100 >= cxsNum.get();) {
				if (flag && (cxsNum.get() % 2 != 0)) {
					try {
						Thread.sleep(100);// 防止打印速度过快导致混乱
					} catch (InterruptedException e) {
						// NO
					}
					System.out.println(cxsNum.getAndIncrement());
					flag = false;
				}
			}
		});
		t1.start();
		t2.start();
	}
}
