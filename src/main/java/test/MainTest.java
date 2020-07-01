package test;

public class MainTest {
	public MainTest note;
	public int i;
	public  MainTest(int j){
		this.i=j;
		
		}
		public void println(){
			System.out.println(" "+i);
		}
	
	public static void main(String[] args) {
		
		MainTest n1=new MainTest(1);
		MainTest n2=new MainTest(2);
		//n1=n1.note=n2;//------1-------
		n1.note=n1=n2;//------2-------		
		n1.println();
		n1.note.println();
    }
}