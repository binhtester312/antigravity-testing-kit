# TEST CASES — CRM CONTRACTS MODULE

| Thông tin | Chi tiết |
|-----------|----------|
| **Dự án** | CRM (Perfex CRM - Anh Tester Demo) |
| **Module** | Contracts (Hợp đồng) |
| **URL** | https://crm.anhtester.com/admin/contracts |
| **Phương pháp** | AI-RBT (Risk-Based Testing) — FULL 6 bước |
| **Ngày tạo** | 2026-07-28 |
| **Tổng số TC** | 21 |

---

## TỔNG HỢP RISK LEVEL

| Module / Sub-module | Risk Level | Số TC |
|---------------------|-----------|-------|
| MOD-04: Cross-Module Integration & Finance | 🔴 CRITICAL / High | 6 |
| MOD-02: Create & Edit Form Validation | 🔴 High | 6 |
| MOD-03: Detail View & Contract Lifecycle | 🔴 High | 5 |
| MOD-01: Contracts List & Multi-filtering | 🟡 Medium | 4 |

---

## TRACEABILITY MATRIX

| Mã Yêu Cầu (Requirement ID) | Test Cases (TC ID) |
|:----------------------------|:-------------------|
| **FR-CON-001** (Xem danh sách hợp đồng) | CRM_CON_TC_001 |
| **FR-CON-002** (Tìm kiếm và lọc hợp đồng) | CRM_CON_TC_002, CRM_CON_TC_003 |
| **FR-CON-003** & **016** (Xóa mềm / Trash Bin) | CRM_CON_TC_004 |
| **FR-CON-004** (Tạo mới hợp đồng) | CRM_CON_TC_005, CRM_CON_TC_006, CRM_CON_TC_007, CRM_CON_TC_008, CRM_CON_TC_009, CRM_CON_TC_010 |
| **FR-CON-006** & **014** (Xem chi tiết & Ký điện tử) | CRM_CON_TC_011 |
| **FR-CON-007** (Quản lý tệp đính kèm - Attachments) | CRM_CON_TC_012, CRM_CON_TC_013 |
| **FR-CON-009** (Gia hạn hợp đồng - Renewal History) | CRM_CON_TC_014 |
| **FR-CON-013** & **015** (Xuất file PDF & In) | CRM_CON_TC_015 |
| **FINANCE-001** (Tính toán Công nợ Khách hàng) | CRM_CON_TC_016, CRM_CON_TC_017 |
| **INTEGRATION-001** (Đồng bộ API Kế toán & Retry Queue) | CRM_CON_TC_018, CRM_CON_TC_019 |
| **SECURITY-001** (Phân quyền RBAC & Khóa hợp đồng Signed) | CRM_CON_TC_020, CRM_CON_TC_021 |

---

## BẢNG TEST CASES CHI TIẾT

### SUB-01: Contracts List & Multi-filtering (🟡 Medium Risk)

| TC ID | Module | Risk Level | Test Title | Pre-Condition | Test Steps | Expected Result | Priority | Test Data |
|-------|--------|------------|------------|---------------|------------|-----------------|----------|-----------|
| CRM_CON_TC_001 | MOD-1: List View | 🟡 Medium | Verify hiển thị danh sách Hợp đồng và thanh phân trang | Đã đăng nhập admin@example.com, CSDL có 15+ hợp đồng | 1. Truy cập `/admin/contracts`<br>2. Quan sát bảng Datatable & thanh phân trang | 1. Bảng hiển thị đủ các cột: `#`, `Subject`, `Customer`, `Contract Value`, `Contract Type`, `Start Date`, `End Date`, `Project`, `Signature`<br>2. Phân trang hiển thị chính xác | High | URL: `/admin/contracts` |
| CRM_CON_TC_002 | MOD-1: Search | 🟡 Medium | Verify tìm kiếm Hợp đồng theo từ khóa Subject | Đã mở danh sách Hợp đồng, có HD "CRM 2026" | 1. Nhập "CRM 2026" vào ô Search<br>2. Nhập "INVALID_9999" vào ô Search | 1. Hiển thị đúng hợp đồng "CRM 2026"<br>2. Hiển thị "No matching records found" | Medium | Search 1: "CRM 2026"<br>Search 2: "INVALID_9999" |
| CRM_CON_TC_003 | MOD-1: Filters | 🟡 Medium | Verify lọc hợp đồng theo Customer và xem cờ Trash Bin | Khách hàng "Anh Tester Demo" có 2 HD active, 1 HD trong Trash | 1. Select Customer "Anh Tester Demo" ở dropdown Filter<br>2. Tick chọn cờ Trash Bin | 1. Lọc đúng 2 HD active của "Anh Tester Demo"<br>2. Hiển thị đúng 1 HD trong Trash Bin | Medium | Customer: "Anh Tester Demo" |
| CRM_CON_TC_004 | MOD-1: Trash | 🟡 Medium | Verify thao tác Xóa mềm hợp đồng (Move to Trash) | Hợp đồng "HD Thử Nghiệm Xóa" đang ở trạng thái Draft | 1. Hover vào HD "HD Thử Nghiệm Xóa", click biểu tượng Delete<br>2. Click OK trên Popup xác nhận | 1. Popup xác nhận xuất hiện<br>2. Thông báo "Contract deleted", HD chuyển sang danh mục Trash | Medium | Subject: "HD Thử Nghiệm Xóa" |

