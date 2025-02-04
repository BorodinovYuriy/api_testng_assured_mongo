package aqa.test.base;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.example.aqa.pojo.AuthRequestLogin;
import org.example.aqa.pojo.AuthResponse;
import org.example.utils.ConfigurationReader;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class AQAAdminBaseTest {

    private static String baseURI;
    protected static String login;
    private static String password;
    protected static String token;
    private static String mongoUri;
    private static String mongoDbName;
    protected static String mongoCollectionName;

    protected static MongoClient mongoClient;
    protected static MongoDatabase database;
    protected static AuthResponse authResponse;
    private static RequestSpecification requestSpec;
    private static ResponseSpecification responseSpec;

    public AQAAdminBaseTest() {
        this.baseURI = ConfigurationReader.getProperty("baseURI");
        this.login = ConfigurationReader.getProperty("login");
        this.password = ConfigurationReader.getProperty("password");
        this.mongoUri = ConfigurationReader.getProperty("mongoUri");
        this.mongoDbName = ConfigurationReader.getProperty("mongoDbName");
        this.mongoCollectionName = ConfigurationReader.getProperty("mongoCollectionName");

    }
    public static RequestSpecification getRequestSpec() {
        return requestSpec;
    }

    public static ResponseSpecification getResponseSpec() {
        return responseSpec;
    }

    @BeforeClass(description = "Установка спецификации, авторизация на портале, получение токена")
    public static void setup() {
        RestAssured.baseURI = baseURI;

        requestSpec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(baseURI)
                .build();

        responseSpec = new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .build();

        // Инициализация MongoClient
        mongoClient = MongoClients.create(mongoUri);
        database = mongoClient.getDatabase(mongoDbName);
        authorization();
    }
    @Step("Отправка запроса на авторизацию")
    private static void authorization() {
        AuthRequestLogin authRequestLogin = new AuthRequestLogin();
        authRequestLogin.setUsername(login);
        authRequestLogin.setPassword(password);

        // Используем Response для получения полного ответа
        Response response = given()
                .spec(requestSpec)
                .body(authRequestLogin)
                .when()
                .post("/api/auth/login");

        checkStatusCode(response,200);
        // Десериализуем ответ в AuthResponse
        authResponse = response.as(AuthResponse.class);

        token = authResponse.getToken(); // Сохраняем токен
        System.out.println("Токен получен: " + token); // Для отладки

    }
    @Step("Проверка кода состояния ответа: {statusCode}")
    public static void checkStatusCode(Response response, int statusCode) {
        assertEquals(response.getStatusCode(), statusCode, "Status code должен быть " + statusCode);
    }
    @AfterClass(description = "MongoDB connection closed. Afterclass method complete")
    public void closeMongoDBConnection() {
        mongoClient.close();
        System.out.println("MongoDB connection closed.");
    }

}