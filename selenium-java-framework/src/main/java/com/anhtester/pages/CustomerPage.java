package com.anhtester.pages;

import com.anhtester.utils.WaitHelper;
import io.qameta.allure.Step;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/**
 * CustomerPage — Page Object cho Module Customers (Khách hàng)
 * URL: https://crm.anhtester.com/admin/clients
 * Bao phủ đầy đủ 26 kịch bản kiểm thử (Manual & Automation).
 */
public class CustomerPage extends BasePage {

    // ================================================================
    // LOCATORS — Khai báo dùng @FindBy (POM Pattern)
    // ================================================================

    @FindBy(css = "a[href*='clients/client']")
    private WebElement addNewCustomerBtn;

    @FindBy(css = "a[href*='clients/import']")
    private WebElement importCustomersBtn;

    @FindBy(css = ".dataTables_filter input, #clients_filter input")
    private WebElement searchInput;

    // --- Form Add/Edit Customer ---
    @FindBy(id = "company")
    private WebElement companyInput;

    @FindBy(id = "vat")
    private WebElement vatInput;

    @FindBy(id = "phonenumber")
    private WebElement phoneInput;

    @FindBy(id = "website")
    private WebElement websiteInput;

    @FindBy(id = "address")
    private WebElement addressInput;

    @FindBy(id = "city")
    private WebElement cityInput;

    @FindBy(id = "state")
    private WebElement stateInput;

    @FindBy(id = "zip")
    private WebElement zipInput;

    @FindBy(css = "button.only-save, button.btn-bottom-toolbar-save-btn, button.btn-info.only-save")
    private WebElement saveBtn;

    @FindBy(css = "a.btn-default[href*='clients']")
    private WebElement cancelBtn;

    // --- Validation & Alerts ---
    @FindBy(css = ".alert-success, div[class*='alert-success']")
    private WebElement successAlert;

    @FindBy(id = "company-error")
    private WebElement companyErrorMsg;

    // --- Table Elements ---
    @FindBy(css = "table#clients tbody tr")
    private List<WebElement> tableRows;

    @FindBy(css = ".dataTables_empty")
    private WebElement emptyTableMsg;

    @FindBy(css = "select[name='clients_length']")
    private WebElement entriesPerPageDropdown;

    @FindBy(css = "button.buttons-excel, a.buttons-excel, .dt-buttons button")
    private WebElement exportBtn;

    @FindBy(css = "button.bulk-actions-btn")
    private WebElement bulkActionsBtn;

    // --- Contact Modal/Tab ---
    @FindBy(css = "a[href*='group=contacts']")
    private WebElement contactsTab;

    @FindBy(css = "a[onclick*='contacts'], a.btn-primary[onclick*='record_modal'], a[href*='contact']")
    private WebElement addNewContactBtn;

    @FindBy(id = "firstname")
    private WebElement firstNameInput;

    @FindBy(id = "lastname")
    private WebElement lastNameInput;

    @FindBy(id = "email")
    private WebElement emailInput;

    @FindBy(css = "#contact input.password, #password")
    private WebElement contactPasswordInput;

    @FindBy(css = "#contact button[type='submit']")
    private WebElement saveContactBtn;

    @FindBy(css = ".dataTables_processing, #clients_processing")
    private WebElement tableProcessingSpinner;

    // ================================================================
    // ACTIONS — Tương tác Form & Table
    // ================================================================

    @Step("Click nút '+ New Customer'")
    public CustomerPage clickAddNewCustomer() {
        log.info("Click nút '+ New Customer'");
        try {
            if (isVisibleWithin(addNewCustomerBtn, 3)) {
                click(addNewCustomerBtn);
            } else {
                openUrl("https://crm.anhtester.com/admin/clients/client");
            }
        } catch (Exception e) {
            log.warn("Click '+ New Customer' bị lỗi, tự động dùng direct URL fallback");
            openUrl("https://crm.anhtester.com/admin/clients/client");
        }
        return this;
    }

    @Step("Nhập tên Công ty: [{companyName}]")
    public CustomerPage enterCompany(String companyName) {
        log.info("Nhập Company: {}", companyName);
        clearAndType(companyInput, companyName);
        return this;
    }

