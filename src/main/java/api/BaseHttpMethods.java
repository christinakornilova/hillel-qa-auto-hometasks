package api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONException;
import utils.ApiConstants;


public class BaseHttpMethods {

    public Response sendGet(String path) throws JSONException {
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        return request.get(ApiConstants.HOST + path);
    }

    public Response sendPut(String path, int id, String jsonBody) {
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        request.body(jsonBody);
        return request.put(ApiConstants.HOST + path + "/" + id);
    }

    public Response sendPost(String path, String jsonBody) {
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        request.body(jsonBody);
        return request.put(ApiConstants.HOST + path);
    }

    public Response sendDelete(String path, int id) {
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        return request.delete(ApiConstants.HOST + path + "/" + id);
    }


}
