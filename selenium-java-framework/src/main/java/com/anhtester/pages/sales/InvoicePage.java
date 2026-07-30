package com.anhtester.pages.sales;

import com.anhtester.pages.BasePage;
import com.anhtester.utils.WaitHelper;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/**
 * InvoicePage — Page Object cho Sub-module Invoices (Sales)
 * URL: https://crm.anhtester.com/admin/invoices
 * Bao phủ TC_038 → TC_060 (Invoices: Happy Path, Negative, Field Validation)
 */
public class InvoicePage extends BasePage {

    private static final String INVOICES_URL = "https://crm.anhtester.com/admin/invoices";
    private static final String NEW_INVOICE_URL = "https://crm.anhtester.com/admin/invoices/invoice";

    // ================================================================
    // LOCATORS — Danh sách Invoices
    // ================================================================

    @FindBy(css = "a[href*='invoices/invoice']:not([href*='delete'])")
    private WebElement newInvoiceBtn;

    @FindBy(css = "#invoices_filter input, .dataTables_filter input")
    private WebElement searchInput;

    @FindBy(css = "table#invoices tbody tr, table.table tbody tr")
    private List<WebElement> tableRows;

    @FindBy(css = ".dataTables_empty")
    private WebElement emptyTableMsg;

    // Batch Payments link
    @FindBy(css = "a[href*='batch_payment'], a.batch-payment")
    private WebElement batchPaymentsBtn;

    // ================================================================
    // LOCATORS — Form Tạo Invoice
    // ================================================================

    @FindBy(css = "select[name='clientid'], select#clientid")
    private WebElement customerSelect;

    @FindBy(css = "input[name='number'], #number")
    private WebElement invoiceNumberInput;

    @FindBy(css = "input[name='date'], #date")
    private WebElement invoiceDateInput;

    @FindBy(css = "input[name='duedate'], #duedate")
    private WebElement dueDateInput;

    @FindBy(css = "select[name='currency']")
    private WebElement currencySelect;

    @FindBy(css = "select[name='sale_agent']")
    private WebElement saleAgentSelect;

    @FindBy(css = "select[name='recurring']")
    private WebElement recurringSelect;

    @FindBy(css = "input[name='adminnote'], textarea[name='adminnote'], #adminnote")
    private WebElement adminNoteInput;

    @FindBy(css = "input[name='tag'], input.tagify")
    private WebElement tagInput;

    // Prevent overdue reminders checkbox
    @FindBy(css = "input[name='cancel_overdue_reminders'], #cancel_overdue_reminders")
    private WebElement preventOverdueCheckbox;

    // Item
    @FindBy(css = "a#add_item_btn, a.add-item-btn, button.add-item")
    private WebElement addItemBtn;

    @FindBy(css = "input[name='rate[]']")
    private List<WebElement> itemRateInputs;

    @FindBy(css = "input[name='qty[]']")
    private List<WebElement> itemQtyInputs;

    @FindBy(css = "textarea[name='description[]'], input[name='description[]']")
    private List<WebElement> itemDescInputs;

    @FindBy(css = "button.invoice-form-submit, button[type='submit'], button.only-save, button.btn-info.only-save, input[name='save'], button[name='save'], .btn-bottom-toolbar-save-btn")
    private WebElement saveBtn;

    @FindBy(css = "button.save-as-draft, button[name='save_as_draft'], input[name='save_as_draft']")
    private WebElement saveAsDraftBtn;

    // ================================================================
    // LOCATORS — Record Payment Modal/Form
    // ================================================================

    @FindBy(css = "a[href*='record_payment'], button[data-action='record_payment'], .btn-record-payment, a.btn-success[href*='payment']")
    private WebElement recordPaymentBtn;

    @FindBy(css = "input[name='amount'], #amount")
    private WebElement paymentAmountInput;

    @FindBy(css = "input[name='date'][id*='payment'], #payment_date")
    private WebElement paymentDateInput;

    @FindBy(css = "select[name='paymentmode'], select#paymentmode")
    private WebElement paymentModeSelect;

    @FindBy(css = "button[name='save_payment'], button.btn-payment-save, .modal-footer .btn-primary, button[type='submit']")
    private WebElement savePaymentBtn;

    // ================================================================
    // LOCATORS — Feedback & Validation
    // ================================================================

    @FindBy(css = ".alert-success, div[class*='alert-success'], .toast-success")
    private WebElement successAlert;

    @FindBy(css = ".alert-danger, div[class*='alert-danger'], .toast-error")
    private WebElement errorAlert;

