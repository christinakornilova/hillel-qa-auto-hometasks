package api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONException;
import utils.ApiConstants;

public class BaseHttpMethods {

    private static RequestSpecification composeRequestSpec() {
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        return request;
    }

    public static Response sendGet(String path) throws JSONException {
        return composeRequestSpec().get(ApiConstants.HOST + path);
    }

    public static Response sendPut(String path, int id, String jsonBody) {
        return composeRequestSpec().body(jsonBody).put(ApiConstants.HOST + path + "/" + id);
    }

    public static Response sendPost(String path, String jsonBody) {
        return composeRequestSpec().body(jsonBody).post(ApiConstants.HOST + path);
    }

    public static Response sendDelete(String path, int id) {
        return composeRequestSpec().delete(ApiConstants.HOST + path + "/" + id);
    }

    public static Response sendInvalidGet(String path, String contentType) throws JSONException {
        return composeRequestSpec().contentType(contentType).get(ApiConstants.HOST + path);
    }


}
