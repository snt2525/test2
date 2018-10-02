package dto;

public class SetData {
	private int startIndex; 
	private int lastIndex;
	
	public SetData(){
		this.startIndex = -1;
		this.lastIndex = -1;
	}	
	
	public void SetStartData(int s){
		this.startIndex = s;
	}
	
	public void SetLastData(int l) {
		this.lastIndex = l;
		//System.out.println("호출:"+startIndex+" "+lastIndex);
	}
	
	public int GetStartData() {
		return startIndex;
	}
	public int GetLastData() {
		return lastIndex;
	}
	
	public int isSame() {
		if(startIndex == lastIndex)
			return 1;
		return 0;
	}
}
