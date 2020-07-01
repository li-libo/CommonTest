package runningrace;

import java.util.concurrent.ThreadLocalRandom;

public class RunningRaceTestInJoin {

	public static void main(String[] args) throws InterruptedException {
		Thread runner1 = new Thread(new Runner(), "1号");
		Thread runner2 = new Thread(new Runner(), "2号");
		Thread runner3 = new Thread(new Runner(), "3号");
		Thread runner4 = new Thread(new Runner(), "4号");
		Thread runner5 = new Thread(new Runner(), "5号");
		runner1.start();
		runner2.start();
		runner3.start();
		runner4.start();
		runner5.start();
		runner1.join();
		runner2.join();
		runner3.join();
		runner4.join();
		runner5.join();
		// 裁判等待5名选手准备完毕
		System.out.println("裁判：比赛开始~~");
	}
}

class Runner implements Runnable {
	@Override
	public void run() {
		try {
			int sleepMills = ThreadLocalRandom.current().nextInt(1000);
			Thread.sleep(sleepMills);
			System.out.println(Thread.currentThread().getName() + " 选手已就位, 准备共用时： " + sleepMills + "ms");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
