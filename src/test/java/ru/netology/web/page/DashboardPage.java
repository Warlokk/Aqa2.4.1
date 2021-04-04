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
  private ElementsCollection cards = $$(".list__item");

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

  public static TransferPage transfer(String toCard){
    $( toCard + " [data-test-id=action-deposit]").click();
    return new TransferPage();
  }

}
