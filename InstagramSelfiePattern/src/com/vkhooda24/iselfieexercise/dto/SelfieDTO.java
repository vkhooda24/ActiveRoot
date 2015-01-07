package com.vkhooda24.iselfieexercise.dto;

import android.graphics.Bitmap;

/**
 * File Info:-
 * File Name	: SelfieData.java
 * Created By	: VK Hooda
 * Date			: 16-Dec-2014
 * About File	: 
 */

public class SelfieDTO {
	
	String photoID="";
	String userName="",photoUrl="";
	Bitmap bitmap=null;
	
	public void setPhotoID(String id){
		this.photoID=id;
	}
	
	public String getPhotoID() {
	       return photoID;
	}
	
	public void setUserName(String name) {
	       this.userName = name;
	}

    public String getUserName() {
       return userName;
    }
	    
    public void setPhotoUrl(String url) {
	       this.photoUrl = url;
	}

    public String getPhotoUrl() {
       return photoUrl;
    }
    
	public void setBitmap(Bitmap bitmap){
		this.bitmap=bitmap;
	}
	
	public Bitmap getBitmap(){
		return bitmap;
	}

}
