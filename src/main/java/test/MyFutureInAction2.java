package test;
 
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
 
/**
 * Created by mengxiaopeng on 2018/3/9.
 * com.company.LambdaExpressions.Futures
 * MyFutureInAction2 会在MyFutureInAction基础上注册一个Listen监听 当存在值后通知调用者线程
 * 好处：不阻塞主线程  Future执行完后会通知调用者 不用采用while方式获取Callable的返回值
 */
public class MyFutureInAction2 {
 
    public static void main(String[] args) {
 
        //######测试自定义 submit()
        Future<String> submitFuture = submit(() -> {
            try {
                Thread.sleep(10000);
                return "Success";
            } catch (InterruptedException e) {
                e.printStackTrace();
                return "Error";
            }
        });
 
        submitFuture.AddListener(new ListenBack<String>() {
            @Override
            public void onSuccess(String value) {
                System.out.println("Get Result By Listener..Value=["+value+"]");
            }
 
            @Override
            public void OnFailed(Throwable cause) {
              cause.printStackTrace();
            }
        });
        System.out.println("3==main=no==Blocker===");
        //上面执行时 不需要将主线程Block住  因为执行submit时 不是守护进程
        //但是对于Java 8 CompletableFuture.supplyAsync 执行时是守护进程  如果主线程一直在 不用采用其他线程池或者Join 可以获取执行的返回值
        //否则需要采用其他的线程池或者Join 保证CompletableFuture.supplyAsync 在执行过程中主线程不会提前执行完
    }
 
    /**
     * Executors.newCachedThreadPool().submit()
     * 自定义实现了上面的功能
     * @param callable
     * @param <T>
     * @return 更加理解Future底层采用的方式
     */
    private static <T> Future<T> submit(Callable<T> callable) {
        AtomicReference<T> result = new AtomicReference<T>();
        AtomicBoolean isFlag = new AtomicBoolean(false);
        //##先初始化Future
        Future<T> future = new Future<T>() {
            //后续通过Future.AddListener 回调函数传进来
            private ListenBack<T> listenBack;
            @Override
            public T get() {
                T t = result.get();
                return t;
            }
 
            @Override
            public boolean isDone() {
                return isFlag.get();
            }
 
            @Override
            public void AddListener(ListenBack<T> listenBack) {
                this.listenBack=listenBack;
            }
 
            @Override
            public ListenBack<T> getLister() {
                return listenBack;
            }
        };
        //## 正在处理任务
        new Thread(() -> {
            try {
                T action = callable.action();
                result.set(action);
                isFlag.set(true);
                if (future.getLister()!=null){
                    future.getLister().onSuccess(action);
                }
                //为空不做处理 因为不回调就不用管
            } catch (Exception e) {
                //处理异常
                if (future.getLister()!=null){
                    future.getLister().OnFailed(e);
                }
            }
        }).start();
        return future;
    }
 
 
    /**
     * 定义Future  将来由future返回
     * @param <T>
     */
    public interface Future<T> {
        T get();
 
        boolean isDone();
 
        //增加一个回调监听
        void AddListener(ListenBack<T> listenBack);
 
        ListenBack<T>  getLister();
     }
 
 
    //增加一个回调监听 里面存在两个方法 一个成功 一个Exception
    private interface ListenBack<T>{
 
        void onSuccess(T value);
 
        void OnFailed(Throwable cause);
    }
 
    /**
     * 定义Callable的接口 具体需要做的事情
     * 可以采用lamdba表达式
     * @param <T>
     */
    private interface Callable<T> {
        T action();
    }
}
