# Manual Test Cases — Module: Sales (CRM Perfex)
## Part 2: Payments | Credit Notes | Items | State Transition | Business Rules (kèm Automation Mapping)

**Dự án:** CRM Anhtester  
**URL:** https://crm.anhtester.com  
**Ngày cập nhật:** 2026-07-29  
**Mode:** QUICK (Workflow: /generate_testcases_from_requirements)  
**Người tạo:** Antigravity AI Agent  

---

## 🏗️ Kiến trúc Automation Mapping (Framework Architecture)

### 1. Selenium Java Framework (`selenium-java-framework`)
- **Base Package:** `com.anhtester`
- **Page Objects:** 
  - `com.anhtester.pages.sales.PaymentPage`
  - `com.anhtester.pages.sales.CreditNotePage`
  - `com.anhtester.pages.sales.ItemPage`
- **Test Classes:**
  - `com.anhtester.tests.sales.PaymentTest`
  - `com.anhtester.tests.sales.CreditNoteTest`
  - `com.anhtester.tests.sales.ItemTest`
  - `com.anhtester.tests.sales.SalesStateTransitionTest`

### 2. Playwright TypeScript Framework (`playwright-typescript-framework`)
- **Page Objects:**
  - `src/pages/sales/payments.page.ts` (`PaymentsPage`)
  - `src/pages/sales/credit-notes.page.ts` (`CreditNotesPage`)
  - `src/pages/sales/items.page.ts` (`ItemsPage`)
- **Test Specs:**
  - `src/tests/sales/payments.spec.ts`
  - `src/tests/sales/credit-notes.spec.ts`
  - `src/tests/sales/items.spec.ts`
  - `src/tests/sales/state-transition.spec.ts`

---

## 📋 Phạm vi Part 2

| Sub-module | Số TC | Class Selenium Java | Spec Playwright TS |
|-----------|-------|----------------------|-------------------|
| Payments | TC_061 → TC_067 | `PaymentTest.java` | `payments.spec.ts` |
| Credit Notes | TC_068 → TC_084 | `CreditNoteTest.java` | `credit-notes.spec.ts` |
| Items | TC_085 → TC_100 | `ItemTest.java` | `items.spec.ts` |
| State Transition | TC_101 → TC_105 | `SalesStateTransitionTest.java` | `state-transition.spec.ts` |

---

## 2.4 Sub-module: Payments (Thanh toán)

### 🔵 HAPPY PATH & BUSINESS RULES

| TC ID | Module | Test Scenario | Pre-Condition | Test Steps | Test Data | Expected Result | Priority | Selenium Java Mapping | Playwright TS Mapping |
|-------|--------|--------------|---------------|-----------|-----------|-----------------|---------|-----------------------|----------------------|
| CRM_SALES_TC_061 | Payments | Xem danh sách Payments | Có ít nhất 1 Payment | 1. Sales > Payments | — | 1. Hiển thị bảng với đủ cột<br>2. Có Search, Pagination, Export | High | `PaymentTest#testVerifyPaymentListTable()` | `payments.spec.ts > verify payment list table` |
| CRM_SALES_TC_062 | Payments | Kiểm tra KHÔNG có nút "Create Payment" | Đăng nhập Admin | 1. Sales > Payments<br>2. Quan sát giao diện | — | Không có nút "Create Payment" (BR-PAY-01) | Critical | `PaymentTest#testVerifyNoCreatePaymentButton()` | `payments.spec.ts > verify no create payment button` |
| CRM_SALES_TC_063 | Payments | Tìm kiếm Payment theo Invoice # | Có Payments | 1. Sales > Payments<br>2. Search: "INV-000001" | Search: INV-000001 | Bảng lọc đúng Payment liên kết với INV-000001 | High | `PaymentTest#testSearchPaymentByInvoiceNumber()` | `payments.spec.ts > search payment by invoice number` |
| CRM_SALES_TC_064 | Payments | Xuất danh sách Payments (Export CSV) | Có Payment | 1. Sales > Payments<br>2. Click Export CSV | Format: CSV | File CSV được tải về thành công | Medium | `PaymentTest#testExportPaymentList()` | `payments.spec.ts > export payment list to csv` |
| CRM_SALES_TC_065 | Payments | Xem chi tiết Payment | Có Payment | 1. Sales > Payments<br>2. Click vào Payment # | — | Hiển thị chi tiết Payment và link Invoice | Medium | `PaymentTest#testViewPaymentDetail()` | `payments.spec.ts > view payment detail` |

