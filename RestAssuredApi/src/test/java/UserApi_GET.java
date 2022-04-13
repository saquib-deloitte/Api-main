import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.SortedMap;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;

public class UserApi_GET {

    @BeforeClass
    public void setup() {
        RestAssured.useRelaxedHTTPSValidation();
    }

    public static Response doGetRequest(String endpoint) {
        RestAssured.defaultParser = Parser.JSON;

        return
                given().headers("Content-Type", ContentType.JSON, "Accept", ContentType.JSON).
                        when().get(endpoint).
                        then().contentType(ContentType.JSON).extract().response();
    }

    @Test
    public void get_call_Gender_Validation() {

        Response response = doGetRequest("https://gorest.co.in/public/v1/users");

        String gender = response.jsonPath().getString("data.gender");
        System.out.println("Gender: " + gender);

        String[] genderName = gender.split(",");

        ArrayList<String> genderList = new ArrayList<>();

        for (int i = 0; i < genderName.length; i++) {
            genderName[i] = genderName[i].replaceAll("\\s+", "");
            genderList.add(genderName[i]);
        }

        Assert.assertTrue((genderList.contains("male") && genderList.contains("female")));

        //verifying the status code.
        Assert.assertEquals(response.statusCode(), 200);

        //verifying the content type.
        Assert.assertEquals(response.contentType(), "application/json; charset=utf-8");

    }

    @Test
    public void get_call_Id_Validation() {

        //setting up the base url.
        RestAssured.baseURI = "https://gorest.co.in";

        //creating the request specification object.
        RequestSpecification httpRequest = RestAssured.given();

        //creating the response object.
        Response response = httpRequest.request(Method.GET, "/public/v1/users");

        System.out.println(response.getBody().asString());

        System.out.println("--------------------------------------------------");

        //Getting the list of id
        List<Integer> idList = response.jsonPath().getList("data.id");
        System.out.println(idList.size());

        System.out.println(idList);

        //Verifying that the api is returing 20 products.
        Assert.assertEquals(idList.size(), 20);

        //Verifying that the id value is unique for all the product.
        List<Integer> distinctlist = idList.stream().distinct().collect(Collectors.toList());
        Assert.assertTrue(distinctlist.size()==20);

        //verifying the status code.
        Assert.assertEquals(response.statusCode(),200);

        //verifying the content type.
        Assert.assertEquals(response.contentType(),"application/json; charset=utf-8");

    }
}

