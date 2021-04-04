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
        int expectedFirst = first.getCardBalance();
        assertEquals(expectedFirst, actualFirst);

        int actualSecond = DashboardPage.getCardBalance(second.getId());
        int expectedSecond = second.getCardBalance();
        assertEquals(expectedSecond, actualSecond);

    }

    @Test
    void shouldTransferMoneyFromSecondToFirst() {

        new DashboardPage().transfer(first.getId());
        new TransferPage().transferAmount(Integer.toString(transferAmount), second.getCardNumber().strip());

        int actualFirst = DashboardPage.getCardBalance(first.getId());
        int expectedFirst = first.getCardBalance();
        assertEquals(expectedFirst, actualFirst);

        int actualSecond = DashboardPage.getCardBalance(second.getId());
        int expectedSecond = second.getCardBalance();
        assertEquals(expectedSecond, actualSecond);
    }

    private String overDraft(int cardBalance) {

        int amount = cardBalance + 1;
        return Integer.toString(amount);
    }

    @Test
    void shouldNotTransferOverDraft() {

        Cards toCard = first;
        Cards fromCard = second;
        new DashboardPage().transfer(toCard.getId());
        String overDraft = overDraft(fromCard.getCardBalance());
        new TransferPage().transferAmount(overDraft, fromCard.getCardNumber().strip());

        int actualToCardBalance = DashboardPage.getCardBalance(toCard.getId());
        int expectedToCardBalance = toCard.getCardBalance();
        assertEquals(expectedToCardBalance, actualToCardBalance);

        int actualFromCardBalance = DashboardPage.getCardBalance(fromCard.getId());
        int expectedFromCardBalance = fromCard.getCardBalance();
        assertEquals(expectedFromCardBalance, actualFromCardBalance);
    }


}

