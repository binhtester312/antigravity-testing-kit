# Tài liệu Yêu cầu (Requirements Specification)
## Module: Sales — Perfex CRM
**Hệ thống:** https://crm.anhtester.com
**Phiên bản:** 1.0
**Ngày khảo sát:** 2026-07-29
**Người khảo sát:** Antigravity AI Agent
**Phương pháp:** Inspect DOM thực tế trên browser (headed mode, 1920x1080)

---

## 1. Tổng quan (Overview)

Module **Sales** trong Perfex CRM là nhóm chức năng quản lý toàn bộ vòng đời doanh thu của doanh nghiệp, từ giai đoạn đề xuất báo giá (Proposals), ước tính chi phí (Estimates), xuất hóa đơn (Invoices), ghi nhận thanh toán (Payments), xử lý hoàn tiền (Credit Notes), đến quản lý danh mục sản phẩm/dịch vụ (Items).

Module này bao gồm **6 sub-module chính:**

| STT | Sub-module | URL | Mô tả |
|-----|-----------|-----|-------|
| 1 | Proposals | `/admin/proposals` | Quản lý đề xuất báo giá gửi đến khách hàng/lead |
| 2 | Estimates | `/admin/estimates` | Quản lý bản ước tính giá trị hợp đồng |
| 3 | Invoices | `/admin/invoices` | Quản lý hóa đơn bán hàng |
| 4 | Payments | `/admin/payments` | Theo dõi và ghi nhận các khoản thanh toán |
| 5 | Credit Notes | `/admin/credit_notes` | Quản lý ghi chú tín dụng / hóa đơn điều chỉnh |
| 6 | Items | `/admin/invoice_items` | Quản lý danh mục sản phẩm và dịch vụ |

---

## 2. Yêu cầu Chức năng (Functional Requirements)

---

### 2.1. Proposals (Báo giá)

#### User Stories

**US-PROP-01:** Là một nhân viên kinh doanh, tôi muốn tạo mới một bản báo giá (Proposal) để gửi cho khách hàng hoặc lead, nhằm bắt đầu quá trình chốt hợp đồng.

**Tiêu chí chấp nhận (Acceptance Criteria):**
- Truy cập vào `Sales > Proposals`, nhấn nút "New Proposal"
- Hệ thống chuyển đến form tạo mới tại URL `/admin/proposals/proposal`
- Điền đầy đủ các trường bắt buộc: Subject, Related (Lead/Customer), Date, Currency, To, Email
- Nhấn "Save" → Hệ thống lưu bản nháp thành công và hiển thị thông báo xác nhận
- Nhấn "Save & Send" → Hệ thống lưu và gửi email báo giá đến địa chỉ email đã nhập

**US-PROP-02:** Là một nhân viên, tôi muốn xem danh sách tất cả các Proposals để theo dõi tiến độ và trạng thái từng đề xuất.

**Tiêu chí chấp nhận:**
- Trang danh sách hiển thị bảng với đầy đủ các cột: Proposal #, Subject, To, Total, Date, Open Till, Project, Tags, Date Created, Status
- Có thể lọc theo trạng thái (Status)
- Có chức năng tìm kiếm (Search)
- Có phân trang (Pagination)
- Có thể xuất dữ liệu (Export: PDF/CSV/Excel)

**US-PROP-03:** Là một nhân viên, tôi muốn chỉnh sửa hoặc xóa một Proposal đã tạo.

**Tiêu chí chấp nhận:**
- Click vào Proposal # hoặc icon Edit → Mở form chỉnh sửa với dữ liệu hiện có
- Có thể thay đổi bất kỳ trường nào và nhấn Save để cập nhật
- Có thể xóa Proposal thông qua icon Delete (có hộp thoại xác nhận)

---

### 2.2. Estimates (Ước tính)

#### User Stories

**US-EST-01:** Là một nhân viên kinh doanh, tôi muốn tạo bản ước tính (Estimate) để trình bày chi phí dịch vụ cho khách hàng trước khi xuất hóa đơn chính thức.

**Tiêu chí chấp nhận:**
- Truy cập `Sales > Estimates`, nhấn "Create New Estimate"
- Hệ thống chuyển đến form tại `/admin/estimates/estimate`
- Điền đầy đủ: Customer, Estimate Number (auto-generate), Estimate Date, Currency
- Thêm ít nhất một dòng sản phẩm/dịch vụ vào bảng Items
- Nhấn "Save" → Hệ thống lưu với trạng thái mặc định là "Draft"
- Số Estimate được tự động tăng theo định dạng EST-XXXXXX

