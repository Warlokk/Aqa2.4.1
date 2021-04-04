package ru.netology.web.test;

import lombok.val;
import org.junit.jupiter.api.*;
import ru.netology.web.page.*;
import ru.netology.web.page.TransferPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.web.data.DataHelper.*;


class MoneyTransferTest {

    Cards first = firstCard();
    Cards second = secondCard();
    int transferAmount = 5000;
    int overDraft = 15000;


    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
        val login = new LoginPage();
        login.validLogin(getAuthInfo()).validVerify(getVerificationCodeFor(getAuthInfo()));
        int balanceFirstCard = DashboardPage.getCardBalance(first.getId());
        int balanceSecondCard = DashboardPage.getCardBalance(second.getId());
        resetBalance(balanceFirstCard, balanceSecondCard);

    }


    @Test
    void shouldTransferMoneyFromFirstToSecond() {

        new DashboardPage().transfer(second.getId());
        new TransferPage().transferAmount(Integer.toString(transferAmount), first.getCardNumber().strip());
        int actualFirst = DashboardPage.getCardBalance(first.getId());
        int actualSecond = DashboardPage.getCardBalance(second.getId());
        int expectedFirst = first.getCardBalance() - transferAmount;
        int expectedSecond = second.getCardBalance() + transferAmount;
        assertEquals(expectedFirst, actualFirst);
        assertEquals(expectedSecond, actualSecond);

    }

    @Test
    void shouldTransferMoneyFromSecondToFirst() {

        new DashboardPage().transfer(first.getId());
        new TransferPage().transferAmount(Integer.toString(transferAmount), second.getCardNumber().strip());
        int actualFirst = DashboardPage.getCardBalance(first.getId());
        int actualSecond = DashboardPage.getCardBalance(second.getId());
        int expectedFirst = first.getCardBalance() + transferAmount;
        int expectedSecond = second.getCardBalance() - transferAmount;
        assertEquals(expectedFirst, actualFirst);
        assertEquals(expectedSecond, actualSecond);
    }

    @Test
    void shouldNotTransferOverDraft() {

        new DashboardPage().transfer(first.getId());
        new TransferPage().transferAmount(Integer.toString(overDraft), second.getCardNumber().strip());
        int actualFirst = DashboardPage.getCardBalance(first.getId());
        int actualSecond = DashboardPage.getCardBalance(second.getId());
        int expectedFirst = first.getCardBalance();
        int expectedSecond = second.getCardBalance();
        assertEquals(expectedFirst, actualFirst);
        assertEquals(expectedSecond, actualSecond);
    }


}

