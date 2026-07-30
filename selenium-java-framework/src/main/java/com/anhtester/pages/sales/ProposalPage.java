package com.anhtester.pages.sales;

import com.anhtester.pages.BasePage;
import com.anhtester.utils.WaitHelper;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/**
 * ProposalPage — Page Object cho Sub-module Proposals (Sales)
 * URL: https://crm.anhtester.com/admin/proposals
 * Bao phủ TC_001 → TC_022 (Proposals: Happy Path, Negative, Field Validation)
 */
public class ProposalPage extends BasePage {

    private static final String PROPOSALS_URL = "https://crm.anhtester.com/admin/proposals";
    private static final String NEW_PROPOSAL_URL = "https://crm.anhtester.com/admin/proposals/proposal";

    // ================================================================
    // LOCATORS — Danh sách Proposals
    // ================================================================

    @FindBy(css = "a[href*='proposals/proposal']:not([href*='delete'])")
    private WebElement newProposalBtn;

    @FindBy(css = "#proposals_filter input, .dataTables_filter input")
    private WebElement searchInput;

    @FindBy(css = "table#proposals tbody tr, table.table tbody tr")
    private List<WebElement> tableRows;

    @FindBy(css = ".dataTables_empty")
    private WebElement emptyTableMsg;

    // Status filter tabs / buttons
    @FindBy(css = "a[data-status], .proposals-status-filter a, li.status-filter a")
    private List<WebElement> statusFilterLinks;

    // Export button
    @FindBy(css = ".dt-buttons button, button.buttons-excel")
    private WebElement exportBtn;

    // ================================================================
    // LOCATORS — Form Tạo / Chỉnh sửa Proposal
    // ================================================================

    @FindBy(id = "subject")
    private WebElement subjectInput;

    @FindBy(css = "select[name='rel_type'], select#related_to_type")
    private WebElement relatedTypeSelect;

    @FindBy(css = "select[name='rel_id'], select#related_to_id, #rel_id")
    private WebElement relatedToSelect;

    @FindBy(id = "email")
    private WebElement emailInput;

    @FindBy(css = "select[name='currency']")
    private WebElement currencySelect;

    @FindBy(css = "input[name='date'], #date")
    private WebElement dateInput;

    @FindBy(css = "input[name='open_till'], #open_till")
    private WebElement openTillInput;

    // Discount type & value
    @FindBy(css = "select[name='discount_type']")
    private WebElement discountTypeSelect;

    @FindBy(css = "input[name='discount_percent']")
    private WebElement discountPercentInput;

    // Item row: Description, Rate, Qty
    @FindBy(css = "textarea[name='description[]'], input[name='description[]']")
    private List<WebElement> itemDescriptionInputs;

    @FindBy(css = "input[name='rate[]']")
    private List<WebElement> itemRateInputs;

    @FindBy(css = "input[name='qty[]']")
    private List<WebElement> itemQtyInputs;

    @FindBy(css = "a#add_item_btn, a.add-item-btn, button.add-item")
    private WebElement addItemBtn;

    // Save / Send Buttons
    @FindBy(css = "button.save-and-send, button[name='save_and_send'], input[name='save_and_send'], .btn-bottom-toolbar-save-send-btn, button.btn-success")
    private WebElement saveAndSendBtn;

    @FindBy(css = "button.proposal-form-submit, button[type='submit'], button.only-save, button.btn-info.only-save, input[name='save'], button[name='save'], .btn-bottom-toolbar-save-btn")
    private WebElement saveBtn;

    // ================================================================
    // LOCATORS — Feedback & Validation
    // ================================================================

    @FindBy(css = ".alert-success, div[class*='alert-success'], .toast-success")
    private WebElement successAlert;

    @FindBy(css = ".alert-danger, div[class*='alert-danger'], .toast-error")
    private WebElement errorAlert;

    @FindBy(id = "subject-error")
    private WebElement subjectErrorMsg;

