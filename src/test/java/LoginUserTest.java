import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ThreadLocalRandom;

/* Логин пользователя:
логин под существующим пользователем,
логин с неверным логином и паролем.*/

public class LoginUserTest {
    private final UserMethods userMethods = new UserMethods();
    private final UserChecks userChecks = new UserChecks();
    private User user;
    private String accessToken;

    @Before
    public void setUp() {
        user = User.successUser();
        ValidatableResponse response = userMethods.createUser(user);
        accessToken = response.extract().path("accessToken");
    }

    @Test
    @DisplayName("Successfully login")
    @Description("Проверка на успешную авторизацию существующего пользователя")
    public void userCanLoginSuccessfullyTest() {
        ValidatableResponse response = userMethods.logIn(user);
        userChecks.checkUserLoggedIn(response);
    }

    @Test
    @DisplayName("Login with wrong password")
    @Description("Проверка на провальную авторизацию с неверным паролем")
    public void userCannotLoginWithWrongPasswordTest() {
        user.setPassword("9876543210");
        ValidatableResponse response = userMethods.logIn(user);
        userChecks.checkInvalidCredentials(response);
    }

    @Test
    @DisplayName("Login unregistered user")
    @Description("Проверка на провальную авторизацию несуществующего пользователя")
    public void userCannotLoginUnregisteredUserTest() {
        int random = ThreadLocalRandom.current().nextInt(100,100_000);
        User fake = new User("nesveta" + random + "@yandex.ru", "9876543210", "NeSveta");
        ValidatableResponse response = userMethods.logIn(fake);
        userChecks.checkInvalidCredentials(response);
    }

    // удаляем созданного пользователя
    @After
    public void cleanUp() {
        if (accessToken != null) {
            userMethods.deleteUser(accessToken);
        }
    }
}