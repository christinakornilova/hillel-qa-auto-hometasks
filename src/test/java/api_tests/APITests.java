package api_tests;

import api.ApiMethods;
import api.entities.ApiResponse;
import api.entities.User;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.ApiConstants;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class APITests {
    /*
    1. Во всех запросах Content-Type: application/json
2. Получение списка пользователя было реализовано по GET на хост
3. Сохранение пользователя было реализовано методом PUT на на хост + '/' + id (где id - id редактируемого человека)
4. Создание нового пользователя было реализовано методом POST на winow.crudURL, в ответ приходил id новой созданной сущности
5. При сохранении администратора надо было делать GET на /refreshAdmins на том же хосте где был хост
6. При создании администратора или помощника надо было передать в поле role имя роли, для студента было не надо
7. Для удаления пользователя необходимо отправить DELETE на хост + '/' + id (где id - id удаляемого человека)
8. Сервер должен запускаться на порту 20007
9. Во всех ответах должен присутстовать заголовок Content-Type application/json
10. Если клиент передает некорректный заголовок Content-Type либо он отсутствует - возвращать 401 ошибку
11. Начальное состояние сервера (при перезапуске):
{ id: '1', name: 'Illya Klymov', phone: '+380504020799', role: 'Administrator' },
{ id: '2', name: 'Ivanov Ivan', phone: '+380670000002', role: 'Student', strikes: 1 },
{ id: '3', name: 'Petrov Petr', phone: '+380670000001', role: 'Support', location: 'Kiev' }
12. Если приходит некорректное поле role (смотри условие задания №4) - возвращать 401 ошибку
13. Если происходит попытка модификации или удаления несуществующего id - сервер должен возвращать 404 ошибку
     */

    @Test(priority = -2)
    public void checkServerInitState() {
        //expected failure
        User u1 = new User("1", "Illya Klymov", "+380504020799", "Administrator");
        User u2 = new User("2", "Ivanov Ivan", "+380670000002", "Student", "1", "");
        User u3 = new User("3", "Petrov Petr", "+380670000001", "Support", "Kiev");
        List<User> initStateList = new ArrayList<>();
        initStateList.add(u1);
        initStateList.add(u2);
        initStateList.add(u3);
        ApiResponse response = ApiMethods.sendGetUsers();
        List<User> usersList = response.getUsersList();
        //check that server contains 3 entries on start
        Assert.assertEquals(usersList.size(), 3);
        //check entries
        for(int i = 0; i < 3; i++) {
            Assert.assertEquals(usersList.get(i).getId(), initStateList.get(i).getId());
            Assert.assertEquals(usersList.get(i).getName(), initStateList.get(i).getName());
            Assert.assertEquals(usersList.get(i).getPhone(), initStateList.get(i).getPhone());
            Assert.assertEquals(usersList.get(i).getRole(), initStateList.get(i).getRole());
            Assert.assertEquals(usersList.get(i).getStrikes(), initStateList.get(i).getStrikes());
            Assert.assertEquals(usersList.get(i).getLocation(), initStateList.get(i).getLocation());
        }
        //All calls Content-Type: application/json
        Assert.assertEquals(response.getContentType(), ApiConstants.CONTENT_TYPE);

    }

    @Test(priority = -1)
    public void testGetUsersList() {
        ApiResponse response = ApiMethods.sendGetUsers();
        List<User> usersList = response.getUsersList();
        //server responded with ok
        Assert.assertEquals(response.getStatusCode(), 200);
        //assert that server returns 3 entities
        Assert.assertEquals(usersList.size(), 3);
        //All calls Content-Type: application/json
        Assert.assertEquals(response.getContentType(), ApiConstants.CONTENT_TYPE);
    }

    @Test(dependsOnMethods = "testGetUsersList")
    public void testCreateRegularUser() throws URISyntaxException {
        String userJSON = ApiMethods.userStringToJson("4", "Neil Sanderson", "+380670000011", "Student");
        ApiResponse response = ApiMethods.createUser(userJSON);
        //check that server resp with ok
        Assert.assertEquals(response.getStatusCode(), 200);
        //assert that json object contains 4 entries including newly created one
        Assert.assertTrue(ApiMethods.sendGetUsers().isUserPresent(4));
        //All calls Content-Type: application/json
        Assert.assertEquals(response.getContentType(), ApiConstants.CONTENT_TYPE);

    }

    @Test(dependsOnMethods = "testGetUsersList")
    public void createInvalidUser() throws URISyntaxException {
        //Send wrong "role" value - server should return 401 error
        String userJSON = ApiMethods.userStringToJson("5", "Invalid User", "+380670000022", "InvalidRole");
        ApiResponse response = ApiMethods.createUser(userJSON);
        //check that server resp with 401
        Assert.assertEquals(response.getStatusCode(), 401);
        //assert that server has no user with id 5
        Assert.assertFalse(ApiMethods.sendGetUsers().isUserPresent(5));
        //All calls Content-Type: application/json
        Assert.assertEquals(response.getContentType(), ApiConstants.CONTENT_TYPE);
    }

    @Test(dependsOnMethods = "testCreateRegularUser")
    public void testEditUser() {
        //User edit via PUT to host + '/' + id (where id - id of edited user)
        String userJSON = ApiMethods.userStringToJson("4", "Neil Christopher Sanderson", "+380670000011", "Student");
        List<User> usersList = ApiMethods.sendGetUsers().getUsersList();
        int listSize = usersList.size();
        ApiResponse response = ApiMethods.updateUser(4, userJSON);
        //check that server response with ok
        Assert.assertEquals(response.getStatusCode(), 204);
        //assert that server still returns 4 entities
        usersList = ApiMethods.sendGetUsers().getUsersList();
        Assert.assertEquals(usersList.size(), listSize);
        //Check that user's name updated properly
        Assert.assertEquals(usersList.get(3).getName(), "Neil Christopher Sanderson");
        //All calls Content-Type: application/json
        Assert.assertEquals(response.getContentType(), ApiConstants.CONTENT_TYPE);
    }


    @Test(dependsOnMethods = "testCreateRegularUser")
    public void testAddUserAdmin() throws URISyntaxException {
        //role is required field for admin and support
        String userJSON = ApiMethods.userStringToJson("5", "Adam Gontier", "+380670000055", "Administrator");
        ApiResponse response = ApiMethods.createAdmin(userJSON);
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(ApiMethods.sendGetUsers().isUserPresent(5));
        //check newly created user role
        Assert.assertEquals(ApiMethods.sendGetUsers().getUsersList().get(4).getRole(), "Administrator");
        //All calls Content-Type: application/json
        Assert.assertEquals(response.getContentType(), ApiConstants.CONTENT_TYPE);
    }

    @Test(dependsOnMethods = "testAddUserAdmin")
    public void testAddUserSupport() {
        //role is required field for admin and support
        String userJSON = ApiMethods.userStringToJson("6", "Matt Walst", "+380670000077", "Support");
        ApiResponse response = ApiMethods.createUser(userJSON);
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(ApiMethods.sendGetUsers().isUserPresent(6));
        //check newly created user role
        Assert.assertEquals(ApiMethods.sendGetUsers().getUsersList().get(5).getRole(), "Support");
        //All calls Content-Type: application/json
        Assert.assertEquals(response.getContentType(), ApiConstants.CONTENT_TYPE);
    }

    @Test(dependsOnMethods = {"testEditUser", "testAddUserAdmin"})
    public void testEditAdminUser() {
        //Edit admin user - GET to host + /refreshAdmins

    }

    @Test (dependsOnMethods = {"testCreateRegularUser", "testAddUserAdmin"})
    public void testCreateRegularUserWithNoRoleSpecified() {
        //role is not required field for student
        String userJSON = ApiMethods.userStringToJson("7", "Brad Walst", "+380670000088");
        ApiResponse response = ApiMethods.createUser(userJSON);
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(ApiMethods.sendGetUsers().isUserPresent(7));
        //create student user with no role than check that role added automatically
        Assert.assertEquals(ApiMethods.sendGetUsers().getUsersList().get(6).getRole(), "Student");
        //All calls Content-Type: application/json
        Assert.assertEquals(response.getContentType(), ApiConstants.CONTENT_TYPE);
    }

    @Test(dependsOnMethods = {"testCreateRegularUser", "testEditUser"})
    public void testDeleteRegularUser() {
        //For user deletion: send DELETE to host with '/{$id} of deleted user
        //deleteUser()
        ApiResponse response = ApiMethods.deleteUser(4);
        //assert that server resp with ok
        Assert.assertEquals(response.getStatusCode(), 204);
        //assert that user deleted successfully
        Assert.assertFalse(ApiMethods.sendGetUsers().isUserPresent(4));
        //All calls Content-Type: application/json
        Assert.assertEquals(response.getContentType(), ApiConstants.CONTENT_TYPE);
    }

    @Test(dependsOnMethods = {"testCreateRegularUser", "testAddUserAdmin", "testDeleteRegularUser"})
    public void testDeleteAdminUser(){
        //For user deletion: send DELETE to host with '/{$id} of deleted user
        //deleteUser()
        ApiResponse response = ApiMethods.deleteUser(7);
        //assert that server resp with ok
        Assert.assertEquals(response.getStatusCode(), 204);
        //assert that user deleted successfully
        Assert.assertFalse(ApiMethods.sendGetUsers().isUserPresent(7));
        Assert.assertEquals(response.getContentType(), ApiConstants.CONTENT_TYPE);
    }

    @Test(dependsOnMethods = {"testCreateRegularUser", "testDeleteRegularUser"})
    public void testDeleteUserInvalidId() {
        //Delete user with non-existent id - server returns 404 error
        //Sent DELETE to host with nonexistent id
        ApiResponse response = ApiMethods.deleteUser(100);
        //assert that server responds with 404 error
        Assert.assertEquals(response.getStatusCode(), 404);
        Assert.assertEquals(response.getContentType(), ApiConstants.CONTENT_TYPE);
    }

    @Test(dependsOnMethods = "testEditUser")
    public void testEditNonExistentUser() {
        //User edit via PUT to host + '/' + id (where id - id of edited user)
        //Edit user with non-existent id - server returns 404 error
        String userJSON = ApiMethods.userStringToJson("100", "Null Null", "Null");
        ApiResponse response = ApiMethods.updateUser(100, userJSON);
        //check that server response with ok
        Assert.assertEquals(response.getStatusCode(), 404);
        //All calls Content-Type: application/json
        Assert.assertEquals(response.getContentType(), ApiConstants.CONTENT_TYPE);
    }

    @Test(dependsOnMethods = "testGetUsersList")
    public void testInvalidContentType() {
        //Passing incorrect Content-Type - return 401 error
        //sent invalid content type
        ApiResponse response = ApiMethods.sendInvalidGetUsers("text/javascript");
        //check that server resp with 401
        Assert.assertEquals(response.getStatusCode(), 401);
    }

    @Test(dependsOnMethods = "testGetUsersList")
    public void testNoContentType() {
        //Passing empty Content-Type - return 401 error
        //sent no content type
        ApiResponse response = ApiMethods.sendInvalidGetUsers("");
        //check that server resp with 401
        Assert.assertEquals(response.getStatusCode(), 401);
    }
}