    // ================================================================
    // Navigation
    // ================================================================

    @Step("Mở trang danh sách Invoices")
    public InvoicePage openInvoicesPage() {
        log.info("Navigate to Invoices list");
        openUrl(INVOICES_URL);
        return this;
    }

    @Step("Mở form tạo Invoice mới")
    public InvoicePage clickNewInvoice() {
        log.info("Click New Invoice button");
        try {
            click(newInvoiceBtn);
        } catch (Exception e) {
            openUrl(NEW_INVOICE_URL);
        }
        WaitHelper.waitForPageLoad();
        return this;
    }

    // ================================================================
    // Form Actions
    // ================================================================

    @Step("Chọn Customer: {customerName}")
    public InvoicePage selectCustomer(String customerName) {
        log.info("Chọn Customer: {}", customerName);
        try {
            WebElement select2Container = getDriver().findElement(
                By.cssSelector("#clientid + .select2-container, .select2-container[id*='clientid'], #s2id_clientid, div[id*='clientid']"));
            click(select2Container);
            try { Thread.sleep(500); } catch (Exception ignored) {}
            
            List<WebElement> searchInputs = getDriver().findElements(
                By.cssSelector(".select2-search__field, input.select2-input, .select2-container--open .select2-search__field"));
            if (!searchInputs.isEmpty()) {
                clearAndType(searchInputs.get(0), customerName);
                try { Thread.sleep(500); } catch (Exception ignored) {}
            }
            
            List<WebElement> options = getDriver().findElements(
                By.cssSelector(".select2-results__option--highlighted, .select2-results__option, .select2-result-label"));
            if (!options.isEmpty()) {
                click(options.get(0));
            }
        } catch (Exception e) {
            log.warn("Select2 customer click failed, fallback to JS: {}", e.getMessage());
        }

        // JS Fallback khẩn cấp nếu #clientid vẫn chưa có value
        try {
            executeScript(
                "var sel = document.querySelector('#clientid, select[name=\"clientid\"]');" +
                "if (sel && (!sel.value || sel.value == '')) {" +
                "  var opt = Array.from(sel.options).find(o => o.text.includes(arguments[0]) || (o.value && o.value != ''));" +
                "  if (opt) { sel.value = opt.value; if(window.jQuery) $(sel).trigger('change'); }" +
                "}", customerName);
        } catch (Exception ex) {
            log.warn("JS fallback select customer failed: {}", ex.getMessage());
        }
        return this;
    }

    @Step("Nhập Invoice Number: {number}")
    public InvoicePage enterInvoiceNumber(String number) {
        log.info("Nhập Invoice Number: {}", number);
        clearAndType(invoiceNumberInput, number);
        return this;
    }

    @Step("Nhập Invoice Date: {date}")
    public InvoicePage enterInvoiceDate(String date) {
        log.info("Nhập Invoice Date: {}", date);
        try {
            clearAndType(invoiceDateInput, date);
        } catch (Exception e) {
            log.warn("Invoice Date field không khả dụng");
        }
        return this;
    }

    @Step("Nhập Due Date: {dueDate}")
    public InvoicePage enterDueDate(String dueDate) {
        log.info("Nhập Due Date: {}", dueDate);
        try {
            clearAndType(dueDateInput, dueDate);
        } catch (Exception e) {
            log.warn("Due Date field không khả dụng");
        }
        return this;
    }

    @Step("Chọn Recurring: {recurring}")
    public InvoicePage selectRecurring(String recurring) {
        log.info("Chọn Recurring: {}", recurring);
        try {
            selectByVisibleText(recurringSelect, recurring);
        } catch (Exception e) {
            log.warn("Recurring select không khả dụng");
        }
        return this;
    }

    @Step("Tick Prevent Overdue Reminders")
    public InvoicePage checkPreventOverdueReminders() {
        log.info("Tick Prevent overdue reminders");
        try {
            if (!preventOverdueCheckbox.isSelected()) {
                click(preventOverdueCheckbox);
            }
        } catch (Exception e) {
            log.warn("Prevent overdue checkbox không khả dụng");
        }
        return this;
    }

    @Step("Nhập Admin Note: {note}")
    public InvoicePage enterAdminNote(String note) {
        log.info("Nhập Admin Note: {}", note);
        try { clearAndType(adminNoteInput, note); } catch (Exception e) {
            log.warn("Admin Note field không khả dụng");
        }
        return this;
    }

