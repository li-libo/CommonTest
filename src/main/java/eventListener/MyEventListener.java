package eventListener;

public interface MyEventListener extends java.util.EventListener {
    //事件处理
    public void handleEvent(MyEventObject event);
}
