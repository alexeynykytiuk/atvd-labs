package edu.ntudp.pzks.fourthLab;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


import static io.restassured.RestAssured.given;

public class FourthPostmanLabRunner {
    private static final String baseUrl = "https://0d2e0124-ea64-44a8-9ace-1376e5f1121b.mock.pstmn.io";
    private static final String GetSuccess = "/ownerName/success";
    private static final String GetUnSuccess = "/ownerName/unsuccess";
    private static final String POSTcreateSomething200 = "/createSomething?permisson=yes";
    private static final String POSTcreateSomething400 = "/createSomething";
    private static final String PUTupdateMe = "/updateMe";
    private static final String DELETEdeleteWorld = "/deleteWorld";
    private static final String SessionID = "123456789";

    @BeforeClass
    public void setUp() {
        RestAssured.baseURI = baseUrl;
        RestAssured.defaultParser = Parser.JSON;
        RestAssured.requestSpecification = new RequestSpecBuilder().setContentType(ContentType.JSON).build();
        RestAssured.responseSpecification = new ResponseSpecBuilder().build();
    }

    @Test()
    public void GetSuccess() {
        Response response = given().get(GetSuccess);
        System.out.println("\nTest 1. Get Success status code = " + response.statusCode() + " Name = " + response.jsonPath().get("name"));
    }

    @Test(dependsOnMethods = "GetSuccess")
    public void GetUnsuccess() {
        Response response = given().get(GetUnSuccess);
        System.out.println("\nTest 2. Get UnSuccess status code = " + response.statusCode() + " Exception = " + response.jsonPath().get("exception"));
    }

    @Test(dependsOnMethods = "GetUnsuccess")
    public void PostR_200_OK() {
        Response response = given().post(POSTcreateSomething200);
        System.out.println("\nTest 3. POST createSomething 200 OK status code = " + response.statusCode() + " otvet = " + response.jsonPath().get("result"));
    }

    @Test(dependsOnMethods = "PostR_200_OK")
    public void PostR_400() {
        Response response = given().post(POSTcreateSomething400);
        System.out.println("\nТest 4. POST createSomething 400 status code = " + response.statusCode() + " otvet = " + response.jsonPath().get("result"));
    }

    @Test(dependsOnMethods = "PostR_400")
    public void PutR() {
        Response response = given().put(PUTupdateMe);
        System.out.println("\nТest 5. PUT updateMe status code = " + response.statusCode());
    }

    @Test(dependsOnMethods = "PutR")
    public void DeleteR() {
        Response response = given().header("SessionID", SessionID).delete(DELETEdeleteWorld);
        response.then().statusCode(HttpStatus.SC_GONE);
        System.out.println("\nТest 6. DELETE deleteWorld status code = " + response.statusCode() + " World = " + response.jsonPath().get("world"));
    }
}

