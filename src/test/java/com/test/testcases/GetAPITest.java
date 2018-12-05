package com.test.testcases;

import java.io.IOException;
import java.util.HashMap;

import org.apache.http.Header;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.test.base.TestBase;
import com.test.client.RestClient;
import com.test.util.TestUtil;

public class GetAPITest extends TestBase {
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

	// get API without headers

	@Test(priority = 2)
	public void getAPITest() throws ClientProtocolException, IOException {
		restClient = new RestClient();
		closebaleHttpResponse = restClient.get(url);

		// Status code
		int statusCode = closebaleHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status Code--->" + statusCode);
		Assert.assertEquals(statusCode, RESPONSE_STATUS_CODE_200, "Status code is not 200");

		// return closehttpresponse;
		System.out.println("The Status Code is" + statusCode);

		// Json String
		String responseString = EntityUtils.toString(closebaleHttpResponse.getEntity(), "UTF-8");
		JSONObject responseJsonObject = new JSONObject(responseString);
		System.out.println("The response json object" + responseJsonObject);

		// single value assertion:
		// per_page:
		String perPageValue = TestUtil.getValueByJPath(responseJsonObject, "/per_page");
		System.out.println("value of per page is-->" + perPageValue);
		Assert.assertEquals(Integer.parseInt(perPageValue), 3);

		// total:
		String totalValue = TestUtil.getValueByJPath(responseJsonObject, "/total");
		System.out.println("value of total is-->" + totalValue);
		Assert.assertEquals(Integer.parseInt(totalValue), 12);

		// for total pages
		String totalPagesValue = TestUtil.getValueByJPath(responseJsonObject, "/total_pages");
		System.out.println("value of total is-->" + totalPagesValue);
		Assert.assertEquals(Integer.parseInt(totalPagesValue), 4);

		// for page
		String pages = TestUtil.getValueByJPath(responseJsonObject, "/page");
		System.out.println("value of total is-->" + pages);
		Assert.assertEquals(Integer.parseInt(pages), 1);

		// get the value from JSON ARRAY:
		String lastName = TestUtil.getValueByJPath(responseJsonObject, "/data[0]/last_name");
		String id = TestUtil.getValueByJPath(responseJsonObject, "/data[0]/id");
		String avatar = TestUtil.getValueByJPath(responseJsonObject, "/data[0]/avatar");
		String firstName = TestUtil.getValueByJPath(responseJsonObject, "/data[0]/first_name");

		System.out.println(lastName);
		System.out.println(id);
		System.out.println(avatar);
		System.out.println(firstName);

		Assert.assertEquals(lastName, "Bluth");
		Assert.assertEquals(id, "1");
		Assert.assertEquals(firstName, "George");
		Assert.assertEquals(avatar, "https://s3.amazonaws.com/uifaces/faces/twitter/calebogden/128.jpg");

		// All Header
		Header[] headerArray = closebaleHttpResponse.getAllHeaders();
		HashMap<String, String> allHeaders = new HashMap<String, String>();
		for (Header header : headerArray) {
			allHeaders.put(header.getName(), header.getValue());
			System.out.println("Header Array" + allHeaders);
		}
	}

	@Test(priority = 2)
	public void getAPIWithoutTest() throws ClientProtocolException, IOException {
		restClient = new RestClient();

		restClient = new RestClient();
		HashMap<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("Content-Type", "applica");
		closebaleHttpResponse = restClient.get(url);

	}
}
