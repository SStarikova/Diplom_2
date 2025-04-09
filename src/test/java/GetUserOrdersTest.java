import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/* Получение заказов конкретного пользователя:
авторизованный пользователь,
неавторизованный пользователь.*/

public class GetUserOrdersTest {

    private final UserMethods userMethods = new UserMethods();
    private final OrderMethods orderMethods = new OrderMethods();
    private final OrderChecks orderChecks = new OrderChecks();
    private String accessToken;

    @Before
    public void setUp() {
        User user = User.successUser();
        ValidatableResponse response = userMethods.createUser(user);
        accessToken = response.extract().path("accessToken");

        // Создать хотя бы один заказ, чтобы было что получать
        List<String> ingredients = orderMethods.getAvailableIngredients().extract().path("data._id");
        Order order = Order.validOrder(ingredients);
        orderMethods.createOrderWithAuth(order, accessToken);
    }

    @Test
    @DisplayName("Get orders of an authorized user")
    @Description("Проверка получения заказов авторизованного пользователя")
    public void getOrdersWithAuthTest() {
        ValidatableResponse response = orderMethods.getOrders(accessToken);
        orderChecks.checkGetOrdersWithAuth(response);
    }

    @Test
    @DisplayName("Get orders of an unauthorized user")
    @Description("Проверка получения заказов неавторизованного пользователя")
    public void getOrdersWithoutAuthTest() {
        ValidatableResponse response = orderMethods.getOrders(null);
        orderChecks.checkGetOrdersWithoutAuth(response);
    }

    @After
    public void cleanUp() {
        if (accessToken != null) {
            userMethods.deleteUser(accessToken);
        }
    }
}