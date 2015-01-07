package com.vkhooda24.iselfieexercise.utilities;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.protocol.HTTP;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;


/**
 * File Info:- File Name : UtilityMethods.java Created By : VK Hooda Date :
 * 16-Dec-2014 About File :
 */

@SuppressLint("NewApi")
public class UtilityMethods {
 
	static HttpURLConnection connection = null;

	// Check internet connectivity 
	public static boolean checkNetworkConnectivity(Context context) {

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED)
						return true;

		}
		return false;
	}
 
	// Fetch data from server on API call
	public static String fetchTagData(String urlPath) {

		String dataStr = "";
		try {
			InputStream is = getInputStreamData(urlPath);
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(is, HTTP.UTF_8), 8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				is.close();
				reader.close();
				connection.disconnect();
				dataStr = sb.toString();
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataStr;
	}

	public static Bitmap tagImageBitmap(String imageURL) {
		Bitmap bitmap = null;
		try {
			InputStream is = getInputStreamData(imageURL);
			bitmap = BitmapFactory.decodeStream(is);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	private static InputStream getInputStreamData(String urlPath) {

		InputStream is = null;
		try {

			URL url = new URL(urlPath);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setDoInput(true);
			connection.connect();

			is = connection.getInputStream();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return is;
	}

}