### 🔴 NEGATIVE PATH

| TC ID | Module | Test Scenario | Pre-Condition | Test Steps | Test Data | Expected Result | Priority | Selenium Java Mapping | Playwright TS Mapping |
|-------|--------|--------------|---------------|-----------|-----------|-----------------|---------|-----------------------|----------------------|
| CRM_SALES_TC_066 | Payments | Ghi nhận Payment từ Invoice đã Paid | Invoice đã Paid | 1. Vào chi tiết Invoice Paid<br>2. Kiểm tra "Record Payment" | Status: Paid | Nút "Record Payment" bị disabled hoặc ẩn | High | `PaymentTest#testRecordPaymentOnPaidInvoiceDisabled()` | `payments.spec.ts > record payment on paid invoice disabled` |
| CRM_SALES_TC_067 | Payments | Tìm kiếm Payment không tồn tại | Đăng nhập Admin | 1. Sales > Payments<br>2. Search: "PAYMENT_NOT_FOUND_9999" | Search: PAYMENT_NOT_FOUND_9999 | Bảng rỗng, báo "No records found" | Medium | `PaymentTest#testSearchNonExistentPayment()` | `payments.spec.ts > search non existent payment` |

---

## 2.5 Sub-module: Credit Notes (Ghi chú tín dụng)

### 🔵 HAPPY PATH

| TC ID | Module | Test Scenario | Pre-Condition | Test Steps | Test Data | Expected Result | Priority | Selenium Java Mapping | Playwright TS Mapping |
|-------|--------|--------------|---------------|-----------|-----------|-----------------|---------|-----------------------|----------------------|
| CRM_SALES_TC_068 | Credit Notes | Tạo Credit Note mới thành công | Đăng nhập Admin. Có Customer | 1. Sales > Credit Notes > "New Credit Note"<br>2. Chọn Customer: "Test Customer 01"<br>3. Add Item: Rate: 1500, Qty: 1<br>4. Save | Customer: Test Customer 01<br>Rate: 1500 | 1. Credit Note tạo thành công<br>2. Number auto CN-XXXXXX<br>3. Remaining Amount = 1500 USD | Critical | `CreditNoteTest#testCreateCreditNoteSuccess()` | `credit-notes.spec.ts > create new credit note success` |
| CRM_SALES_TC_069 | Credit Notes | Credit Note # tự động tăng dần | Có CN-000001 | 1. Tạo Credit Note mới<br>2. Quan sát CN # tự động điền | CN-000001 | Credit Note # mới = CN-000002 | High | `CreditNoteTest#testCreditNoteNumberAutoIncrement()` | `credit-notes.spec.ts > credit note number auto increment` |
| CRM_SALES_TC_070 | Credit Notes | Tạo Credit Note và gửi ngay (Save & Send) | Customer email hợp lệ | 1. New Credit Note<br>2. Add Item<br>3. Save & Send | Email: test_cn@domain.com | Lưu CN và gửi email thành công | High | `CreditNoteTest#testCreateAndSendCreditNote()` | `credit-notes.spec.ts > create and send credit note` |
| CRM_SALES_TC_071 | Credit Notes | Apply Credit Note vào Invoice | Credit Note Remaining = 1500 USD. Invoice Unpaid = 3000 USD | 1. Chi tiết Credit Note<br>2. Click "Apply to Invoice"<br>3. Chọn Invoice INV-000005, amount 1500 → Confirm | CN: 1500 USD<br>INV: 3000 USD | 1. Invoice còn lại 1500 USD (Partially Paid)<br>2. Credit Note Remaining = 0 | Critical | `CreditNoteTest#testApplyCreditNoteToInvoice()` | `credit-notes.spec.ts > apply credit note to invoice` |
| CRM_SALES_TC_072 | Credit Notes | Xem Remaining Amount của Credit Note | Credit Note chưa Apply | 1. Sales > Credit Notes<br>2. Xem cột Remaining Amount | — | Remaining Amount = Total Amount | High | `CreditNoteTest#testVerifyCreditNoteRemainingAmount()` | `credit-notes.spec.ts > verify credit note remaining amount` |
| CRM_SALES_TC_073 | Credit Notes | Xem danh sách Credit Notes | Có Credit Note | 1. Sales > Credit Notes | — | Bảng hiển thị đầy đủ, có Search, Filter, Export | High | `CreditNoteTest#testVerifyCreditNoteListTable()` | `credit-notes.spec.ts > verify credit note list table` |
| CRM_SALES_TC_074 | Credit Notes | Chỉnh sửa Credit Note ở trạng thái Draft | Credit Note Draft | 1. Edit Credit Note<br>2. Sửa Rate: 1500 → 2000 → Save | Rate: 2000 | Cập nhật thành công, Remaining Amount = 2000 USD | High | `CreditNoteTest#testEditCreditNoteDraft()` | `credit-notes.spec.ts > edit credit note draft` |
| CRM_SALES_TC_075 | Credit Notes | Áp dụng 1 Credit Note cho nhiều Invoice | Credit Note 3000 USD. 2 Invoices 1000 USD | 1. Apply CN vào INV-A 1000 USD<br>2. Apply CN vào INV-B 1000 USD | CN: 3000 USD<br>INV-A: 1000, INV-B: 1000 | 1. INV-A & INV-B chuyển Paid<br>2. CN Remaining = 1000 USD | High | `CreditNoteTest#testApplyCreditNoteToMultipleInvoices()` | `credit-notes.spec.ts > apply credit note to multiple invoices` |

