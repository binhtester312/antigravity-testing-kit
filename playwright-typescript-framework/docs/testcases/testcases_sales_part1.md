# Manual Test Cases — Module: Sales (CRM Perfex)
## Part 1: Proposals | Estimates | Invoices (kèm Automation Mapping for Selenium Java & Playwright TS)

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
  - `com.anhtester.pages.sales.ProposalPage`
  - `com.anhtester.pages.sales.EstimatePage`
  - `com.anhtester.pages.sales.InvoicePage`
- **Test Classes:**
  - `com.anhtester.tests.sales.ProposalTest`
  - `com.anhtester.tests.sales.EstimateTest`
  - `com.anhtester.tests.sales.InvoiceTest`

### 2. Playwright TypeScript Framework (`playwright-typescript-framework`)
- **Page Objects:**
  - `src/pages/sales/proposals.page.ts` (`ProposalsPage`)
  - `src/pages/sales/estimates.page.ts` (`EstimatesPage`)
  - `src/pages/sales/invoices.page.ts` (`InvoicesPage`)
- **Test Specs:**
  - `src/tests/sales/proposals.spec.ts`
  - `src/tests/sales/estimates.spec.ts`
  - `src/tests/sales/invoices.spec.ts`

---

## 📋 Phạm vi Part 1

| Sub-module | Số TC | Class Selenium Java | Spec Playwright TS |
|-----------|-------|----------------------|-------------------|
| Proposals | TC_001 → TC_022 | `ProposalTest.java` | `proposals.spec.ts` |
| Estimates | TC_023 → TC_037 | `EstimateTest.java` | `estimates.spec.ts` |
| Invoices | TC_038 → TC_060 | `InvoiceTest.java` | `invoices.spec.ts` |

---

## 2.1 Sub-module: Proposals (Báo giá)

### 🔵 HAPPY PATH — Luồng chính

