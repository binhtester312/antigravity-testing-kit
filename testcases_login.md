# 📋 BỘ TEST CASE — MODULE LOGIN
**Hệ thống:** Perfex CRM — crm.anhtester.com  
**URL:** https://crm.anhtester.com/admin/authentication  
**Phương pháp:** Risk-Based Testing (RBT) | Kỹ thuật: EP + BVA + Decision Table + State Transition  
**Ngày xuất:** 17/07/2026

---

## ⚖️ ĐÁNH GIÁ RỦI RO (Risk Assessment)

| Sub-Module | Risk Level | Lý do | Phạm vi TC |
|------------|-----------|-------|------------|
| **Đăng nhập** | 🔴 HIGH | Core Auth — cổng vào toàn hệ thống | TC_001 → TC_015 (15 TCs) |
| **Forgot Password** | 🟡 MEDIUM | Tính năng phụ trợ quan trọng | TC_016 → TC_019 (4 TCs) |
| **Đăng xuất** | 🟢 LOW | Flow đơn giản, 1 bước | TC_020 → TC_021 (2 TCs) |

---

## 📌 GIẢ ĐỊNH (Assumptions)

| ID | Giả định |
|----|---------|
| [A-01] | Không có cơ chế Account Lockout sau nhiều lần đăng nhập sai (hệ thống demo) |
| [A-02] | Password không có giới hạn min/max length — test với 1 ký tự và 255 ký tự |
| [A-03] | Email không có maxlength — test với chuỗi rất dài |
| [A-04] | "Remember me" duy trì phiên trong **30 ngày** |
| [A-05] | Link reset password có hiệu lực trong **24 giờ** |
| [A-06] | Forgot Password với email không tồn tại → vẫn hiển thị thông báo gửi email chung (bảo mật) |
| [A-07] | Đã đăng nhập mà truy cập lại URL Login → redirect về Dashboard |
| [A-08] | Form Login hỗ trợ submit bằng phím **Enter** |

---

## 🧪 SHEET 1 — TEST CASES

