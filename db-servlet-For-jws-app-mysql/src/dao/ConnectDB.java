package dao;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import dto.Address;
import dto.CustomerInfo;
import dto.DBRoute2Data;
import dto.DBRouteData;
import dto.Route2DataCall;

public class ConnectDB {
	static DataSource ds;
	static Connection connection;
	static Statement st;
	static ResultSet rs;
	public ConnectDB(){
		try {	
			InitialContext ct = new InitialContext();
			ds = (DataSource)ct.lookup("java:comp/env/jdbc/mysqldb");
			

		} catch (Exception e) {
			
		}
	}
	public void CheckID(CustomerInfo info) {
		System.out.println("checkID");
		try {			
			connection = ds.getConnection();
			st = connection.createStatement();
			rs = st.executeQuery("show databases");	
			if (st.execute("SHOW DATABASES")) {
				rs = st.getResultSet();
			}
			while (rs.next()) {
				String str = rs.getNString(1);
				System.out.println(str);
			}
			rs = st.executeQuery("SELECT * FROM customer where id='"+ info.getId()+"'");			
			if(rs.next()) { //이미 있는 아이디
				System.out.println("이미 있는 아이디입니다.");
			}else{
				System.out.println("없는 아이디입니다.");
				CreateDB(info); //없는 아이디
				rs = st.executeQuery("SELECT * FROM customer");
				while (rs.next()) {
					String str = rs.getNString(1);
					System.out.println(str);
				}
			}
			rs.close();
			st.close();
			connection.close();
		} catch (SQLException SQLex) {
			// 나중에 문제 발생하면 여기 보기
			System.out.println("DB입력 에러 발생");
			System.out.println("SQLException: " + SQLex.getMessage());
			System.out.println("SQLState: " + SQLex.getSQLState());
		}
	}
	private void CreateDB(CustomerInfo info) {
		System.out.println("DB를 생성합니다");
		try {
			connection = ds.getConnection();
			st = connection.createStatement();
			st.executeUpdate("INSERT INTO customer " +
					"VALUES('"+info.getId()+"','"+info.getEmail()
					+"',"+info.getGender()+","+info.getAge()+");");
			System.out.println("DB가 저장되었습니다.");
			rs.close();
			st.close();
			connection.close();
		} catch (SQLException SQLex) {
			System.out.println("SQLException: " + SQLex.getMessage());
			System.out.println("SQLState: " + SQLex.getSQLState());
		}		
	}
	public String makeRID(LinkedList<Address> ad) {
		String result = "";
		int size = ad.size();
		for(int i = 0;i<size;i++) {
			//result+= Double.toString(ad.get(i).getLat()) + Double.toString(ad.get(i).getLng());
			result += ad.get(i).getAddress();
		}
		System.out.println("rID:" + result);
		return result;
	}
	
	public String CheckSameData(LinkedList<Address> ad, String cID, String what, String name) { //저장 -> 중복되는 애가 있는지 검사
		System.out.print("cID"+cID);
		//rID를 만든다
		String rID =  makeRID(ad);
		try {
			connection = ds.getConnection();
			st = connection.createStatement();
			rs = st.executeQuery("SELECT * FROM route where rid='"+rID+"' AND cid='"+cID+"'");
			if(rs.next()) { //이미 있는 아이디
				System.out.println("이미 있는 경로");
				rs.close();
				st.close();
				return "0";
			}else {
				System.out.println("없는 저장 정보 입니다."); 
				if(what.equals("1")) {
					DBRouteData data = DataIntoDBRouteData(ad, rID ,cID, name); //리스트에 있는 데이터를 dto에 넣어준다
					SaveData(data); //중복되는 데이터가 없으면 저장 한다.					
					rs = st.executeQuery("SELECT * FROM Route where cid='"+cID+"'"); //회원의 전체 리스트를 봐본다
					while (rs.next()) {
						String str = rs.getNString(1);
						System.out.println(str);
					}
				}
				rs.close();
				st.close();
				connection.close();
			}
		} catch (SQLException SQLex) {
			System.out.println("CheckSameData 데이터베이스 에러");
			System.out.println("SQLException: " + SQLex.getMessage());
		}
		return "1";
	}
	
