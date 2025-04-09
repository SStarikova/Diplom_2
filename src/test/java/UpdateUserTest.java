import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ThreadLocalRandom;

/* Изменение данных пользователя:
с авторизацией,
без авторизации,
Для обеих ситуаций нужно проверить, что любое поле можно изменить.
Для неавторизованного пользователя — ещё и то, что система вернёт ошибку.*/

public class UpdateUserTest {
    private final UserMethods userMethods = new UserMethods();
    private final UserChecks userChecks = new UserChecks();
    private String accessToken;
    int random = ThreadLocalRandom.current().nextInt(100,100_000);

    @Before
    public void setUp() {
        User user = User.successUser();
        ValidatableResponse response = userMethods.createUser(user);
        accessToken = response.extract().path("accessToken");
    }

    @Test
    @DisplayName("Update user with authorization")
    @Description("Проверка на обновление данных пользователя с авторизацией")
    public void userCanBeUpdatedWithAuthTest() {
        User updated = new User("nesveta" + random + "@yandex.ru", "123456", "NeSveta");

        ValidatableResponse response = userMethods.updateUser(updated, accessToken);
        userChecks.checkUserDataUpdated(response, updated);
    }

    @Test
    @DisplayName("Update user without authorization cannot be updated")
    @Description("Проверка на получение ошибки при обновление данных пользователя без авторизации")
    public void userCannotBeUpdatedWithoutAuthTest() {
        User updated = new User("nesveta" + random + "@yandex.ru", "123456", "NeSveta");

        ValidatableResponse response = userMethods.updateUserUnauthorized(updated);
        userChecks.checkUnauthorized(response);
    }

    // удаляем созданного пользователя
    @After
    public void cleanUp() {
        if (accessToken != null) {
            userMethods.deleteUser(accessToken);
        }
    }
}