**US-EST-02:** Là một nhân viên, tôi muốn chuyển Estimate thành Invoice để tiến hành thanh toán.

**Tiêu chí chấp nhận:**
- Từ chi tiết Estimate, có thể chọn action "Convert to Invoice"
- Hệ thống tự động tạo Invoice mới với dữ liệu từ Estimate

---

### 2.3. Invoices (Hóa đơn)

#### User Stories

**US-INV-01:** Là kế toán viên, tôi muốn tạo hóa đơn (Invoice) mới gửi cho khách hàng để yêu cầu thanh toán.

**Tiêu chí chấp nhận:**
- Truy cập `Sales > Invoices`, nhấn "Create New Invoice"
- Form tạo mới tại `/admin/invoices/invoice`
- Điền bắt buộc: Customer, Invoice Number (auto), Invoice Date, Currency
- Cấu hình Due Date (hạn thanh toán, mặc định +30 ngày)
- Chọn phương thức thanh toán được chấp nhận (Allowed payment modes)
- Thêm sản phẩm/dịch vụ vào Items
- Nhấn "Save" → Invoice được tạo với trạng thái mặc định
- Nhấn "Save as Draft" → Lưu bản nháp

**US-INV-02:** Là kế toán, tôi muốn thiết lập hóa đơn định kỳ (Recurring Invoice) để hệ thống tự động tạo hóa đơn theo chu kỳ.

**Tiêu chí chấp nhận:**
- Trong form Invoice, cấu hình trường "Recurring Invoice?" chọn chu kỳ: Weekly/Monthly/Bimonthly/Quarterly/6 Months/Annually
- Hệ thống tự động tạo Invoice mới theo chu kỳ đã chọn

**US-INV-03:** Là kế toán, tôi muốn xem tổng quan hóa đơn theo trạng thái để quản lý công nợ hiệu quả.

**Tiêu chí chấp nhận:**
- Trang danh sách có filter theo Status: All/Draft/Sent/Overdue/Partially Paid/Paid/Cancelled
- Có chức năng Batch Payments để ghi nhận thanh toán cho nhiều Invoice cùng lúc
- Xem danh sách Recurring Invoices riêng biệt

---

### 2.4. Payments (Thanh toán)

#### User Stories

**US-PAY-01:** Là kế toán, tôi muốn ghi nhận một khoản thanh toán cho một Invoice cụ thể.

**Tiêu chí chấp nhận:**
- Truy cập chi tiết Invoice → Click "Record Payment"
- Hoặc từ `Sales > Invoices` → Click "Batch Payments"
- Điền thông tin: Payment Mode, Payment Date, Amount, Transaction ID (tùy chọn)
- Lưu → Hệ thống ghi nhận Payment và cập nhật trạng thái Invoice

**US-PAY-02:** Là kế toán, tôi muốn xem lịch sử tất cả các khoản thanh toán.

**Tiêu chí chấp nhận:**
- Trang danh sách `/admin/payments` hiển thị: Payment #, Invoice #, Payment Mode, Transaction ID, Customer, Amount, Date
- Không có chức năng tạo Payment trực tiếp từ trang này (phải qua Invoice)

---

### 2.5. Credit Notes (Ghi chú tín dụng)

#### User Stories

**US-CN-01:** Là kế toán, tôi muốn tạo Credit Note để hoàn tiền hoặc điều chỉnh giá trị hóa đơn cho khách hàng.

**Tiêu chí chấp nhận:**
- Truy cập `Sales > Credit Notes`, nhấn "New Credit Note"
- Form tại `/admin/credit_notes/credit_note`
- Điền bắt buộc: Customer, Currency, Credit Note Date, Credit Note # (auto)
- Thêm sản phẩm/dịch vụ vào Items
- Nhấn "Save" hoặc "Save & Send"
- Số Credit Note tự động theo định dạng CN-XXXXXX

**US-CN-02:** Là kế toán, tôi muốn áp dụng Credit Note vào Invoice để cấn trừ công nợ.

**Tiêu chí chấp nhận:**
- Từ chi tiết Credit Note, có thể Apply to Invoice
- Trường "Remaining Amount" hiển thị số tiền Credit Note chưa cấn trừ

---

### 2.6. Items (Sản phẩm/Dịch vụ)

