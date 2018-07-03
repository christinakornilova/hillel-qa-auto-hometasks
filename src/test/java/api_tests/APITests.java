package api_tests;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.Test;

public class APITests extends APITestBase {
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



    public String getApiResponse(Object call) {
        //send call
        return "response";
    }


//        Начальное состояние сервера (при перезапуске):
//        { id: '1', name: 'Illya Klymov', phone: '+380504020799', role: 'Administrator' },
//        { id: '2', name: 'Ivanov Ivan', phone: '+380670000002', role: 'Student', strikes: 1 },
//        { id: '3', name: 'Petrov Petr', phone: '+380670000001', role: 'Support', location: 'Kiev' }

    @Test
    public void testGetUsersList() {
        //jsonobject json = api.getUsersList();
        //check that server resp with ok
        //assert that json contains 3 entities
    }

    @Test(dependsOnMethods = {"testGetUserList"})
    public void testAddRegularUser() {
//        1. Во всех запросах Content-Type: application/json
//        4. Создание нового пользователя было реализовано методом POST на winow.crudURL, в ответ приходил id новой созданной сущности
        //api.addUser(id); with no role
        //check that server resp with ok
        //jsonobject json = api.getUsersList()
        //assert that json object contains 4 entries including newly created one

    }

    @Test(dependsOnMethods = {"testGetUserList", "testAddRegularUser"})
    public void testEditUser() {
//        1. Во всех запросах Content-Type: application/json
//        3. Сохранение пользователя было реализовано методом PUT на на хост + '/' + id (где id - id редактируемого человека)

    }


    @Test(dependsOnMethods = {"testGetUserList"})
    public void testAddUserAdmin() {
//        5. При сохранении администратора надо было делать GET на /refreshAdmins на том же хосте где был хост
//        6. При создании администратора или помощника надо было передать в поле role имя роли, для студента было не надо

        //create admin user
        //assert that server responded with ok
        //assert that admins list refreshed
        //assert that newly created admin user contains role: admin

    }

    @Test(dependsOnMethods = {"testGetUserList"})
    public void testAddUserSupport() {
//        6. При создании администратора или помощника надо было передать в поле role имя роли, для студента было не надо
        //create support user
        //assert that server responded with ok
        //get users list
        //assert that newly created support user contains role: support

    }

    @Test
    public void testAddInvalidUserRole() {
        //    12. Если приходит некорректное поле role (смотри условие задания №4) - возвращать 401 ошибку
        //create admin user with role = 123
        //assert that server resp with 401
    }

    @Test
    public void testAddNoUserRoleForRegularUser() {
        //для студента было не надо
        //create student user with role = student
        //assert that server resp with ok
    }


    @Test(dependsOnMethods = {"testGetUserList", "testAddRegularUser"})
    public void testDeleteRegularUser() {
//        7. Для удаления пользователя необходимо отправить DELETE на хост + '/' + id (где id - id удаляемого человека)
        //deleteUser()
        //assert that server resp with ok
        //getUsersList()
        //assert that user deleted successfully

    }

    @Test(dependsOnMethods = {"testGetUserList", "testAddAdminUser"})
    public void testDeleteAdminUser(){
//        7. Для удаления пользователя необходимо отправить DELETE на хост + '/' + id (где id - id удаляемого человека)
        //deleteUser()
        //assert that server resp with ok
        //refresh???
        //getUsersList()
        //assert that user deleted successfully

    }

    @Test(dependsOnMethods = {"testGetUserList", "testAddRegularUser"})
    public void testDeleteUserNoId() {
//        13. Если происходит попытка модификации или удаления несуществующего id - сервер должен возвращать 404 ошибку
        //Sent DELETE to host with no id
        //assert that server responds with 404 error

    }

    @Test(dependsOnMethods = {"testGetUserList", "testAddRegularUser"})
    public void testDeleteUserInvalidId() {
        //13. Если происходит попытка модификации или удаления несуществующего id - сервер должен возвращать 404 ошибку
        //Sent DELETE with non existent id
        //assert that server responds with 404 error
    }

    @Test
    public void testInvalidContentType() {
//    10. Если клиент передает некорректный заголовок Content-Type либо он отсутствует - возвращать 401 ошибку
        //sent invalid content type
        //check that server resp with 401
    }

    @Test
    public void testNoContentType() {
//    10. Если клиент передает некорректный заголовок Content-Type либо он отсутствует - возвращать 401 ошибку
        //sent no content type
        //check that server resp with 401
    }




}
