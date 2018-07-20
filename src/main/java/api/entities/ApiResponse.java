package api.entities;

import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;

public class ApiResponse {
    Response response;
    private static Logger log = LogManager.getLogger(ApiResponse.class);

    public ApiResponse(Response response) {
        this.response = response;
    }

    public List<User> getUsersList() {
        return Arrays.asList(response.getBody().as(User[].class));
    }

    public int getStatusCode() {
        return response.getStatusCode();
    }

    public String getStatusLine() {
        return response.getStatusLine();
    }

    public boolean isUserPresent(int id) {
        List<User> list = getUsersList();
        for (int i = 0; i < list.size(); i++) {
            if (Integer.parseInt(list.get(i).getId()) == id)
                return true;
        }
        return false;
    }

    public int getUserId(String userName) {
        List<User> list = getUsersList();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getName().equals(userName))
                return i;
        }
        return -1;
    }

    public String getContentType() {
        return response.getContentType();
    }

    public String getUser() {
        return response.body().toString();
    }

}