| TC ID | Module | Test Scenario | Pre-Condition | Test Steps | Test Data | Expected Result | Priority | Selenium Java Mapping | Playwright TS Mapping |
|-------|--------|--------------|---------------|-----------|-----------|-----------------|---------|-----------------------|----------------------|
| CRM_SALES_TC_001 | Proposals | Tạo Proposal mới thành công (Save as Draft) | Đăng nhập Admin. Có Customer | 1. Truy cập Sales > Proposals<br>2. Nhấn "New Proposal"<br>3. Nhập Subject: "Proposal bán dịch vụ tư vấn - 2026"<br>4. Chọn Related: "Customer"<br>5. Chọn To: "Test Customer 01"<br>6. Giữ Date (ngày hiện tại)<br>7. Currency: USD<br>8. Email: test_customer_01@domain.com<br>9. Add Item: Description: "Tư vấn phần mềm", Rate: 1000, Qty: 2<br>10. Nhấn "Save" | Subject: Proposal tư vấn 2026<br>Customer: Test Customer 01<br>Email: test_customer_01@domain.com<br>Rate: 1000, Qty: 2 | 1. Proposal lưu thành công với Status: Draft<br>2. Hiển thị trong danh sách<br>3. Tổng tiền = 2000 USD | Critical | `ProposalTest#testCreateProposalDraftSuccess()` | `proposals.spec.ts > create new proposal as draft` |
| CRM_SALES_TC_002 | Proposals | Tạo Proposal và gửi ngay (Save & Send) | Đăng nhập Admin. Customer có email hợp lệ | 1. Vào Sales > Proposals > New Proposal<br>2. Nhập Subject: "Báo giá dịch vụ hosting - 2026"<br>3. Related: Customer → To: "Test Customer 01"<br>4. Email: test_customer_send@domain.com<br>5. Add Item: Rate: 500, Qty: 1<br>6. Nhấn "Save & Send" | Email: test_customer_send@domain.com<br>Item: Hosting 1 năm, 500 USD | 1. Proposal được lưu<br>2. Email gửi tới customer<br>3. Status chuyển sang "Sent" | Critical | `ProposalTest#testCreateAndSendProposal()` | `proposals.spec.ts > create and send proposal` |
| CRM_SALES_TC_003 | Proposals | Tạo Proposal với Related = Lead | Đăng nhập Admin. Có Lead trong hệ thống | 1. New Proposal<br>2. Chọn Related: "Lead"<br>3. Chọn Lead từ dropdown "To"<br>4. Nhập đủ thông tin và Save | Related: Lead | 1. Dropdown "To" populate danh sách Leads (BR-PROP-01)<br>2. Proposal lưu thành công | High | `ProposalTest#testCreateProposalForLead()` | `proposals.spec.ts > create proposal for lead` |
| CRM_SALES_TC_004 | Proposals | Xem danh sách Proposals | Có ít nhất 1 Proposal | 1. Truy cập Sales > Proposals | — | 1. Hiển thị bảng đầy đủ cột<br>2. Có Search, Filter Status, Pagination, Export | High | `ProposalTest#testVerifyProposalListTable()` | `proposals.spec.ts > verify proposal list table` |
| CRM_SALES_TC_005 | Proposals | Lọc Proposals theo Status | Có Proposals nhiều Status | 1. Vào Sales > Proposals<br>2. Click bộ lọc Status chọn "Sent"<br>3. Quan sát kết quả | Filter: Status = Sent | Danh sách chỉ hiển thị Proposals có Status = "Sent" | Medium | `ProposalTest#testFilterProposalsByStatus()` | `proposals.spec.ts > filter proposals by status` |
| CRM_SALES_TC_006 | Proposals | Chỉnh sửa Proposal đã tạo | Có Proposal ở trạng thái Draft | 1. Vào Sales > Proposals<br>2. Click Edit Proposal<br>3. Sửa Subject: "Proposal Updated - 2026"<br>4. Save | Subject: "Proposal Updated - 2026" | 1. Subject cập nhật thành công<br>2. Thông báo thành công | High | `ProposalTest#testEditProposal()` | `proposals.spec.ts > edit existing proposal` |
| CRM_SALES_TC_007 | Proposals | Xóa Proposal | Có Proposal ở trạng thái Draft | 1. Vào danh sách Proposals<br>2. Click icon Delete<br>3. Xác nhận xóa | — | 1. Hộp thoại confirm hiển thị<br>2. Proposal bị xóa khỏi bảng | High | `ProposalTest#testDeleteProposal()` | `proposals.spec.ts > delete proposal` |
| CRM_SALES_TC_008 | Proposals | Tạo Proposal với Discount (Before Tax) | Đăng nhập Admin | 1. New Proposal<br>2. Điền thông tin bắt buộc<br>3. Discount Type: "Before Tax" 10%<br>4. Add Item: Rate 1000, Qty 1<br>5. Save | Discount: 10% Before Tax<br>Item Rate: 1000 | Tổng tiền tính đúng: 1000 - 10% = 900 USD | Medium | `ProposalTest#testCreateProposalWithDiscount()` | `proposals.spec.ts > create proposal with discount` |

### 🔴 NEGATIVE PATH — Trường hợp lỗi