### 🔴 NEGATIVE PATH

| TC ID | Module | Test Scenario | Pre-Condition | Test Steps | Test Data | Expected Result | Priority | Selenium Java Mapping | Playwright TS Mapping |
|-------|--------|--------------|---------------|-----------|-----------|-----------------|---------|-----------------------|----------------------|
| CRM_SALES_TC_076 | Credit Notes | Submit Credit Note khi chưa chọn Customer | Đăng nhập Admin | 1. New Credit Note<br>2. Bỏ trống Customer → Save | Customer: (trống) | Báo lỗi Customer là bắt buộc | Critical | `CreditNoteTest#testCreateCreditNoteEmptyCustomer()` | `credit-notes.spec.ts > validation empty customer` |
| CRM_SALES_TC_077 | Credit Notes | Submit Credit Note khi không có Items | Đăng nhập Admin | 1. New Credit Note<br>2. Không thêm Item → Save | Items: (trống) | Ngăn lưu khi không có Items | High | `CreditNoteTest#testCreateCreditNoteNoItems()` | `credit-notes.spec.ts > validation no items` |
| CRM_SALES_TC_078 | Credit Notes | Apply Credit Note vào Invoice của Customer khác | CN Customer A, Invoice Customer B | 1. Chi tiết CN Customer A<br>2. Apply to Invoice → Tim Invoice Customer B | Customers khác nhau | Không hiển thị Invoice của Customer B | High | `CreditNoteTest#testApplyCreditNoteCrossCustomerPrevented()` | `credit-notes.spec.ts > validation cross customer apply prevented` |

### 🟡 FIELD VALIDATION

