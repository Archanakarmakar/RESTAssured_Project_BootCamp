package userAPI.test;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import io.restassured.response.Response;
import userAPI.RequestResponsePayload.Address;
import userAPI.RequestResponsePayload.User;
import userAPI.enpoints.UserCRUD_opearation1;
import userAPI.utilities.DataProviders;

public class UserAPI {
	

		Faker faker = new Faker();
		User userPayload =  new User();
		Address address = new Address();
		public String auth_username = UserCRUD_opearation1.auth_username;
		public String auth_pwd = UserCRUD_opearation1.auth_pwd;
		public Logger logger = LogManager.getLogger(this.getClass());
		long responseTime;
		public String contentType;
		public static String CreatedUserID;
		public static String CreatedFirstName;
		public static String CreatedSecondName;
		public SoftAssert sa = new SoftAssert();


		@BeforeMethod
		public void setup() throws JsonMappingException, JsonProcessingException {
			logger.info("-------------Setting up the User Details---------");

		}


		@Test (priority = 1, dataProvider="PostUser", dataProviderClass=DataProviders.class)
		public void testPostUser(String ContentType, String Username, String pwd, String FirstName, String LastName, String ContactNo, String EmailID, String PlotNo, String Street, String State, String Country, String ZipCode, String StatusCode, String Scenario) throws JsonMappingException, JsonProcessingException
		{
			//------------------Set Up USER PAYLOAD to create new User Details-------------------------
			address.setPlotNumber(PlotNo);
			address.setStreet(Street);
			address.setState(State);
			address.setZipCode(ZipCode);
			address.setCountry(Country);

			
			userPayload.setUserAddress(address);
			userPayload.setUser_first_name(FirstName);
			userPayload.setUser_last_name(LastName);
			userPayload.setUser_contact_number(ContactNo);
			userPayload.setUser_email_id(EmailID);

//			

			//------------------------Invalid Authorization Setup-----------------------
			if(pwd.equalsIgnoreCase("Invalid")){
				auth_pwd = pwd;}
			else if(Username.equalsIgnoreCase("Invalid")){
				auth_username= Username;}
			else if(Username.equalsIgnoreCase("noAuth")) {
				auth_username="";
				auth_pwd = "";}

			logger.info("-------------Creating new User---------");

			//Response responseFirst = UserEndpointsNumpyDDT.createUser(auth_username, auth_pwd, ContentType, userPayload);
			Response response = UserCRUD_opearation1.createUser(auth_username, auth_pwd, ContentType, userPayload);

			response.then().log().all();
			System.out.println("The Scenario tested is: "+Scenario);

			//-------------------------------Assertions-----------------------------------
			if (response.getStatusCode() == 201) {
				System.out.println("The Expected Status is: "+StatusCode);
				System.out.println("The Actual Status is: "+response.getStatusCode());

				CreatedUserID = Integer.toString(response.jsonPath().getInt("user_id"));
				CreatedFirstName = response.jsonPath().getString("user_first_name");
				System.out.println("Created User ID in Post is: "+CreatedUserID);
				System.out.println("Created User First Name in Post is: "+CreatedFirstName);

				//-------- Convert the response body to a JSON Node-------------------
				
				ObjectMapper objectMapper = new ObjectMapper();
				JsonNode actualResponse = objectMapper.readTree(response.getBody().asString());

				// Verify the response matches the expected schema
				boolean schemaMatches = UserCRUD_opearation1.verifySchema(actualResponse, UserCRUD_opearation1.expectedSchemaDefinition());

				sa.assertEquals(response.getStatusCode(), Integer.parseInt(StatusCode)); // check why this isnt a duplicate
				sa.assertEquals(response.getContentType(),"application/json");
				sa.assertTrue(schemaMatches);
				sa.assertAll();
			}
			else {
				System.out.println("The Expected Status is: "+StatusCode);
				System.out.println("The Actual Status is: "+response.getStatusCode());
				sa.assertEquals(response.getStatusCode(),Integer.parseInt(StatusCode));
				sa.assertAll();
			}

			//--------Reset Authorization User name and Password---------
			auth_username = UserCRUD_opearation1.auth_username;
			auth_pwd =UserCRUD_opearation1.auth_pwd;

			logger.info("-------------Created new User successfully---------");

		}


