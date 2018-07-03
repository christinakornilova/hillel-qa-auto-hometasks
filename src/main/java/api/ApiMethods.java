package api;


import api.entities.User;
import io.restassured.response.Response;
import utils.ApiConstants;

import java.util.Arrays;
import java.util.List;

public class ApiMethods {
    static BaseHttpMethods baseHttpMethod;
    static String path = "/api/users";

    public ApiMethods(BaseHttpMethods baseHttpMethod) {
        this.baseHttpMethod = baseHttpMethod;
    }

    public static List<User> getUsersList() {
        Response response = baseHttpMethod.sendGet(path);
        return Arrays.asList(response.getBody().as(User[].class));
//        for (Users user: usersList) {
//            System.out.println(user.getId() + " " + user.getName());
//        }
    }

    public static void createUser(String jsonString) {
        baseHttpMethod.sendPost(path, jsonString);
    }

    public static void createAdmin(String jsonString) {
        createUser(jsonString);
        baseHttpMethod.sendGet("/refreshAdmins");
    }

    public static void  updateUser(int id, String json) {
        baseHttpMethod.sendPut(path, id, json);
    }

    public static void deleteUser(int id) {
        baseHttpMethod.sendDelete(path, id);
    }

    public static void main(String[] args) {
        baseHttpMethod = new BaseHttpMethods();
        getUsersList();
    }
}
