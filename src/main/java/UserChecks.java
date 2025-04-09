import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import java.net.HttpURLConnection;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class UserChecks {
    // Проверка на то, что пользователь создан
    @Step("User is created")
    public void checkSuccess(ValidatableResponse response) {
        response
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_OK)
                .body("success", equalTo(true));
    }
    // Проверка на то, что пользователь уже существует
    @Step("User is already exist")
    public void checkUserAlreadyExists(ValidatableResponse response) {
        response
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_FORBIDDEN)
                .assertThat()
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
    }
    // Проверка на то, что пользователь не создан (отсутствует обязательное поле)
    @Step("User is not created")
    public void checkMissingFieldError(ValidatableResponse response) {
        response
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_FORBIDDEN)
                .assertThat()
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }
    // Проверка на успешную авторизацию пользователя
    @Step("Success login")
    public void checkUserLoggedIn(ValidatableResponse response, String expectedToken) {
        response.assertThat()
                .statusCode(HttpURLConnection.HTTP_OK)
                .body("success", equalTo(true))
                .body("accessToken", notNullValue());
    }
    // Проверка на то, что пользователь не авторизован
    @Step("User is unauthorized")
    public void checkUnauthorized(ValidatableResponse response) {
        response.assertThat()
                .statusCode(HttpURLConnection.HTTP_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }
    //Проверка на то, что указаны неверные учетные данные
    @Step("Invalid credentials")
    public void checkInvalidCredentials(ValidatableResponse response) {
        response.assertThat()
                .statusCode(HttpURLConnection.HTTP_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }
    //Проверка на успешное обновление данных пользователя
    @Step("Success user data updated")
    public void checkUserDataUpdated(ValidatableResponse response, User expectedUser) {
        response.assertThat()
                .statusCode(HttpURLConnection.HTTP_OK)
                .body("success", equalTo(true))
                .body("user.email", equalTo(expectedUser.getEmail().toLowerCase()))
                .body("user.name", equalTo(expectedUser.getName()));
    }
}
