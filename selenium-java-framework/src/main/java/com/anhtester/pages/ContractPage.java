package com.anhtester.pages;

import com.anhtester.utils.WaitHelper;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/**
 * ContractPage — Page Object cho Module Contracts (Hợp đồng)
 * URL: https://crm.anhtester.com/admin/contracts
 */
public class ContractPage extends BasePage {

    // ================================================================
    // LOCATORS — Khai báo dùng @FindBy (POM Pattern)
    // ================================================================

    @FindBy(css = "a[href*='contracts/contract']")
    private WebElement addNewContractBtn;

    @FindBy(css = ".dataTables_filter input, input[type='search']")
    private WebElement searchInput;

    @FindBy(css = "table.table-contracts, table.dataTable")
    private WebElement contractsTable;

    @FindBy(css = "table.table-contracts tbody tr, table.dataTable tbody tr")
    private List<WebElement> tableRows;

    @FindBy(css = "input[name='trash'], #trash")
    private WebElement trashFilterCheckbox;

    // --- Form Add/Edit Contract ---
    @FindBy(css = "button[data-id='clientid']")
    private WebElement customerDropdownBtn;

    @FindBy(css = ".bs-searchbox input")
    private WebElement customerSearchBox;

    @FindBy(id = "subject")
    private WebElement subjectInput;

    @FindBy(id = "contract_value")
    private WebElement contractValueInput;

    @FindBy(css = "button[data-id='contract_type']")
    private WebElement contractTypeDropdownBtn;

    @FindBy(id = "datestart")
    private WebElement startDateInput;

    @FindBy(id = "dateend")
    private WebElement endDateInput;

    @FindBy(id = "description")
    private WebElement descriptionInput;

    @FindBy(css = "form#contract-form button[type='submit'], button[type='submit'].btn-primary")
    private WebElement saveBtn;

    // --- Validation Messages ---
    @FindBy(id = "clientid-error")
    private WebElement customerErrorMsg;

    @FindBy(id = "subject-error")
    private WebElement subjectErrorMsg;

    @FindBy(id = "dateend-error")
    private WebElement dateErrorMsg;

    @FindBy(css = ".alert-danger, .text-danger")
    private WebElement generalErrorMsg;

    // --- Actions ---
    @FindBy(css = "a:has-text('Sign Contract'), a.btn-success[href*='sign']")
    private WebElement signContractBtn;

    @FindBy(css = "a[href*='tab_attachments'], a[href*='attachments']")
    private WebElement attachmentsTab;

    @FindBy(css = "input[type='file']")
    private WebElement fileUploadInput;

    @FindBy(css = "a.btn-default[href*='renew'], button[data-target*='renew']")
    private WebElement renewBtn;

    @FindBy(css = "a[href*='pdf']")
    private WebElement pdfBtn;

    public ContractPage() {
        super();
    }

    // ================================================================
    // ACTIONS
    // ================================================================

    @Step("Mở form tạo mới Hợp đồng")
    public ContractPage openCreateForm() {
        log.info("Mở form tạo mới Hợp đồng");
        try {
            click(addNewContractBtn);
        } catch (Exception e) {
            openUrl("https://crm.anhtester.com/admin/contracts/contract");
        }
        WaitHelper.waitForPageLoad();
        return this;
    }

    @Step("Tìm kiếm hợp đồng với từ khóa: {keyword}")
    public ContractPage searchContract(String keyword) {
        log.info("Tìm kiếm hợp đồng: {}", keyword);
        type(searchInput, keyword);
        searchInput.sendKeys(Keys.ENTER);
        try { Thread.sleep(500); } catch (InterruptedException ignored) {}
        return this;
    }

    @Step("Điền thông tin hợp đồng: Customer={customer}, Subject={subject}")
    public ContractPage fillContractForm(String customer, String subject, String value, String startDate, String endDate, String description) {
        log.info("Điền form hợp đồng: Subject={}", subject);

        if (customer != null && !customer.isEmpty()) {
            try {
                click(customerDropdownBtn);
                if (isVisibleWithin(customerSearchBox, 2)) {
                    type(customerSearchBox, customer);
                    try { Thread.sleep(300); } catch (InterruptedException ignored) {}
                }
                WebElement opt = getDriver().findElement(By.xpath("//div[contains(@class,'dropdown-menu')]//a[contains(.,'" + customer + "')]"));
                click(opt);
            } catch (Exception e) {
                log.warn("Chưa chọn được Customer qua dropdown UI, thử chọn mặc định");
            }
        }

        if (subject != null) {
            type(subjectInput, subject);
        }

        if (value != null) {
            type(contractValueInput, value);
        }

        if (startDate != null) {
            type(startDateInput, startDate);
        }

        if (endDate != null) {
            type(endDateInput, endDate);
        }

        if (description != null) {
            type(descriptionInput, description);
        }

        return this;
    }

    @Step("Nhấn Save lưu hợp đồng")
    public void saveContract() {
        log.info("Nhấn Save hợp đồng");
        click(saveBtn);
        WaitHelper.waitForPageLoad();
    }

    // ================================================================
    // QUERIES & VERIFICATIONS
    // ================================================================

    @Step("Kiểm tra bảng Contracts hiển thị")
    public boolean isContractsTableDisplayed() {
        return isVisibleWithin(contractsTable, 5);
    }

    @Step("Kiểm tra hợp đồng có trong bảng")
    public boolean isContractInTable(String subject) {
        searchContract(subject);
        try {
            By locator = By.xpath("//table//tr[contains(., '" + subject + "')]");
            return isVisibleWithin(getDriver().findElement(locator), 3);
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Kiểm tra có hiển thị lỗi validation các trường bắt buộc")
    public boolean isRequiredFieldErrorDisplayed() {
        return isVisibleWithin(subjectErrorMsg, 3) || isVisibleWithin(customerErrorMsg, 3) || isVisibleWithin(generalErrorMsg, 3);
    }

    @Step("Kiểm tra hiển thị lỗi logic ngày tháng")
    public boolean isDateLogicErrorDisplayed() {
        return isVisibleWithin(dateErrorMsg, 3) || isVisibleWithin(generalErrorMsg, 3);
    }
}
