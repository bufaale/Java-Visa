package com.petstore.tests;

import com.petstore.models.Pet;
import com.petstore.utils.Endpoints;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.entity.FileEntity;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;


import static io.restassured.RestAssured.given;

/**
 * Test class for Pet-related API endpoints.
 */
public class PetTests extends TestBase {

    @Test(priority = 1)
    public void testAddPet() {
        // Create a new pet
        Pet.Category category = new Pet.Category(1, "Dogs");
        List<String> photoUrls = new ArrayList<>();
        photoUrls.add("url3");
        Pet.Tag tag = new Pet.Tag(2, "tag4");
        List<Pet.Tag> tags = new ArrayList<>();
        tags.add(tag);

        Pet pet = new Pet(43, category, "doggie", photoUrls, tags, Pet.PetStatus.PENDING);

        // Send the request to add the pet
        Response response = given()
                .spec(requestSpec)
                .contentType(ContentType.JSON)
                .body(pet)
                .when()
                .post(Endpoints.PET)
                .then()
                .statusCode(200)
                .extract()
                .response();

        // Validate the response data
        String petName = response.jsonPath().getString("name");
        Assert.assertEquals(petName, "doggie", "The added pet's name doesn't match.");

        // You can add more assertions if needed
        // For example, validate the ID, category, and status of the added pet
        long petId = response.jsonPath().getLong("id");
        Assert.assertEquals(petId, 43, "The added pet's ID doesn't match.");

        String categoryName = response.jsonPath().getString("category.name");
        Assert.assertEquals(categoryName, "Dogs", "The added pet's category name doesn't match.");

        String petStatus = response.jsonPath().getString("status");
        Assert.assertEquals(petStatus, Pet.PetStatus.PENDING.toString(), "The added pet's status doesn't match.");
    }


    @Test(priority = 2)
    public void testGetPetById() {
        // Create a new pet
        Pet.Category category = new Pet.Category(1, "Dogs");
        List<String> photoUrls = new ArrayList<>();
        photoUrls.add("url3");
        Pet.Tag tag = new Pet.Tag(2, "tag4");
        List<Pet.Tag> tags = new ArrayList<>();
        tags.add(tag);

        Pet pet = new Pet(1, category, "doggie", photoUrls, tags, Pet.PetStatus.PENDING);

        // Add the pet using POST request
        given()
                .spec(requestSpec)
                .contentType(ContentType.JSON)
                .body(pet)
                .when()
                .post(Endpoints.PET)
                .then()
                .statusCode(200);

        // Retrieve the pet by ID
        Response response = given()
                .spec(requestSpec)
                .pathParam("petId", pet.getId())
                .when()
                .get(Endpoints.PET_BY_ID)
                .then()
                .statusCode(200)
                .extract()
                .response();

        // Validate the retrieved pet's ID
        long returnedPetId = response.jsonPath().getLong("id");
        Assert.assertEquals(returnedPetId, pet.getId(), "The retrieved pet's ID doesn't match the expected ID.");

        // Validate the retrieved pet's name
        String returnedPetName = response.jsonPath().getString("name");
        Assert.assertEquals(returnedPetName, "doggie", "The retrieved pet's name doesn't match.");

        // Validate the retrieved pet's category
        Pet.Category returnedCategory = response.jsonPath().getObject("category", Pet.Category.class);
        Assert.assertEquals(returnedCategory.getId(), category.getId(), "The retrieved pet's category ID doesn't match.");
        Assert.assertEquals(returnedCategory.getName(), category.getName(), "The retrieved pet's category name doesn't match.");

        // Validate the retrieved pet's tags
        List<Pet.Tag> returnedTags = response.jsonPath().getList("tags", Pet.Tag.class);
        Assert.assertEquals(returnedTags.size(), 1, "The retrieved pet should have exactly one tag.");

        Pet.Tag returnedTag = returnedTags.get(0);
        Assert.assertEquals(returnedTag.getId(), tag.getId(), "The retrieved pet's tag ID doesn't match.");
        Assert.assertEquals(returnedTag.getName(), tag.getName(), "The retrieved pet's tag name doesn't match.");
    }

    @Test(priority = 3)
    public void testFindPetByTags() {
        // Create a new pet
        Pet.Category category = new Pet.Category(1, "Dogs");
        List<String> photoUrls = new ArrayList<>();
        photoUrls.add("url3");
        Pet.Tag tag = new Pet.Tag(2, "tag4");
        List<Pet.Tag> tags = new ArrayList<>();
        tags.add(tag);

        Pet pet = new Pet(1, category, "doggie", photoUrls, tags, Pet.PetStatus.PENDING);

        // Add the pet using POST request
        given()
                .spec(requestSpec)
                .contentType(ContentType.JSON)
                .body(pet)
                .when()
                .post(Endpoints.PET)
                .then()
                .statusCode(200);

        // Search for pets by tags
        String[] searchTags = {"tag4"};

        Response response = given()
                .spec(requestSpec)
                .queryParam("tags", searchTags)
                .when()
                .get(Endpoints.PET_BY_TAGS)
                .then()
                .statusCode(200)
                .extract()
                .response();

        // Validate the response
        List<Pet> returnedPets = response.jsonPath().getList("", Pet.class);
        Assert.assertFalse(returnedPets.isEmpty(), "No pets were returned in the response.");

        // Validate the tags of the returned pets
        for (Pet returnedPet : returnedPets) {
            List<Pet.Tag> returnedPetTags = returnedPet.getTags();
            Assert.assertFalse(returnedPetTags.isEmpty(), "The returned pet should have tags.");

            boolean tagFound = false;

            for (Pet.Tag returnedTag : returnedPetTags) {
                if (returnedTag.getId() == tag.getId() && returnedTag.getName().equals(tag.getName())) {
                    tagFound = true;
                    break;
                }
            }

            Assert.assertTrue(tagFound, "The returned pet's tags don't match the expected tag.");
        }
    }


