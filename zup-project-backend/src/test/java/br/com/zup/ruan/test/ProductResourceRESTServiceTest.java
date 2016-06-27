package br.com.zup.ruan.test;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

import com.jayway.restassured.RestAssured;

public class ProductResourceRESTServiceTest{

    public ProductResourceRESTServiceTest(){
		RestAssured.baseURI = "http://localhost:8080/zup-project-backend/rest/products";
	}
	
	@Test
    public void testGetProduct() {
		given()
         .contentType("application/json")
         .when()
         .get("/1")   
         .then().log().all()
         	.body("id", is(1))
            .statusCode(200)
            .assertThat()
              .body(matchesJsonSchemaInClasspath("schema_product.json"))
              .extract().response();
		
    }
}