		@Test(priority = 2, dataProvider = "GetAllUsers", dataProviderClass = DataProviders.class)
		public void testGetAllUsers(String StatusCode, String Status, String Username, String pwd, String Comments) {
			logger.info("-------------Get ALL User Details---------");

			// Invalid Authorization Setup
			if (pwd.equalsIgnoreCase("Invalid")) {
				auth_pwd = pwd;
			} else if (Username.equalsIgnoreCase("Invalid")) {
				auth_username = Username;
			} else if (Username.equalsIgnoreCase("noAuth")) {
				auth_username = "";
				auth_pwd = "";
			}

			// Call API
			Response response = UserCRUD_opearation1.getAllUsers(auth_username, auth_pwd);
			response.then().log().all();

			System.out.println("The Scenario tested is: " + Comments);
			System.out.println("The Expected Status is: " + StatusCode);
			System.out.println("The Actual Status is: " + response.getStatusCode());

			// Debug: Print full JSON response
			System.out.println("Response JSON: " + response.getBody().asString());

			// Assertions
			if (response.getStatusCode() == 200) {
				int UsersCount = response.jsonPath().getList("users").size();  // ✅ Fix JSON Path
				System.out.println("Total Number of Users are: " + UsersCount);

				sa.assertEquals(response.getStatusCode(), Integer.parseInt(StatusCode));
				sa.assertEquals(response.getContentType(), "application/json");
				sa.assertAll();
			} else {
				sa.assertEquals(response.getStatusCode(), Integer.parseInt(StatusCode));
				sa.assertAll();
			}

			// Reset Authorization Credentials
			auth_username =UserCRUD_opearation1.auth_username;
			auth_pwd = UserCRUD_opearation1.auth_pwd;

			logger.info("-------------Got All Users Details successfully---------");
		}


		@Test (priority =3,dataProvider="GetUserById", dataProviderClass=DataProviders.class)
		public void GetUserById(String UserID,String StatusCode, String Status,String Username,String pwd, String Scenario) throws JsonMappingException, JsonProcessingException {
			logger.info("--------Get User By Id--------");

			//------------------------Invalid Authorization Setup-----------------------
			if(pwd.equalsIgnoreCase("Invalid")){
				auth_pwd = pwd;}
			else if(Username.equalsIgnoreCase("Invalid")){
				auth_username= Username;}
			else if(Username.equalsIgnoreCase("noAuth")) {
				auth_username="";
				auth_pwd = "";}

			if(Integer.parseInt(StatusCode)==200) {
				UserID = CreatedUserID;
				System.out.println("User Id in GetBYID: "+CreatedUserID);
			}

			Response response = UserCRUD_opearation1.getUserById( auth_username, auth_pwd, UserID);
			response.then().log().all();

			System.out.println("The Scenario tested is: "+Scenario);
			System.out.println("The Expected Status is: "+StatusCode);
			System.out.println("The Actual Status is: "+response.getStatusCode());

			//-------------------------------Assertions--------------------------------
			if(Integer.parseInt(StatusCode)==200){
				//-------- Convert the response body to a JSON Node-------------------
				ObjectMapper objectMapper = new ObjectMapper();
				JsonNode actualResponse = objectMapper.readTree(response.getBody().asString());

				// Verify the response matches the expected schema
				boolean schemaMatches = UserCRUD_opearation1.verifySchema(actualResponse, UserCRUD_opearation1.expectedSchemaDefinition());

				sa.assertEquals(response.getStatusCode(),Integer.parseInt(StatusCode));
				sa.assertEquals(response.jsonPath().getInt("user_id"),Integer.parseInt(UserID));
				sa.assertEquals(response.getContentType(),"application/json");
				sa.assertTrue(schemaMatches);
				sa.assertAll();
			}
			else {
				sa.assertEquals(response.getStatusCode(),Integer.parseInt(StatusCode));
				sa.assertAll();}

			//--------Reset Authorization User name and Password---------
			auth_username = UserCRUD_opearation1.auth_username;
			auth_pwd =UserCRUD_opearation1.auth_pwd;

			logger.info("-------------Get User by User ID tested successfully---------");
		}


