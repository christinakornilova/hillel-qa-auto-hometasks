package api_tests;

import api.ApiMethods;
import api.entities.ApiResponse;
import api.entities.User;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.ApiConstants;

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

    private void assertUserParameters(int userId, boolean isUserExpected) {
        assertUserParameters(userId, "", isUserExpected, false);
    }

    private void assertUserParameters(int userId, String expectedValue, boolean assertRole) {
        assertUserParameters(userId, expectedValue, true, assertRole);
    }

    private void assertUserParameters(int userId, String expectedValue, boolean isUserExpected, boolean assertRole) {
        ApiResponse getUsers = ApiMethods.sendGetUsers();
        if (isUserExpected) {
            Assert.assertTrue(getUsers.isUserPresent(userId));
            if (assertRole)
                Assert.assertEquals(getUsers.getUsersList().get(userId - 1).getRole(), expectedValue);
        } else {
            Assert.assertFalse(getUsers.isUserPresent(userId));
        }
    }

    private void assertResponseParameters(ApiResponse response, int expectedStatusCode) {
        Assert.assertEquals(response.getStatusCode(), expectedStatusCode);
        Assert.assertEquals(response.getContentType(), ApiConstants.CONTENT_TYPE);
    }

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
    public void testCreateRegularUser() {
        String userJSON = ApiMethods.userStringToJson("4", "Neil Sanderson", "+380670000011", "Student");
        ApiResponse response = ApiMethods.createUser(userJSON);
        assertResponseParameters(response, 200);
        assertUserParameters(4, true);
    }

    @Test(dependsOnMethods = "testGetUsersList")
    public void createInvalidUser() {
        //Send wrong "role" value - server should return 401 error
        String userJSON = ApiMethods.userStringToJson("5", "Invalid User", "+380670000022", "InvalidRole");
        ApiResponse response = ApiMethods.createUser(userJSON);
        assertResponseParameters(response, 401);
        assertUserParameters(5, false);
    }

    @Test(dependsOnMethods = "testCreateRegularUser")
    public void testEditUser() {
        //User edit via PUT to host + '/' + id (where id - id of edited user)
        String userJSON = ApiMethods.userStringToJson("4", "Neil Christopher Sanderson", "+380670000011", "Student");
        List<User> usersList = ApiMethods.sendGetUsers().getUsersList();
        int listSize = usersList.size();
        ApiResponse response = ApiMethods.updateUser(4, userJSON);
        assertResponseParameters(response, 204);
        //assert that server still returns 4 entities
        usersList = ApiMethods.sendGetUsers().getUsersList();
        Assert.assertEquals(usersList.size(), listSize);
        //Check that user's name updated properly
        Assert.assertEquals(usersList.get(3).getName(), "Neil Christopher Sanderson");
    }

    @Test(dependsOnMethods = "testCreateRegularUser")
    public void testAddUserAdmin() {
        String userJSON = ApiMethods.userStringToJson("5", "Adam Gontier", "+380670000055", "Administrator");
        ApiResponse response = ApiMethods.createAdmin(userJSON);
        assertResponseParameters(response, 200);
        assertUserParameters(5, "Administrator", true);
    }

    @Test(dependsOnMethods = "testAddUserAdmin")
    public void testAddUserSupport() {
        String userJSON = ApiMethods.userStringToJson("6", "Matt Walst", "+380670000077", "Support");
        ApiResponse response = ApiMethods.createUser(userJSON);
        assertResponseParameters(response, 200);
        assertUserParameters(6, "Support", true);
    }

    @Test(dependsOnMethods = {"testEditUser", "testAddUserAdmin"})
    public void testEditAdminUser() {
        //Edit admin user - GET to host + /refreshAdmins
        ApiResponse res1 = ApiMethods.sendGetUsers();
        int id = res1.getUserId("Adam Gontier");
        Assert.assertTrue(id!=-1);
        String userJSON = ApiMethods.userStringToJson(String.valueOf(id), "Adam Wade Gontier", "+380679998877", "Administrator");
        ApiResponse response = ApiMethods.updateAdminUser(id, userJSON);
        assertResponseParameters(response, 200);

        //Check that admin's name and phone updated properly
        List<User> usersList = ApiMethods.sendGetUsers().getUsersList();
        Assert.assertEquals(usersList.get(id-1).getName(), "Adam Wade Gontier");
        Assert.assertEquals(usersList.get(id-1).getPhone(), "+380679998877");
    }

    @Test (dependsOnMethods = {"testCreateRegularUser", "testAddUserAdmin"})
    public void testCreateRegularUserWithNoRoleSpecified() {
        //role is not required field for student
        String userJSON = ApiMethods.userStringToJson("7", "Brad Walst", "+380670000088");
        ApiResponse response = ApiMethods.createUser(userJSON);
        assertResponseParameters(response, 200);
        assertUserParameters(7, "Student", true);
    }

    @Test(dependsOnMethods = {"testCreateRegularUser", "testEditUser"})
    public void testDeleteRegularUser() {
        //For user deletion: send DELETE to host with '/{$id} of deleted user
        ApiResponse response = ApiMethods.deleteUser(4);
        assertResponseParameters(response, 204);
        assertUserParameters(4,false);
    }

    @Test(dependsOnMethods = {"testCreateRegularUser", "testAddUserAdmin", "testDeleteRegularUser"})
    public void testDeleteAdminUser(){
        //For user deletion: send DELETE to host with '/{$id} of deleted user
        ApiResponse response = ApiMethods.deleteUser(7);
        assertResponseParameters(response, 204);
        assertUserParameters(7,false);
    }

    @Test(dependsOnMethods = {"testCreateRegularUser", "testDeleteRegularUser"})
    public void testDeleteUserInvalidId() {
        //Delete user with non-existent id - server returns 404 error
        ApiResponse response = ApiMethods.deleteUser(100);
        //assert that server responds with 404 error
        assertResponseParameters(response, 404);
    }

    @Test(dependsOnMethods = "testEditUser")
    public void testEditNonExistentUser() {
        //Edit user with non-existent id - server returns 404 error
        String userJSON = ApiMethods.userStringToJson("100", "Null Null", "Null");
        ApiResponse response = ApiMethods.updateUser(100, userJSON);
        assertResponseParameters(response, 404);
    }

    @Test(dependsOnMethods = "testGetUsersList")
    public void testInvalidContentType() {
        //Passing incorrect Content-Type - return 401 error
        ApiResponse response = ApiMethods.sendInvalidGetUsers("text/javascript");
        //check that server resp with 401
        assertResponseParameters(response, 401);
    }

    @Test(dependsOnMethods = "testGetUsersList")
    public void testNoContentType() {
        //Passing empty Content-Type - return 401 error
        ApiResponse response = ApiMethods.sendInvalidGetUsers("");
        //check that server resp with 401
        assertResponseParameters(response, 401);
    }
}