#### User Stories

**US-ITEM-01:** Là quản trị viên, tôi muốn thêm sản phẩm/dịch vụ mới vào danh mục để sử dụng khi tạo Proposals, Estimates, Invoices.

**Tiêu chí chấp nhận:**
- Truy cập `Sales > Items`, nhấn "New Item"
- Hiển thị Modal form trực tiếp trên trang danh sách
- Điền bắt buộc: Description (Tên sản phẩm), Rate - USD (Đơn giá)
- Nhấn "Save" → Modal đóng và Item mới xuất hiện trong danh sách

**US-ITEM-02:** Là quản trị viên, tôi muốn nhập hàng loạt sản phẩm từ file CSV.

**Tiêu chí chấp nhận:**
- Click "Import Items" → Tải lên file CSV đúng định dạng
- Hệ thống nhập dữ liệu và hiển thị kết quả

**US-ITEM-03:** Là quản trị viên, tôi muốn tổ chức sản phẩm theo nhóm (Groups).

**Tiêu chí chấp nhận:**
- Click "Groups" → Quản lý danh mục nhóm sản phẩm
- Gán Item vào Group khi tạo hoặc chỉnh sửa

---

## 3. Đặc tả Trường Dữ liệu (Field Specifications)

### 3.1. Form Tạo Proposal Mới

| Tên trường | Loại UI | Bắt buộc | Giá trị mặc định | Ràng buộc / Ghi chú |
|-----------|---------|----------|-----------------|---------------------|
| Subject | Text Input | Có | Trống | Tiêu đề của Proposal |
| Related | Dropdown | Có | Trống | Chọn: Lead hoặc Customer. Khi chọn, hiển thị trường "To" tương ứng |
| To | Dropdown/Text | Có | Trống | Phụ thuộc vào Related: chọn Lead cụ thể hoặc Customer cụ thể |
| Date | Date Picker | Có | Ngày hiện tại | Định dạng dd/mm/yyyy |
| Open Till | Date Picker | Không | Ngày hiện tại + 7 ngày | Hạn hiệu lực của Proposal |
| Currency | Dropdown | Có | USD | Danh sách các loại tiền tệ |
| Discount Type | Dropdown | Không | No discount | Các lựa chọn: No discount / Before tax / After tax |
| Tags | Tag Input | Không | Trống | Nhập nhiều tags, phân cách bằng Enter |
| Allow Comments | Toggle Switch | Không | Off | Bật/Tắt cho phép khách hàng bình luận |
| Status | Dropdown | Không | Draft | Draft / Sent / Open / Revised / Declined / Accepted |
| Assigned | Dropdown | Không | Admin (current user) | Nhân viên phụ trách |
| Email | Email Input | Có | Lấy từ thông tin Customer/Lead | Định dạng email hợp lệ |
| Phone | Text Input | Không | Lấy từ thông tin Customer/Lead | |
| Address | Textarea | Không | Lấy từ thông tin Customer/Lead | |
| City | Text Input | Không | Lấy từ thông tin Customer/Lead | |
| State | Text Input | Không | Lấy từ thông tin Customer/Lead | |
| Country | Dropdown | Không | Lấy từ thông tin Customer/Lead | |
| Zip Code | Text Input | Không | Lấy từ thông tin Customer/Lead | |
| Item Description | Text Input | Có (khi thêm item) | Trống | Tên sản phẩm/dịch vụ trong Proposal |
| Item Long Description | Textarea | Không | Trống | Mô tả chi tiết sản phẩm |
| Item Quantity | Number Input | Có (khi thêm item) | 1 | Số lượng, phải > 0 |
| Item Rate | Number Input | Có (khi thêm item) | 0 | Đơn giá |
| Item Tax | Dropdown | Không | No Tax | Chọn % thuế áp dụng |
| Show quantity as | Radio Group | Không | Qty | Qty / Hours / Qty & Hours |

### 3.2. Form Tạo Estimate Mới

