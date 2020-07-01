package Observer;

import java.util.Observable;
import java.util.Observer;

public class TestObserverMode {
	public static void main(String[] args) {
        Watched watched = new Watched();
        Watcher watcherDemo = new Watcher();
        watched.addObserver(watcherDemo);
        watched.addObserver(new Observer(){
            @Override
            public void update(Observable o, Object arg) {
                if(arg.toString().equals("closeWindows")){
                    System.out.println("已经关闭窗口");
                }
            }
        });
        //触发打开窗口事件，通知观察者
        watched.notifyObservers("openWindows");
        //触发关闭窗口事件，通知观察者
        watched.notifyObservers("closeWindows");

    }

}
