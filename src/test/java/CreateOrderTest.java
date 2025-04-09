import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/* Создание заказа:
с авторизацией,
без авторизации,
с ингредиентами,
без ингредиентов,
с неверным хешем ингредиентов.*/

public class CreateOrderTest {

    private final UserMethods userMethods = new UserMethods();
    private final OrderMethods orderMethods = new OrderMethods();
    private final OrderChecks orderChecks = new OrderChecks();
    private String accessToken;

    private List<String> getValidIngredients() {
        return orderMethods.getAvailableIngredients()
                .extract()
                .path("data._id");
    }

    @Before
    public void setUp() {
        User user = User.successUser();
        ValidatableResponse response = userMethods.createUser(user);
        accessToken = response.extract().path("accessToken");
    }

    @Test
    @DisplayName("Create order with authorization and valid ingredients")
    @Description("Проверка на успешное создание заказа с авторизацией и валидными ингредиентами")
    public void orderCanBeCreatedWithAuthAndValidIngredientsTest() {
        List<String> ingredients = getValidIngredients();
        Order order = Order.validOrder(ingredients);

        ValidatableResponse response = orderMethods.createOrderWithAuth(order, accessToken);
        orderChecks.checkOrderCreatedSuccessfully(response);
    }

    @Test
    @DisplayName("Create order without authorization")
    @Description("Проверка на создание заказа без авторизации")
    public void orderCanBeCreatedWithoutAuthTest() {
        List<String> ingredients = getValidIngredients();
        Order order = Order.validOrder(ingredients);

        ValidatableResponse response = orderMethods.createOrderWithoutAuth(order);
        orderChecks.checkOrderCreatedSuccessfully(response);
    }

    @Test
    @DisplayName("Create order without ingredients")
    @Description("Проверка на невозможность создания заказа без ингредиентов")
    public void orderCannotBeCreatedWithoutIngredientsTest() {
        Order order = Order.emptyOrder();

        ValidatableResponse response = orderMethods.createOrderWithAuth(order, accessToken);
        orderChecks.checkOrderCreationWithoutIngredientsFails(response);
    }

    @Test
    @DisplayName("Create order with invalid ingredients")
    @Description("Проверка на невозможность создания заказа с невалидными ингредиентами")
    public void orderCannotBeCreatedWithInvalidIngredientsTest() {
        Order order = Order.invalidOrder();

        ValidatableResponse response = orderMethods.createOrderWithAuth(order, accessToken);
        orderChecks.checkOrderCreationWithInvalidIngredientsFails(response);
    }

    // удаляем созданного пользователя
    @After
    public void cleanUp() {
        if (accessToken != null) {
            userMethods.deleteUser(accessToken);
        }
    }
}