| TC ID | Module | Test Scenario | Pre-Condition | Test Steps | Test Data | Expected Result | Priority | Selenium Java Mapping | Playwright TS Mapping |
|-------|--------|--------------|---------------|-----------|-----------|-----------------|---------|-----------------------|----------------------|
| CRM_SALES_TC_009 | Proposals | Submit form khi bỏ trống Subject (bắt buộc) | Đăng nhập Admin | 1. New Proposal<br>2. Bỏ trống Subject<br>3. Điền đủ trường khác và Save | Subject: (trống) | Lỗi validation hiển thị tại trường Subject, không cho lưu | Critical | `ProposalTest#testCreateProposalEmptySubject()` | `proposals.spec.ts > validation empty subject` |
| CRM_SALES_TC_010 | Proposals | Submit form khi chưa chọn Related (bắt buộc) | Đăng nhập Admin | 1. New Proposal<br>2. Nhập Subject, bỏ qua Related<br>3. Save | Related: (chưa chọn) | Lỗi validation hiển thị tại trường Related | Critical | `ProposalTest#testCreateProposalEmptyRelated()` | `proposals.spec.ts > validation empty related` |
| CRM_SALES_TC_011 | Proposals | Submit form khi Email không đúng định dạng | Đăng nhập Admin | 1. New Proposal<br>2. Nhập Email: "not-an-email"<br>3. Save | Email: "not-an-email" | Lỗi validation: Email không đúng định dạng | Critical | `ProposalTest#testCreateProposalInvalidEmailFormat()` | `proposals.spec.ts > validation invalid email format` |
| CRM_SALES_TC_012 | Proposals | Nhập Email thiếu domain | Đăng nhập Admin | 1. New Proposal<br>2. Email: "testuser@"<br>3. Save | Email: "testuser@" | Báo lỗi Email không hợp lệ | High | `ProposalTest#testCreateProposalEmailMissingDomain()` | `proposals.spec.ts > validation email missing domain` |
| CRM_SALES_TC_013 | Proposals | Nhập Email với nhiều ký tự @ | Đăng nhập Admin | 1. New Proposal<br>2. Email: "user@@domain.com"<br>3. Save | Email: "user@@domain.com" | Báo lỗi Email không hợp lệ | High | `ProposalTest#testCreateProposalEmailMultipleAt()` | `proposals.spec.ts > validation email multiple at signs` |
| CRM_SALES_TC_014 | Proposals | Nhập Item Quantity = 0 | Đăng nhập Admin | 1. New Proposal<br>2. Item Qty: 0<br>3. Save | Item Qty: 0 | Báo lỗi Qty phải > 0 | High | `ProposalTest#testCreateProposalItemQuantityZero()` | `proposals.spec.ts > validation item quantity zero` |
| CRM_SALES_TC_015 | Proposals | Nhập Item Rate âm | Đăng nhập Admin | 1. New Proposal<br>2. Item Rate: -500<br>3. Save | Item Rate: -500 | Từ chối hoặc báo lỗi giá trị âm | High | `ProposalTest#testCreateProposalNegativeItemRate()` | `proposals.spec.ts > validation negative item rate` |

### 🟡 FIELD VALIDATION — Từng trường

| TC ID | Module | Test Scenario | Pre-Condition | Test Steps | Test Data | Expected Result | Priority | Selenium Java Mapping | Playwright TS Mapping |
|-------|--------|--------------|---------------|-----------|-----------|-----------------|---------|-----------------------|----------------------|
| CRM_SALES_TC_016 | Proposals | Validate Subject — XSS injection | Đăng nhập Admin | 1. New Proposal<br>2. Subject: `<script>alert('XSS')</script>`<br>3. Save | Subject: `<script>alert('XSS')</script>` | Sanitize/escape an toàn, không thực thi script | Critical | `ProposalTest#testSubjectXSSInjection()` | `proposals.spec.ts > security subject xss injection` |
| CRM_SALES_TC_017 | Proposals | Validate Subject — SQL Injection | Đăng nhập Admin | 1. New Proposal<br>2. Subject: `' OR 1=1--`<br>3. Save | Subject: `' OR 1=1--` | Không thực thi SQL, lưu an toàn | Critical | `ProposalTest#testSubjectSQLInjection()` | `proposals.spec.ts > security subject sql injection` |
| CRM_SALES_TC_018 | Proposals | Validate Subject — Whitespace-only | Đăng nhập Admin | 1. New Proposal<br>2. Subject: "     "<br>3. Save | Subject: "     " | Từ chối, báo lỗi trường bắt buộc | High | `ProposalTest#testSubjectWhitespaceOnly()` | `proposals.spec.ts > validation subject whitespace only` |
| CRM_SALES_TC_019 | Proposals | Validate Open Till Date — Ngày không tồn tại | Đăng nhập Admin | 1. New Proposal<br>2. Open Till: "31/02/2026"<br>3. Save | Open Till: 31/02/2026 | Datepicker ngăn chọn ngày không tồn tại | High | `ProposalTest#testInvalidOpenTillDate()` | `proposals.spec.ts > validation invalid open till date` |
| CRM_SALES_TC_020 | Proposals | Validate Open Till — Trước Proposal Date | Đăng nhập Admin | 1. New Proposal<br>2. Date: 29/07/2026, Open Till: 28/07/2026<br>3. Save | Date: 29/07/2026, Open Till: 28/07/2026 | Cảnh báo hoặc ngăn lưu Open Till < Date | Medium | `ProposalTest#testOpenTillBeforeDate()` | `proposals.spec.ts > validation open till before date` |
| CRM_SALES_TC_021 | Proposals | Validate Item Rate — Ký tự chữ | Đăng nhập Admin | 1. New Proposal<br>2. Item Rate: "abc"<br>3. Save | Item Rate: "abc" | Không nhận ký tự chữ, chỉ cho nhập số | High | `ProposalTest#testItemRateNonNumeric()` | `proposals.spec.ts > validation item rate non numeric` |
| CRM_SALES_TC_022 | Proposals | Validate Email — Ký tự Unicode đặc biệt | Đăng nhập Admin | 1. New Proposal<br>2. Email: "tèst@domain.com"<br>3. Save | Email: "tèst@domain.com" | Kiểm tra xử lý email unicode đúng quy chuẩn | Low | `ProposalTest#testEmailUnicodeChar()` | `proposals.spec.ts > validation email unicode characters` |