	//리트스에 있는 데이터를 DTO에 넣어준다. CheckSameData - >DataIntoDBRouteData -> SaveData 순서로
	private DBRouteData DataIntoDBRouteData(LinkedList<Address> ad,String rID,String cID, String name) {
		int size = ad.size();
		DBRouteData tmpData = new DBRouteData(rID, cID);
		tmpData.setName(name);
		tmpData.setDataSize(size);
		
		System.out.println("DataIntoDBRouteData의 경로 개수 : " + size);
		for(int i =0;i<size;i++) {
			tmpData.setAddress(i,ad.get(i).getAddress());
			tmpData.setLat(i, ad.get(i).getLat());
			tmpData.setLng(i, ad.get(i).getLng());
		}		
		return tmpData;
	}
	
	public void SaveData(DBRouteData data) { //데이터 저장		
		String sqlStr = "INSERT INTO route VALUES('"
		        +data.getRid()+"',"+data.getDatasize()+",'"+data.getCid()+"','"+data.getName()+"','"
		        +data.getAddress(0)+"',"+ data.getLat(0) +","+data.getLng(0)+",'"
		        +data.getAddress(1)+"',"+ data.getLat(1) +","+data.getLng(1)+",'"
		        +data.getAddress(2)+"',"+ data.getLat(2) +","+data.getLng(2)+",'"
		        +data.getAddress(3)+"',"+ data.getLat(3) +","+data.getLng(3)+",'"
		        +data.getAddress(4)+"',"+ data.getLat(4) +","+data.getLng(4)+",'"
		        +data.getAddress(5)+"',"+ data.getLat(5) +","+data.getLng(5)+",'"
		        +data.getAddress(6)+"',"+ data.getLat(6) +","+data.getLng(6)+")";
		System.out.println("Route DB에 데이터를 삽입합니다.: " + sqlStr);
		try {
			connection = ds.getConnection();
			st = connection.createStatement();
			st.executeUpdate(sqlStr);
			rs.close();
			st.close();
			connection.close();
		} catch (SQLException SQLex) {
			System.out.println("saveData 데이터 안들어감 문제발생!!!!!!");
			//System.out.println("SQLException: " + SQLex.getMessage());
			//System.out.println("SQLState: " + SQLex.getSQLState());
		}	
	}
	
	public void SaveRoute2Data(DBRoute2Data tmp) { //car_html, car_xml, car_mark... pt등등의 데이터 저장	
		System.out.println("Route2 DB에 데이터를 삽입합니다.: INSERT INTO route2 VALUES('"+tmp.getRid()+"', '"+tmp.getCid()+"','"+
				tmp.getPt_order()+"', '"+tmp.getCar_order()+"',"+tmp.getSize()+","+tmp.getStart()+","+tmp.getLast()+")");
		try {
			connection = ds.getConnection();
			st = connection.createStatement();
			int Query = st.executeUpdate("INSERT INTO route2 VALUES('"+tmp.getRid()+"', '"+tmp.getCid()+"','"+
			tmp.getPt_order()+"', '"+tmp.getCar_order()+"',"+tmp.getSize()+","+tmp.getStart()+","+tmp.getLast()+")");
					       
			rs.close();
			st.close();
			connection.close();
		} catch (SQLException SQLex) {
			System.out.println("saveRoute2Data 에러");
			System.out.println("SQLException: " + SQLex.getMessage());
			//System.out.println("SQLState: " + SQLex.getSQLState());
		}	
	}
	
