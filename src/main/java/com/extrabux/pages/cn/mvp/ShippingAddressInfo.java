package com.extrabux.pages.cn.mvp;

public class ShippingAddressInfo {

	String userName;
	String email;
	String country;
	String province;
	String city;
	String district;
	String address;
	String zipCode;
	String mobilePhoneNumber;
	String telePhoneNumber;
	
	public ShippingAddressInfo(String userName,String email, String country,String province,
			String city,String district,String address,String zipCode,
			String mobilePhoneNumber,String telePhoneNumber) 
	{
		this.userName = userName;
		this.email = email;
		this.country = country;
		this.province = province;
		this.city = city;
		this.district = district;
		this.address = address;
		this.zipCode = zipCode;
		this.mobilePhoneNumber = mobilePhoneNumber;
		this.telePhoneNumber = telePhoneNumber;
	}
	
	public String getUserName(){
		return userName;
	}
	
	public String getEmail(){
		return email;
	}
	
	public String getDetailedAddress(){
		return province + " " + city + "市 " + district + " " + address;
	}
	
	public String getZipCode(){
		return zipCode;
	}
	
	public String getMobilePhoneNumber(){
		return mobilePhoneNumber;
	}
	
	public String getTelePhoneNumber(){
		return telePhoneNumber;
	}
	
}