| TC ID | Module | Test Scenario | Pre-Condition | Test Steps | Test Data | Expected Result | Priority | Selenium Java Mapping | Playwright TS Mapping |
|-------|--------|--------------|---------------|-----------|-----------|-----------------|---------|-----------------------|----------------------|
| CRM_SALES_TC_079 | Credit Notes | Validate Credit Note Date — Để trống | Đăng nhập Admin | 1. New Credit Note<br>2. Xóa Date → Save | Date: (trống) | Báo lỗi Credit Note Date là bắt buộc | Critical | `CreditNoteTest#testEmptyCreditNoteDate()` | `credit-notes.spec.ts > validation empty credit note date` |
| CRM_SALES_TC_080 | Credit Notes | Validate Credit Note # — Số trùng | Đã có CN-000001 | 1. New Credit Note<br>2. CN #: "CN-000001" → Save | CN-000001 | Báo lỗi Số CN đã tồn tại (BR-02) | High | `CreditNoteTest#testDuplicateCreditNoteNumber()` | `credit-notes.spec.ts > validation duplicate credit note number` |
| CRM_SALES_TC_081 | Credit Notes | Validate Reference # — SQL Injection | Đăng nhập Admin | 1. New Credit Note<br>2. Ref #: `' UNION SELECT * FROM users--`<br>3. Save | Ref: SQL Injection | Escape an toàn, không chạy SQL | Critical | `CreditNoteTest#testReferenceSQLInjection()` | `credit-notes.spec.ts > security reference sql injection` |
| CRM_SALES_TC_082 | Credit Notes | Validate Item Rate — Số âm | Đăng nhập Admin | 1. New Credit Note<br>2. Rate: -200 → Save | Rate: -200 | Từ chối giá trị âm, báo lỗi | High | `CreditNoteTest#testNegativeItemRate()` | `credit-notes.spec.ts > validation negative item rate` |
| CRM_SALES_TC_083 | Credit Notes | Validate Admin Note — Max length (>5000 chars) | Đăng nhập Admin | 1. New Credit Note<br>2. Admin Note: 5001 ký tự "A"<br>3. Save | 5001 ký tự | Truncate hoặc báo lỗi max length | Low | `CreditNoteTest#testAdminNoteMaxLength()` | `credit-notes.spec.ts > validation admin note max length` |
| CRM_SALES_TC_084 | Credit Notes | Validate Item Quantity — Nhập 0 | Đăng nhập Admin | 1. New Credit Note<br>2. Item Qty: 0 → Save | Qty: 0 | Từ chối Qty = 0 | High | `CreditNoteTest#testItemQuantityZero()` | `credit-notes.spec.ts > validation item quantity zero` |

---

## 2.6 Sub-module: Items (Sản phẩm/Dịch vụ)

### 🔵 HAPPY PATH

| TC ID | Module | Test Scenario | Pre-Condition | Test Steps | Test Data | Expected Result | Priority | Selenium Java Mapping | Playwright TS Mapping |
|-------|--------|--------------|---------------|-----------|-----------|-----------------|---------|-----------------------|----------------------|
| CRM_SALES_TC_085 | Items | Tạo Item mới thành công qua Modal | Đăng nhập Admin | 1. Sales > Items > "New Item"<br>2. Modal form hiển thị<br>3. Description: "Dịch vụ tư vấn giờ", Rate USD: 150<br>4. Save | Description: Dịch vụ tư vấn giờ<br>Rate USD: 150 | 1. Modal mở trực tiếp trên page<br>2. Item mới hiển thị trong bảng | Critical | `ItemTest#testCreateItemModalSuccess()` | `items.spec.ts > create new item via modal` |
| CRM_SALES_TC_086 | Items | Tạo Item với đầy đủ thông tin | Đăng nhập Admin | 1. New Item<br>2. Description: "Gói CRM", Rate USD: 500, EUR: 460, Tax: 10%, Unit: License, Group: Software<br>3. Save | Multi-currency & Group | Item tạo thành công với đủ thông tin | High | `ItemTest#testCreateItemWithFullFields()` | `items.spec.ts > create item with full fields` |
| CRM_SALES_TC_087 | Items | Xem danh sách Items | Có Items | 1. Sales > Items | — | Hiển thị bảng Items, có Search, Pagination, Export, Groups | High | `ItemTest#testVerifyItemListTable()` | `items.spec.ts > verify item list table` |
| CRM_SALES_TC_088 | Items | Chỉnh sửa Item đã tạo | Có Item | 1. Edit Item "Dịch vụ tư vấn giờ"<br>2. Sửa Rate USD: 150 → 200 → Save | Rate: 200 USD | Cập nhật thành công Rate = 200 USD | High | `ItemTest#testEditItem()` | `items.spec.ts > edit existing item` |
| CRM_SALES_TC_089 | Items | Xóa Item | Item chưa dùng trong Invoice | 1. Click Delete trên Item<br>2. Confirm | — | Hộp thoại confirm hiển thị, Item bị xóa | High | `ItemTest#testDeleteItem()` | `items.spec.ts > delete unused item` |
| CRM_SALES_TC_090 | Items | Import Items từ file CSV hợp lệ | File CSV đúng định dạng (5 items) | 1. Sales > Items > "Import Items"<br>2. Upload file CSV → Confirm import | items_import.csv (5 items) | 5 Items được import thành công | High | `ItemTest#testImportItemsCSV()` | `items.spec.ts > import items from csv` |
| CRM_SALES_TC_091 | Items | Quản lý Groups — Tạo nhóm mới | Đăng nhập Admin | 1. Sales > Items > "Groups"<br>2. Nhập Group Name: "Software Products" → Save | Group: Software Products | Group mới được tạo thành công | Medium | `ItemTest#testCreateItemGroup()` | `items.spec.ts > create item group` |
| CRM_SALES_TC_092 | Items | Gán Item vào Group khi tạo mới | Có Group "Software Products" | 1. New Item<br>2. Chọn Item Group: "Software Products"<br>3. Save | Group: Software Products | Item được gán đúng vào Group | Medium | `ItemTest#testAssignItemToGroup()` | `items.spec.ts > assign item to group` |
| CRM_SALES_TC_093 | Items | Tìm kiếm Item theo Description | Có Items | 1. Sales > Items<br>2. Search: "Tư vấn" | Search: "Tư vấn" | Bảng hiển thị Items khớp từ khóa | Medium | `ItemTest#testSearchItemByDescription()` | `items.spec.ts > search item by description` |
| CRM_SALES_TC_094 | Items | Item được dùng khi tạo Invoice | Có Item "Dịch vụ tư vấn giờ" | 1. New Invoice > Chọn Item "Dịch vụ tư vấn giờ" từ dropdown | — | Item hiển thị trong dropdown, Rate tự động điền 150 USD | High | `ItemTest#testItemDropdownInInvoice()` | `items.spec.ts > item dropdown integration in invoice` |

