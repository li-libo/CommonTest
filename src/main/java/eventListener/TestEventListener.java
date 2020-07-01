package eventListener;

public class TestEventListener {
	public static void main(String[] args) {
		
		MyEventSource eventSource = new MyEventSource();

		eventSource.addListener(new MyEventListener() {
			@Override
			public void handleEvent(MyEventObject event) {
				event.doEvent();
				if (event.getSource().equals("closeWindows")) {
					System.out.println("doClose");
				}
			}

		});

		/*
		 * 传入openWindows事件，通知listener，事件监听器， 对open事件感兴趣的listener将会执行
		 **/
		eventSource.notifyListenerEvents(new MyEventObject("openWindows"));
		eventSource.notifyListenerEvents(new MyEventObject("closeWindows"));
	}

}