---

### SUB-02: Create & Edit Form Validation (🔴 High Risk)

| TC ID | Module | Risk Level | Test Title | Pre-Condition | Test Steps | Expected Result | Priority | Test Data |
|-------|--------|------------|------------|---------------|------------|-----------------|----------|-----------|
| CRM_CON_TC_005 | MOD-2: Create | 🔴 High | Verify tạo mới hợp đồng thành công với dữ liệu chuẩn (Happy Path) | Mở Form tạo mới tại `/admin/contracts/contract` | 1. Select Customer: "Công ty TNHH Anh Tester Demo"<br>2. Input Subject: "Hợp đồng Cung cấp Dịch vụ CRM 2026"<br>3. Input Value: 150000000<br>4. Select Type: "Service Level Agreement"<br>5. Select Start Date: "28/07/2026", End Date: "28/07/2027"<br>6. Click Save | 1. Lưu hợp đồng thành công vào CSDL<br>2. Hiển thị thông báo "Contract added successfully"<br>3. Chuyển hướng sang trang chi tiết với Status = Not Signed | High | Customer: "Anh Tester Demo"<br>Subject: "Hợp đồng CRM 2026"<br>Value: 150000000<br>Start Date: 28/07/2026<br>End Date: 28/07/2027 |
| CRM_CON_TC_006 | MOD-2: Validation | 🔴 High | Verify validation các trường bắt buộc Customer và Subject | Form tạo mới hợp đồng đang trống | 1. Mở Form tạo mới<br>2. Không nhập gì, nhấn nút Save | 1. Form bị chặn submit<br>2. Hiển thị lỗi inline màu đỏ dưới ô Customer & Subject: "This field is required." | High | N/A (Form rỗng) |
| CRM_CON_TC_007 | MOD-2: Date Logic | 🔴 High | Verify validation logic ngày tháng khi End Date < Start Date | Đang nhập Form tạo hợp đồng | 1. Chọn Start Date: "28/07/2026"<br>2. Chọn End Date: "27/07/2026"<br>3. Click Save | 1. Form bị chặn submit<br>2. Hiển thị thông báo lỗi: "End Date must be greater than or equal to Start Date" | High | Start Date: 28/07/2026<br>End Date: 27/07/2026 |
| CRM_CON_TC_008 | MOD-2: Numeric | 🔴 High | Verify validation trường Contract Value với số âm và ký tự chữ | Form tạo mới hợp đồng | 1. Nhập Value = -50000000 -> Save<br>2. Nhập Value = "abc@#$123" -> Save | 1. Với số âm: Báo lỗi "Contract Value must be greater than or equal to 0"<br>2. Với chữ: Ô input tự động lọc bỏ hoặc báo lỗi numeric | High | Case A: -50000000<br>Case B: "abc@#$123" |
| CRM_CON_TC_009 | MOD-2: BVA | 🟡 Medium | Field Validation trường Subject với Max Length 191 ký tự | Form tạo mới hợp đồng | 1. Paste chuỗi 191 ký tự 'A' vào Subject -> Save<br>2. Paste chuỗi 192 ký tự 'B' vào Subject -> Save | 1. Với 191 chars: Lưu thành công<br>2. Với 192 chars: Input bị trảm ký tự thứ 192 hoặc báo lỗi vượt max length | Medium | Case 191 chars: "A...A"<br>Case 192 chars: "B...B" |
| CRM_CON_TC_010 | MOD-2: Security | 🔴 High | Validation Bảo mật Field: XSS Injection vào Description | Form tạo mới hợp đồng | 1. Nhập đủ thông tin hợp lệ<br>2. Nhập `<script>alert('XSS')</script>` vào Description<br>3. Save và mở trang chi tiết xem | 1. Lưu hợp đồng an toàn<br>2. Trang chi tiết render dạng text thuần `&lt;script&gt;...`, KHÔNG popup alert script | High | Description: `<script>alert('XSS')</script>` |