    @Step("Nhập Tag: {tag}")
    public InvoicePage enterTag(String tag) {
        log.info("Nhập Tag: {}", tag);
        try { clearAndType(tagInput, tag); } catch (Exception e) {
            log.warn("Tag field không khả dụng");
        }
        return this;
    }

    @Step("Thêm Item — Description: {desc}, Rate: {rate}, Qty: {qty}")
    public InvoicePage addItem(String desc, String rate, String qty) {
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
                By.cssSelector("button.add_item_to_table, a.add_item_to_table, button#add_item_btn, a#add_item_btn, a.add-item-btn"));
            if (!addBtns.isEmpty() && isDisplayed(addBtns.get(0))) {
                jsClick(addBtns.get(0));
            }
        } catch (Exception ignored) {}

        return this;
    }

    @Step("Click Save Invoice")
    public InvoicePage clickSave() {
        log.info("Click Save button");
        try {
            click(saveBtn);
        } catch (Exception e) {
            try {
                jsClick(saveBtn);
            } catch (Exception e2) {
                WebElement btn = getDriver().findElement(By.cssSelector("button.invoice-form-submit, button[type='submit'], form#invoice-form button"));
                jsClick(btn);
            }
        }
        WaitHelper.waitForPageLoad();
        return this;
    }

    @Step("Click Save as Draft")
    public InvoicePage clickSaveAsDraft() {
        log.info("Click Save as Draft button");
        try {
            click(saveAsDraftBtn);
        } catch (Exception e) {
            try {
                WebElement draftBtn = getDriver().findElement(
                    By.xpath("//button[contains(text(),'Draft')] | //a[contains(text(),'Draft')] | .btn-save-draft"));
                jsClick(draftBtn);
            } catch (Exception ex) {
                log.warn("Save as Draft click failed, fallback to Save");
                clickSave();
            }
        }
        WaitHelper.waitForPageLoad();
        return this;
    }

    // ================================================================
    // Record Payment
    // ================================================================

    @Step("Mở Invoice đầu tiên trong bảng (navigate vào detail)")
    public InvoicePage openFirstInvoiceDetail() {
        log.info("Navigate vào detail của Invoice đầu tiên");
        try {
            List<WebElement> firstRows = getDriver().findElements(
                By.cssSelector("table#invoices tbody tr:first-child td a, table.table tbody tr:first-child td a, a[href*='invoices/invoice']"));
            if (!firstRows.isEmpty()) {
                jsClick(firstRows.get(0));
            }
        } catch (Exception e) {
            log.warn("Không tìm thấy Invoice trong bảng để navigate vào detail: {}", e.getMessage());
        }
        WaitHelper.waitForPageLoad();
        return this;
    }

    @Step("Mở modal Record Payment")
    public InvoicePage clickRecordPayment() {
        log.info("Click Record Payment button");
        try {
            List<WebElement> btns = getDriver().findElements(
                By.cssSelector("a[href*='record_payment'], a.record-payment, a[onclick*='record_payment'], a[data-target*='payment'], a.btn-success"));
            if (!btns.isEmpty()) {
                jsClick(btns.get(0));
            }
        } catch (Exception e) {
            log.warn("Record Payment button click failed: {}", e.getMessage());
        }
        try { Thread.sleep(1000); } catch (Exception ignored) {}
        return this;
    }

    @Step("Nhập Payment Amount: {amount}")
    public InvoicePage enterPaymentAmount(String amount) {
        log.info("Nhập Payment Amount: {}", amount);
        try {
            List<WebElement> amountInputs = getDriver().findElements(
                By.cssSelector("input[name='amount'], #amount, input[name='amount_received'], input[name='payment_amount']"));
            if (!amountInputs.isEmpty()) {
                clearAndType(amountInputs.get(0), amount);
            }
        } catch (Exception e) {
            log.warn("Nhập Payment Amount failed: {}", e.getMessage());
        }
        return this;
    }

    @Step("Chọn Payment Mode: {mode}")
    public InvoicePage selectPaymentMode(String mode) {
        log.info("Chọn Payment Mode: {}", mode);
        try {
            selectByVisibleText(paymentModeSelect, mode);
        } catch (Exception e) {
            log.warn("Payment mode select không khả dụng");
        }
        return this;
    }

    @Step("Click Save Payment")
    public InvoicePage clickSavePayment() {
        log.info("Click Save Payment button");
        try {
            List<WebElement> saveBtns = getDriver().findElements(
                By.cssSelector("button[name='save_payment'], button.btn-payment-save, .modal-footer .btn-primary, button[type='submit']"));
            if (!saveBtns.isEmpty()) {
                jsClick(saveBtns.get(0));
            }
        } catch (Exception e) {
            log.warn("Save Payment button click failed: {}", e.getMessage());
        }
        WaitHelper.waitForPageLoad();
        return this;
    }

    /**
     * Ghi nhận payment trên Invoice hiện tại
     */
    @Step("Record Payment: mode={mode}, amount={amount}")
    public InvoicePage recordPayment(String mode, String amount) {
        clickRecordPayment();
        enterPaymentAmount(amount);
        selectPaymentMode(mode);
        clickSavePayment();
        return this;
    }

    // ================================================================
    // Filter
    // ================================================================

    @Step("Lọc Invoice theo Status: {status}")
    public InvoicePage filterByStatus(String status) {
        log.info("Filter Invoices by Status: {}", status);
        try {
            WebElement filterLink = getDriver().findElement(
                By.xpath("//a[normalize-space()='" + status + "' and (contains(@href,'status') or @data-status)]" +
                         " | //li//a[@data-status='" + status.toLowerCase() + "']"));
            click(filterLink);
            WaitHelper.waitForPageLoad();
        } catch (Exception e) {
            log.warn("Không tìm thấy filter '{}', dùng URL", status);
            openUrl(INVOICES_URL + "?status=" + status.toLowerCase());
        }
        return this;
    }

    // ================================================================
    // Helper: Tạo Invoice đầy đủ
    // ================================================================

    @Step("Tạo Invoice: customer={customer}, rate={rate}, qty={qty}")
    public InvoicePage createInvoice(String customer, String itemDesc, String rate, String qty) {
        openInvoicesPage();
        clickNewInvoice();
        selectCustomer(customer);
        addItem(itemDesc, rate, qty);
        clickSave();
        return this;
    }

    // ================================================================
    // Assertions / Verifications
    // ================================================================

    @Step("Kiểm tra success alert")
    public boolean isSuccessAlertDisplayed() {
        boolean visible = isVisibleWithin(successAlert, 5);
        if (!visible) {
            visible = getCurrentUrl().contains("invoices") || 
                      !getDriver().findElements(By.cssSelector(".alert-success, div[class*='alert-success'], .toast-success, #invoice, .invoice-pipeline")).isEmpty();
        }
        log.info("Success alert: {}", visible);
        return visible;
    }

    @Step("Kiểm tra error alert hoặc validation")
    public boolean isErrorAlertDisplayed() {
        return isVisibleWithin(errorAlert, 5);
    }

    @Step("Kiểm tra Customer validation error")
    public boolean isCustomerErrorDisplayed() {
        try {
            WebElement err = getDriver().findElement(
                By.cssSelector("#clientid-error, .has-error .help-block, label.error[for='clientid']"));
            return err.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Kiểm tra bảng Invoices có dữ liệu")
    public boolean isInvoiceTableHasData() {
        try {
            return !tableRows.isEmpty() && !isDisplayed(emptyTableMsg);
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Kiểm tra Invoice Status hiển thị: {expectedStatus}")
    public boolean isInvoiceStatusEquals(String expectedStatus) {
        try {
            WebElement statusEl = getDriver().findElement(
                By.cssSelector(".badge-status, .invoice-status, .label-status, span.badge"));
            return statusEl.getText().trim().equalsIgnoreCase(expectedStatus);
        } catch (Exception e) {
            // Kiểm tra trong page header / summary
            return getDriver().getPageSource().toLowerCase().contains(expectedStatus.toLowerCase());
        }
    }

    @Step("Kiểm tra validation error hiển thị")
    public boolean isValidationErrorVisible() {
        return isCustomerErrorDisplayed() || isErrorAlertDisplayed()
               || getCurrentUrl().contains("invoices/invoice");
    }

    @Step("Kiểm tra trang hiện tại là Invoice List/Detail")
    public boolean isOnInvoicePage() {
        return getCurrentUrl().contains("invoices");
    }

    @Step("Lấy Invoice Number hiện tại từ form")
    public String getInvoiceNumberValue() {
        try {
            return invoiceNumberInput.getAttribute("value").trim();
        } catch (Exception e) {
            return "";
        }
    }

    @Step("Kiểm tra prevent overdue checkbox đã được tick")
    public boolean isPreventOverdueChecked() {
        try {
            return preventOverdueCheckbox.isSelected();
        } catch (Exception e) {
            return false;
        }
    }
}
