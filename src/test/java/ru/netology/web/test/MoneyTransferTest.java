package ru.netology.web.test;

import lombok.val;
import org.junit.jupiter.api.*;
import ru.netology.web.page.*;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.web.data.DataHelper.*;


class MoneyTransferTest {
    private DashboardPage dashboardPage;

    int transferAmount = 5000;


    @BeforeEach
    void setUp() {

        open("http://localhost:9999");
        val login = new LoginPage();
        dashboardPage = login.validLogin(getAuthInfo()).validVerify(getVerificationCodeFor(getAuthInfo()));
        val balanceFirstCard = dashboardPage.getCardBalance(firstCard().getId());
        val balanceSecondCard = dashboardPage.getCardBalance(secondCard().getId());
        resetBalance(dashboardPage, balanceFirstCard, balanceSecondCard);

    }


    @Test
    void shouldTransferMoneyFromFirstToSecond() {

        val transferPage = dashboardPage.transfer(secondCard().getId());
        transferPage.transferAmount(Integer.toString(transferAmount), firstCard().getCardNumber().strip());

        val actualFirst = dashboardPage.getCardBalance(firstCard().getId());
        val expectedFirst = firstCard().getCardBalance() - transferAmount;
        assertEquals(expectedFirst, actualFirst);

        val actualSecond = dashboardPage.getCardBalance(secondCard().getId());
        val expectedSecond = secondCard().getCardBalance() + transferAmount;
        assertEquals(expectedSecond, actualSecond);

    }

    @Test
    void shouldTransferMoneyFromSecondToFirst() {

        val transferPage = dashboardPage.transfer(firstCard().getId());
        transferPage.transferAmount(Integer.toString(transferAmount), secondCard().getCardNumber().strip());

        val actualFirst = dashboardPage.getCardBalance(firstCard().getId());
        val expectedFirst = firstCard().getCardBalance() + transferAmount;
        assertEquals(expectedFirst, actualFirst);

        val actualSecond = dashboardPage.getCardBalance(secondCard().getId());
        val expectedSecond = secondCard().getCardBalance() - transferAmount;
        assertEquals(expectedSecond, actualSecond);
    }

    private String overDraft(int cardBalance) {

        int amount = cardBalance + 1;
        return Integer.toString(amount);
    }

    @Test
    void shouldNotTransferOverDraft() {

        Cards toCard = firstCard();
        Cards fromCard = secondCard();
        val transferPage = dashboardPage.transfer(toCard.getId());
        String overDraft = overDraft(fromCard.getCardBalance());
        transferPage.transferAmount(overDraft, fromCard.getCardNumber().strip());

        val actualToCardBalance = dashboardPage.getCardBalance(toCard.getId());
        val expectedToCardBalance = toCard.getCardBalance();
        assertEquals(expectedToCardBalance, actualToCardBalance);

        val actualFromCardBalance = dashboardPage.getCardBalance(fromCard.getId());
        val expectedFromCardBalance = fromCard.getCardBalance();
        assertEquals(expectedFromCardBalance, actualFromCardBalance);
    }


}

