import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;

public class UserApi_Post {

    CSVReader reader;
    Logger log = LogManager.getLogger(UserApi_Post.class);

    @BeforeClass
    public void setup(){
        RestAssured.useRelaxedHTTPSValidation();
    }

    @Test
    public void post_call() throws CsvValidationException, IOException {

        String token = "1e3fbfc04d510423f16db6b1976fb8d49a339e4e486ef5006ebe118b552a2a32";

        DataDriven d = new DataDriven();

        log.info("setting the base URI");
        RestAssured.baseURI = "https://gorest.co.in";

        log.info("creating request object");
        RequestSpecification httpRequest = RestAssured.given();

        httpRequest.header("Authorization", "Bearer " + token).header("Content-Type", "application/json");

        try {
            log.info("Reading product data from csv file...");
            reader = d.readCSV("src/main/resources/UserData.csv");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvValidationException e) {
            e.printStackTrace();
        }

        String[] userData;
        JSONObject requestParams = new JSONObject();

        log.info("putting all the product into the request parameter to perform post operation");
        while ((userData = reader.readNext()) != null) {
            requestParams.put(userData[0], userData[1]);
            System.out.println(userData[0]+" "+userData[1]);
        }

        httpRequest.header("Content-Type", "application/json; charset=utf-8");

        httpRequest.body(requestParams.toJSONString());

        Response response = httpRequest.request(Method.POST, "/public/v1/users");

        String str = response.body().asString();
        System.out.println(str);

        Assert.assertEquals(response.getBody().asString().contains("has already been taken"),true);

        log.info("verifying the status code..");
        Assert.assertEquals(response.statusCode(), 422);

        log.info("Verifying the content type");
        Assert.assertEquals(response.contentType(), "application/json; charset=utf-8");
    }
}