---

### SUB-03: Detail View & Contract Lifecycle (🔴 High Risk)

| TC ID | Module | Risk Level | Test Title | Pre-Condition | Test Steps | Expected Result | Priority | Test Data |
|-------|--------|------------|------------|---------------|------------|-----------------|----------|-----------|
| CRM_CON_TC_011 | MOD-3: E-Signature | 🔴 High | Verify ký hợp đồng điện tử thành công (State: Not Signed -> Signed) | Hợp đồng "HD-2026-001" đang có Status = Not Signed | 1. Mở trang chi tiết hợp đồng HD-2026-001<br>2. Click nút "Sign Contract"<br>3. Nhập tên "Nguyễn Văn A", vẽ chữ ký<br>4. Click "Sign" | 1. Xác nhận ký thành công<br>2. Status hợp đồng đổi từ Not Signed -> Signed<br>3. Ghi nhận hình chữ ký, IP và Timestamp | High | Signer: "Nguyễn Văn A" |
| CRM_CON_TC_012 | MOD-3: Attachments | 🟡 Medium | Verify upload file đính kèm hợp lệ dạng PDF dung lượng 3.5MB | Mở tab Attachments trên trang chi tiết hợp đồng | 1. Chọn file "hop_dong_ky_so_2026.pdf" (3.5MB)<br>2. Click Upload | 1. Upload thành công 100%<br>2. File xuất hiện trong danh sách kèm kích thước 3.5MB và nút Tải về/Xem | High | File: `hop_dong_ky_so_2026.pdf` (3.5 MB) |
| CRM_CON_TC_013 | MOD-3: Security | 🔴 High | Verify chặn upload file .exe và File PDF vượt quá 10MB | Mở tab Attachments trên trang chi tiết hợp đồng | 1. Upload file "malicious.exe" (2MB)<br>2. Upload file "heavy_doc.pdf" (12MB) | 1. Với file .exe: Báo lỗi "File type not allowed."<br>2. Với file 12MB: Báo lỗi "File size exceeds maximum limit of 10MB." | High | File A: `malicious.exe` (2 MB)<br>File B: `heavy_doc.pdf` (12 MB) |
| CRM_CON_TC_014 | MOD-3: Renew | 🟡 Medium | Verify Gia hạn Hợp đồng (Renew Contract Workflow) | Hợp đồng HD-2026-001 có End Date 28/07/2027, Value 150M | 1. Click nút "Renew"<br>2. Nhập New End Date: "28/07/2028"<br>3. Nhập Renewal Value: 50000000<br>4. Click Submit | 1. Gia hạn thành công<br>2. End Date mới cập nhật thành 28/07/2028<br>3. Giá trị hợp đồng cập nhật thành 200,000,000 VND | High | New End Date: 28/07/2028<br>Renewal Value: 50000000 |
| CRM_CON_TC_015 | MOD-3: PDF/Print | 🟢 Low | Verify Xuất file PDF hợp đồng và kiểm tra bản in HTML | Hợp đồng đầy đủ thông tin và chữ ký | 1. Click nút "PDF" trên thanh công cụ<br>2. Mở file PDF vừa tải xuống<br>3. Click nút "Print" xem bản in | 1. File PDF tải về thành công, hiển thị chuẩn font tiếng Việt<br>2. Layout xem bản in khớp với template | Low | Action: Export PDF / Print |