---

## 2.2 Sub-module: Estimates (Ước tính)

### 🔵 HAPPY PATH

| TC ID | Module | Test Scenario | Pre-Condition | Test Steps | Test Data | Expected Result | Priority | Selenium Java Mapping | Playwright TS Mapping |
|-------|--------|--------------|---------------|-----------|-----------|-----------------|---------|-----------------------|----------------------|
| CRM_SALES_TC_023 | Estimates | Tạo Estimate mới thành công | Đăng nhập Admin. Có Customer | 1. Sales > Estimates > "Create New Estimate"<br>2. Chọn Customer: "Test Customer 01"<br>3. Kiểm tra Estimate Number (format EST-XXXXXX)<br>4. Currency: USD<br>5. Add Item: Rate: 2000, Qty: 1<br>6. Save | Customer: Test Customer 01<br>Rate: 2000, Qty: 1 | 1. Estimate tạo với Status: Draft<br>2. Số EST auto-generate<br>3. Tổng tiền = 2000 USD | Critical | `EstimateTest#testCreateEstimateSuccess()` | `estimates.spec.ts > create new estimate success` |
| CRM_SALES_TC_024 | Estimates | Estimate Number tự động tăng | Có ít nhất 1 Estimate | 1. Xem Estimate Number hiện tại (EST-000001)<br>2. Tạo Estimate mới<br>3. Kiểm tra số auto-fill | EST-000001 | Estimate Number mới = EST-000002 | High | `EstimateTest#testEstimateNumberAutoIncrement()` | `estimates.spec.ts > estimate number auto increment` |
| CRM_SALES_TC_025 | Estimates | Convert Estimate sang Invoice | Estimate Status: Accepted | 1. Chi tiết Estimate Accepted<br>2. Click "Convert to Invoice"<br>3. Confirm | Status: Accepted | 1. Tạo Invoice mới tự động<br>2. Giữ nguyên dữ liệu từ Estimate | Critical | `EstimateTest#testConvertEstimateToInvoice()` | `estimates.spec.ts > convert estimate to invoice` |
| CRM_SALES_TC_026 | Estimates | Xem danh sách Estimates | Có Estimate | 1. Sales > Estimates | — | Hiển thị bảng dữ liệu, có Search, Filter, Export | High | `EstimateTest#testVerifyEstimateListTable()` | `estimates.spec.ts > verify estimate list table` |
| CRM_SALES_TC_027 | Estimates | Tạo Estimate với Expiry Date | Đăng nhập Admin | 1. New Estimate<br>2. Chọn Expiry Date: +7 ngày<br>3. Add Item và Save | Expiry Date: 05/08/2026 | Estimate lưu đúng Expiry Date | Medium | `EstimateTest#testCreateEstimateWithExpiryDate()` | `estimates.spec.ts > create estimate with expiry date` |
| CRM_SALES_TC_028 | Estimates | Chỉnh sửa Estimate ở trạng thái Draft | Estimate Status: Draft | 1. Edit Estimate Draft<br>2. Sửa Rate: 2000 → 3000<br>3. Save | Rate: 3000 | Cập nhật thành công, tổng tiền = 3000 USD | High | `EstimateTest#testEditEstimateDraft()` | `estimates.spec.ts > edit estimate draft` |