		@Test(priority=4, dataProvider="GetUserByFirstName",dataProviderClass = DataProviders.class)
		public void GetUserByFirstName(String FirstName, String Path, String Endpoint, String StatusCode, String Status, String Username, String pwd, String Scenario) throws JsonMappingException, JsonProcessingException{

			logger.info("----------Testing Get User By First Name-----");

			//------------------------Invalid Authorization Setup-----------------------
			if(pwd.equalsIgnoreCase("Invalid")){
				auth_pwd = pwd;}
			else if(Username.equalsIgnoreCase("Invalid")){
				auth_username= Username;}
			else if(Username.equalsIgnoreCase("noAuth")) {
				auth_username="";
				auth_pwd = "";}

			if(Integer.parseInt(StatusCode)==200) {FirstName = CreatedFirstName; System.out.println("User Name in GetBYFirstName is: "+CreatedFirstName);}

			Response response = UserCRUD_opearation1.getUserByUserName(Path, Endpoint, auth_username, auth_pwd, FirstName);
			response.then().log().all();

			System.out.println("The Scenario tested is: "+Scenario);
			System.out.println("The Expected Status is: "+StatusCode);
			System.out.println("The Actual Status is: "+response.getStatusCode());

			//---------------------Assertions-----------------------------
			if(Integer.parseInt(StatusCode)==200){
				sa.assertEquals(response.getStatusCode(),Integer.parseInt(StatusCode),"The Status Code is incorrect");
				sa.assertEquals(response.jsonPath().getString("user_first_name[0]"),FirstName,"The first name is not correct");
				sa.assertEquals(response.getContentType(),"application/json","The content type is mismatching....");
				sa.assertAll();}
			else {sa.assertEquals(response.getStatusCode(),Integer.parseInt(StatusCode),"The Status Code is incorrect");
				sa.assertAll();}

			//--------Reset Authorization User name and Password---------
			auth_username = UserCRUD_opearation1.auth_username;
			auth_pwd =UserCRUD_opearation1.auth_pwd;

			logger.info("-------------Get User by user First Name tested successfully---------");
		}


		@Test(priority=5,dataProvider="UpdateUserById", dataProviderClass=DataProviders.class)
		public void UpdateUserById(String Endpoint, String UserID, String ContentType, String Username, String pwd, String FirstName, String LastName,String ContactNo,String EmailID, String PlotNo, String Street, String State, String Country, String ZipCode, String StatusCode, String Scenario) {

			logger.info("---------Update User Details----------");

			//--------------Set Up USER PAYLOAD to UPDATE------------------
			address.setPlotNumber(PlotNo);
			address.setStreet(Street);
			address.setState(State);
			address.setZipCode(ZipCode);
			address.setCountry(Country);

			userPayload.setUser_first_name(FirstName);
			userPayload.setUser_last_name(LastName);
			userPayload.setUserAddress(address);
			if(Integer.parseInt(StatusCode)==200){
				EmailID = faker.internet().safeEmailAddress();
				userPayload.setUser_email_id(EmailID);
				ContactNo="3"+RandomStringUtils.randomNumeric(9);
				userPayload.setUser_contact_number(ContactNo);
				UserID = CreatedUserID; System.out.println("User Id in UPDATE BY ID: "+CreatedUserID);}
			else{
				userPayload.setUser_contact_number(ContactNo);
				userPayload.setUser_email_id(EmailID);}

			//------------------------Invalid Authorization Setup-----------------------
			if(pwd.equalsIgnoreCase("Invalid")){
				auth_pwd = pwd;}
			else if(Username.equalsIgnoreCase("Invalid")){
				auth_username= Username;}
			else if(Username.equalsIgnoreCase("noAuth")) {
				auth_username="";
				auth_pwd = "";}

			logger.info("-------------Updating the User Details---------");
			Response response = UserCRUD_opearation1.updateUserByUserId(Endpoint,UserID,ContentType,auth_username,auth_pwd,userPayload);
			response.then().log().all();

			System.out.println("The Scenario tested is: "+Scenario);
			System.out.println("The Expected Status is: "+StatusCode);
			System.out.println("The Actual Status is: "+response.getStatusCode());

			if (Integer.parseInt(StatusCode)==200) {
				sa.assertEquals(response.getStatusCode(), Integer.parseInt(StatusCode));
				sa.assertEquals(response.getBody().jsonPath().getString("user_last_name"), LastName);
				sa.assertEquals(response.getBody().jsonPath().getString("user_email_id"), EmailID);
				sa.assertEquals(response.getBody().jsonPath().getString("user_contact_number"),ContactNo);
				sa.assertEquals(response.getContentType(),"application/json");
				sa.assertAll();}
			else{
				sa.assertEquals(response.getStatusCode(), StatusCode);
			}

			//--------Reset Authorization User name and Password---------
			auth_username = UserCRUD_opearation1.auth_username;
			auth_pwd =UserCRUD_opearation1.auth_pwd;

			logger.info("---------Updated User By ID Successfully-------");

		}


