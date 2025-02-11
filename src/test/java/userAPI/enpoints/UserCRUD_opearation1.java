package userAPI.enpoints;

import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import com.fasterxml.jackson.databind.JsonNode;
import userAPI.RequestResponsePayload.User;

	public class UserCRUD_opearation1 {

		public static String auth_username = getURL().getString("username");
		public static String auth_pwd = getURL().getString("password");
		public static Map<String, Object> expectedSchema = new HashMap<>();

		//------------To collect data from Routes.properties file-----------------
		static ResourceBundle getURL() {
			ResourceBundle resRoute = ResourceBundle.getBundle("routesDDT");
			return resRoute;
		}

		//-------------To Create a User---------------
		public static Response createUser(String auth_username, String auth_pwd, String ContentType, User payload) {
			String post_url = getURL().getString("post_url");
			Response response =
					given()
							.auth().basic(auth_username, auth_pwd)
							.contentType(ContentType)
							.accept("application/json")
							.body(payload)

							.when()
							.post(post_url);

			return response;
		}

		//------------To Get All Users--------------
		public static Response getAllUsers(String auth_username, String auth_pwd) {
			String get_all_url = getURL().getString("get_all_users_url");
			Response response =
					given()
							.auth().basic(auth_username, auth_pwd)
							.when()
							.get(get_all_url);
			return response;
		}


		//-----------Get User By User ID------------
		public static Response getUserById(String username, String password, String UserID) {
			String get_user_by_id_url = getURL().getString("get_by_user_id_url");

			Response response =
					given()
							.auth().basic(username, password)
							//.pathParam("userPath", "user")
							.pathParam("userId", UserID)
							.when()
							.get(get_user_by_id_url);

			return response;
		}


		//-----------Get User By User First name------------
		public static Response getUserByUserName(String Path, String endpoint, String username, String password, String firstname) {
			String get_user_by_username_url = getURL().getString("get_by_firstName_url");

			Response response =
					given()
							.auth().basic(username, password)
							.pathParam("path1", Path)
							.pathParam("path2", endpoint)
							.pathParam("userFirstName", firstname)
							.when()
							.get(get_user_by_username_url);

			return response;
		}


		//-----------For Updating User By UserID----------
		public static Response updateUserByUserId(String endpoint, String userID, String ContentType, String username, String password, User payload) {
			String update_by_user_id_url = getURL().getString("update_url");
			Response response =
					given()
							.auth().basic(username, password)
							.pathParam("endpoint", endpoint)
							.pathParam("userId", userID)
							.contentType(ContentType)
							.accept("application/json")
							.body(payload)
							.when()
							.put(update_by_user_id_url);

			return response;
		}


		//------------Delete User By User ID-----------------------
		public static Response deleteUserById(String Endpoint, String userID, String username, String pwd) {
			String delete_url = getURL().getString("delete_user_by_id_url");

			Response response =
					given()
							.auth().basic(username, pwd)
							.pathParam("endpoint", Endpoint)
							.pathParam("userId", userID)

							.when()
							.delete(delete_url);

			return response;
		}


		//------------Delete User By User First Name-----------------------
		public static Response deleteUserByFirstName(String Path, String Endpoint, String FirstName, String username, String password) {
			String delete_url = getURL().getString("delete_user_by_firstname_url");

			Response response =
					given()
							.auth().basic(username, password)
							.pathParam("userFirstName", FirstName)

							.when()
							.delete(delete_url);

			return response;
		}


		//-------------Method to verify the SCHEMA of the Response body----------------
		public static boolean verifySchema(JsonNode actualResponse, Map<String, Object> expectedSchema) {
			for (Map.Entry<String, Object> entry : expectedSchema.entrySet()) {
				String key = entry.getKey();
				Object expectedValue = entry.getValue();

				if (actualResponse.has(key)) {
					JsonNode actualValue = actualResponse.get(key);
					if (expectedValue instanceof Map) {
						if (!actualValue.isObject() || !verifySchema(actualValue, (Map<String, Object>) expectedValue)) {
							return false;
						}
					} else {
						if (!checkType(actualValue, expectedValue)) {
							return false;
						}
					}
				} else {
					return false; // Key not found in actual response
				}
			}
			return true; // All checks passed
		}

		public static boolean checkType(JsonNode actualValue, Object expectedValue) {
			// Check if expectedValue is a list
			if (expectedValue instanceof List) {
				for (Object type : (List<?>) expectedValue) {
					if (type.equals("null") && actualValue.isNull()) {
						return true; // Accept null value
					} else if (checkType(actualValue, type)) {
						return true; // Accept if matches one of the types
					}
				}
				return false; // None of the types matched
			}

			// Single type check
			switch ((String) expectedValue) {
				case "STRING":
					return actualValue.isTextual();
				case "NUMBER":
					return actualValue.isNumber();
				default:
					return false;
			}
		}

		//------------------ Define the expected schema---------------------------
		public static Map<String, Object> expectedSchemaDefinition() {
			expectedSchema.put("userAddress", Map.of(
					"addressId", "NUMBER",
					"plotNumber", Arrays.asList("STRING", "null"),
					"street", Arrays.asList("STRING", "null"),
					"state", Arrays.asList("STRING", "null"),
					"country", Arrays.asList("STRING", "null"),
					"zipCode", Arrays.asList("NUMBER", "null")
			));
			expectedSchema.put("user_id", "NUMBER");
			expectedSchema.put("user_first_name", "STRING");
			expectedSchema.put("user_last_name", "STRING");
			expectedSchema.put("user_contact_number", "NUMBER");
			expectedSchema.put("user_email_id", "STRING");
			expectedSchema.put("creation_time", "STRING");
			expectedSchema.put("last_mod_time", "STRING");

			return expectedSchema;
		}
	}