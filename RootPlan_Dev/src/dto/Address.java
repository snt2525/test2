package dto;

public class Address {
   //����, �浵, ���ּ�, ��
   private double lat; //y
   private double lng; //x
   private String address;
   //private String Si;
   
   public Address(double lat, double lng, String address) {
      this.lat = lat; 
      this.lng = lng;
      this.address = address;
   }
    
   public double getLat() {
      return lat;
   }
   
   public double getLng() {
      return lng;
   }
   
   public String getAddress() {
      return address;
   } 
}