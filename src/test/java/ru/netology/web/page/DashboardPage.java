package ru.netology.web.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.val;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;


public class DashboardPage {
    private SelenideElement heading = $("[data-test-id=dashboard]");
    private static final String balanceStart = "баланс: \n";
    private static final String balanceFinish = "\n р. ";
    private static final String transferButton = " [data-test-id=action-deposit]";

    public DashboardPage() {
        heading.shouldBe(visible);
    }

    public static int getCardBalance(String id) {
        String text = $(id).getOwnText();
        return extractBalance(text);
    }

    private static int extractBalance(String text) {
        val start = text.indexOf(balanceStart);
        val finish = text.indexOf(balanceFinish);
        val value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value);
    }

    public static TransferPage transfer(String toCard) {
        $(toCard + transferButton).click();
        return new TransferPage();
    }

}
