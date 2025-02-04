package aqa.test.aqa_test;


import aqa.test.base.AQAAdminBaseTest;
import io.restassured.http.ContentType;
import org.example.aqa.pojo.AuthRequestLogin;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class AQAAdminTest extends AQAAdminBaseTest {


    @Test(description = "Добавление юзера")
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