| TC ID | Module | Risk Level | Test Scenario | Pre-Condition | Test Steps | Test Data | Expected Result | Priority |
|-------|--------|-----------|---------------|---------------|-----------|-----------|-----------------|----------|
| **CRM_LOGIN_TC_001** | Đăng nhập | 🔴 High | Đăng nhập thành công với email và password hợp lệ | Tài khoản tồn tại; Chưa đăng nhập; Đang ở trang `/admin/authentication` | 1. Mở URL: https://crm.anhtester.com/admin/authentication <br>2. Nhập Email vào trường Email Address <br>3. Nhập Password vào trường Password <br>4. Click nút `Login` | Email: `admin@example.com` <br>Password: `12345678` | - Chuyển hướng thành công đến `https://crm.anhtester.com/admin/` <br>- Trang Dashboard hiển thị tiêu đề "Dashboard" <br>- Không có thông báo lỗi | 🔴 High |
| **CRM_LOGIN_TC_002** | Đăng nhập | 🔴 High | Đăng nhập thành công + tick "Remember me" — phiên duy trì sau khi đóng browser | Tài khoản tồn tại; Chưa đăng nhập | 1. Mở trang Login <br>2. Nhập Email <br>3. Nhập Password <br>4. Tick vào checkbox "Remember me" <br>5. Click "Login" <br>6. Đóng trình duyệt hoàn toàn <br>7. Mở lại trình duyệt, truy cập `https://crm.anhtester.com/admin/` | Email: `admin@example.com` <br>Password: `12345678` <br>Remember me: ✅ checked | - Sau bước 5: Chuyển hướng đến Dashboard thành công <br>- Sau bước 7: Vẫn ở trạng thái đã đăng nhập <br>**[A-04]** Giả định: phiên duy trì 30 ngày | 🔴 High |
| **CRM_LOGIN_TC_003** | Đăng nhập | 🔴 High | Để trống cả Email và Password — hiển thị 2 thông báo lỗi đồng thời | Đang ở trang Login; Cả hai trường đang trống | 1. Mở trang Login <br>2. KHÔNG nhập gì vào Email <br>3. KHÔNG nhập gì vào Password <br>4. Click nút "Login" | Email: *(để trống)* <br>Password: *(để trống)* | - Không chuyển hướng, vẫn ở trang Login <br>- Hiển thị **đồng thời** 2 thông báo lỗi: <br>  • "The Email Address field is required." <br>  • "The Password field is required." | 🔴 High |
| **CRM_LOGIN_TC_004** | Đăng nhập | 🔴 High | Để trống Email, nhập Password bất kỳ — lỗi email bắt buộc | Đang ở trang Login | 1. Mở trang Login <br>2. KHÔNG nhập Email <br>3. Nhập Password bất kỳ <br>4. Click "Login" | Email: *(để trống)* <br>Password: `anyPassword99` | - Không chuyển hướng <br>- Hiển thị: "The Email Address field is required." <br>- Không hiện lỗi về Password | 🔴 High |
| **CRM_LOGIN_TC_005** | Đăng nhập | 🔴 High | Nhập Email hợp lệ, để trống Password — lỗi password bắt buộc | Đang ở trang Login | 1. Mở trang Login <br>2. Nhập Email hợp lệ <br>3. KHÔNG nhập Password <br>4. Click "Login" | Email: `admin@example.com` <br>Password: *(để trống)* | - Không chuyển hướng <br>- Hiển thị: "The Password field is required." <br>- Không hiện lỗi về Email | 🔴 High |
| **CRM_LOGIN_TC_006** | Đăng nhập | 🔴 High | Email sai định dạng — thiếu ký tự `@` (client-side HTML5 validation) | Đang ở trang Login | 1. Mở trang Login <br>2. Nhập Email không có ký tự `@` <br>3. Nhập Password bất kỳ <br>4. Click "Login" | Email: `invalidemail` <br>Password: `anyPassword99` | - Trình duyệt **chặn form, không submit** <br>- Hiển thị HTML5 tooltip: "Please include an '@' in the email address." <br>- Không có request server-side | 🔴 High |
| **CRM_LOGIN_TC_007** | Đăng nhập | 🟡 Medium | Email sai định dạng — có `@` nhưng thiếu domain (BVA — biên định dạng) | Đang ở trang Login | 1. Mở trang Login <br>2. Nhập Email có `@` nhưng không có domain <br>3. Nhập Password bất kỳ <br>4. Click "Login" | Email: `user@` <br>Password: `anyPassword99` | - Trình duyệt **chặn form, không submit** <br>- Hiển thị HTML5 tooltip yêu cầu nhập phần domain sau `@` | 🟡 Medium |
| **CRM_LOGIN_TC_008** | Đăng nhập | 🔴 High | Email đúng định dạng nhưng không tồn tại — lỗi server-side chung | Đang ở trang Login | 1. Mở trang Login <br>2. Nhập Email đúng định dạng nhưng sai <br>3. Nhập Password bất kỳ <br>4. Click "Login" | Email: `wrong_user_99@gmail.com` <br>Password: `AnyPass@123` | - Không chuyển hướng <br>- Hiển thị (server-side): "Invalid email or password" <br>- Thông báo chung, KHÔNG tiết lộ email có tồn tại hay không | 🔴 High |
| **CRM_LOGIN_TC_009** | Đăng nhập | 🔴 High | Email đúng + Password sai — lỗi server-side | Đang ở trang Login | 1. Mở trang Login <br>2. Nhập Email đúng của tài khoản có thật <br>3. Nhập Password **sai** <br>4. Click "Login" | Email: `admin@example.com` <br>Password: `WrongPass@9999` | - Không chuyển hướng <br>- Hiển thị: "Invalid email or password" <br>- Thông báo chung, không tiết lộ password sai | 🔴 High |
| **CRM_LOGIN_TC_010** | Đăng nhập | 🟡 Medium | Submit form bằng phím Enter thay vì click nút Login (Alternate Path) | Đang ở trang Login | 1. Mở trang Login <br>2. Nhập Email hợp lệ <br>3. Nhập Password đúng <br>4. Nhấn **phím Enter** (không click nút) | Email: `admin@example.com` <br>Password: `12345678` | - Form được submit <br>- Chuyển hướng đến Dashboard thành công <br>**[A-08]** Giả định: form hỗ trợ Enter | 🟡 Medium |
| **CRM_LOGIN_TC_011** | Đăng nhập | 🟡 Medium | Email có khoảng trắng đầu/cuối (leading/trailing spaces) — BVA edge case | Đang ở trang Login | 1. Mở trang Login <br>2. Nhập Email có 2 dấu cách ở đầu và 2 dấu cách ở cuối <br>3. Nhập Password đúng <br>4. Click "Login" | Email: `'  admin@example.com  '` (2 spaces đầu, 2 spaces cuối) <br>Password: `12345678` | **[A-03]** Nếu server tự trim: Đăng nhập thành công → redirect Dashboard <br>Nếu server KHÔNG trim: Hiển thị "Invalid email or password" | 🟡 Medium |
| **CRM_LOGIN_TC_012** | Đăng nhập | 🟡 Medium | Password 1 ký tự — BVA biên dưới | Đang ở trang Login | 1. Mở trang Login <br>2. Nhập Email đúng <br>3. Nhập Password chỉ 1 ký tự <br>4. Click "Login" | Email: `admin@example.com` <br>Password: `A` (1 ký tự) | - Không hiện lỗi client-side về độ dài <br>- Form submit lên server <br>- Kết quả: "Invalid email or password" <br>**[A-02]** Giả định: không có min/max length | 🟡 Medium |
| **CRM_LOGIN_TC_013** | Đăng nhập | 🟢 Low | Password 255 ký tự — BVA biên trên | Đang ở trang Login | 1. Mở trang Login <br>2. Nhập Email đúng <br>3. Nhập Password dài 255 ký tự <br>4. Click "Login" | Email: `admin@example.com` <br>Password: `Aa1!` lặp lại đủ 255 ký tự | - Form không crash, không timeout <br>- Không có lỗi client-side <br>- Server trả về "Invalid email or password" <br>**[A-02]** | 🟢 Low |
| **CRM_LOGIN_TC_014** | Đăng nhập | 🟡 Medium | Đã đăng nhập, truy cập lại URL Login — kiểm tra State Transition redirect | Đã đăng nhập thành công; Đang ở Dashboard | 1. Đang ở Dashboard (đã đăng nhập) <br>2. Nhập trực tiếp URL `https://crm.anhtester.com/admin/authentication` vào thanh địa chỉ <br>3. Nhấn Enter | *(Không cần nhập — đã có session)* | - Hệ thống redirect về Dashboard `/admin/` <br>- Không hiển thị lại form Login <br>**[A-07]** | 🟡 Medium |
| **CRM_LOGIN_TC_015** | Đăng nhập | 🔴 High | Kiểm tra CSRF token — request thiếu token bị server từ chối (NFR-01) | Đang ở trang Login | 1. Mở trang Login, mở DevTools (F12) → tab Network <br>2. Xác nhận form có trường hidden `csrf_token_name` <br>3. Dùng Postman gửi POST đến `/admin/authentication` KHÔNG kèm csrf_token | `csrf_token_name`: (bị bỏ qua) <br>Email: `admin@example.com` <br>Password: `12345678` | - Server **từ chối request** (HTTP 403 hoặc lỗi tương đương) <br>- Không đăng nhập được <br>(NFR-01) | 🔴 High |
| **CRM_LOGIN_TC_016** | Forgot Password | 🟡 Medium | Click "Forgot Password?" — chuyển đến đúng URL | Đang ở trang Login | 1. Mở trang Login <br>2. Click vào link **"Forgot Password?"** | *(Không cần nhập data)* | - Trình duyệt chuyển hướng đến: <br>`https://crm.anhtester.com/admin/authentication/forgot_password` <br>- Trang hiển thị form với trường Email và nút "Confirm" | 🟡 Medium |
| **CRM_LOGIN_TC_017** | Forgot Password | 🔴 High | Nhập email đã đăng ký + Confirm — hệ thống gửi email khôi phục thành công | Đang ở trang Forgot Password; Email `admin@example.com` tồn tại | 1. Truy cập `/admin/authentication/forgot_password` <br>2. Nhập Email đã đăng ký <br>3. Click "Confirm" | Email: `admin@example.com` | - Hệ thống gửi email hướng dẫn đặt lại mật khẩu <br>- Hiển thị thông báo xác nhận trên trang <br>- Kiểm tra hộp thư nhận được email hướng dẫn | 🔴 High |
| **CRM_LOGIN_TC_018** | Forgot Password | 🔴 High | Để trống Email + Click Confirm — HTML5 required validation | Đang ở trang Forgot Password | 1. Truy cập `/admin/authentication/forgot_password` <br>2. KHÔNG nhập gì vào Email <br>3. Click "Confirm" | Email: *(để trống)* | - Trình duyệt **chặn form** (HTML5 client-side required) <br>- Hiển thị tooltip: "Please fill out this field." <br>- Không submit lên server | 🔴 High |
| **CRM_LOGIN_TC_019** | Forgot Password | 🔴 High | Nhập email không tồn tại + Confirm — kiểm tra bảo mật thông tin (EP) | Đang ở trang Forgot Password | 1. Truy cập `/admin/authentication/forgot_password` <br>2. Nhập Email đúng định dạng nhưng KHÔNG tồn tại <br>3. Click "Confirm" | Email: `nonexistent_user_xyz@gmail.com` | **[A-06]** Hệ thống KHÔNG hiển thị "Email không tồn tại" <br>- Hiển thị thông báo chung: "Nếu email tồn tại, chúng tôi đã gửi hướng dẫn" <br>- Không tiết lộ thông tin tài khoản | 🔴 High |
| **CRM_LOGIN_TC_020** | Đăng xuất | 🟢 Low | Đăng xuất thành công — chuyển về trang Login (State Transition) | Đã đăng nhập; Đang ở Dashboard | 1. Từ Dashboard, click vào **avatar/profile dropdown** (góc trên phải) <br>2. Click "Logout" trong dropdown | *(Không cần nhập data)* | - Phiên đăng nhập bị hủy <br>- Chuyển hướng về `https://crm.anhtester.com/admin/authentication` <br>- Form Login hiển thị lại | 🔴 High |
| **CRM_LOGIN_TC_021** | Đăng xuất | 🟢 Low | Sau logout, nhấn Back browser — không truy cập được Dashboard (State Transition) | Đã logout xong (TC_020); Đang ở trang Login | 1. Sau khi logout thành công <br>2. Nhấn **nút Back** của trình duyệt | *(Không cần nhập data)* | - Hệ thống **KHÔNG cho phép** quay lại Dashboard <br>- Redirect về trang Login hoặc hiển thị yêu cầu đăng nhập lại <br>- Session cookie đã bị xóa/vô hiệu | 🔴 High |