	public String GetAllData(String cID) { //모든 데이터 넘겨주기
		String resultStr = "<SaveData>";
		try {	
			System.out.println("db 연동됨");
			String result =resultStr; // 이렇게 해야만 중간에 데이터 안 삭제됨, 왜인진 모르겠다..
			connection = ds.getConnection();
			st = connection.createStatement();
			String sqlStr= "SELECT * FROM route WHERE cid='"+cID+"'";
			System.out.println("GetAllData sqlStr : " + sqlStr);
			rs = st.executeQuery(sqlStr);
			
			while(rs.next()) {
				result += "<Data>";
				result += "<rID>" + rs.getString(1) + "</rID>";
				int size = rs.getInt(2); //사이즈
				result += "<size>" + Integer.toString(size) + "</size>";
				result += "<name>" + rs.getString(4) + "</name>";
				int addressCnt = 5;
				for(int i = 0;i<size;i++) {
					result += "<address"+Integer.toString(i)+">"+ rs.getString(addressCnt) +"</address"+Integer.toString(i)+">";
					addressCnt += 3;
				}
				result += "</Data>";
			}	
			resultStr = result + "</SaveData>";
			rs.close();
			st.close();
			connection.close();
		} catch (SQLException SQLex) {
			System.out.println("DB 연동 안됨");
			System.out.println("SQLException: " + SQLex.getMessage());
			System.out.println("SQLState: " + SQLex.getSQLState());
		}
		//System.out.println("result2 : " + resultStr);
		return resultStr;
	}
	
	public int DeleteData(String rID,String cID) { //삭제
		System.out.println("DB삭제");
		try {		
			connection = ds.getConnection();
			st = connection.createStatement();
			st.executeUpdate("DELETE FROM route WHERE rid='"+rID+"' AND cid='"+cID+"'");	
			rs = st.executeQuery("SELECT * FROM route where cid='"+cID+"'"); //회원의 전체 리스트를 봐본다 삭제 됬는지 검사
			rs.close();
			st.close();
			connection.close();
			return 1; // db 입력 성공 
		} catch (SQLException SQLex) {
			System.out.println("삭제 실패");
			System.out.println("SQLException: " + SQLex.getMessage());
			System.out.println("SQLState: " + SQLex.getSQLState());
		}
		return 0; // db 입력 실패
	}
	
	 //rid로 찾아서 넘길때 - > AddressDataManager에 list에 데이터를 삽입해주기 위해 DBRouteData에 데이터를 넣어 return;
	public DBRouteData CallDBData_INDEX(String rID,String cID) {		
		System.out.println(rID +", " + cID);
		DBRouteData tmpIndex = new DBRouteData(rID, cID);
		try {	
			connection = ds.getConnection();
			st = connection.createStatement();
			String sqlStr = "SELECT * FROM route WHERE cid='"+cID+"' AND rid='"+rID+"'";
			System.out.println("CallDBData_INDEX : " + sqlStr);
			rs = st.executeQuery(sqlStr);		
			while(rs.next()) {
				int size = rs.getInt(2);
				tmpIndex.setDataSize(size);
				int addressCnt = 5;
				int latCnt = 6, lngCnt = 7;
				for(int i =0;i<size;i++) {
					tmpIndex.setAddress(i, rs.getString(addressCnt));
					tmpIndex.setLat(i, Double.parseDouble(rs.getString(latCnt)));
					tmpIndex.setLng(i, Double.parseDouble(rs.getString(lngCnt)));
					addressCnt += 3;
					latCnt += 3;
					lngCnt += 3;			
				}			
			}
			rs.close();
			st.close();
			connection.close();
		}catch (SQLException SQLex) {
			System.out.println("CallDBData_INDEX 오류발생");
			System.out.println("SQLException: " + SQLex.getMessage());
		}					
		return tmpIndex;
	}
	
	public Route2DataCall GetSavedRoute2Data(String cID,String rID) { //로딩 페이지에서 route2에 있는 모든 데이터 DATATOAL DTO로 넘겨주기		
		Route2DataCall result = new Route2DataCall();
		try {	
			connection = ds.getConnection();
			st = connection.createStatement();
			String sqlStr = "SELECT * FROM route2 WHERE cid='"+cID+"' AND rid='"+ rID+"'";
			rs = st.executeQuery(sqlStr);	
			System.out.println("GetSavedRoute2Data : " + sqlStr);
			while(rs.next()) {
				result.pushData(rs.getString(3), rs.getString(4), rs.getInt(5), rs.getInt(6), rs.getInt(7));
			}
			rs.close();
			st.close();
			connection.close();
		} catch (SQLException SQLex) {
			System.out.println("GetSavedRoute2Data 검색 에러");
			System.out.println("SQLException: " + SQLex.getMessage());
			System.out.println("SQLState: " + SQLex.getSQLState());
		}
		return result;
	}	
}