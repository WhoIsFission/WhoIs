package org.e8.whois.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class WhoIsClient {

	public static String callCommandRestClient(String aCommand){
		StringBuffer response=new StringBuffer();
		try {
			Process process=Runtime.getRuntime().exec("whois "+aCommand);
			BufferedReader bufReader=new BufferedReader(new InputStreamReader(process.getInputStream()));
			String str;
			while((str=bufReader.readLine())!=null){
				response.append(str);
				response.append(System.getProperty("line.separator"));
			}
			process.waitFor();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("IO Exception occured",e);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("Interrupted Exception occured",e);
		}
		return response.toString();
	}
}
