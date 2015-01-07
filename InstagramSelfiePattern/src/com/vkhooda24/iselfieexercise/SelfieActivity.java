package com.vkhooda24.iselfieexercise;

/**
 * File Info:-
 * File Name	: SelfieActivity.java
 * Created By	: VK Hooda
 * Date			: 16-Dec-2014
 * About File	: Get Selfie list and Enlarge Image on tap
 */

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.vkhooda24.iselfieexercise.dto.SelfieDTO;
import com.vkhooda24.iselfieexercise.utilities.UtilityConstants;
import com.vkhooda24.iselfieexercise.utilities.UtilityMethods;

@SuppressLint({ "NewApi", "ClickableViewAccessibility" })
public class SelfieActivity extends FragmentActivity{
	
	String TAG	= "SelfieActivity";
	boolean animZoomInFlag=false;
	List<SelfieDTO> selfieDataList;
	  
	Animation animZoomIn, animZoomOut; 
	 
	LinearLayout statusLiLayout,containerLiLayout;
	ScrollView scrollView;
	ProgressBar	progressBarView;
	TextView 	statusTextView;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Hide top title bar from the application
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//Set UI for the activity
		setContentView(R.layout.selfie_list);
		//Views Initialization
		initViewID();
		//making server data call
		serverDataFetching();
	}

	//Views Initialization
	private void initViewID() {

		statusLiLayout	= (LinearLayout) findViewById(R.id.statusLiLayoutID);
		containerLiLayout=(LinearLayout) findViewById(R.id.containerLiLayoutID);
		scrollView		= (ScrollView) findViewById(R.id.scrollViewID);
		progressBarView	= (ProgressBar) findViewById(R.id.progressBarViewID);
		statusTextView	= (TextView) findViewById(R.id.statusTextViewID);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		Log.i(TAG, "onStart");
	}
	 
	@Override
	protected void onResume() {
		super.onResume();
		Log.i(TAG, "onResume");
	}
	
	private void serverDataFetching() {
		//Check network connection & Getting Data from server
		boolean netConnected=UtilityMethods.checkNetworkConnectivity(getBaseContext());
		if (netConnected) 
			new SelfieTagDataRequest().execute();
		else {
			progressBarView.setVisibility(View.GONE);
			statusTextView.setText(R.string.internetnotavailbale);
		}
	}

	//SelfieTagDataRequest:Data fetch using Instagram API call 
	private class SelfieTagDataRequest extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			statusLiLayout.setVisibility(View.VISIBLE);
			statusTextView.setText(R.string.loadingphotos);
		}
		@Override
		protected Void doInBackground(Void... params) {
			//Fetch data from server 
			String url = UtilityConstants.BASE_URL;
			String data = UtilityMethods.fetchTagData(url);
			 
			//Parse json data
			selfieDataList=new ArrayList<SelfieDTO>();
			if (!data.equalsIgnoreCase(""))
			selfieDataList=ParseJsonSelifeData.parseSelfieData(data);
			
			return null;
		}
		 
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			
			if (selfieDataList.isEmpty()){
				progressBarView.setVisibility(View.GONE);
				statusTextView.setText(R.string.tagdataempty);
			}else
			{
				if (selfieDataList.size()>0) 
					new PhotoLoadTask().execute();
			}
		}
	}

	//PhotoLoadTask: Convert image_url to bitmap and bind with imageview
	private	class PhotoLoadTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			
				String imageURL="";
					for(int i=0; i< selfieDataList.size(); i++ ){
						imageURL=selfieDataList.get(i).getPhotoUrl();
						Bitmap bitmap=getBitmapData(imageURL,selfieDataList.get(i).getBitmap());
						if (bitmap!=null) 
							selfieDataList.get(i).setBitmap(bitmap);
				}
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			bindDataToView(selfieDataList);
		}
		
		private Bitmap getBitmapData(String imageURL, Bitmap bitmapAvaiable) {
			Bitmap bitmap=null;
				if (!imageURL.equalsIgnoreCase("") && bitmapAvaiable==null) 
					 bitmap=UtilityMethods.tagImageBitmap(imageURL);
			return bitmap;
		}
	}
	
	OnTouchListener tapToEnlargePhoto = new OnTouchListener() {
		
		@Override
		public boolean onTouch(final View v, MotionEvent event) {		
			int eventaction = event.getAction();
		    switch (eventaction) {
		        case MotionEvent.ACTION_DOWN: 
		            break;

		        case MotionEvent.ACTION_MOVE:
		            break;

		        case MotionEvent.ACTION_UP:   
		        	animZoomIn = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoomin);
		    		animZoomOut = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoomout);
		    		Log.e(TAG, v.getId()+"");
		    		if (v.getId() <= 0)
		    		{
		    			animZoomInFlag=false;
						v.startAnimation(animZoomIn);
						v.setId(1);
					}else{
						animZoomInFlag=true;
						v.startAnimation(animZoomOut);
						v.setId(0);
//						v.clearAnimation();
					}
		        	break;
		    }
		    return true; 
		}
	};
	
	
	@Override
	protected void onPause() {
		super.onPause();
		Log.i(TAG, "onPause");
	}
	

	public void bindDataToView(List<SelfieDTO> selfieDataList) {

		if(((LinearLayout) containerLiLayout).getChildCount() > 0) 
		    ((LinearLayout) containerLiLayout).removeAllViews(); 
		for (int position = 0; position < selfieDataList.size(); position++) {
			
			int margin=(int)getResources().getDimension(R.dimen.marginImageView);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			
			TextView textView	= new TextView(getBaseContext());
			ImageView imageView	= new ImageView(getBaseContext());
			layoutParams.gravity=Gravity.CENTER;
			layoutParams.setMargins(margin, margin, margin, margin);
			imageView.setLayoutParams(layoutParams);
	         
	        textView.setText(" Username: "+selfieDataList.get(position).getUserName());
			imageView.setImageBitmap(selfieDataList.get(position).getBitmap());

			containerLiLayout.addView(textView); 
	        containerLiLayout.addView(imageView); 
	        
	        imageView.setOnTouchListener(tapToEnlargePhoto);
	        
	        if (position == selfieDataList.size()-1) 
	        {
	        	statusLiLayout.setVisibility(View.GONE);
				containerLiLayout.setVisibility(View.VISIBLE);
	        }
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.i(TAG, "onStop");
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.i(TAG, "onDestroy");
	}
}
