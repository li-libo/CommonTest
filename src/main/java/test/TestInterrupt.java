package test;

import org.junit.Test;

public class TestInterrupt {

	@Test
	public void testInTerrupt() throws InterruptedException {
		Thread t1 = new Thread(()->{
			while(!Thread.currentThread().isInterrupted()) { //中断可用作退出线程
				try {
					Thread.sleep(2 * 1000);
				} catch (InterruptedException e) {
					System.out.println("t1.isInterrupted() == " + Thread.currentThread().isInterrupted()); //抛出异常后JVM清除中断标志
					Thread.currentThread().interrupt();					
					System.out.println("t1.isInterrupted() == " + Thread.currentThread().isInterrupted()); 
				}
			}
			System.out.println("退出t1线程!");
		});
		t1.setName("t1");
		t1.start();	
		Thread.sleep(10 * 1000);
		t1.interrupt();	
		// System.out.println("t1.interrupted() == " + t1.interrupted()); --- interrupted是看当前线程,当前为main线程
		t1.join();
	}
	
	/*
	 * 中断用法示例:
       private void stopJetty() {
        // Jetty does not really stop the server if port is busy.
        try {
            if (httpSrv != null) {
                // If server was successfully started, deregister ports.
                if (httpSrv.isStarted())
                    ctx.ports().deregisterPorts(getClass());

                // Record current interrupted status of calling thread.
                boolean interrupted = Thread.interrupted();

                try {
                    httpSrv.stop();
                }
                finally {
                    // Reset interrupted flag on calling thread.
                    if (interrupted)
                        Thread.currentThread().interrupt();
                }
            }
        }
        catch (InterruptedException ignored) {
            if (log.isDebugEnabled())
                log.debug("Thread has been interrupted.");

            Thread.currentThread().interrupt();
        }
        catch (Exception e) {
            U.error(log, "Failed to stop Jetty HTTP server.", e);
        }
    }
	 */
}