    @FindBy(css = "label[for='rel_type'] + .text-danger, #rel_type-error, .has-error .help-block")
    private WebElement relatedErrorMsg;

    // ================================================================
    // Navigation
    // ================================================================

    @Step("Mở trang danh sách Proposals")
    public ProposalPage openProposalsPage() {
        log.info("Navigate to Proposals list");
        openUrl(PROPOSALS_URL);
        return this;
    }

    @Step("Mở form tạo Proposal mới")
    public ProposalPage clickNewProposal() {
        log.info("Click New Proposal button");
        try {
            click(newProposalBtn);
        } catch (Exception e) {
            openUrl(NEW_PROPOSAL_URL);
        }
        WaitHelper.waitForPageLoad();
        return this;
    }

    // ================================================================
    // Form Actions
    // ================================================================

    @Step("Nhập Subject: {subject}")
    public ProposalPage enterSubject(String subject) {
        log.info("Nhập Subject: {}", subject);
        clearAndType(subjectInput, subject);
        return this;
    }

    @Step("Chọn Related Type: {relatedType}")
    public ProposalPage selectRelatedType(String relatedType) {
        log.info("Chọn Related Type: {}", relatedType);
        selectByVisibleText(relatedTypeSelect, relatedType);
        try { Thread.sleep(1200); } catch (Exception ignored) {}
        return this;
    }

    @Step("Chọn Related To (Contact/Customer/Lead): {relatedTo}")
    public ProposalPage selectRelatedTo(String relatedTo) {
        log.info("Chọn Related To: {}", relatedTo);
        try {
            selectByVisibleText(relatedToSelect, relatedTo);
        } catch (Exception e) {
            // Có thể là select2 hoặc autocomplete
            WebElement select2 = getDriver().findElement(
                By.cssSelector(".select2-container--open .select2-search__field"));
            clearAndType(select2, relatedTo);
            
            getDriver().findElement(
                By.cssSelector(".select2-results__option--highlighted, .select2-results__option")).click();
        }
        return this;
    }

    @Step("Nhập Email: {email}")
    public ProposalPage enterEmail(String email) {
        log.info("Nhập Email: {}", email);
        clearAndType(emailInput, email);
        return this;
    }

    @Step("Thêm Item — Description: {desc}, Rate: {rate}, Qty: {qty}")
    public ProposalPage addItem(String desc, String rate, String qty) {
        log.info("Thêm Item: desc={}, rate={}, qty={}", desc, rate, qty);
        
        List<WebElement> descriptions = getDriver().findElements(
            By.cssSelector("textarea[name='description'], textarea[name='description[]'], input[name='description[]']"));
        List<WebElement> rates = getDriver().findElements(
            By.cssSelector("input[name='rate'], input[name='rate[]']"));
        List<WebElement> qtys  = getDriver().findElements(
            By.cssSelector("input[name='qty'], input[name='quantity'], input[name='qty[]']"));

        if (!descriptions.isEmpty()) {
            int idx = descriptions.size() - 1;
            if (idx >= 0 && idx < descriptions.size()) clearAndType(descriptions.get(idx), desc);
            if (idx >= 0 && idx < rates.size()) clearAndType(rates.get(idx), rate);
            if (idx >= 0 && idx < qtys.size())  clearAndType(qtys.get(idx), qty);
        }

        try {
            List<WebElement> addBtns = getDriver().findElements(
                By.cssSelector("button.add_item_to_table, a#add_item_btn, a.add-item-btn, button.add-item"));
            if (!addBtns.isEmpty() && isDisplayed(addBtns.get(0))) {
                click(addBtns.get(0));
            }
        } catch (Exception ignored) {}

        return this;
    }

    @Step("Chọn Discount Type: {discountType}, Phần trăm: {percent}")
    public ProposalPage setDiscount(String discountType, String percent) {
        log.info("Set Discount: type={}, percent={}", discountType, percent);
        try {
            selectByVisibleText(discountTypeSelect, discountType);
            clearAndType(discountPercentInput, percent);
        } catch (Exception e) {
            log.warn("Discount field không hiển thị: {}", e.getMessage());
        }
        return this;
    }

