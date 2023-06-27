package com.akasia.investment.task.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.http.HttpHeaders;

import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.akasia.investment.task.company.CompanyDao;

@Service
public class ApplicationService {
	private Environment environment;
	private CompanyDao companyDao;
	//private Company company;
	private String objectName;
	private String host;
	private String accessToken;
	private int responseCode;

	// Application GET object list API - returns list of Application object
	public JSONArray getObjectList(String objectName) throws IOException, JSONException {
		JSONArray jsonArray = null;
		URL url = new URL(host + objectName);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		//con.setRequestProperty("Authorization", environment.getProperty("app.akasia.access-token"));
		con.setRequestProperty("Authorization", accessToken);

		responseCode = con.getResponseCode();
		if (responseCode == HttpURLConnection.HTTP_OK) { // success
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			String responseString;
			responseString = response.toString();
			JSONObject jsonObject = new JSONObject(responseString);
			jsonArray = jsonObject.getJSONArray("data");
		}
		return jsonArray;
	}
	
	// Application GET object by Id API - returns Application object
	public JSONObject getObject(String objectName, String objectId) throws IOException, JSONException {		
		URL url = new URL(host + objectName + "/" + objectId);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		//con.setRequestProperty("Authorization", environment.getProperty("app.akasia.access-token"));
		con.setRequestProperty("Authorization", accessToken);

		responseCode = con.getResponseCode();
		if (responseCode != HttpURLConnection.HTTP_OK)
			return null;
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		String responseString;
		responseString = response.toString();
		if (responseString.isBlank())
			return null;
		JSONObject jsonObject = new JSONObject(responseString);
        
		return jsonObject;
	}
	
	public JSONObject postObject(String objectName, JSONObject jsonInputObject) throws IOException, JSONException {		
		System.out.println(host + " " + objectName + " " + accessToken);
		URL url = new URL(host + objectName);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("Authorization", accessToken);
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Accept", "application/json");
		con.setDoOutput(true);
		
		try(OutputStream os = con.getOutputStream()) {
		    byte[] input = jsonInputObject.toString().getBytes("utf-8");
		    os.write(input, 0, input.length);			
		}
		
		responseCode = con.getResponseCode();
		if (responseCode != HttpURLConnection.HTTP_OK)
			return null;
		
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		String responseString;
		responseString = response.toString();
		if (responseString.isBlank())
			return null;
		JSONObject jsonOutputObject = new JSONObject(responseString);
        
		return jsonOutputObject;
	}

	public JSONObject postFormObject(String objectName, String inputString) throws IOException, JSONException {		
		System.out.println(host + " " + objectName);
		URL url = new URL(host + objectName);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();

		con.setRequestMethod("POST");
		con.setRequestProperty("Authorization", accessToken);
		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		
		//con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Accept", "application/json");
		con.setDoOutput(true);
		
		try(OutputStream os = con.getOutputStream()) {
		    byte[] input = inputString.getBytes("utf-8");
		    os.write(input, 0, input.length);			
		}
		
		responseCode = con.getResponseCode();

		System.out.println(con.getResponseMessage());
		if (responseCode != HttpURLConnection.HTTP_OK)
			return null;
		
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		String responseString;
		responseString = response.toString();
		if (responseString.isBlank())
			return null;
		JSONObject jsonOutputObject = new JSONObject(responseString);
        
		return jsonOutputObject;
	}
	
	// Returns response code after a recent API call
	public int getResponseCode() {
		return(responseCode);
	}	

	// Constructor for ApplicationService
	// Since application.properties cannot be accessed by ApplicationService, they are obtained by the task
	// and passed as constructor parameter 

	public ApplicationService(Environment environment, CompanyDao companyDao) {
		this.environment = environment;
		this.companyDao = companyDao;
		//this.host = environment.getProperty("app.akasia.host");
		//this.accessToken = environment.getProperty("app.akasia.access-token");
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}	

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}	

}