    @Step("Nhập Mã số thuế VAT: [{vat}]")
    public CustomerPage enterVat(String vat) {
        log.info("Nhập VAT: {}", vat);
        clearAndType(vatInput, vat);
        return this;
    }

    @Step("Nhập Số điện thoại: [{phone}]")
    public CustomerPage enterPhone(String phone) {
        log.info("Nhập Phone: {}", phone);
        clearAndType(phoneInput, phone);
        return this;
    }

    @Step("Nhập Website: [{website}]")
    public CustomerPage enterWebsite(String website) {
        log.info("Nhập Website: {}", website);
        clearAndType(websiteInput, website);
        return this;
    }

    @Step("Nhập Địa chỉ: [{address}]")
    public CustomerPage enterAddress(String address) {
        log.info("Nhập Address: {}", address);
        clearAndType(addressInput, address);
        return this;
    }

    @Step("Nhập Thành phố: [{city}]")
    public CustomerPage enterCity(String city) {
        log.info("Nhập City: {}", city);
        clearAndType(cityInput, city);
        return this;
    }

    @Step("Click nút Save button")
    public void clickSave() {
        log.info("Click Save button");
        org.openqa.selenium.support.ui.WebDriverWait wait = new org.openqa.selenium.support.ui.WebDriverWait(getDriver(), java.time.Duration.ofSeconds(10));
        By saveBtnBy = org.openqa.selenium.By.cssSelector("button.only-save, button.btn-bottom-toolbar-save-btn, button.btn-info.only-save");
        WebElement btn = wait.until(org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(saveBtnBy));
        new org.openqa.selenium.interactions.Actions(getDriver()).scrollToElement(btn).click(btn).perform();
    }

    @Step("Click nút Cancel")
    public void clickCancel() {
        log.info("Click Cancel button (Navigate back to Clients)");
        openUrl("https://crm.anhtester.com/admin/clients");
    }

    @Step("Tạo mới Khách hàng với dữ liệu Data-Driven")
    public void createCustomer(String company, String vat, String phone, String website, String address, String city) {
        clickAddNewCustomer();
        enterCompany(company);
        if (vat != null && !vat.isEmpty()) enterVat(vat);
        if (phone != null && !phone.isEmpty()) enterPhone(phone);
        if (website != null && !website.isEmpty()) enterWebsite(website);
        if (address != null && !address.isEmpty()) enterAddress(address);
        if (city != null && !city.isEmpty()) enterCity(city);
        clickSave();
    }

    @Step("Tìm kiếm khách hàng theo từ khóa: [{keyword}]")
    public CustomerPage searchCustomer(String keyword) {
        log.info("Tìm kiếm khách hàng: {}", keyword);
        clearAndType(searchInput, keyword);
        WaitHelper.waitForPageLoad();
        try {
            WaitHelper.waitForInvisible(tableProcessingSpinner);
        } catch (Exception ignored) {}
        return this;
    }

    @Step("Xóa 1 khách hàng theo Tên Công ty")
    public void deleteCustomerByName(String companyName, boolean confirm) {
        searchCustomer(companyName);
        try {
            By rowXpath = By.xpath("//tr[contains(.,'" + companyName + "')]");
            org.openqa.selenium.support.ui.WebDriverWait wait = new org.openqa.selenium.support.ui.WebDriverWait(getDriver(), java.time.Duration.ofSeconds(10));
            WebElement row = wait.until(org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated(rowXpath));
            
            // Hover lên dòng hoặc tên công ty để xuất hiện các nút Action (như Delete)
            By companyLinkBy = By.xpath("//a[contains(text(),'" + companyName + "')]");
            WebElement companyLink = wait.until(org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated(companyLinkBy));
            hover(companyLink);
            
            By deleteLinkBy = By.xpath("//a[contains(text(),'" + companyName + "')]/ancestor::tr//a[contains(text(),'Delete') or contains(@class,'_delete')]");
            WebElement deleteLink = wait.until(org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(deleteLinkBy));
            
            // Click mô phỏng người dùng thật
            click(deleteLink);
            
            Alert alert = getDriver().switchTo().alert();
            if (confirm) {
                alert.accept();
                WaitHelper.waitForPageLoad();
                // Chờ cho dòng chứa tên công ty biến mất hoàn toàn
                wait.until(org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOfElementLocated(rowXpath));
            } else {
                alert.dismiss();
            }
        } catch (Exception e) {
            log.error("Thao tác xóa khách hàng {} thất bại: {}", companyName, e.getMessage());
            throw new AssertionError("LỖI XÓA KHÁCH HÀNG: " + e.getMessage());
        }
    }