### 🔴 NEGATIVE PATH

| TC ID | Module | Test Scenario | Pre-Condition | Test Steps | Test Data | Expected Result | Priority | Selenium Java Mapping | Playwright TS Mapping |
|-------|--------|--------------|---------------|-----------|-----------|-----------------|---------|-----------------------|----------------------|
| CRM_SALES_TC_029 | Estimates | Submit khi chưa chọn Customer | Đăng nhập Admin | 1. New Estimate<br>2. Bỏ trống Customer<br>3. Add Item và Save | Customer: (trống) | Báo lỗi Customer là bắt buộc | Critical | `EstimateTest#testCreateEstimateEmptyCustomer()` | `estimates.spec.ts > validation empty customer` |
| CRM_SALES_TC_030 | Estimates | Submit khi không có Item nào | Đăng nhập Admin | 1. New Estimate<br>2. Chọn Customer<br>3. Không thêm Item → Save | Items: (trống) | Ngăn lưu khi không có Items | High | `EstimateTest#testCreateEstimateNoItems()` | `estimates.spec.ts > validation no items` |
| CRM_SALES_TC_031 | Estimates | Chỉnh sửa Estimate Number trùng | Có EST-000001 | 1. New Estimate<br>2. Thay Estimate Number thành: "EST-000001"<br>3. Save | Estimate Number: EST-000001 | Báo lỗi Số Estimate đã tồn tại (BR-02) | High | `EstimateTest#testDuplicateEstimateNumber()` | `estimates.spec.ts > validation duplicate estimate number` |

### 🟡 FIELD VALIDATION

| TC ID | Module | Test Scenario | Pre-Condition | Test Steps | Test Data | Expected Result | Priority | Selenium Java Mapping | Playwright TS Mapping |
|-------|--------|--------------|---------------|-----------|-----------|-----------------|---------|-----------------------|----------------------|
| CRM_SALES_TC_032 | Estimates | Validate Estimate Date — Ngày không hợp lệ | Đăng nhập Admin | 1. New Estimate<br>2. Estimate Date: "99/99/2026"<br>3. Save | Date: 99/99/2026 | Không nhận ngày sai | High | `EstimateTest#testInvalidEstimateDate()` | `estimates.spec.ts > validation invalid estimate date` |
| CRM_SALES_TC_033 | Estimates | Validate Reference # — Ký tự XSS | Đăng nhập Admin | 1. New Estimate<br>2. Reference #: `<script>xss</script>`<br>3. Save | Ref: `<script>xss</script>` | Escape an toàn, không chạy script | Medium | `EstimateTest#testReferenceXSSInjection()` | `estimates.spec.ts > security reference xss injection` |
| CRM_SALES_TC_034 | Estimates | Validate Admin Note — HTML Tags | Đăng nhập Admin | 1. New Estimate<br>2. Admin Note: `<b>test</b>`<br>3. Save | Admin Note: `<b>test</b>` | Xử lý text an toàn | Low | `EstimateTest#testAdminNoteHTMLHandling()` | `estimates.spec.ts > validation admin note html handling` |
| CRM_SALES_TC_035 | Estimates | Validate Item Quantity — Số thập phân | Đăng nhập Admin | 1. New Estimate<br>2. Item Qty: 1.5<br>3. Save | Qty: 1.5 | Chấp nhận hoặc làm tròn theo config | Medium | `EstimateTest#testDecimalItemQuantity()` | `estimates.spec.ts > validation decimal item quantity` |
| CRM_SALES_TC_036 | Estimates | Validate Expiry Date — Trước Estimate Date | Đăng nhập Admin | 1. New Estimate<br>2. Estimate Date: 29/07/2026, Expiry Date: 20/07/2026<br>3. Save | Expiry Date < Date | Cảnh báo Expiry Date phải sau Estimate Date | High | `EstimateTest#testExpiryDateBeforeEstimateDate()` | `estimates.spec.ts > validation expiry date before estimate date` |
| CRM_SALES_TC_037 | Estimates | Validate Item Rate — Nhập 0 | Đăng nhập Admin | 1. New Estimate<br>2. Item Rate: 0<br>3. Save | Rate: 0 | Lưu thành công với Rate = 0 | Low | `EstimateTest#testZeroItemRate()` | `estimates.spec.ts > validation zero item rate` |

