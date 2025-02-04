package aqa.test.aqa_test;

import aqa.test.aqa_test.steps.UserAuthValidatror;
import aqa.test.base.AQAAdminBaseTest;
import com.mongodb.client.MongoCollection;
import io.qameta.allure.Step;
import org.bson.Document;
import org.testng.annotations.Test;

public class AQAAdminTest extends AQAAdminBaseTest {

    @Test(description = "Авторизация на портале - (сверка с базой)")
    @Step("Сверка авторизованного пользователя с базой")
    public void authUser(){
        MongoCollection<Document> collection = database.getCollection(mongoCollectionName);
        UserAuthValidatror.validateUserInMongoDB(login, authResponse, collection);
    }
    @Test(description = "Добавление юзера()")
    public void addUser(){}

    @Test(description = "Добавление вопроса")
    public void addQuestion(){}

    @Test(description = "Редактирование вопроса")
    public void editQuestion(){}

    @Test(description = "Добавление квиза")
    public void addQuiz(){}

    @Test(description = "Добавление модуля")
    public void addModule(){}

    @Test(description = "Добавление курса")
    public void addCurse(){}

    @Test(description = "Добавление экзамена")
    public void addExam(){}

    @Test(description = "Добавление темплейта")
    public void addTemplate(){}

    @Test(description = "Авторизация с неверным логином или паролем")
    public void wrongCredential(){}

}

