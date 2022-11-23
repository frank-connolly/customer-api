package de.cronn.customer;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class CustomerResourceTest {

    @Test
    public void getAll() {
        given()
                .when().get("api/customers")
                .then()
                .statusCode(200);
    }

    @Test
    public void getById() {
        Customer customer = createCustomer();
        Customer saved = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(customer)
                .post("api/customers")
                .then()
                .statusCode(201)
                .extract().as(Customer.class);
        Customer got = given()
                .when().get("api/customers/{customerId}", saved.getCustomerId())
                .then()
                .statusCode(200)
                .extract().as(Customer.class);
        Assertions.assertEquals(saved, got);
    }

    @Test
    public void post() {
        Customer customer = createCustomer();
        Customer saved = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(customer)
                .post("/api/customers")
                .then()
                .statusCode(201)
                .extract().as(Customer.class);
        Assertions.assertNotNull(saved.getCustomerId());
    }

    @Test
    public void postFailNoFirstName() {
        Customer customer = createCustomer();
        customer.setFirstName(null);
        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(customer)
                .post("/api/customers")
                .then()
                .statusCode(400);
    }

    @Test
    public void put() {
        Customer customer = createCustomer();
        Customer saved = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(customer)
                .post("/api/customers")
                .then()
                .statusCode(201)
                .extract().as(Customer.class);
        saved.setFirstName("Updated");
        Customer updated = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(saved)
                .put("/api/customers")
                .then()
                .statusCode(200)
                .extract().as(Customer.class);
        Assertions.assertEquals("Updated", updated.getFirstName());
    }

    @Test
    public void putFailNoLastName() {
        Customer customer = createCustomer();
        Customer saved = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(customer)
                .post("/api/customers")
                .then()
                .statusCode(201)
                .extract().as(Customer.class);
        saved.setLastName(null);
        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(saved)
                .put("/api/customers")
                .then()
                .statusCode(400);
    }

    private Customer createCustomer() {
        Customer customer = new Customer();
        customer.setFirstName(RandomStringUtils.randomAlphabetic(10));
        customer.setMiddleName(RandomStringUtils.randomAlphabetic(10));
        customer.setLastName(RandomStringUtils.randomAlphabetic(10));
        customer.setEmail(RandomStringUtils.randomAlphabetic(10) + "@cronn.de");
        customer.setPhone(RandomStringUtils.randomNumeric(10));
        return customer;
    }

}