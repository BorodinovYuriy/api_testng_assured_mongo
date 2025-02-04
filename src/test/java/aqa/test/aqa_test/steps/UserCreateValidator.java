package aqa.test.aqa_test.steps;

import io.qameta.allure.Step;
import org.bson.Document;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

public class UserCreateValidator {

    @Step("Сверка тела ответа и ответа от БД")
    public static void checkJSONBody(Document responseBody, Document userDocumentInDb) {
        SoftAssert softAssert = new SoftAssert();

        Assert.assertNotNull(responseBody);
        Assert.assertNotNull(userDocumentInDb);

        softAssert.assertEquals(
                responseBody.get("data", Document.class).getString("username"),
                userDocumentInDb.getString("username"),
                "Username mismatch between Response and MongoDB."
        );

        softAssert.assertEquals(responseBody.get("data", Document.class).getString("email"),
                userDocumentInDb.getString("email"),
                "email mismatch between Response and MongoDB"
        );
    }
}
