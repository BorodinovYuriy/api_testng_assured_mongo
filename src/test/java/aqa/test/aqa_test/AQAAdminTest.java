package aqa.test.aqa_test;

import aqa.test.aqa_test.steps.UserAuthValidator;
import aqa.test.aqa_test.steps.UserCreateValidator;
import aqa.test.base.AQAAdminBaseTest;
import com.mongodb.client.MongoCollection;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.bson.Document;
import org.example.aqa.pojo.UserCreateRequest;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static io.restassured.RestAssured.given;

public class AQAAdminTest extends AQAAdminBaseTest {

    @Test(description = "Авторизация на портале - (сверка с базой)")
    @Step("Сверка авторизованного пользователя с базой")
    public void authUser() {
        MongoCollection<Document> collection = database.getCollection(mongoCollectionName);
        UserAuthValidator.validateUserInMongoDB(login, authResponse, collection);
    }

    @Test(description = "Добавление пользавателя")
    @Step("Добавление нового пользователя и проверка данных в базе")
    public void addUser() {
        String endpoint = "/api/user-auth1";
        UserCreateRequest userCreateRequest = createUserRequest();
        Response response = given()
                .spec(getRequestSpec())  // Используем общую спецификацию из BaseTest
                .header("Authorization", token) // Добавляем токен авторизации
                .body(userCreateRequest)
                .when()
                .post(endpoint);

        checkStatusCode(response, 200);

        String newUsername = userCreateRequest.getUsername();
        MongoCollection<Document> collection = database.getCollection(mongoCollectionName);

        UserCreateValidator.checkJSONBody(
                Document.parse(response.getBody().asString()),
                Objects.requireNonNull(collection.find(new Document("username", newUsername)).first())
        );

        // 6. Вывод в логи
        System.out.println("User added successfully and verified in database.");
    }

    @Test(description = "Добавление вопроса")
    public void addQuestion() {
    }

    @Test(description = "Редактирование вопроса")
    public void editQuestion() {
    }

    @Test(description = "Добавление квиза")
    public void addQuiz() {
    }

    @Test(description = "Добавление модуля")
    public void addModule() {
    }

    @Test(description = "Добавление курса")
    public void addCurse() {
    }

    @Test(description = "Добавление экзамена")
    public void addExam() {
    }

    @Test(description = "Добавление темплейта")
    public void addTemplate() {
    }

    @Test(description = "Авторизация с неверным логином или паролем")
    public void wrongCredential() {
    }

    private UserCreateRequest createUserRequest() {
        UserCreateRequest userCreateRequest = new UserCreateRequest();
        String username = "test";
        userCreateRequest.setUsername("test");
        userCreateRequest.setPassword("test");
        userCreateRequest.setEmail("test");
        userCreateRequest.setFirstName("test");
        userCreateRequest.setSurname("test");
        userCreateRequest.setName("test");

        List<String> roles = new ArrayList<>();
        roles.add("admin");
        userCreateRequest.setRoles(roles);

        List<String> positions = new ArrayList<>();
        positions.add("tester");
        userCreateRequest.setPositions(positions);

        List<String> cities = new ArrayList<>();
        cities.add("Moscow");
        userCreateRequest.setCities(cities);

        List<String> companies = new ArrayList<>();
        companies.add("Exceed Team");
        userCreateRequest.setCompanies(companies);

        return userCreateRequest;
    }
}
//    },
//            "first_name": "test",
//            "surname": "test",
//            "email": "test",
//            "username": "test",
//            "plain_password": "test",
//            "roles": "admin"
//            }

