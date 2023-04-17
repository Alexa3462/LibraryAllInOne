package com.library.steps;

import com.library.pages.BookPage;
import com.library.pages.LoginPage;
import com.library.utility.BrowserUtil;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class B2831_191_StepDef_UI_VS {
    LoginPage loginPage;
    BookPage bookPage;
    @And("I logged in Library UI as {string} - VS")
    public void iLoggedInLibraryUIAsVS(String userType) {
        loginPage=new LoginPage();
        loginPage.login(userType);
        BrowserUtil.waitFor(3);
    }

    @And("I navigate to {string} page - VS")
    public void iNavigateToPageVS(String moduleName) {
        bookPage = new BookPage();
        bookPage.navigateModule(moduleName);
        BrowserUtil.waitFor(1);
    }
}
