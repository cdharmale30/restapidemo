package com.test.testcases;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.base.TestBase;
import com.test.client.RestClient;
import com.test.data.Users;

public class POSTAPITest extends TestBase {
	TestBase testBase;
	String apiUrl;
	String serviceUrl;
	String url;
	RestClient restClient;
	CloseableHttpResponse closebaleHttpResponse;

	@BeforeMethod
	public void setUp() throws ClientProtocolException, IOException {
		testBase = new TestBase();
		serviceUrl = prop.getProperty("URL");
		apiUrl = prop.getProperty("serviceURL");

		url = serviceUrl + apiUrl;
	}

	@Test
	public void postAPITest() throws JsonGenerationException, JsonMappingException, IOException {
		restClient = new RestClient();
		HashMap<String, String> headerMap = new HashMap<String, String>();

		headerMap.put("Content-Type", "application/json");

		// jackson api
		ObjectMapper mapper = new ObjectMapper();
		Users users = new Users("morpheus", "leader");

		mapper.writeValue(new File("I:\\All Eclipse Code6\\RestAPIDemo\\src\\main\\java\\com\\test\\data\\users.json"),
				users);

		// object to json string
		String userJsonString = mapper.writeValueAsString(users);
		System.out.println(userJsonString);

		closebaleHttpResponse = restClient.post(url, userJsonString, headerMap); // call the API
		// Status code
		int statusCode = closebaleHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status Code--->" + statusCode);
		Assert.assertEquals(statusCode, testBase.RESPONSE_STATUS_CODE_201, "Status code is not 200");
		String responseString = EntityUtils.toString(closebaleHttpResponse.getEntity(), "UTF-8");

		JSONObject responseJson = new JSONObject(responseString);
		System.out.println("The response from API is:" + responseJson);

		Users usersobj = mapper.readValue(responseString, Users.class);
		System.out.println(usersobj);

		Assert.assertTrue(users.getName().equals(usersobj.getName()));

		Assert.assertTrue(users.getJob().equals(usersobj.getJob()));

		System.out.println(usersobj.getId());
		System.out.println(usersobj.getCreatedAt());
	}

}
