package edu.ntudp.pzks.thirdLab;

import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import org.apache.hc.core5.http.HttpStatus;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class ThirdLabIndivid {
    private static final String baseUrl = "https://petstore.swagger.io/v2";

    private static final String PET = "/pet";

    private String petId;
    private String petName;

    private String tagName = "Nykytiuk Oleksii";
    private String category = "122-21-21";

    private String statusN = "available";

    private static final String STORE = "/store/order",
            STORE_ORDER_GET = STORE + "/{orderId}";

    private String orderId;

    String JsonBoduPet() {
        return " {\n" +
                "  \"category\": {\n" +
                "    \"id\": 0,\n" +
                "    \"name\": \"" + category + "\"\n" +
                "  },\n" +
                "  \"name\": \"" + Faker.instance().dog().name() + "\",\n" +
                "  \"photoUrls\": [\n" +
                "    \"" + Faker.instance().dog().memePhrase() + "\"\n" +
                "  ],\n" +
                "  \"tags\": [\n" +
                "    {\n" +
                "      \"id\": 0,\n" +
                "      \"name\": \"" + tagName + "\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"status\": \"" + statusN + "\"\n" +
                "}";
    }

    String JsonBoduPetWithId(String petId) {
        return " {\n" +
                " \"id\":" + petId + "," +
                "  \"category\": {\n" +
                "    \"id\": 0,\n" +
                "    \"name\": \"" + category + "\"\n" +
                "  },\n" +
                "  \"name\": \"" + tagName + "\",\n" +
                "  \"photoUrls\": [\n" +
                "    \"" + Faker.instance().dog().memePhrase() + "\"\n" +
                "  ],\n" +
                "  \"tags\": [\n" +
                "    {\n" +
                "      \"id\": 0,\n" +
                "      \"name\": \"" + tagName + "\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"status\": \"" + statusN + "\"\n" +
                "}";
    }

    @BeforeClass
    public void setUp() {
        RestAssured.baseURI = baseUrl;
        RestAssured.defaultParser = Parser.JSON;
        RestAssured.requestSpecification = new RequestSpecBuilder().setContentType(ContentType.JSON).build();
        RestAssured.responseSpecification = new ResponseSpecBuilder().build();
    }

    @Test
    public void verifyAddPet() {

        Response response = given()
                .contentType(ContentType.JSON).body(JsonBoduPet())
                .post(PET);
        response.then()
                .statusCode(HttpStatus.SC_OK);

        petId = response.jsonPath().get("id").toString();
        petName = response.jsonPath().get("name").toString();

        System.out.println("1. Секція домашня тварина. Додано тварину: індентифікатор " + petId + " Ім'я " + petName + " Категорія "
                + response.jsonPath().get("category.name").toString()
                + " Тег " + response.jsonPath().get("tags.name").toString()
                + " Статус " + response.jsonPath().get("status").toString());
    }

    @Test(dependsOnMethods = "verifyAddPet")
    public void verifyPutPet() {

        category = "122-21SK-1.10variant";

        statusN = "pending";

        Response response = given().contentType(ContentType.JSON).body(JsonBoduPetWithId(petId))
                .put(PET);
        response.then()
                .statusCode(HttpStatus.SC_OK);

        System.out.println("2. Секція домашня тварина. Оновлено тварину: індентифікатор " + petId + " Ім'я " + petName + " Категорія "
                + response.jsonPath().get("category.name").toString()
                + " Тег " + response.jsonPath().get("tags.name").toString()
                + " Статус " + response.jsonPath().get("status").toString());
    }

    @Test(dependsOnMethods = "verifyPutPet")
    public void verifyAddOrder() {
        Map<String, ?> body = Map.of(
                "petId", petId,
                "quantity", Integer.valueOf("3"),
                "shipDate", "2024-03-11T20:34:22.821Z",
                "status", "placed",
                "complete", Boolean.valueOf("true")
        );

        Response response = given()
                .body(body)
                .post(STORE);
        response.then()
                .statusCode(HttpStatus.SC_OK);

        orderId = response.jsonPath().get("id").toString();

        System.out.println("3. Секція заказ. Індентифікатор " + orderId
                + " Індентифікатор тварини " + response.jsonPath().get("petId").toString());
    }

    @Test(dependsOnMethods = "verifyAddOrder")
    public void verifyGetOrder() {

        Response response = given().pathParams("orderId", orderId)
                .get(STORE_ORDER_GET);

        response.then()
                .statusCode(org.apache.http.HttpStatus.SC_OK);

        System.out.println("4. Секція заказ. Пошук інформації " + response.jsonPath().get("id").toString()
                + " Індентифікатор тварини "
                + response.jsonPath().get("petId").toString()
                + " \nКількість " + response.jsonPath().get("quantity").toString()
                + " \nДата " + response.jsonPath().get("shipDate").toString()
                + " \nСтатус " + response.jsonPath().get("status").toString());
    }

    @Test(dependsOnMethods = "verifyGetOrder")
    public void verifyDeleteOrder() {

        Response response = given().pathParams("orderId", orderId)
                .delete(STORE_ORDER_GET);

        response.then()
                .statusCode(org.apache.http.HttpStatus.SC_OK);

        System.out.println("5. Секція заказ. Видалення. " + response.statusCode());
    }
}

