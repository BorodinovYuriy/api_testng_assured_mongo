package aqa.test.base;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.bson.Document;
import org.example.aqa.pojo.AuthRequestLogin;
import org.example.aqa.pojo.AuthResponse;
import org.example.utils.ConfigurationReader;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.asserts.SoftAssert;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class AQAAdminBaseTest {

    private static String baseURI;
    private static String login;
    private static String password;
    private static String token;
    private static String mongoUri;
    private static String mongoDbName;
    private static String mongoCollectionName;

    private static MongoClient mongoClient;
    private static MongoDatabase database;
    private static MongoCollection<Document> collection;


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
        collection = database.getCollection(mongoCollectionName);
        //Авторизация для получения токена
        authorization();
    }

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

        assertEquals(response.getStatusCode(), 200, "Status code should be 200");

        // Десериализуем ответ в AuthResponse
        AuthResponse authResponse = response.as(AuthResponse.class);

        token = authResponse.getToken(); // Сохраняем токен
        System.out.println("Токен получен: " + token); // Для отладки

        // Сравнение данных с MongoDB
        validateUserInMongoDB(login, password,authResponse);
    }

    private static void validateUserInMongoDB(String username, String password, AuthResponse authResponse) {
        // Мягкие проверки для сбора всех ошибок
        SoftAssert softAssert = new SoftAssert();
        Assert.assertNotNull(authResponse, "AuthResponse object is null.  Cannot proceed with validation.");
        Document userDocument = collection.find(Filters.eq("username", username)).first();
        Assert.assertNotNull(userDocument, "User not found in MongoDB with username: " + username);

        //Сравниваем username
        String mongoUsername = userDocument.getString("username");
        softAssert.assertEquals(authResponse.getUser()
                .getUsername(), mongoUsername,
                "Username mismatch between AuthResponse and MongoDB.");


        //сравниваем зашифрованный пароль (очень важно)
        String mongoPassword = userDocument.getString("password");
        softAssert.assertEquals(authResponse.getUser()
                .getPassword(), mongoPassword,
                "Encrypted password mismatch between AuthResponse and MongoDB.");

        //Сравниваем _id
        Integer mongoId = userDocument.getInteger("_id");
//  Теперь нужно преобразовать Integer в String, чтобы сравнить с authResponse.getUser().getId()
        softAssert.assertEquals(authResponse.getUser().getId(),
                String.valueOf(mongoId),
                "_id mismatch between AuthResponse and MongoDB.");

        //Сравниваем email (пример, добавьте остальные поля по аналогии)
        String mongoEmail = userDocument.getString("email");
        softAssert.assertEquals(authResponse.getUser().getEmail(),
                mongoEmail,
                "Email mismatch between AuthResponse and MongoDB.");

        //Сравниваем roles (пример, как сравнивать списки)
        List<String> mongoRoles = userDocument.getList("roles", String.class);
        softAssert.assertEquals(authResponse.getUser().getRoles(),
                mongoRoles,
                "Roles mismatch between AuthResponse and MongoDB.");

        //Проверяем все Assert-ы. Если хоть один из них упадет, то тест упадет
        softAssert.assertAll(); // Выполняем все проверки и выводим результаты
        System.out.println("User validation in MongoDB successful for user: " + username);
    }

    @AfterClass(description = "MongoDB connection closed. Afterclass method complete")
    public void closeMongoDBConnection() {
        if (mongoClient != null) {
            mongoClient.close();
            System.out.println("MongoDB connection closed.");
        }
    }

}