		@Test(priority =6,dataProvider="DeleteUserById", dataProviderClass=DataProviders.class)
		public void DeleteUserByID(String Endpoint, String UserId, String Username, String pwd,String StatusCode,String Scenario) {

			logger.info("-------Delete User By ID-------------");

			//------------------------Invalid Authorization Setup-----------------------
			if(pwd.equalsIgnoreCase("Invalid"))
				auth_pwd = pwd;
			else if(Username.equalsIgnoreCase("Invalid"))
				auth_username= Username;
			else if(Username.equalsIgnoreCase("noAuth"))
			{auth_username="";
				auth_pwd = "";}
			if(Integer.parseInt(StatusCode)==200) {UserId = CreatedUserID; System.out.println("The User ID deleted is: "+CreatedUserID);}

			Response response = UserCRUD_opearation1.deleteUserById(Endpoint,UserId,auth_username,auth_pwd );
			response.then().log().all();
			System.out.println("The Auth details are: "+auth_username+" "+auth_pwd);
			System.out.println("The Scenario tested is: "+Scenario);
			System.out.println("The Expected Status is: "+StatusCode);
			System.out.println("The Actual Status is: "+response.getStatusCode());

			//-------------------------Assertions-----------------------------------------
			if(Integer.parseInt(StatusCode)==200)
				Assert.assertEquals(response.getStatusCode(),Integer.parseInt(StatusCode),"The Status Code is incorrect");
			else
				Assert.assertEquals(response.getStatusCode(),Integer.parseInt(StatusCode),"The Status Code is incorrect");

			//--------Reset Authorization User name and Password---------
			auth_username = UserCRUD_opearation1.auth_username;
			auth_pwd =UserCRUD_opearation1.auth_pwd;

			logger.info("---------Deleted User By ID Successfully-------");

		}


		@Test (priority = 7)
		public void testPostUserWithMandatoryDetails(String postEndpoint, String auth_username, String auth_pwd, String contentType ) throws JsonProcessingException
		{
			address.setPlotNumber(null);
			address.setStreet(null);
			address.setState(null);
			address.setZipCode(null);
			address.setCountry(null);

			userPayload.setUser_first_name(faker.name().firstName());
			userPayload.setUser_last_name(faker.name().lastName());
			String PhoneNo="5"+RandomStringUtils.randomNumeric(9);
			userPayload.setUser_contact_number(PhoneNo);
			userPayload.setUser_email_id(faker.internet().safeEmailAddress());
			userPayload.setUserAddress(address);

			logger.info("-------------Creating new User---------");

			Response response = UserCRUD_opearation1.createUser(auth_username,auth_pwd, contentType,userPayload);
			response.then().log().all();

			System.out.println("The Expected Status is: 201");
			System.out.println("The Actual Status is: "+response.getStatusCode());

			CreatedSecondName = response.getBody().jsonPath().getString("user_first_name");
			System.out.println("The Second Name created is: "+CreatedSecondName);

			//----------------------------Assertions-----------------------
			sa.assertEquals(response.getStatusCode(),201);
			sa.assertEquals(response.getContentType(),"application/json");
			sa.assertAll();

			//--------Reset Authorization User name and Password---------
			auth_username = UserCRUD_opearation1.auth_username;
			auth_pwd =UserCRUD_opearation1.auth_pwd;

			logger.info("-------------Created new User With Mandatory Details alone Successfully---------");
		}


		@Test(priority=8,dataProvider="DeleteUserByFirstName",dataProviderClass=DataProviders.class)
		public void DeleteUserByFirstName(String Path, String Endpoint, String FirstName, String Username, String pwd, String StatusCode, String Scenario) {

			logger.info("--------------Delete User By FirstName--------");

			//------------------------Invalid Authorization Setup-----------------------
			if(pwd.equalsIgnoreCase("Invalid")){
				auth_pwd = pwd;}
			else if(Username.equalsIgnoreCase("Invalid")){
				auth_username= Username;}
			else if(Username.equalsIgnoreCase("noAuth")) {
				auth_username="";
				auth_pwd = "";}
			if(Integer.parseInt(StatusCode)==200) {FirstName = CreatedSecondName;System.out.println("The FirstName Deleted is: "+CreatedSecondName);}

			Response response = UserCRUD_opearation1.deleteUserByFirstName(Path,Endpoint,FirstName,auth_username,auth_pwd );
			response.then().log().all();

			System.out.println("The Scenario tested is: "+Scenario);
			System.out.println("The Expected Status is: "+StatusCode);
			System.out.println("The Actual Status is: "+response.getStatusCode());

			//-------------------------Assertions-----------------------------------------
			if(Integer.parseInt(StatusCode)==200)
				sa.assertEquals(response.getStatusCode(),Integer.parseInt(StatusCode),"The Status Code is incorrect");
			else
				sa.assertEquals(response.getStatusCode(),Integer.parseInt(StatusCode),"The Status Code is incorrect");

			//--------Reset Authorization User name and Password---------
			auth_username = UserCRUD_opearation1.auth_username;
			auth_pwd =UserCRUD_opearation1.auth_pwd;

			logger.info("---------Deleted User By FirstName Successfully-------");
		}

	}




