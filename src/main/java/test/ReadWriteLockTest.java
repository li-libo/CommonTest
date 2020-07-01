package test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;


public class ReadWriteLockTest {
	
	private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock(false);
    private final WriteLock writeLock = readWriteLock.writeLock();
	private final ReadLock readLock = readWriteLock.readLock();
	
	private void printWriteThreadInfo(){		
		writeLock.lock();
		try {		
			String threadName = Thread.currentThread().getName();
			long threaId = Thread.currentThread().getId();
			System.out.println("准备写数据,当前线程Name为:" + threadName +", 当前线程Id为:" + threaId);			
		}finally {
			writeLock.unlock();
		}		
	}
	
	private void printReadThreadInfo(){
		readLock.lock();
		try {				
			String threadName = Thread.currentThread().getName();
			long threaId = Thread.currentThread().getId();
			System.out.println("准备读数据,当前线程Name为:" + threadName +", 当前线程Id为:" + threaId);	
		}
		finally {
			readLock.unlock();
		}		
	}
	
	@Test
	public void testReadWriteLock() {
		try {
			Thread t2 = new Thread(()->{
				for (int i = 0; i < 100; i++) {
					Thread readThread = new Thread(() -> printReadThreadInfo());
					readThread.setName("读线程");
					readThread.start();
				}
			});
			t2.start();
			Thread t1 = new Thread(()->{
				for (int i = 0; i < 10; i++) {
					Thread writeThread = new Thread(() -> printWriteThreadInfo());
					writeThread.setName("写线程");
					writeThread.start();
				}	
			});
			t1.start();		
			Thread.sleep(600 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testJson1() throws JSONException {
		// 模拟发送
		String jsonMessage = "[1,{'role':'worker','joinClusterAddresses':'192.168.191.31:47500'}]";
		JSONArray sendJsonArray = new JSONArray(jsonMessage);
		String sendString = sendJsonArray.toString();
		// 模拟接收
		JSONArray receiveJsonArray = new JSONArray(sendString);
		System.out.println("responseId: " + receiveJsonArray.optLong(0));
		String registryResponse = receiveJsonArray.optString(1);
		System.out.println(registryResponse);
		// 客户端转为jsonObject
		JSONObject jsonObject = new JSONObject(registryResponse);
		String role = jsonObject.optString("role");
		String joinClusterAddresses = jsonObject.optString("joinClusterAddresses");
		System.out.println("role:" + role + ", joinClusterAddresses:" + joinClusterAddresses);
	}

	@Test
	public void testJson2() throws JSONException {
		// 模拟发送
		JSONArray sendJsonArray = new JSONArray();
		sendJsonArray.put(1).put(null);
		String sendString = sendJsonArray.toString();
		// 模拟接收
		JSONArray receiveJsonArray = new JSONArray(sendString);
		System.out.println("responseId: " + receiveJsonArray.optLong(0));
		String registryResponse = receiveJsonArray.optString(1);
		if ("null".equals(registryResponse)) {
			System.out.println("null取optString为'null'");
		}
	}
	
	@Test
	public void testJson() throws JSONException {
		JSONObject jsonObject = new JSONObject();
		JSONObject jsonObject2 = new JSONObject();
		jsonObject2.put("2", "hello");
		jsonObject.put("1", jsonObject2);
		JSONObject jsonObject3 = jsonObject.optJSONObject("1");
		System.out.println(jsonObject3.optString("2"));
	}
	
	@Test
	public void testArrayList() {
		List<String> list1 = new ArrayList<>();
		list1.add("hello");
		list1.add("tc");
		System.out.println(list1);
		list1.remove(1);
		System.out.println(list1);
		List<String> linkedList = new LinkedList<>();
		linkedList.add("hello");
		linkedList.add("o");
		linkedList.remove(1);		
	}
	
	@Test
	public void testArrayCopy() {
		String[] array1 = {"a","b","c"};
		String[] array2 = new String[5];
		/*
		 * src:源数组；
		   srcPos:源数组要复制的起始位置；
           dest:目的数组；
           destPos:目的数组放置的起始位置；
           length:复制的长度。
           注意：src and dest都必须是同类型或者可以进行转换类型的数组．
		 */
		System.arraycopy(array1, 0, array2, 0, array1.length);
		for(String o : array2) {
			System.out.println(o);
		}
		System.out.println(array1 == array2);
		System.out.println("**************");
		System.arraycopy(array1, 0, array1, 1, 2);
		for(String o : array1) {
			System.out.println(o);
		}
	}
	
//	@Test
//	public void testPlus() {
//		int i = 0;
//		i = i++;
//		System.out.println("i=" + i);
//		int j = 0;
//		j = ++j;
//		System.out.println("j=" + j);
//	}
	
	@Test
	public void testStringFormat() throws JSONException {
		//占位符完整格式为： %[index$][标识]*[最小宽度][.精度]转换符 
		String s = String.format("%3$s,%n%2$s", "hello","a","c");
		System.out.println(s);
		String d = String.format("%d,%n%3$d", 123, "d", 345);
		System.out.println(d);
		System.out.printf("3>7的结果是：%b %n", 3>7);  
		Date date = new Date();
	    System.out.printf("相对于GMT的RFC822时区的偏移量:%tz%n", date);  
	    System.out.printf("时区缩写字符串:%tZ%n", date);  
	    System.out.printf("1970-1-1 00:00:00 到现在所经过的秒数：%ts%n", date);  
	    System.out.printf("1970-1-1 00:00:00 到现在所经过的毫秒数：%tQ%n", date);  
	    System.out.println("hello" + null);	    
	    try {
			JSONObject jsonObject = new JSONObject();
			System.out.println(jsonObject.getString("a"));
		} catch (Exception e) {
			//String a = getStackTraceString(e);
			String eString = e.toString();
			System.out.println(eString);
		}
	}
	
	public static String getStackTraceString(Exception e)
    {  
        try(StringWriter sw = new StringWriter(); PrintWriter pw = new PrintWriter(sw))
        {
        	e.printStackTrace(pw);
            return sw.toString();
        }
        catch(Exception e1)
        {
            return "";
        }
    }	


	@Test
	public void testJsonAndTimestamp() throws JSONException, NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		Instant startInstant1 = Instant.now();
		// 模拟发送
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("role", "broker").put("joinClusterAddresses", "192.168.191.31");
		System.out.println("响应信息包:" + jsonObject);
		Instant startInstant2 = Instant.now();
		int d = startInstant2.compareTo(startInstant1);
		if (d >= 0) {
			System.out.println("startInstant2>=startInstant1");
		} else {
			System.out.println("startInstant2<startInstant1");
		}
		long seconds = startInstant2.getEpochSecond();
		int nanos = startInstant2.getNano();
		Instant startInstant3 = null;
		Method InstantMethod = Instant.class.getDeclaredMethod("create", long.class, int.class);
		InstantMethod.setAccessible(true);
		startInstant3 = (Instant) InstantMethod.invoke(startInstant3, seconds, nanos);
		d = startInstant2.compareTo(startInstant3);
		if (d > 0) {
			System.out.println("startInstant3>endInstant2");
		} else if (d < 0) {
			System.out.println("startInstant3<endInstant2");
		} else {
			System.out.println("startInstant3==endInstant2");
		}
		Timestamp timestamp = Timestamp.from(startInstant3);
		System.out.println(timestamp);	
		Timestamp timestamp2 = Timestamp.valueOf(timestamp.toString());
		System.out.println(timestamp.compareTo(timestamp2));
	}
	
	@Test
	public void testJsonObject() throws JSONException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("long", 10L);
		String longString = jsonObject.optString("long");
		long a = Long.parseLong(longString);
		System.out.println(a);
	}
	
	@Test
	public void testTimer() throws InterruptedException {
		MyTaskA task1 = new MyTaskA();
		MyTaskB task2 = new MyTaskB();
		Timer timer = new Timer();
		timer.schedule(task1, 0, 5 * 1000);
		timer.schedule(task2, 0, 5 * 1000);
		Thread.sleep(600 * 1000);
	}

	class MyTaskA extends TimerTask {

		@Override
		public void run() {
			System.out.println("A run timer=" + new Date());
			//this.cancel();// 调用的是TimerTask类中的cancel()方法
			System.out.println("A任务自己移除自己,B任务不受影响，继续运行");
		}
	}

	class MyTaskB extends TimerTask {

		@Override
		public void run() {
			System.out.println("B run timer=" + new Date());
		}
	}
	
	@Test
	public void testCountDownLatch() throws InterruptedException {
		CountDownLatch countDownLatch = new CountDownLatch(1);
		Thread t1 = new Thread(()->{
			System.out.println("hello");
			try {
				Thread.sleep(10 * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			countDownLatch.countDown();
			System.out.println("release countdownlatch");
		});
		t1.start();
		Thread t2 = new Thread(()->{
			System.out.println("hello");
			try {
				Thread.sleep(15 * 1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			countDownLatch.countDown();
			System.out.println("release countdownlatch");
		});
		t2.start();
		countDownLatch.await();
		System.out.println("end");
		Thread.sleep(600 * 1000);
	}
	
	@Test
	public void testBoolean() {
		if(getBoolean()) {
			System.out.println("hello");
		}
	}
	
	private Boolean getBoolean() {
		return null;		
	}
	
    public static boolean checkAddress(String s) {
        return s.matches("((25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))");
    }
 
    public static boolean checkPort(String s) {
        return s.matches("^[1-9]$|(^[1-9][0-9]$)|(^[1-9][0-9][0-9]$)|(^[1-9][0-9][0-9][0-9]$)|(^[1-6][0-5][0-5][0-3][0-5]$)");
    }
    
    public class MyThread extends Thread {
        @Override
        public  void run() {
            for (int i = 0; i < 30; i++) {
                System.out.println("i="+(++i));
            }
        }
    }
    
    @SuppressWarnings("static-access")
	@Test
    public void testInterrupt() throws InterruptedException {
    	MyThread thread=new MyThread();
		thread.start();
		thread.interrupt();
		System.out.println("第一次调用thread.isInterrupted()："+thread.isInterrupted());
		System.out.println("第二次调用thread.isInterrupted()："+thread.isInterrupted());
	    //测试interrupted（）函数
		System.out.println("第一次调用thread.interrupted()："+thread.interrupted());
		System.out.println("第二次调用thread.interrupted()："+thread.interrupted());
		System.out.println("thread是否存活："+thread.isAlive());
    }
    
    @SuppressWarnings("static-access")
	@Test
    public void testInterrupt1() throws InterruptedException {
    	Thread thread = Thread.currentThread();
    	thread.interrupt();
		System.out.println("第一次调用thread.isInterrupted()："+thread.isInterrupted()); //true
		System.out.println("第二次调用thread.isInterrupted()："+thread.isInterrupted()); //true
	    //测试interrupted（）函数
		System.out.println("第一次调用thread.interrupted()："+thread.interrupted()); //true
		System.out.println("第二次调用thread.interrupted()："+thread.interrupted()); //false
		System.out.println("thread是否存活："+thread.isAlive()); //true
    }
    
    /*
     * 若果想要是实现调用interrupt（）方法真正的终止线程，则可以在线程的run方法中做处理即可，比如直接跳出run()方法使线程结束
     */
    public class MyThread1 extends Thread {
        @SuppressWarnings("static-access")
		@Override
        public  void run() {
            for (int i = 0; i < 1000; i++) {
                System.out.println("i="+(i+1));
                if(this.isInterrupted()){
                    System.out.println("通过this.isInterrupted()检测到中断");                   
                    System.out.println("第一个interrupted()"+this.interrupted());
                    System.out.println("第二个interrupted()"+this.interrupted());
                    break;
                }
            }
            System.out.println("因为检测到中断，所以跳出循环，线程到这里结束，因为后面没有内容了");
        }
    }
    
    @SuppressWarnings("static-access")
	@Test
    public void testInterrupt2() throws InterruptedException {
		MyThread1 myThread=new MyThread1();
		myThread.start();
		myThread.interrupt();
		//sleep等待一秒，等myThread运行完
		Thread.currentThread().sleep(1000);
		System.out.println("myThread线程是否存活："+myThread.isAlive());
    }
        
    @Test
    public void testDaemon() throws InterruptedException {
    	Thread thread = new Thread(()->{
    		while(true) {
    			System.out.println("hello");
    			break;
    		}
    	});
    	thread.setDaemon(true);
    	thread.setName("daemon");
    	thread.start();
    	Thread.sleep(1000);
    	System.out.println("主线程结束" + "thread是否存活:" + thread.isAlive());
    }

    @Test
    public void testAtomicLong() {
    	AtomicLong pageIdAtomicLong = new AtomicLong(0);
    	AtomicLong startIdAtomicLong = new AtomicLong(0);
    	AtomicLong endIdAtomicLong = new AtomicLong(10000);
    	for(int i = 0; i<10; i++) {
    		System.out.println("pageId:" + pageIdAtomicLong.incrementAndGet());
    		System.out.println("startId:" + startIdAtomicLong.addAndGet(10000));
    		System.out.println("endId:" + endIdAtomicLong.addAndGet(10000));
    	}
    }
    
    @Test
    public void testRandom() {
    	List<String> aList = new ArrayList<String>();
    	aList.add("a");
    	aList.add("b");
    	aList.add("c");
    	for(int i =0; i<100; i++) {
    		int r = (int) (Math.random() * aList.size());
    		System.out.println("随机值: " + r);
    	}
    }
    
    @Test
    public void testSplit() {
    	StringBuilder sBuilder = new StringBuilder();
    	for(int i=0; i<10; i++) {
    		sBuilder.append(i+",");
    	}
    	sBuilder.append("");
    	String o = sBuilder.toString();
    	List<String> aList = Arrays.asList(o.split(","));
    	aList.forEach(x->System.out.println("输出:" +x));
    }
    
    private Map<Integer, ReentrantLock> lockMap = new ConcurrentHashMap<>();
    
    private void printNum() {
    	int index = (int)(Math.random()*10);
    	Lock lock = lockMap.get(index);    	
    	try {
			if(lock.tryLock(10, TimeUnit.SECONDS)) {
				try {
					System.out.println("index:" + index);
				}finally {
					lock.unlock();	
				}
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
        
    @Test
    public void testLock() throws InterruptedException {
    	for(int i =0; i<10; i++) {
    		lockMap.put(i, new ReentrantLock());
    	}
    	for(int i=0; i<100; i++) {
    		Thread thread = new Thread(()->{
    			printNum();
        	});
    		thread.start();
    	}
    	Thread.sleep(600 * 1000);
    }
    
    @Test
    public void testThreadPool() {
    	ExecutorService executorService = Executors.newFixedThreadPool(3);
    	for(int i=0; i<100; i++) {
    		executorService.execute(()->{
	    		System.out.println("当前线程名为:" + Thread.currentThread().getName());	
	    		try {
					Thread.sleep(2 * 1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		});    		
    	}   	
    }
    
    @Test
    public void testSplit2() {
    	List<String> oldBackupAdpsIdList = new ArrayList<String>(
				Arrays.asList("".split("\\s*,\\s*")));
    	oldBackupAdpsIdList.forEach(x->System.out.println(":" + x));
    }
    
    @Test
    public void test1() {
    	StringBuilder sBuilder = new StringBuilder();
    	System.out.println(":"+sBuilder.toString()+":");
    	List<String> list = new ArrayList<>();
    	list.add("hello");
    	for(int i= 1; i<list.size(); i++) {
    		System.out.println("******");
    	}
    }
    
    private void testFun() {
    	System.out.println("hello");
    }
    
	@Test
	public void testScheduleThreadPool() throws InterruptedException {
		ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1,
				(r) -> new Thread(r, "testSchedule" + UUID.randomUUID().toString()));
//		for (int i = 0; i < 10; i++) {
//			Thread t1 = new Thread(() -> {
//				scheduledExecutorService.scheduleAtFixedRate(() -> testFun(), 2, 2, TimeUnit.SECONDS);
//			});
//			t1.start();
//		}
		scheduledExecutorService.scheduleAtFixedRate(() -> testFun(), 2, 2, TimeUnit.SECONDS);
		Thread.sleep(600 * 1000);
	}
	
	@Test
	public void testSum() {
		Map<String, List<String>> map = new ConcurrentHashMap<>();
		List<String> list = new ArrayList<>();
		list.add("a");
		list.add("b");
		List<String> list1 = new ArrayList<>();
		list.add("c");
		List<String> keyList = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			if (i % 2 == 0) {
				map.put(i + "", list);
				keyList.add(i + "");
			} else {
				map.put(i + "", list1);
				keyList.add(i + "");
			}
		}
		int sum = keyList.stream().map(key -> map.get(key).size()).reduce((x, y) -> x + y).get();
		System.out.println("sum和为:" + sum);
	}
	
   @Test
    public void testString() {
        String aString = "abc";
        String bString = "abc";
        String cString = new String(aString);
        String dString = new String(aString);
        System.out.println("aString == bString?:" + (aString == bString));
        System.out.println("aString == cString?:" + (aString == cString));
        System.out.println("aString == dString?:" + (aString == dString));
    }
   
   @Test
   public void testDate() {
	    String startProcessTimeString = "2020-02-14T14:36:50.419+0000";
		String endProcessTimeString = "2020-02-14T14:36:51.470+0000";
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD'T'HH:mm:ss.SSSZ");
		try {
			Date startDate = sdf.parse(startProcessTimeString);
			Date endDate  = sdf.parse(endProcessTimeString);		
			System.out.println("流程耗时" + Duration.between(startDate.toInstant(), endDate.toInstant()).toMillis() + "ms");
		} catch (ParseException e) {
			e.printStackTrace();
		}
   }
   
   @Test
   public void testFor() {
	   for(int i=0; i<10; System.out.println(i),++i);
	   for(int i=0; i<10; ++i,System.out.println(i));
   }
   
   @Test
   public void testStringMatchs() {
	   if("GEN_CallGen_c5a576664425f04315065eafdb59f549".matches("GEN\\w+_\\w+")) {
           System.out.println("匹配");
       }else {
    	   System.out.println("不匹配!");
       }
   }
   
   @Test
   public void testMatch() {
	   if("GEN_核身/开户核身_e4774cdda0793f86414e8b9140bb6db4".startsWith("GEN_")) {
		   System.out.println("匹配!");
	   }
   }

}
