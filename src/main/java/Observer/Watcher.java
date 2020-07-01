package Observer;

import java.util.Observable;
import java.util.Observer;

public class Watcher implements Observer {
    @Override
    public void update(Observable o, Object arg) {
        if(arg.toString().equals("openWindows")){
            System.out.println("已经打开窗口");
        }
    }
}
