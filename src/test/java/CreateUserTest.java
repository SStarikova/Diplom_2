import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;

/* Создание пользователя:
создать уникального пользователя;
создать пользователя, который уже зарегистрирован;
создать пользователя и не заполнить одно из обязательных полей.*/

public class CreateUserTest {
    private final UserMethods userMethods = new UserMethods();
    private final UserChecks userChecks = new UserChecks();
    private String accessToken;

    @Test
    @DisplayName("Successfully create")
    @Description("Проверка на успешное создание пользователя.")
    public void userCanCreateSuccessfullyTest() {
        User user = User.successUser();
        ValidatableResponse response = userMethods.createUser(user);
        accessToken = response.extract().path("accessToken");

        userChecks.checkSuccess(response);
    }

    @Test
    @DisplayName("User cannot create twice")
    @Description("Проверка на получение ошибки при повторном создании пользователя")
    public void userCannotCreateTwiceTest() {
        User user = User.successUser();
        ValidatableResponse firstResponse = userMethods.createUser(user);
        accessToken = firstResponse.extract().path("accessToken");

        ValidatableResponse secondResponse = userMethods.createUser(user);

        userChecks.checkUserAlreadyExists(secondResponse);
    }

    @Test
    @DisplayName("Create without email")
    @Description("Проверка на получение ошибки при создании пользователя без почты")
    public void createWithoutEmailTest() {
        User user = User.withoutEmail();
        ValidatableResponse createResponse = userMethods.createUser(user);

        userChecks.checkMissingFieldError(createResponse);
    }

    @Test
    @DisplayName("Create without password")
    @Description("Проверка на получение ошибки при создании пользователя без пароля")
    public void createWithoutPasswordTest() {
        User user = User.withoutPassword();
        ValidatableResponse createResponse = userMethods.createUser(user);

        userChecks.checkMissingFieldError(createResponse);
    }

    @Test
    @DisplayName("Create without name")
    @Description("Проверка на получение ошибки при создании пользователя без имени")
    public void createWithoutNameTest() {
        User user = User.withoutName();
        ValidatableResponse createResponse = userMethods.createUser(user);

        userChecks.checkMissingFieldError(createResponse);
    }

    // удаляем созданного пользователя
    @After
    public void cleanUp() {
        if (accessToken != null) {
            userMethods.deleteUser(accessToken);
        }
    }
}