package ru.netology.web.page;


import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static org.openqa.selenium.Keys.*;

public class TransferPage {

    private SelenideElement heading = $("h1").shouldHave(exactText("Пополнение карты"));
    private SelenideElement amount = $("[data-test-id=amount] .input__control");
    private SelenideElement fromCard = $("[data-test-id=from] .input__control");
    private SelenideElement submitTransfer = $("[data-test-id=action-transfer]");
    private SelenideElement cancelTransfer = $("[data-test-id=action-cancel]");


    public TransferPage() {
        heading.shouldBe(visible);
    }



    public void transferAmount(String sum, String from) {
        amount.sendKeys(CONTROL +"a", DELETE);
        amount.setValue(sum);
        fromCard.sendKeys(CONTROL +"a", DELETE);
        fromCard.setValue(from);
        submitTransfer.click();

    }


}
