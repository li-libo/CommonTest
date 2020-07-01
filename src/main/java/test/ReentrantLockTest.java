package test;

import java.util.Collection;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.Test;

public class ReentrantLockTest {
    private static Lock fairLock = new ReentrantLock2(true);
    private static Lock unfairLock = new ReentrantLock2();

    @Test
    public void fair() {
        System.out.println("fair version");
        try {
            for (int i = 0; i < 5; i++) {
                Thread thread = new Thread(new Job(fairLock)) {
                    public String toString() {
                        return getName();
                    }
                };
                thread.setName("" + i);
                thread.start();
            }
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void unfair() {
        System.out.println("unfair version");
        try {
            for (int i = 0; i < 5; i++) {
                Thread thread = new Thread(new Job(unfairLock)) {
                    public String toString() {
                        return getName();
                    }
                };
                thread.setName("" + i);
                thread.start();
            }
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static class Job implements Runnable {
        private Lock lock;

        public Job(Lock lock) {
            this.lock = lock;
        }

        @Override
        public void run() {
            for (int i = 0; i < 5; i++) {
                lock.lock();
                try {
                    System.out.println("Lock by:"
                            + Thread.currentThread().getName() + " and "
                            + ((ReentrantLock2) lock).getQueuedThreads()
                            + " waits.");
                    Thread.sleep(100);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    // 重写ReentrantLock中的getQueuedThreads()方法，获取等待的线程队列
    private static class ReentrantLock2 extends ReentrantLock {

        private static final long serialVersionUID = 1773716895097002072L;

        public ReentrantLock2() {}

        public ReentrantLock2(boolean fair) {
            super(fair);
        }

        public Collection<Thread> getQueuedThreads() {
            return super.getQueuedThreads();
        }
    }
}
