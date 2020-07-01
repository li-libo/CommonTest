package test;

public class ContinuedAssignmentTest {
	public ContinuedAssignmentTest note;
	public int i;
	public  ContinuedAssignmentTest(int j){
		this.i=j;
		
		}
		public void println(){
			System.out.println(" "+i);
		}
	
	public static void main(String[] args) {
		
		ContinuedAssignmentTest n1=new ContinuedAssignmentTest(1);
		ContinuedAssignmentTest n2=new ContinuedAssignmentTest(2);
		//n1=n1.note=n2;//------1-------
		n1.note=n1=n2;//------2-------		
		n1.println();
		n1.note.println();
    }
}