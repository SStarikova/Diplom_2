import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

/* Методы для создания, авторизации, обновления и удаления курьера */
public class UserMethods extends Client{
    private static final String USER = "/api/auth/";

    // Метод для создания пользователя
    @Step("Create user")
    // Создание курьера
    public ValidatableResponse createUser(User user) {
        return spec()
                .body(user)
                .when()
                .post(USER + "register")
                .then().log().all();
    }
    // Метод для авторизации пользователя
    @Step("Login user")
    public ValidatableResponse logIn(User user) {
        return spec()
                .body(user)
                .when()
                .post(USER + "login")
                .then().log().all();
    }
    // Метод для удаления пользователя
    @Step("Delete user")
    public ValidatableResponse deleteUser(String token) {
         return spec()
                 .header("Authorization", token.startsWith("Bearer ") ? token : "Bearer " + token)
                .when()
                .delete(USER + "user")
                .then().log().all();
    }
    //Метод для обновления пользователя с токеном
    @Step("Update user with token")
    public ValidatableResponse updateUser(User user, String token) {
        return spec()
                .header("Authorization", token.startsWith("Bearer ") ? token : "Bearer " + token)
                .body(user)
                .when()
                .patch(USER + "user")
                .then().log().all();
    }
    //Метод для обновления пользователя без токена
    @Step("Update user without token")
    public ValidatableResponse updateUserUnauthorized(User user) {
        return spec()
                .body(user)
                .when()
                .patch(USER + "user")
                .then().log().all();
    }
}