---

### SUB-04: Cross-Module Integration & Finance (🔴 CRITICAL / High Risk)

| TC ID | Module | Risk Level | Test Title | Pre-Condition | Test Steps | Expected Result | Priority | Test Data |
|-------|--------|------------|------------|---------------|------------|-----------------|----------|-----------|
| CRM_CON_TC_016 | MOD-4: Debt Logic | 🔴 High | Verify Công nợ Khách hàng KHÔNG tăng khi Hợp đồng ở trạng thái Not Signed | Customer "Anh Tester Demo" có dư nợ = 0 VND | 1. Tạo HD mới 150,000,000 VND cho "Anh Tester Demo"<br>2. Giữ status = Not Signed<br>3. Mở xem thông tin Customer | 1. Tổng công nợ Khách hàng vẫn duy trì chính xác = 0 VND (chưa bị cộng dồn) | High | Customer: "Anh Tester Demo"<br>Contract Value: 150000000 |
| CRM_CON_TC_017 | MOD-4: Finance | 🔴 CRITICAL | Verify Tự động cộng Công nợ khi Hợp đồng chuyển sang trạng thái Signed | Customer "Anh Tester Demo" dư nợ = 0 VND, HD-150M đang Not Signed | 1. Mở Hợp đồng HD-150M<br>2. Ký hợp đồng / Đổi trạng thái sang Signed<br>3. Mở xem màn hình Khách hàng | 1. Hệ thống tự động kích hoạt tính toán<br>2. Dư nợ Khách hàng "Anh Tester Demo" tự động CỘNG THÊM đúng bằng 150,000,000 VND | CRITICAL | Status Change: Not Signed -> Signed |
| CRM_CON_TC_018 | MOD-4: Webhook | 🔴 High | Verify tự động gửi API Kế toán Realtime khi Hợp đồng Signed | API Kế toán hoạt động bình thường (200 OK) | 1. Đổi status hợp đồng HD-2026-001 sang Signed<br>2. Kiểm tra trang Sync Queue Log | 1. Request API tự động phát sang Kế toán<br>2. Sync Queue Log ghi nhận record ID HD-2026-001 với Status = SUCCESS (200 OK) | High | Contract ID: HD-2026-001 |
| CRM_CON_TC_019 | MOD-4: Retry Queue | 🔴 High | Verify xử lý lỗi API Kế toán Timeout và Re-sync qua Sync Queue | API Kế toán bị ngắt kết nối (500 Error / Timeout) | 1. Đổi status HD-FAIL-01 sang Signed<br>2. Kiểm tra Sync Queue Log (thấy Status FAILED)<br>3. Mở lại API Kế toán, bấm Re-sync tại bảng Log | 1. CRM lưu status Signed an toàn không bị crash<br>2. Sync Queue Log hiển thị FAILED<br>3. Nhấn Re-sync thành công, Status đổi sang SUCCESS | High | Contract ID: HD-FAIL-01 |
| CRM_CON_TC_020 | MOD-4: RBAC Lock | 🔴 High | Verify Sales Staff bị Khóa quyền Edit/Delete hợp đồng Signed | Đăng nhập tài khoản sales_staff_01@anhtester.com (Quyền Staff) | 1. Mở Hợp đồng HD-SIGNED-99 đang ở trạng thái Signed<br>2. Quan sát các nút bấm<br>3. Thử gõ trực tiếp URL Edit hợp đồng | 1. Nút Edit và Delete bị ẩn hoàn toàn (Read-only view)<br>2. Gõ URL Edit trực tiếp bị hệ thống chặn với thông báo "Access Denied" | High | User: `sales_staff_01@anhtester.com`<br>Password: 123456 |
| CRM_CON_TC_021 | MOD-4: RBAC Admin | 🟡 Medium | Verify Admin có toàn quyền Edit/Delete trên hợp đồng Signed | Đăng nhập tài khoản admin@example.com (Administrator) | 1. Mở Hợp đồng HD-SIGNED-99 đang Signed<br>2. Quan sát các nút bấm trên giao diện | 1. Nút Edit, Renew, Delete vẫn hiển thị đầy đủ và cho phép Admin thao tác | Medium | User: `admin@example.com`<br>Password: 123456 |