    @Test(priority = 4)
    public void testFindPetByStatus() {
        // Add the pet first
        Pet.Category category = new Pet.Category(1, "Dogs");
        List<String> photoUrls = new ArrayList<>();
        photoUrls.add("url3");
        Pet.Tag tag = new Pet.Tag(2, "tag4");
        List<Pet.Tag> tags = new ArrayList<>();
        tags.add(tag);

        Pet pet = new Pet(1, category, "doggie", photoUrls, tags, Pet.PetStatus.AVAILABLE);

        // Add the pet
        Response addResponse = given()
                .spec(requestSpec)
                .contentType(ContentType.JSON)
                .body(pet)
                .when()
                .post(Endpoints.PET)
                .then()
                .statusCode(200)
                .extract()
                .response();

        long addedPetId = addResponse.jsonPath().getLong("id");

        // Perform the search for pets by status
        Pet.PetStatus searchStatus = Pet.PetStatus.AVAILABLE;

        Response response = given()
                .spec(requestSpec)
                .queryParam("status", searchStatus.toString().toLowerCase())
                .when()
                .get(Endpoints.PET_BY_STATUS)
                .then()
                .statusCode(200)
                .extract()
                .response();

        List<Pet> returnedPets = response.jsonPath().getList("", Pet.class);

        Assert.assertFalse(returnedPets.isEmpty(), "No pets were returned for the specified status.");

        for (Pet returnedPet : returnedPets) {
            Assert.assertEquals(returnedPet.getStatus().toString(), searchStatus.toString(), "The returned pet's status doesn't match the expected status.");
        }
    }

    @Test(priority = 5)
    public void testUpdatePetStatus() {
        // Add a pet first
        Pet.Category category = new Pet.Category(1, "Dogs");
        List<String> photoUrls = new ArrayList<>();
        photoUrls.add("string");
        Pet.Tag tag = new Pet.Tag(0, "string");
        List<Pet.Tag> tags = new ArrayList<>();
        tags.add(tag);

        Pet pet = new Pet(10, category, "doggie", photoUrls, tags, Pet.PetStatus.AVAILABLE);

        // Add the pet
        Response addResponse = given()
                .spec(requestSpec)
                .contentType(ContentType.JSON)
                .body(pet)
                .when()
                .post(Endpoints.PET)
                .then()
                .statusCode(200)
                .extract()
                .response();

        long addedPetId = addResponse.jsonPath().getLong("id");

        // Update the pet's status
        Pet.PetStatus newStatus = Pet.PetStatus.SOLD;

        pet.setStatus(newStatus.toString());

        Response updateResponse = given()
                .spec(requestSpec)
                .contentType(ContentType.JSON)
                .body(pet)
                .when()
                .put(Endpoints.PET)
                .then()
                .statusCode(200)
                .extract()
                .response();

        Assert.assertEquals(updateResponse.jsonPath().getString("status"), newStatus.toString(), "The updated pet's status doesn't match the expected status.");

        // Verify the updated status
        Response verifyResponse = given()
                .spec(requestSpec)
                .pathParam("petId", addedPetId)
                .when()
                .get(Endpoints.PET_BY_ID)
                .then()
                .statusCode(200)
                .extract()
                .response();

        String updatedStatus = verifyResponse.jsonPath().getString("status");
        Assert.assertEquals(updatedStatus, newStatus.toString(), "The verified pet's status doesn't match the expected status.");
    }

    @Test(priority = 8)
    public void testDeletePet() {
        long petId = 1L;

        Response response = given()
                .spec(requestSpec)
                .pathParam("petId", petId)
                .when()
                .delete(Endpoints.PET_BY_ID)
                .then()
                .statusCode(200)
                .extract()
                .response();

        long returnedPetId = response.jsonPath().getLong("id");
        Assert.assertEquals(returnedPetId, petId);
    }

    @Test(priority = 9)
    public void testUploadImage() throws IOException {
        // Agregar la mascota primero
        Pet.Category category = new Pet.Category(1, "Dogs");
        List<String> photoUrls = new ArrayList<>();
        photoUrls.add("url3");
        Pet.Tag tag = new Pet.Tag(2, "tag4");
        List<Pet.Tag> tags = new ArrayList<>();
        tags.add(tag);

        Pet pet = new Pet(1, category, "doggie", photoUrls, tags, Pet.PetStatus.PENDING);

        given()
                .spec(requestSpec)
                .contentType(ContentType.JSON)
                .body(pet)
                .when()
                .post(Endpoints.PET)
                .then()
                .statusCode(200);

        // Preparación: Definir datos para subir
        long petId = pet.getId();
        String additionalMetadata = "1";
        File imageFile = new File("src/test/resources/visa2.jpg");  // Ruta del archivo de imagen
        byte[] fileContent = Files.readAllBytes(imageFile.toPath());


        Response response = given()
                .spec(requestSpec)
                .pathParam("petId", petId)
                .queryParam("additionalMetadata", additionalMetadata)
                .header("Content-Type", "application/octet-stream; charset=UTF-8") // Set the Content-Type to application/octet-stream
                .body(imageFile)
                .when()
                .post(Endpoints.PET_UPLOAD_IMAGE)
                .then()
                .statusCode(200)
                .extract()
                .response();

        // Step 4: Validate the response
        String message = response.jsonPath().getString("message");
        Assert.assertEquals(message, "File uploaded successfully");
    }
}