---

## 📊 SHEET 2 — SUMMARY (TỔNG KẾT)

### TỔNG QUAN

| Tiêu chí | Số lượng |
|----------|---------:|
| **Tổng số Test Case** | **21 TCs** |
| 🔴 High Priority | 13 TCs |
| 🟡 Medium Priority | 6 TCs |
| 🟢 Low Priority | 2 TCs |

### PHÂN BỔ THEO MODULE

| Module | Số TCs | Phạm vi |
|--------|-------:|---------|
| Đăng nhập (HIGH Risk) | 15 TCs | TC_001 → TC_015 |
| Forgot Password (MEDIUM Risk) | 4 TCs | TC_016 → TC_019 |
| Đăng xuất (LOW Risk) | 2 TCs | TC_020 → TC_021 |

### PHÂN BỔ THEO LUỒNG

| Loại luồng | Số TCs | Danh sách |
|-----------|-------:|-----------|
| Happy Path | 4 TCs | TC_001, TC_002, TC_010, TC_017 |
| Exception Path | 13 TCs | TC_003~009, TC_011~013, TC_015, TC_018, TC_019 |
| Alternate Path | 4 TCs | TC_010, TC_013, TC_014, TC_021 |

### KỸ THUẬT THIẾT KẾ

| Kỹ thuật | Danh sách TC |
|----------|-------------|
| EP (Equivalence Partitioning) | TC_001 → TC_009 |
| BVA (Boundary Value Analysis) | TC_007, TC_011, TC_012, TC_013 |
| Decision Table | TC_003, TC_004, TC_005 |
| State Transition | TC_014, TC_020, TC_021 |

### GIẢ ĐỊNH CẦN XÁC NHẬN

| ID | Nội dung | Q-Ref | Ưu tiên |
|----|----------|-------|---------|
| [A-01] Account Lockout | Chưa xác định | Q-01 | 🔴 Cao |
| [A-04] Remember me duration | Giả định 30 ngày | Q-03 | 🟡 Trung bình |
| [A-06] Forgot PW email không tồn tại | Thông báo chung | Q-07 | 🔴 Cao |
| [A-07] Đã login → truy cập Login URL | Redirect Dashboard | Q-08 | 🟡 Trung bình |

---

> ⚠️ **Pending Clarifications:** Các giả định [A-01] → [A-08] cần PO/BA xác nhận để cập nhật Expected Result chính xác.  
> Đặc biệt: Q-01 (Account Lockout/CAPTCHA), Q-06/Q-07 (thông báo Forgot Password), Q-04 (thời gian Remember me).

---
*Synced from `testcases_login.xlsx` — Generated by Antigravity | 2026-07-17*
