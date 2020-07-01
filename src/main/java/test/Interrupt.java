package test;

/**
* interrupt()方法、isInterrupted()方法、interrupted()方法
*/
public class Interrupt {
   public static void main(String[] args) throws Exception {
       Thread t = new Thread(new Worker());
       t.start();

       Thread.sleep(100);
       t.interrupt();

       System.out.println("Main thread stopped.");
   }

   /**
    * 抛出一次InterruptedException异常后JVm会把线程的中断状态清除掉interrupt的状态设置为false
    */
   public static class Worker implements Runnable {
       public void run() {
           System.out.println("Worker started.");
           try {
               Thread.sleep(200);
           } catch (InterruptedException e) {
               System.out.println("抛出异常JVM会清除线程的中断状态---Worker IsInterrupted?" +
                       Thread.currentThread().isInterrupted());
               // TODO: 2019/1/29 对抛出的InterruptedException的正确处理应该是设置Thread.currentThread().interrupt(); 
               Thread.currentThread().interrupt();
               System.out.println("执行interrupt()设置线程的中断状态---Worker IsInterrupted?" +
                       Thread.currentThread().isInterrupted()); 
               /**
                * 清除线程的中断状态
                */
               Thread.interrupted();
               System.out.println("清除线程的中断状态interrupted()---Worker IsInterrupted?" +
                       Thread.currentThread().isInterrupted());
           }
           System.out.println("Worker stopped.");

       }
   }
}