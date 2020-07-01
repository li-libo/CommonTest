package test;

/**
 * 使用Semaphore类限制连接池访问数量
 */
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class SemaphoreTest {
	public static void main(String[] args) throws Exception {
		ExecutorService executors = Executors.newCachedThreadPool();
		for (int i = 0; i < 20; i++) {
			executors.submit(new Runnable() {
				@Override
				public void run() {
					Connection.getInstance().connect();
				}
			});
		}
		executors.awaitTermination(10, TimeUnit.HOURS);
	}
}

class Connection {
	private static Connection instance = new Connection(); //懒汉模式
	private Semaphore semaphores = new Semaphore(5, false);
	private int connectionNum = 0;

	private Connection() {
	}

	public static Connection getInstance() {
		return instance;
	}

	public void connect() {
		try {
			semaphores.acquire();
			doConnect();
		} catch (InterruptedException e) {
//
		} finally {
			semaphores.release();
		}
	}

	private void doConnect() {
		synchronized (this) {
			connectionNum++;
			System.out.println("1 current connections num: " + connectionNum + ",the current Thread id-" + Thread.currentThread().getId());
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
//
		}
		synchronized (this) {
			connectionNum--;
			System.out.println("2 current connections num : " + connectionNum + ",the current Thread id-" + Thread.currentThread().getId());
		}
	}
}
