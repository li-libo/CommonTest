package runningrace;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class RunningRaceTestInCountDownLatch {

	public static void main(String[] args) throws InterruptedException {
		// 使用线程池的正确姿势
		int size = 5;
		AtomicInteger counter = new AtomicInteger();
		ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(size, size, 1000, TimeUnit.SECONDS,
				new ArrayBlockingQueue<>(100), (r) -> new Thread(r, counter.addAndGet(1) + " 号 "),
				new ThreadPoolExecutor.AbortPolicy());
		CountDownLatch countDownLatch = new CountDownLatch(5);
		for (int i = 0; i < size; i++) {
			threadPoolExecutor.submit(new Runner1(countDownLatch));
		} // 注意：本例5个线程共用1个countDownLatch
		// 裁判等待5名选手准备完毕
		countDownLatch.await(); // 为了避免死等，也可以添加超时时间
		System.out.println("裁判：比赛开始~~");
		threadPoolExecutor.shutdownNow();
	}

}

//参赛选手线程
class Runner1 implements Runnable {
	private CountDownLatch countdownLatch;

	public Runner1(CountDownLatch countdownLatch) {
		this.countdownLatch = countdownLatch;
	}

	@Override
	public void run() {
		try {
			int sleepMills = ThreadLocalRandom.current().nextInt(1000);
			Thread.sleep(sleepMills);
			System.out.println(Thread.currentThread().getName() + " 选手已就位, 准备共用时： " + sleepMills + "ms");
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			// 准备完毕，举手示意
			countdownLatch.countDown();
		}
	}
}