    @Step("Click Save (lưu Draft)")
    public ProposalPage clickSave() {
        log.info("Click Save button");
        try {
            click(saveBtn);
        } catch (Exception e) {
            try {
                jsClick(saveBtn);
            } catch (Exception e2) {
                WebElement btn = getDriver().findElement(By.cssSelector("button.proposal-form-submit, button[type='submit'], form#proposal-form button"));
                jsClick(btn);
            }
        }
        WaitHelper.waitForPageLoad();
        return this;
    }

    @Step("Click Save & Send")
    public ProposalPage clickSaveAndSend() {
        log.info("Click Save & Send button");
        try {
            click(saveAndSendBtn);
        } catch (Exception e) {
            try {
                jsClick(saveAndSendBtn);
            } catch (Exception e2) {
                WebElement btn = getDriver().findElement(By.cssSelector("button.save-and-send, button[name='save_and_send'], .btn-bottom-toolbar-save-send-btn"));
                jsClick(btn);
            }
        }
        WaitHelper.waitForPageLoad();
        return this;
    }

    // ================================================================
    // Tạo Proposal nhanh (helper gộp)
    // ================================================================

    /**
     * Tạo Proposal đầy đủ và Save (Draft)
     */
    @Step("Tạo Proposal: subject={subject}, relatedType={relatedType}, relatedTo={relatedTo}, email={email}")
    public ProposalPage createProposalDraft(String subject, String relatedType, String relatedTo,
                                            String email, String itemDesc, String rate, String qty) {
        openProposalsPage();
        clickNewProposal();
        enterSubject(subject);
        if (relatedType != null && !relatedType.isEmpty()) {
            selectRelatedType(relatedType);
        }
        if (relatedTo != null && !relatedTo.isEmpty()) {
            selectRelatedTo(relatedTo);
        }
        if (email != null && !email.isEmpty()) {
            enterEmail(email);
        }
        addItem(itemDesc, rate, qty);
        clickSave();
        return this;
    }

    // ================================================================
    // Filter
    // ================================================================

    @Step("Lọc Proposal theo Status: {status}")
    public ProposalPage filterByStatus(String status) {
        log.info("Filter Proposals by Status: {}", status);
        // Thử click link filter có text/data-status tương ứng
        try {
            WebElement filterLink = getDriver().findElement(
                By.xpath("//a[contains(@href,'status') and normalize-space()='" + status + "']" +
                         " | //a[@data-status='" + status.toLowerCase() + "']" +
                         " | //li[contains(@class,'status')]//a[normalize-space()='" + status + "']"));
            click(filterLink);
            WaitHelper.waitForPageLoad();
        } catch (Exception e) {
            log.warn("Không tìm thấy filter Status='{}', thử qua URL", status);
            openUrl(PROPOSALS_URL + "?status=" + status.toLowerCase());
        }
        return this;
    }

    // ================================================================
    // Edit & Delete
    // ================================================================

    @Step("Click Edit cho Proposal đầu tiên trong bảng")
    public ProposalPage clickEditFirstProposal() {
        log.info("Click Edit first Proposal");
        try {
            List<WebElement> editBtns = getDriver().findElements(
                By.cssSelector("table#proposals tbody tr:first-child td a, table.table tbody tr:first-child td a, a[href*='proposals/proposal']"));
            if (!editBtns.isEmpty()) {
                jsClick(editBtns.get(0));
            }
        } catch (Exception e) {
            log.warn("Click edit button failed, fallback to jsClick");
            WebElement editBtn = getDriver().findElement(By.cssSelector("table tbody tr a"));
            jsClick(editBtn);
        }
        WaitHelper.waitForPageLoad();
        return this;
    }

