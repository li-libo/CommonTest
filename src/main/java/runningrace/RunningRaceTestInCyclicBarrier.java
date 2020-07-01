package runningrace;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class RunningRaceTestInCyclicBarrier {
	public static void main(String[] args) {
		// 使用线程池的正确姿势
		int size = 5;
		AtomicInteger counter = new AtomicInteger();
		ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(size, size, 1000, TimeUnit.SECONDS,
				new ArrayBlockingQueue<>(100), (r) -> new Thread(r, counter.addAndGet(1) + " 号 "),
				new ThreadPoolExecutor.AbortPolicy());
		CyclicBarrier cyclicBarrier = new CyclicBarrier(5, () -> System.out.println("裁判：比赛开始~~"));
		for (int i = 0; i < 10; i++) {
			threadPoolExecutor.submit(new Runner2(cyclicBarrier));
		}
	}

}

class Runner2 implements Runnable {
	private CyclicBarrier cyclicBarrier;

	public Runner2(CyclicBarrier cyclicBarrier) {
		this.cyclicBarrier = cyclicBarrier;
	}

	@Override
	public void run() {
		try {
			int sleepMills = ThreadLocalRandom.current().nextInt(1000);
			Thread.sleep(sleepMills);
			System.out.println(Thread.currentThread().getName() + " 选手已就位, 准备共用时： " + sleepMills + "ms"
					+ cyclicBarrier.getNumberWaiting());
			cyclicBarrier.await();
		} catch (InterruptedException | BrokenBarrierException e) {
			e.printStackTrace();
		}
	}
}
