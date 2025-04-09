import java.util.concurrent.ThreadLocalRandom;

public class User {

    private String email;
    private String password;
    private String name;
    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    // Создание валидного пользователя
    public static User successUser() {
        int random = ThreadLocalRandom.current().nextInt(100,100_000);
        return new User("sveta" + random + "@yandex.ru", "1234567890", "Sveta");
    }
    // Создание пользователя без почты
    public static User withoutEmail() {
        return new User(null, "1234567890", "Sveta");
    }
    // Создание пользователя без пароля
    public static User withoutPassword() {
        int random = ThreadLocalRandom.current().nextInt(100,100_000);
        return new User("sveta" + random + "@yandex.ru", null, "Sveta");
    }
    // Создание пользователя без имени
    public static User withoutName() {
        int random = ThreadLocalRandom.current().nextInt(100,100_000);
        return new User("sveta" + random + "@yandex.ru", "1234567890", null);
    }
    public User() {
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
