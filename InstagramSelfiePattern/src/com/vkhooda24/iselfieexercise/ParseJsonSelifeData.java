package com.vkhooda24.iselfieexercise;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vkhooda24.iselfieexercise.dto.SelfieDTO;
import com.vkhooda24.iselfieexercise.utilities.UtilityConstants;

/**
 * File Info:-
 * File Name	: FetchParseServerData.java
 * Created By	: VK Hooda
 * Date			: 24-Dec-2014
 * About File	: 
 */

public class ParseJsonSelifeData {

	public static List<SelfieDTO> parseSelfieData(String serverData) {

		List<SelfieDTO> selfieDataList=new ArrayList<SelfieDTO>();
		
		try {
			JSONObject dataObj	= new JSONObject(serverData);
			if (dataObj.length()!=0) 
			{
		    	JSONArray dataArray	= (JSONArray)dataObj.getJSONArray(UtilityConstants.DATA);
		    	for(int i=0; i < dataArray.length(); i++){ 
		    		
		    		//To catch exception for one element of loop.
		    		try {
		    			
		    			JSONObject arryDataObj	= (JSONObject) dataArray.get(i);
			    		//User Data
			    		JSONObject userObj		= arryDataObj.getJSONObject(UtilityConstants.USER);
			    		String userName			= userObj.getString(UtilityConstants.USERNAME);
			    		//Photo id,url data
			    		String photoID			= arryDataObj.getString(UtilityConstants.PHOTOID);
			    		JSONObject imageObj		= arryDataObj.getJSONObject(UtilityConstants.IMAGES);
			    		JSONObject thumbImgObj	= imageObj.getJSONObject(UtilityConstants.THUMBNAIL) ;
			    		JSONObject lowImgObj	= imageObj.getJSONObject(UtilityConstants.LOW_RESOLUTION);
			    		String imgUrl="";
			    		if(i % 3 >0)//Big Size photo
			    			 imgUrl		= thumbImgObj.getString(UtilityConstants.URL);
			    		else
			    			 imgUrl		= lowImgObj.getString(UtilityConstants.URL);
			    		
			    		//Selife data 
			    		SelfieDTO selfieData=new SelfieDTO();
			    		selfieData.setPhotoID(photoID);
			    		selfieData.setUserName(userName);
			    		selfieData.setPhotoUrl(imgUrl);
			    		
			    		//Add data to list
			    		selfieDataList.add(selfieData);
			    		
					} catch (JSONException e) {
							e.printStackTrace();
					}
		    	}
			} 
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return selfieDataList;
	}

}