| Tên trường | Loại UI | Bắt buộc | Giá trị mặc định | Ràng buộc / Ghi chú |
|-----------|---------|----------|-----------------|---------------------|
| Customer | Dropdown/Search | Có | Trống | Tìm kiếm khách hàng trong hệ thống |
| Estimate Number | Text Input | Có | EST-XXXXXX (auto) | Tự động tăng, định dạng EST-XXXXXX |
| Estimate Date | Date Picker | Có | Ngày hiện tại | |
| Expiry Date | Date Picker | Không | Ngày hiện tại + 7 ngày | Hạn hiệu lực của Estimate |
| Tags | Tag Input | Không | Trống | |
| Currency | Dropdown | Có | USD | |
| Status | Dropdown | Không | Draft | Draft / Sent / Declined / Accepted / Expired |
| Reference # | Text Input | Không | Trống | Mã tham chiếu nội bộ |
| Sale Agent | Dropdown | Không | Admin (current user) | Nhân viên kinh doanh phụ trách |
| Discount Type | Dropdown | Không | No discount | No discount / Before tax / After tax |
| Admin Note | Textarea | Không | Trống | Ghi chú nội bộ, không hiển thị cho khách hàng |
| Billing Address | Địa chỉ (Group) | Không | Lấy từ Customer | Bao gồm: Street, City, State, Zip, Country |
| Shipping Address | Địa chỉ (Group) | Không | Lấy từ Customer | Bao gồm: Street, City, State, Zip, Country |
| Items (Product rows) | Dynamic Table | Có | 1 dòng trống | Tương tự Proposals |

### 3.3. Form Tạo Invoice Mới

| Tên trường | Loại UI | Bắt buộc | Giá trị mặc định | Ràng buộc / Ghi chú |
|-----------|---------|----------|-----------------|---------------------|
| Customer | Dropdown/Search | Có | Trống | |
| Invoice Number | Text Input | Có | INV-XXXXXX (auto) | Tự động tăng theo định dạng INV-XXXXXX |
| Invoice Date | Date Picker | Có | Ngày hiện tại | |
| Due Date | Date Picker | Không | Ngày hiện tại + 30 ngày | Hạn thanh toán |
| Prevent sending overdue reminders | Checkbox | Không | Unchecked | Ngăn gửi email nhắc nhở quá hạn |
| Tags | Tag Input | Không | Trống | |
| Allowed payment modes | Multi-select | Không | Bank | Các phương thức: Bank, PayPal, Stripe, etc. |
| Currency | Dropdown | Có | USD | |
| Sale Agent | Dropdown | Không | Admin | Nhân viên kinh doanh |
| Recurring Invoice? | Dropdown | Không | No | No / Weekly / Monthly / Bimonthly / Quarterly / 6 Months / Annually |
| Discount Type | Dropdown | Không | No discount | No discount / Before tax / After tax |
| Admin Note | Textarea | Không | Trống | |
| Billing Address | Địa chỉ (Group) | Không | Lấy từ Customer | |
| Shipping Address | Địa chỉ (Group) | Không | Lấy từ Customer | |
| Items | Dynamic Table | Có | 1 dòng trống | |
| Bill Tasks | Tab/Section | Không | N/A | Lấy giờ làm việc từ Tasks để tính tiền |

### 3.4. Form Tạo Credit Note Mới

| Tên trường | Loại UI | Bắt buộc | Giá trị mặc định | Ràng buộc / Ghi chú |
|-----------|---------|----------|-----------------|---------------------|
| Customer | Dropdown/Search | Có | Trống | |
| Currency | Dropdown | Có | USD | |
| Credit Note Date | Date Picker | Có | Ngày hiện tại | |
| Credit Note # | Text Input | Có | CN-XXXXXX (auto) | Tự động tăng theo định dạng CN-XXXXXX |
| Reference # | Text Input | Không | Trống | |
| Discount Type | Dropdown | Không | No discount | |
| Admin Note | Textarea | Không | Trống | |
| Items | Dynamic Table | Có | 1 dòng trống | |

### 3.5. Form Tạo Item Mới (Modal)

| Tên trường | Loại UI | Bắt buộc | Giá trị mặc định | Ràng buộc / Ghi chú |
|-----------|---------|----------|-----------------|---------------------|
| Description | Text Input | Có | Trống | Tên sản phẩm hoặc dịch vụ |
| Long Description | Textarea | Không | Trống | Mô tả chi tiết |
| Rate (USD) | Number Input | Có | Trống | Đơn giá theo USD |
| Rate (EUR) | Number Input | Không | Trống | Đơn giá theo EUR (nếu có cấu hình đa tiền tệ) |
| Tax 1 | Dropdown | Không | No Tax | Thuế suất áp dụng đầu tiên |
| Tax 2 | Dropdown | Không | No Tax | Thuế suất áp dụng thứ hai |
| Unit | Text Input | Không | Trống | Đơn vị tính (VD: Cái, Giờ, m²...) |
| Item Group | Dropdown | Không | Trống | Nhóm sản phẩm |

