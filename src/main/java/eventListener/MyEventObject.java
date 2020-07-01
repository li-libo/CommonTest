package eventListener;

public class MyEventObject extends java.util.EventObject{
    private static final long serialVersionUID = 1L;
    public MyEventObject(Object source){
        super(source);
    }
    public void doEvent(){
        System.out.println("通知一个事件源 source :"+ this.getSource());
    }

}