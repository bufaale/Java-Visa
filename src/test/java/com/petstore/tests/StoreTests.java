package com.petstore.tests;

import com.petstore.models.Order;
import com.petstore.utils.Endpoints;
import com.petstore.utils.Utils;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

/**
 * Test class for testing store-related endpoints.
 */
public class StoreTests extends TestBase {

    @Test
    public void testGetInventory() {
        Response getResponse = given()
                .spec(requestSpec)
                .when()
                .get(Endpoints.STORE_INVENTORY)
                .then()
                .statusCode(200)
                .extract()
                .response();

        // Ensure that the response is not empty
        String responseBody = getResponse.asString();
        Assert.assertFalse(responseBody.isEmpty(), "Response body should not be empty.");
    }

    @Test
    public void testAddOrder() {
        // Create a new order
        long id = Utils.generateOrderId();
        Order order = new Order();
        order.setId(id);
        order.setPetId(197772L);
        order.setQuantity(2);
        order.setStatus(Order.OrderStatus.PLACED);
        order.setComplete(false);

        // Add the order to the system
        Response postResponse = requestSpec
                .body(order)
                .when()
                .post(Endpoints.ORDER)
                .then()
                .statusCode(200)
                .extract()
                .response();

        // Assert that the order was added correctly
        long postedId = postResponse.jsonPath().getLong("id");
        long postedPetId = postResponse.jsonPath().getLong("petId");
        int postedQuantity = postResponse.jsonPath().getInt("quantity");
        String postedStatus = postResponse.jsonPath().getString("status");
        boolean postedComplete = postResponse.jsonPath().getBoolean("complete");

        Assert.assertEquals(postedId, id, "The posted order ID does not match the expected ID.");
        Assert.assertEquals(postedPetId, 197772L, "The posted pet ID does not match the expected pet ID.");
        Assert.assertEquals(postedQuantity, 2, "The posted quantity does not match the expected quantity.");
        Assert.assertEquals(postedStatus, "PLACED", "The posted order status does not match the expected status.");
        Assert.assertFalse(postedComplete, "The order completion status is not as expected.");
    }

    @Test
    public void testGetOrder() {
        // Create a new order
        long id = Utils.generateOrderId();
        Order order = new Order();
        order.setId(id);
        order.setPetId(198772L);
        order.setQuantity(2);
        order.setStatus(Order.OrderStatus.PLACED);
        order.setComplete(false);

        // Add the order to the system
        Response postResponse = given()
                .spec(requestSpec)
                .body(order)
                .when()
                .post(Endpoints.ORDER)
                .then()
                .statusCode(200)
                .extract()
                .response();

        // Assert that the order was added correctly
        long postedId = postResponse.jsonPath().getLong("id");
        Assert.assertEquals(postedId, id, "The posted order ID does not match the expected ID.");

        // Now, try to retrieve the order you just added
        Response getResponse = given()
                .spec(requestSpec)
                .pathParam("orderId", id)
                .when()
                .get(Endpoints.ORDER_BY_ID)
                .then()
                .statusCode(200)
                .extract()
                .response();

        // Assert that retrieved data matches the expected data
        long retrievedId = getResponse.jsonPath().getLong("id");
        String retrievedStatus = getResponse.jsonPath().getString("status");
        Assert.assertEquals(retrievedId, id, "The retrieved order ID does not match the expected ID.");
        Assert.assertEquals(retrievedStatus, "PLACED", "The order status does not match the expected status.");
    }

    @Test
    public void testDeleteOrder() {
        // 1. Order Creation
        long id = Utils.generateOrderId();
        Order order = new Order();
        order.setId(id);
        order.setPetId(198672L);
        order.setQuantity(2);
        order.setStatus(Order.OrderStatus.PLACED);
        order.setComplete(false);

        Response creationResponse = given()
                .spec(requestSpec)
                .body(order)
                .when()
                .post(Endpoints.ORDER)
                .then()
                .statusCode(200)
                .extract()
                .response();

        long createdOrderId = creationResponse.jsonPath().getLong("id");
        Assert.assertEquals(createdOrderId, id, "Expected order ID does not match the created one.");

        // 2. Order Deletion
        Response deletionResponse = requestSpec
                .pathParam("orderId", id)
                .when()
                .delete(Endpoints.ORDER_BY_ID)
                .then()
                .statusCode(200)
                .extract()
                .response();

        Assert.assertTrue(deletionResponse.asString().isEmpty(), "Expected no content in the deletion response.");

        // 3. Check if the order doesn't exist anymore
        requestSpec
                .pathParam("orderId", id)
                .when()
                .get(Endpoints.ORDER_BY_ID)
                .then()
                .statusCode(404);
    }
}