---

## 4. Luồng xử lý và Quy tắc Nghiệp vụ (Business Rules & Validations)

### 4.1. Quy tắc chung cho tất cả sub-module

| STT | Quy tắc | Mô tả |
|-----|---------|-------|
| BR-01 | Trường bắt buộc | Các trường bắt buộc không được để trống. Hệ thống hiển thị thông báo lỗi validation ngay tại trường khi Submit |
| BR-02 | Số tài liệu tự động | Số Proposal, Estimate, Invoice, Credit Note được hệ thống tự động tăng dần. Người dùng CÓ THỂ chỉnh sửa thủ công nhưng không được trùng |
| BR-03 | Trạng thái mặc định | Tất cả tài liệu mới được tạo với trạng thái "Draft" |
| BR-04 | Truy cập phân quyền | Chỉ Admin và người được phân quyền mới thấy toàn bộ tài liệu của tất cả nhân viên |

### 4.2. Quy tắc riêng cho Proposals

| STT | Quy tắc | Mô tả |
|-----|---------|-------|
| BR-PROP-01 | Ràng buộc Related - To | Khi chọn "Related" là Lead → trường "To" tự động populate danh sách Leads. Khi chọn Customer → "To" populate danh sách Customers |
| BR-PROP-02 | Open Till | Nếu ngày hiện tại vượt quá "Open Till" mà Proposal chưa được Accept/Decline → trạng thái tự động chuyển sang "Expired" (cần xác nhận) |
| BR-PROP-03 | Allow Comments | Khi bật Allow Comments, khách hàng có thể bình luận trực tiếp trên trang xem Proposal công khai |
| BR-PROP-04 | Save & Send | Khi nhấn "Save & Send" → Hệ thống lưu Proposal và gửi email đến địa chỉ trong trường Email |

### 4.3. Quy tắc riêng cho Invoices

| STT | Quy tắc | Mô tả |
|-----|---------|-------|
| BR-INV-01 | Due Date | Nếu Invoice chưa được thanh toán sau ngày Due Date → trạng thái tự động chuyển sang "Overdue" |
| BR-INV-02 | Recurring Invoice | Khi thiết lập Recurring, hệ thống tự động tạo bản sao Invoice mới theo chu kỳ đã cấu hình |
| BR-INV-03 | Prevent overdue reminders | Khi Checkbox được tích → Hệ thống KHÔNG gửi email nhắc nhở tự động khi Invoice quá hạn |
| BR-INV-04 | Bill Tasks | Cho phép kéo dữ liệu giờ làm việc từ Task Management vào Invoice để tính phí dịch vụ theo giờ |
| BR-INV-05 | Batch Payments | Cho phép ghi nhận thanh toán cho nhiều Invoice cùng lúc qua màn hình Batch Payments |

### 4.4. Quy tắc riêng cho Payments

| STT | Quy tắc | Mô tả |
|-----|---------|-------|
| BR-PAY-01 | Không tạo Payment trực tiếp | Không có nút "Create Payment" trên trang `/admin/payments`. Payment chỉ được tạo thông qua chi tiết Invoice hoặc Batch Payments |
| BR-PAY-02 | Cập nhật trạng thái Invoice | Khi ghi nhận Payment đủ tổng giá trị Invoice → Invoice tự động chuyển trạng thái sang "Paid" |
| BR-PAY-03 | Partial Payment | Khi ghi nhận Payment nhỏ hơn tổng Invoice → Invoice chuyển sang "Partially Paid" |

### 4.5. Quy tắc riêng cho Credit Notes

| STT | Quy tắc | Mô tả |
|-----|---------|-------|
| BR-CN-01 | Remaining Amount | Cột "Remaining Amount" hiển thị số tiền Credit Note chưa được cấn trừ vào Invoice |
| BR-CN-02 | Apply to Invoice | Credit Note có thể được áp dụng (apply) vào một hoặc nhiều Invoice để giảm số tiền cần thanh toán |

### 4.6. Quy tắc riêng cho Items

| STT | Quy tắc | Mô tả |
|-----|---------|-------|
| BR-ITEM-01 | Import CSV | Hỗ trợ nhập hàng loạt Items từ file CSV với template định sẵn |
| BR-ITEM-02 | Multi-currency Rate | Mỗi Item có thể cấu hình đơn giá theo nhiều loại tiền tệ khác nhau |
| BR-ITEM-03 | Item Groups | Items có thể được nhóm lại để dễ quản lý và tìm kiếm khi tạo tài liệu |

