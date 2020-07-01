package test;

//保证原子操作类
class CompareAndSwap0 {
	volatile int value; // 共享变量

	public int getEvaluate() {
		return value;
	}

	public int CompareAndSwap(int evaluatedValue, int updateValue) {
		int oldValue = value;
		if (oldValue == evaluatedValue) {
			this.value = updateValue;
		}
		return oldValue;
	}

	public Boolean compareAndSet(int evaluatedValue, int updateValue) {
		return evaluatedValue == CompareAndSwap(evaluatedValue, updateValue);
	}
}

public class CASDemo {

	public static void main(String[] args) {
		final CompareAndSwap0 cas = new CompareAndSwap0();
		Runnable test = new Runnable() {
			@Override
			public void run() {
				int expectedValue = cas.getEvaluate();
				boolean b = cas.compareAndSet(expectedValue, (int) (Math.random() * 101));
				System.out.println("是否更新成功? " + b);
			}
		};
		for (int i = 0; i < 100; i++) {
			new Thread(test).start();
		}
		try {
			Thread.sleep(60 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