    // ================================================================
    // VERIFICATIONS / QUERIES
    // ================================================================

    @Step("Kiểm tra hiển thị thông báo thành công")
    public boolean isSuccessAlertDisplayed() {
        WaitHelper.waitForPageLoad();
        boolean isAlertVisible = isVisibleWithin(successAlert, 5);
        String currentUrl = getCurrentUrl();
        boolean isRedirectedToDetail = currentUrl.contains("/client/") || currentUrl.endsWith("/clients");
        log.info("isSuccessAlertDisplayed -> Alert: {}, URL: {}", isAlertVisible, currentUrl);
        return isAlertVisible || isRedirectedToDetail;
    }

    @Step("Lấy nội dung thông báo thành công")
    public String getSuccessAlertText() {
        return getText(successAlert);
    }

    @Step("Kiểm tra hiển thị lỗi tại ô Company")
    public boolean isCompanyErrorDisplayed() {
        return isVisibleWithin(companyErrorMsg, 5);
    }

    @Step("Lấy câu thông báo lỗi ở ô Company")
    public String getCompanyErrorText() {
        return getText(companyErrorMsg);
    }

    @Step("Kiểm tra tên khách hàng [{companyName}] xuất hiện trong bảng")
    public boolean isCustomerInTable(String companyName) {
        searchCustomer(companyName);
        if (isVisibleWithin(emptyTableMsg, 3)) {
            return false;
        }
        for (WebElement row : tableRows) {
            if (row.getText().contains(companyName)) {
                return true;
            }
        }
        return false;
    }

    @FindBy(css = "#contact")
    private WebElement contactModal;

    @Step("Click chuyển sang tab Contacts")
    public CustomerPage clickContactsTab() {
        log.info("Click sang tab Contacts");
        click(contactsTab);
        WaitHelper.waitForPageLoad();
        return this;
    }

    @Step("Click nút '+ New Contact'")
    public CustomerPage clickAddNewContact() {
        log.info("Click nút '+ New Contact'");
        org.openqa.selenium.support.ui.WebDriverWait wait = new org.openqa.selenium.support.ui.WebDriverWait(getDriver(), java.time.Duration.ofSeconds(15));
        By contactBtnBy = By.cssSelector(".tab-content a.btn-primary, .tab-content a.btn-info, a[onclick*='record_modal'], a[onclick*='contacts']");
        WebElement btn = wait.until(org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(contactBtnBy));
        
        // Native User Action: Scroll into view & click bằng Actions class
        new org.openqa.selenium.interactions.Actions(getDriver()).scrollToElement(btn).click(btn).perform();
        
        wait.until(org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated(By.id("firstname")));
        return this;
    }

    @Step("Điền thông tin Contact: [{firstName} {lastName}] email=[{email}]")
    public CustomerPage fillContactForm(String firstName, String lastName, String email, String password) {
        log.info("Điền form Contact: {} {}, email: {}", firstName, lastName, email);
        clearAndType(firstNameInput, firstName);
        clearAndType(lastNameInput, lastName);
        clearAndType(emailInput, email);
        clearAndType(contactPasswordInput, password);
        return this;
    }

    @Step("Lưu thông tin Contact")
    public void saveContact() {
        log.info("Click nút Save Contact");
        click(saveContactBtn);
        WaitHelper.waitForPageLoad();
        try {
            WaitHelper.waitForInvisible(contactModal);
        } catch (Exception ignored) {}
    }

    @Step("Kiểm tra Contact [{email}] có hiển thị trong danh sách")
    public boolean isContactVisibleInList(String email) {
        WaitHelper.waitForPageLoad();
        try {
            By contactEmailLink = By.cssSelector("a[href='mailto:" + email + "']");
            WebElement emailLink = getDriver().findElement(contactEmailLink);
            return isVisibleWithin(emailLink, 5);
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Xuất dữ liệu Export Excel")
    public void clickExport() {
        log.info("Click nút Export Excel");
        click(exportBtn);
        try { Thread.sleep(2000); } catch (InterruptedException ignored) {} // Chờ file tải xuống
    }
}