### 🔴 NEGATIVE PATH

| TC ID | Module | Test Scenario | Pre-Condition | Test Steps | Test Data | Expected Result | Priority | Selenium Java Mapping | Playwright TS Mapping |
|-------|--------|--------------|---------------|-----------|-----------|-----------------|---------|-----------------------|----------------------|
| CRM_SALES_TC_095 | Items | Submit Item khi để trống Description | Đăng nhập Admin | 1. New Item<br>2. Bỏ trống Description → Save | Description: (trống) | Báo lỗi Description là bắt buộc | Critical | `ItemTest#testCreateItemEmptyDescription()` | `items.spec.ts > validation empty description` |
| CRM_SALES_TC_096 | Items | Submit Item khi để trống Rate USD | Đăng nhập Admin | 1. New Item<br>2. Nhập Description, bỏ trống Rate USD → Save | Rate USD: (trống) | Báo lỗi Rate là bắt buộc | Critical | `ItemTest#testCreateItemEmptyRate()` | `items.spec.ts > validation empty rate` |
| CRM_SALES_TC_097 | Items | Import Items với file sai định dạng | Đăng nhập Admin | 1. Import Items<br>2. Upload file: wrong_format.txt → Confirm | File: wrong_format.txt | Báo lỗi Chỉ chấp nhận file CSV | High | `ItemTest#testImportItemsInvalidFileFormat()` | `items.spec.ts > validation import invalid file format` |
| CRM_SALES_TC_098 | Items | Import Items với file CSV trống | Đăng nhập Admin | 1. Import Items<br>2. Upload file CSV không có data → Confirm | File CSV trống | Thông báo Không có dòng data để import | Medium | `ItemTest#testImportItemsEmptyCSV()` | `items.spec.ts > validation import empty csv` |

### 🟡 FIELD VALIDATION

| TC ID | Module | Test Scenario | Pre-Condition | Test Steps | Test Data | Expected Result | Priority | Selenium Java Mapping | Playwright TS Mapping |
|-------|--------|--------------|---------------|-----------|-----------|-----------------|---------|-----------------------|----------------------|
| CRM_SALES_TC_099 | Items | Validate Description — XSS Injection | Đăng nhập Admin | 1. New Item<br>2. Description: `<script>alert('XSS')</script>` → Save | `<script>alert('XSS')</script>` | Escape/sanitize an toàn, không chạy script | Critical | `ItemTest#testDescriptionXSSInjection()` | `items.spec.ts > security description xss injection` |
| CRM_SALES_TC_100 | Items | Validate Rate USD — Số âm | Đăng nhập Admin | 1. New Item<br>2. Rate USD: -100 → Save | Rate USD: -100 | Từ chối giá trị âm, báo lỗi | High | `ItemTest#testNegativeRateUSD()` | `items.spec.ts > validation negative rate usd` |

