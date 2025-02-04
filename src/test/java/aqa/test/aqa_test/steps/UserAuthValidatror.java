package aqa.test.aqa_test.steps;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import io.qameta.allure.Step;
import org.bson.Document;
import org.example.aqa.pojo.AuthResponse;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

public class UserAuthValidatror {

    @Step("Проверка тела ответа, сравнение данных пользователя с базой")
    public static void validateUserInMongoDB(String username, AuthResponse authResponse, MongoCollection<Document> collection) {
        // Мягкие проверки для сбора всех ошибок
        SoftAssert softAssert = new SoftAssert();

        Assert.assertNotNull(
                authResponse,
                "AuthResponse object is null.  Cannot proceed with validation.");

        Document userDocument = collection.find(
                Filters.eq("username", username)).first();
        Assert.assertNotNull(
                userDocument,
                "User not found in MongoDB with username: " + username);

        //Сравниваем username
        softAssert.assertEquals(
                authResponse.getUser().getUsername(),
                userDocument.getString("username"),
                "Username mismatch between AuthResponse and MongoDB.");

        //сравниваем зашифрованный пароль (очень важно)
        softAssert.assertEquals(
                authResponse.getUser().getPassword(),
                userDocument.getString("password"),
                "Encrypted password mismatch between AuthResponse and MongoDB.");

        //Сравниваем _id
        softAssert.assertEquals(
                authResponse.getUser().getId(),
                String.valueOf(userDocument.getInteger("_id")),
                "_id mismatch between AuthResponse and MongoDB.");

        //Сравниваем email (пример, добавьте остальные поля по аналогии)
        softAssert.assertEquals(
                authResponse.getUser().getEmail(),
                userDocument.getString("email"),
                "Email mismatch between AuthResponse and MongoDB.");

        //Сравниваем roles (пример, как сравнивать списки)
        softAssert.assertEquals(
                authResponse.getUser().getRoles(),
                userDocument.getList("roles", String.class),
                "Roles mismatch between AuthResponse and MongoDB.");

        //Проверяем все Assert-ы. Если хоть один из них упадет, то тест упадет
        softAssert.assertAll(); // Выполняем все проверки и выводим результаты
        System.out.println("User validation in MongoDB successful for user: " + username);
    }
}