---

## 2.3 Sub-module: Invoices (Hóa đơn)

### 🔵 HAPPY PATH

| TC ID | Module | Test Scenario | Pre-Condition | Test Steps | Test Data | Expected Result | Priority | Selenium Java Mapping | Playwright TS Mapping |
|-------|--------|--------------|---------------|-----------|-----------|-----------------|---------|-----------------------|----------------------|
| CRM_SALES_TC_038 | Invoices | Tạo Invoice mới thành công | Đăng nhập Admin. Có Customer | 1. Sales > Invoices > "Create New Invoice"<br>2. Chọn Customer: "Test Customer 01"<br>3. Invoice Number auto INV-XXXXXX<br>4. Payment Mode: Bank<br>5. Add Item: Rate: 5000, Qty: 1<br>6. Save | Customer: Test Customer 01<br>Rate: 5000 | 1. Invoice tạo thành công<br>2. Status: Unpaid/Draft<br>3. Tổng tiền = 5000 USD | Critical | `InvoiceTest#testCreateInvoiceSuccess()` | `invoices.spec.ts > create new invoice success` |
| CRM_SALES_TC_039 | Invoices | Ghi nhận thanh toán đủ → Status: Paid | Invoice Unpaid = 5000 USD | 1. Chi tiết Invoice<br>2. Record Payment: Mode Bank, Amount 5000<br>3. Save | Amount: 5000 USD | 1. Payment ghi nhận<br>2. Invoice Status tự động đổi sang "Paid" | Critical | `InvoiceTest#testRecordFullPaymentInvoicePaid()` | `invoices.spec.ts > record full payment invoice paid` |
| CRM_SALES_TC_040 | Invoices | Ghi nhận thanh toán một phần → Partially Paid | Invoice Unpaid = 5000 USD | 1. Chi tiết Invoice<br>2. Record Payment: Amount 2000<br>3. Save | Amount: 2000 USD | Status chuyển sang "Partially Paid", còn lại 3000 USD | Critical | `InvoiceTest#testRecordPartialPaymentInvoice()` | `invoices.spec.ts > record partial payment invoice` |
| CRM_SALES_TC_041 | Invoices | Lọc Invoice theo Status = Overdue | Có Invoice quá hạn | 1. Sales > Invoices<br>2. Filter Status: Overdue | Filter: Overdue | Chỉ hiển thị Invoices quá hạn | High | `InvoiceTest#testFilterInvoicesOverdue()` | `invoices.spec.ts > filter invoices by overdue status` |
| CRM_SALES_TC_042 | Invoices | Tạo Invoice Recurring - Monthly | Đăng nhập Admin | 1. New Invoice<br>2. Recurring Invoice?: Monthly<br>3. Save | Recurring: Monthly | Cấu hình Recurring Monthly thành công | High | `InvoiceTest#testCreateRecurringInvoiceMonthly()` | `invoices.spec.ts > create recurring invoice monthly` |
| CRM_SALES_TC_043 | Invoices | Tạo Invoice "Save as Draft" | Đăng nhập Admin | 1. New Invoice<br>2. Điền đủ thông tin<br>3. Click "Save as Draft" | — | Invoice lưu với Status: Draft | Medium | `InvoiceTest#testSaveInvoiceAsDraft()` | `invoices.spec.ts > save invoice as draft` |
| CRM_SALES_TC_044 | Invoices | Batch Payments - thanh toán nhiều Invoice | Có ≥2 Invoice Unpaid cùng Customer | 1. Sales > Invoices > Batch Payments<br>2. Chọn 2 Invoices<br>3. Nhập số tiền thanh toán từng cái → Save | Invoices: INV-001, INV-002 | Ghi nhận thanh toán thành công cho cả 2 Invoice | High | `InvoiceTest#testBatchPaymentsMultipleInvoices()` | `invoices.spec.ts > batch payments multiple invoices` |
| CRM_SALES_TC_045 | Invoices | Xem danh sách Invoice với tất cả Status | Có Invoices nhiều Status | 1. Sales > Invoices<br>2. Lần lượt filter All / Draft / Sent / Overdue / Paid | — | Hiển thị đúng dữ liệu tương ứng filter | High | `InvoiceTest#testFilterInvoicesAllStatuses()` | `invoices.spec.ts > filter invoices all statuses` |
| CRM_SALES_TC_046 | Invoices | Bật "Prevent sending overdue reminders" | Đăng nhập Admin | 1. New Invoice<br>2. Tích "Prevent sending overdue reminders"<br>3. Save | Checkbox: Checked | Không gửi email nhắc nhở quá hạn (BR-INV-03) | Medium | `InvoiceTest#testPreventOverdueReminders()` | `invoices.spec.ts > prevent sending overdue reminders` |