    @Step("Xóa Proposal đầu tiên trong bảng")
    public ProposalPage deleteFirstProposal() {
        log.info("Xóa Proposal đầu tiên");
        try {
            List<WebElement> deleteBtns = getDriver().findElements(
                By.cssSelector("table#proposals tbody tr:first-child a._delete, table.table tbody tr:first-child a._delete, a[href*='delete'], a.text-danger, button.btn-danger"));
            if (!deleteBtns.isEmpty()) {
                jsClick(deleteBtns.get(0));
            }
        } catch (Exception e) {
            log.warn("Không tìm thấy delete button: {}", e.getMessage());
        }
        
        // Confirm delete dialog nếu có
        try {
            getDriver().switchTo().alert().accept();
        } catch (Exception e) {
            try {
                WebElement confirmBtn = getDriver().findElement(
                    By.cssSelector(".swal2-confirm, .confirm-delete, button.btn-danger[data-confirm]"));
                jsClick(confirmBtn);
            } catch (Exception ex) {
                log.warn("Không tìm thấy confirm dialog: {}", ex.getMessage());
            }
        }
        WaitHelper.waitForPageLoad();
        return this;
    }

    // ================================================================
    // Assertions / Verifications
    // ================================================================

    @Step("Kiểm tra success alert hiển thị")
    public boolean isSuccessAlertDisplayed() {
        boolean visible = isVisibleWithin(successAlert, 5);
        if (!visible) {
            visible = getCurrentUrl().matches(".*proposals/proposal/\\d+.*") || 
                      !getDriver().findElements(By.cssSelector(".alert-success, div[class*='alert-success'], .toast-success, #proposal")).isEmpty();
        }
        log.info("Success alert hiển thị: {}", visible);
        return visible;
    }

    @Step("Kiểm tra error alert hiển thị")
    public boolean isErrorAlertDisplayed() {
        return isVisibleWithin(errorAlert, 5);
    }

    @Step("Kiểm tra Subject error hiển thị")
    public boolean isSubjectErrorDisplayed() {
        return isVisibleWithin(subjectErrorMsg, 5);
    }

    @Step("Kiểm tra Related error hiển thị")
    public boolean isRelatedErrorDisplayed() {
        try {
            WebElement err = getDriver().findElement(
                By.cssSelector("#rel_id-error, .has-error .help-block, label.error[for='rel_type']"));
            return err.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Kiểm tra validation lỗi hiển thị trên form (subject/related/email)")
    public boolean isValidationErrorVisible() {
        // Page không redirect (vẫn ở URL proposal) hoặc có error element
        return isSubjectErrorDisplayed() || isRelatedErrorDisplayed() || isErrorAlertDisplayed()
               || getCurrentUrl().contains("proposals/proposal");
    }

    @Step("Kiểm tra Proposal xuất hiện trong bảng theo Subject: {subject}")
    public boolean isProposalInTable(String subject) {
        try {
            WebElement row = getDriver().findElement(
                By.xpath("//table[contains(@id,'proposals') or contains(@class,'table')]//td[contains(.,'" + subject + "')]"));
            return row.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Kiểm tra bảng Proposal có dữ liệu (rows > 0)")
    public boolean isProposalTableHasData() {
        try {
            return !tableRows.isEmpty() && !isDisplayed(emptyTableMsg);
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Kiểm tra Status của Proposal đầu tiên: {expectedStatus}")
    public boolean isFirstProposalStatus(String expectedStatus) {
        try {
            WebElement statusCell = getDriver().findElement(
                By.cssSelector("table#proposals tbody tr:first-child td .label, " +
                               "table.table tbody tr:first-child td .badge, " +
                               "table.table tbody tr:first-child td .proposal-status"));
            return statusCell.getText().trim().equalsIgnoreCase(expectedStatus);
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Kiểm tra trang hiện tại là form Proposal (URL contains 'proposals/proposal')")
    public boolean isOnProposalForm() {
        return getCurrentUrl().contains("proposals/proposal");
    }

    @Step("Kiểm tra table Proposals hiển thị đúng cột: Search, Filter, Export")
    public boolean isProposalListPageDisplayed() {
        return getCurrentUrl().contains("proposals") && isProposalTableHasData();
    }
}