---

## 5. Luồng thao tác chính (Key User Flows)

### Flow 1: Tạo Proposal hoàn chỉnh và gửi cho khách hàng
```
Đăng nhập → Sales > Proposals → [New Proposal]
→ Nhập Subject, chọn Related (Customer/Lead), chọn To, nhập Date/Open Till
→ Chọn Currency, cấu hình Discount, thêm Tags
→ Thêm sản phẩm/dịch vụ (Items): nhập Description, Rate, Tax, Qty
→ Kiểm tra tổng tiền → [Save & Send]
→ Hệ thống lưu Proposal + gửi email → Trạng thái: Sent
```

### Flow 2: Tạo Invoice và ghi nhận thanh toán
```
Đăng nhập → Sales > Invoices → [Create New Invoice]
→ Chọn Customer, cấu hình Invoice Date, Due Date
→ Chọn Allowed payment modes, thêm Items
→ [Save] → Invoice được tạo (Status: Unpaid/Draft)
→ Click vào Invoice vừa tạo → [Record Payment]
→ Nhập Payment Mode, Date, Amount → [Save]
→ Invoice tự động cập nhật Status: Paid hoặc Partially Paid
```

### Flow 3: Tạo Estimate và convert sang Invoice
```
Sales > Estimates → [Create New Estimate]
→ Chọn Customer, nhập thông tin, thêm Items
→ [Save] → Estimate Status: Draft
→ Gửi cho khách hàng (Send) → Status: Sent
→ Khách hàng Accept → Status: Accepted
→ [Convert to Invoice] → Hệ thống tạo Invoice mới từ Estimate
```

### Flow 4: Phát hành Credit Note và áp dụng vào Invoice
```
Sales > Credit Notes → [New Credit Note]
→ Chọn Customer, nhập Credit Note Date, thêm Items
→ [Save] → Credit Note được tạo
→ [Apply to Invoice] → Chọn Invoice cần cấn trừ
→ Hệ thống giảm số tiền cần thanh toán trên Invoice đó
```

---

## 6. Yêu cầu Phi chức năng (Non-functional Requirements)

| STT | Yêu cầu | Mô tả quan sát được |
|-----|---------|---------------------|
| NFR-01 | Xuất dữ liệu | Tất cả các trang danh sách đều hỗ trợ Export dữ liệu (PDF/CSV/Excel/Print) |
| NFR-02 | Tìm kiếm & Lọc | Tất cả các trang danh sách có Search bar và bộ lọc theo Status |
| NFR-03 | Phân trang | Tất cả danh sách có Pagination để xử lý dữ liệu lớn |
| NFR-04 | Responsive | Giao diện hỗ trợ desktop (1920x1080). Cần kiểm tra thêm mobile/tablet |
| NFR-05 | Đa tiền tệ | Hệ thống hỗ trợ nhiều loại tiền tệ (USD, EUR, v.v.) cho tất cả tài liệu Sales |

---

## 7. Câu hỏi cần làm rõ với Product Owner (Open Questions)

| STT | Câu hỏi | Lý do cần làm rõ |
|-----|---------|-----------------|
| Q-01 | Validation message cụ thể hiển thị như thế nào khi bỏ trống trường bắt buộc? | Cần verify thực tế để viết test case assertion chính xác |
| Q-02 | Quy tắc phân quyền (RBAC) cho các role Staff/Admin khác nhau như thế nào? | Một số chức năng có thể bị ẩn với role Staff |
| Q-03 | Giá trị tối đa (maxlength) của các trường text là bao nhiêu? | Cần kiểm tra DOM attributes để xác định boundary test cases |
| Q-04 | Khi Open Till của Proposal hết hạn, hệ thống có tự động đổi status không? | Cần xác nhận business rule phía backend |
| Q-05 | Payment Mode có những tùy chọn nào ngoài "Bank"? | Cần kiểm tra cấu hình Payment Gateway của hệ thống demo |
| Q-06 | Import Items từ CSV: template file CSV cần có header gì? | Cần thực tế tải về template để xác định field mapping |

---

*Tài liệu được sinh ra từ khảo sát UI thực tế — 2026-07-29 — Antigravity AI Agent*
