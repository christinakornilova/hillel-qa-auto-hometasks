package api;

import api.entities.ApiResponse;
import api.entities.User;
import com.google.gson.Gson;

public class ApiMethods {
    static BaseHttpMethods baseHttpMethod;
    static String path = "/api/users";

    public ApiMethods(BaseHttpMethods baseHttpMethod) {
        this.baseHttpMethod = baseHttpMethod;
    }

    public static ApiResponse sendGetUsers() {
        return new ApiResponse(baseHttpMethod.sendGet("/api/users"));
    }

    public static ApiResponse createUser(String jsonString) {
        return new ApiResponse(baseHttpMethod.sendPost(path, jsonString));
    }

    public static ApiResponse createAdmin(String jsonString) {
        return createUser(jsonString);
//        baseHttpMethod.sendGet("/refreshAdmins");
    }

    public static ApiResponse updateUser(int id, String json) {
        return new ApiResponse(baseHttpMethod.sendPut(path, id, json));
    }

    public static ApiResponse updateAdminUser(int id, String json) {
        baseHttpMethod.sendPut(path, id, json);
        return new ApiResponse(baseHttpMethod.sendGet("/refreshAdmins"));
    }

    public static ApiResponse deleteUser(int id) {
        return new ApiResponse(baseHttpMethod.sendDelete(path, id));
    }

    public static ApiResponse sendInvalidGetUsers(String contentType) {
        return new ApiResponse(baseHttpMethod.sendInvalidGet("/api/users", contentType));
    }

    public static String userStringToJson(String id, String name, String phone, String role) {
        User testUser = formatUser(id, name, phone);
        testUser.setRole(role);
        return new Gson().toJson(testUser);
    }

    public static String userStringToJson(String id, String name, String phone) {
        User testUser = formatUser(id, name, phone);
        return new Gson().toJson(testUser);
    }

    private static User formatUser(String id, String name, String phone) {
        User testUser = new User();
        testUser.setId(id);
        testUser.setName(name);
        testUser.setPhone(phone);
        return testUser;
    }



}