### 🔴 NEGATIVE PATH

| TC ID | Module | Test Scenario | Pre-Condition | Test Steps | Test Data | Expected Result | Priority | Selenium Java Mapping | Playwright TS Mapping |
|-------|--------|--------------|---------------|-----------|-----------|-----------------|---------|-----------------------|----------------------|
| CRM_SALES_TC_047 | Invoices | Submit Invoice khi không có Customer | Đăng nhập Admin | 1. New Invoice<br>2. Bỏ trống Customer → Save | Customer: (trống) | Báo lỗi Customer là bắt buộc | Critical | `InvoiceTest#testCreateInvoiceEmptyCustomer()` | `invoices.spec.ts > validation empty customer` |
| CRM_SALES_TC_048 | Invoices | Submit Invoice khi không có Items | Đăng nhập Admin | 1. New Invoice<br>2. Chọn Customer, không thêm Item → Save | Items: (trống) | Ngăn lưu khi không có Items | High | `InvoiceTest#testCreateInvoiceNoItems()` | `invoices.spec.ts > validation no items` |
| CRM_SALES_TC_049 | Invoices | Record Payment vượt quá tổng Invoice | Invoice Unpaid = 5000 USD | 1. Chi tiết Invoice 5000 USD<br>2. Record Payment: Amount 9999 → Save | Amount: 9999 USD | Cảnh báo hoặc từ chối Payment > Total | High | `InvoiceTest#testRecordPaymentExceedTotal()` | `invoices.spec.ts > validation payment exceed total` |
| CRM_SALES_TC_050 | Invoices | Invoice Number trùng lặp | Đã có INV-000001 | 1. New Invoice<br>2. Thay Invoice Number: "INV-000001"<br>3. Save | Invoice Number: INV-000001 | Báo lỗi Số Invoice đã tồn tại (BR-02) | High | `InvoiceTest#testDuplicateInvoiceNumber()` | `invoices.spec.ts > validation duplicate invoice number` |

### 🟡 FIELD VALIDATION

