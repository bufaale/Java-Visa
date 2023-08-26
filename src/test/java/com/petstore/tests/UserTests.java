package com.petstore.tests;

import com.petstore.models.User;
import com.petstore.utils.Endpoints;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import com.petstore.utils.Utils;

/**
 * Test class for User-related API endpoints.
 */
public class UserTests extends TestBase {

    @Test(priority = 1)
    public void testCreateUser() {
        long randomId = Utils.generateOrderId();
        User user = new User(randomId, "testuser", "Test", "User", "testuser@example.com", "password123", "1234567890", 1);

        requestSpec
                .body(user)
                .when()
                .post(Endpoints.USER)
                .then()
                .statusCode(200);
    }

    @Test(priority = 2)
    public void testGetUser() {
        String username = "testuser";

        requestSpec
                .pathParam("username", username)
                .when()
                .get(Endpoints.USER_BY_USERNAME)
                .then()
                .statusCode(200)
                .body("username", equalTo(username));
    }

    @Test(priority = 3)
    public void testCreateUserWithList() {
        User user1 = new User(11L, "theUser", "John", "James", "john@email.com", "12345", "12345", 1);
        User user2 = new User(12L, "theUser", "Ron", "James", "ron@email.com", "12345", "12345", 1);
        List<User> usersToSend = Arrays.asList(user1, user2);

        Response response = requestSpec
                .body(usersToSend)
                .when()
                .post(Endpoints.USER_CREATE_WITH_LIST)
                .then()
                .statusCode(200)
                .extract()
                .response();

        // Convert the response to a list of User objects
        List<User> returnedUsers = response.jsonPath().getList("$", User.class);

        // Verify that both lists have the same size
        Assert.assertEquals(usersToSend.size(), returnedUsers.size(), "Size of sent and returned users lists are different.");

        for (int i = 0; i < usersToSend.size(); i++) {
            User sentUser = usersToSend.get(i);
            User returnedUser = returnedUsers.get(i);

            // Compare each field
            Assert.assertEquals(sentUser.getId(), returnedUser.getId());
            Assert.assertEquals(sentUser.getUsername(), returnedUser.getUsername());
            Assert.assertEquals(sentUser.getFirstName(), returnedUser.getFirstName());
            Assert.assertEquals(sentUser.getLastName(), returnedUser.getLastName());
            Assert.assertEquals(sentUser.getEmail(), returnedUser.getEmail());
            Assert.assertEquals(sentUser.getPassword(), returnedUser.getPassword());
            Assert.assertEquals(sentUser.getPhone(), returnedUser.getPhone());
            Assert.assertEquals(sentUser.getUserStatus(), returnedUser.getUserStatus());
        }
    }

    @Test(priority = 4)
    public void testUserLogin() {
        String username = "theUser";
        String password = "12345";

        Response response = given()
                .spec(requestSpec)
                .queryParam("username", username)
                .queryParam("password", password)
                .accept(ContentType.XML)
                .when()
                .get(Endpoints.USER_LOGIN)
                .then()
                .statusCode(200)
                .extract()
                .response();

        String responseBody = response.asString();

        // Verify if the expected message is present in the responseBody
        Assert.assertTrue(responseBody.contains("Logged in user session:"));
    }

    @Test(priority = 5)
    public void testUpdateUser() {
        // Create a user first
        long randomId = Utils.generateOrderId();
        User user = new User(randomId, "testuserUpdate", "Test", "User", "testuser@example.com", "password123", "1234567890", 1);

        Response createUserResponse = given()
                .spec(requestSpec)
                .body(user)
                .when()
                .post(Endpoints.USER)
                .then()
                .statusCode(200)
                .extract()
                .response();

        String userName = createUserResponse.jsonPath().getString("username");
        // Perform the update operation
        User updatedUser = new User();
        updatedUser.setUsername("updateduser");

        Response updateUserResponse = given()
                .spec(requestSpec)
                .pathParam("username", userName)
                .body(updatedUser)
                .when()
                .put(Endpoints.USER_BY_USERNAME)
                .then()
                .statusCode(200)
                .extract()
                .response();

        String responseBody = updateUserResponse.getBody().asString();
        System.out.println(responseBody);

        String username = updateUserResponse.jsonPath().getString("username");
        Assert.assertEquals(username, "updateduser");
    }

    @Test(priority = 6)
    public void testDeleteUser() {
        // Create a user first
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");

        Response createUserResponse = given()
                .spec(requestSpec)
                .body(user)
                .when()
                .post(Endpoints.USER)
                .then()
                .statusCode(200)
                .extract()
                .response();

        String userName = createUserResponse.jsonPath().getString("username");

        // Perform the delete operation
        Response deleteUserResponse = given()
                .spec(requestSpec)
                .pathParam("username", userName)
                .when()
                .delete(Endpoints.USER_BY_USERNAME)
                .then()
                .statusCode(200)
                .extract()
                .response();

        String responseBody = deleteUserResponse.getBody().asString();
        System.out.println(responseBody);

        Assert.assertTrue(responseBody.isEmpty()); // Assuming the response body is empty for successful deletion
    }
}