---

## State Transition Testing — Chuyển trạng thái

| TC ID | Module | Test Scenario | Pre-Condition | Test Steps | Test Data | Expected Result | Priority | Selenium Java Mapping | Playwright TS Mapping |
|-------|--------|--------------|---------------|-----------|-----------|-----------------|---------|-----------------------|----------------------|
| CRM_SALES_TC_101 | Invoices | State: Draft → Sent | Invoice Status: Draft | 1. Chi tiết Invoice Draft<br>2. Click Send Invoice → Confirm | Status: Draft | Invoice Status chuyển Draft → Sent, email được gửi | Critical | `SalesStateTransitionTest#testInvoiceDraftToSent()` | `state-transition.spec.ts > invoice state transition draft to sent` |
| CRM_SALES_TC_102 | Invoices | State: Unpaid → Overdue (quá Due Date) | Invoice Unpaid với Due Date = qua rồi | 1. Truy cập Sales > Invoices<br>2. Kiểm tra Status của Invoice quá hạn | Due Date đã qua | Status tự động chuyển sang Overdue (BR-INV-01) | Critical | `SalesStateTransitionTest#testInvoiceUnpaidToOverdue()` | `state-transition.spec.ts > invoice state transition unpaid to overdue` |
| CRM_SALES_TC_103 | Proposals | State: Draft → Sent → Accepted | Proposal Status: Draft | 1. Send Proposal (Status: Sent)<br>2. Customer Accept Proposal | — | Status chuyển từ Draft → Sent → Accepted | High | `SalesStateTransitionTest#testProposalDraftToSentToAccepted()` | `state-transition.spec.ts > proposal state transition draft to sent to accepted` |
| CRM_SALES_TC_104 | Estimates | State: Draft → Sent → Declined | Estimate Status: Draft | 1. Send Estimate (Status: Sent)<br>2. Customer Decline Estimate | — | Status chuyển từ Draft → Sent → Declined | High | `SalesStateTransitionTest#testEstimateDraftToSentToDeclined()` | `state-transition.spec.ts > estimate state transition draft to sent to declined` |
| CRM_SALES_TC_105 | Invoices | State: Partially Paid → Paid | Invoice Partially Paid, còn 3000 USD | 1. Record Payment 3000 USD (đủ 100%) → Save | Amount: 3000 USD | Invoice Status chuyển từ Partially Paid → Paid | Critical | `SalesStateTransitionTest#testInvoicePartiallyPaidToPaid()` | `state-transition.spec.ts > invoice state transition partially paid to paid` |

---

## 📊 Tổng hợp Test Cases & Mapping

| Sub-module | Tổng TC | Selenium Java Test Class | Playwright TS Spec File |
|-----------|---------|---------------------------|------------------------|
| Proposals (TC_001-022) | 22 | `com.anhtester.tests.sales.ProposalTest` | `src/tests/sales/proposals.spec.ts` |
| Estimates (TC_023-037) | 15 | `com.anhtester.tests.sales.EstimateTest` | `src/tests/sales/estimates.spec.ts` |
| Invoices (TC_038-060) | 23 | `com.anhtester.tests.sales.InvoiceTest` | `src/tests/sales/invoices.spec.ts` |
| Payments (TC_061-067) | 7 | `com.anhtester.tests.sales.PaymentTest` | `src/tests/sales/payments.spec.ts` |
| Credit Notes (TC_068-084) | 17 | `com.anhtester.tests.sales.CreditNoteTest` | `src/tests/sales/credit-notes.spec.ts` |
| Items (TC_085-100) | 16 | `com.anhtester.tests.sales.ItemTest` | `src/tests/sales/items.spec.ts` |
| State Transition (TC_101-105) | 5 | `com.anhtester.tests.sales.SalesStateTransitionTest` | `src/tests/sales/state-transition.spec.ts` |
| **TỔNG** | **105** | **4 Test Classes** | **4 Spec Files** |

---

*Part 2 hoàn tất — 45 Test Cases (TC_061 → TC_105) đã mapped với Selenium Java & Playwright TS*