| TC ID | Module | Test Scenario | Pre-Condition | Test Steps | Test Data | Expected Result | Priority | Selenium Java Mapping | Playwright TS Mapping |
|-------|--------|--------------|---------------|-----------|-----------|-----------------|---------|-----------------------|----------------------|
| CRM_SALES_TC_051 | Invoices | Validate Invoice Date — Để trống | Đăng nhập Admin | 1. New Invoice<br>2. Xóa Invoice Date → Save | Date: (trống) | Báo lỗi Invoice Date là bắt buộc | Critical | `InvoiceTest#testEmptyInvoiceDate()` | `invoices.spec.ts > validation empty invoice date` |
| CRM_SALES_TC_052 | Invoices | Validate Due Date — Trước Invoice Date | Đăng nhập Admin | 1. New Invoice<br>2. Invoice Date: 29/07/2026, Due Date: 01/07/2026<br>3. Save | Due Date < Invoice Date | Cảnh báo Due Date phải sau Invoice Date | High | `InvoiceTest#testDueDateBeforeInvoiceDate()` | `invoices.spec.ts > validation due date before invoice date` |
| CRM_SALES_TC_053 | Invoices | Validate Admin Note — XSS Injection | Đăng nhập Admin | 1. New Invoice<br>2. Admin Note: `<script>alert('XSS')</script>`<br>3. Save | Note: `<script>alert('XSS')</script>` | Escape an toàn, không chạy script | Critical | `InvoiceTest#testAdminNoteXSSInjection()` | `invoices.spec.ts > security admin note xss injection` |
| CRM_SALES_TC_054 | Invoices | Validate Invoice Number — Ký tự đặc biệt | Đăng nhập Admin | 1. New Invoice<br>2. Invoice Number: "INV-@#$%!"<br>3. Save | INV-@#$%! | Xử lý format đúng định dạng | Medium | `InvoiceTest#testInvoiceNumberSpecialChars()` | `invoices.spec.ts > validation invoice number special characters` |
| CRM_SALES_TC_055 | Invoices | Validate Item Rate — Overflow số lớn | Đăng nhập Admin | 1. New Invoice<br>2. Rate: 999999999999<br>3. Save | Rate: 999999999999 | Giới hạn hoặc xử lý số lớn an toàn | Medium | `InvoiceTest#testItemRateOverflow()` | `invoices.spec.ts > validation item rate overflow` |
| CRM_SALES_TC_056 | Invoices | Validate Item Quantity — Số âm | Đăng nhập Admin | 1. New Invoice<br>2. Item Qty: -5<br>3. Save | Qty: -5 | Từ chối số âm, báo lỗi | High | `InvoiceTest#testNegativeItemQuantity()` | `invoices.spec.ts > validation negative item quantity` |
| CRM_SALES_TC_057 | Invoices | Validate Record Payment — Payment Date để trống | Invoice Unpaid | 1. Record Payment<br>2. Xóa Payment Date → Save | Date: (trống) | Báo lỗi Payment Date là bắt buộc | High | `InvoiceTest#testEmptyPaymentDate()` | `invoices.spec.ts > validation empty payment date` |
| CRM_SALES_TC_058 | Invoices | Validate Record Payment — Amount = 0 | Invoice Unpaid | 1. Record Payment<br>2. Amount: 0 → Save | Amount: 0 | Từ chối Amount = 0 | High | `InvoiceTest#testRecordPaymentAmountZero()` | `invoices.spec.ts > validation record payment amount zero` |
| CRM_SALES_TC_059 | Invoices | Validate Record Payment — Ký tự chữ | Invoice Unpaid | 1. Record Payment<br>2. Amount: "abc" → Save | Amount: "abc" | Chỉ nhận giá trị số | High | `InvoiceTest#testRecordPaymentNonNumericAmount()` | `invoices.spec.ts > validation record payment non numeric amount` |
| CRM_SALES_TC_060 | Invoices | Validate Tags — SQL Injection | Đăng nhập Admin | 1. New Invoice<br>2. Tag: `'; DROP TABLE invoices;--`<br>3. Save | Tag: `'; DROP TABLE invoices;--` | Ngăn SQL Injection, lưu an toàn | Critical | `InvoiceTest#testTagsSQLInjection()` | `invoices.spec.ts > security tags sql injection` |

---

*Part 1 hoàn tất — 60 Test Cases (TC_001 → TC_060) đã mapped với Selenium Java & Playwright TS*
