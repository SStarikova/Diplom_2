import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

public class OrderMethods extends Client {
    private static final String ORDER = "/api/orders";

    // Метод для создания заказа с авторизацией
    @Step("Create order with authorization")
    public ValidatableResponse createOrderWithAuth(Order order, String token) {
        if (token != null) {
            return spec()
                    .header("Authorization", token)
                    .body(order)
                    .when()
                    .post(ORDER)
                    .then().log().all();
        } else {
            return spec()
                    .body(order)
                    .when()
                    .post(ORDER)
                    .then().log().all();
        }
    }
    // Метод для создания заказа без авторизации
    @Step("Create order without authorization")
    public ValidatableResponse createOrderWithoutAuth(Order order) {
        return spec()
                .body(order)
                .when()
                .post(ORDER)
                .then().log().all();
    }
    // Метод для получения доступных ингредиентов
    @Step("Get available ingredients")
    public ValidatableResponse getAvailableIngredients() {
        return spec()
                .when()
                .get("/api/ingredients")
                .then().log().all();
    }
    // Метод для получения заказов пользователя
    @Step("Get orders")
    public ValidatableResponse getOrders(String token) {
        if (token != null) {
            return spec()
                    .header("Authorization", token)
                    .when()
                    .get(ORDER)
                    .then().log().all();
        } else {
            return spec()
                    .when()
                    .get(ORDER)
                    .then().log().all();
        }
    }
}