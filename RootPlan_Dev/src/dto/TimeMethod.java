package dto;

public class TimeMethod {
	private int time;
	private boolean method;  //true가 걷기 api
	public TimeMethod(int t, boolean m){
		this.time = t;
		this.method = m;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public boolean getMethod() {
		return method;
	}
	public void setMethod(boolean method) {
		this.method = method;
	}
}

