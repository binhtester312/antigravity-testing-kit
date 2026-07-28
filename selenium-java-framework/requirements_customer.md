# 📜 TÀI LIỆU YÊU CẦU (REQUIREMENTS) — MODULE CUSTOMERS

**Hệ thống:** Perfex CRM — crm.anhtester.com  
**Phiên bản:** 1.0.0  
**Tài liệu tham chiếu:** User Stories & Feature Specifications — Customers Management  

---

## 🎯 1. TỔNG QUAN VỀ MODULE CUSTOMERS

Module Customers (Khách hàng) đóng vai trò trung tâm trong hệ thống Perfex CRM. Module này quản lý toàn bộ thông tin đối tác, doanh nghiệp, các liên hệ (contacts) đại diện, cùng các lịch sử giao dịch liên quan (Hóa đơn, Dự án, Hợp đồng).

* **URL chính:** `https://crm.anhtester.com/admin/clients`
* **Cổng tạo mới:** `https://crm.anhtester.com/admin/clients/client`
* **Quyền hạn truy cập:** Chỉ tài khoản thuộc vai trò Admin/Staff được cấp quyền mới có thể xem, thêm, sửa, xóa dữ liệu khách hàng.

---

## 📌 2. YÊU CẦU CHỨC NĂNG (FUNCTIONAL REQUIREMENTS)

### FR-01: Danh sách Khách hàng (Customer Listing Table)
1. **Hiển thị danh sách:** Bảng Data Table hiển thị danh sách khách hàng gồm các cột: `#`, `Company`, `Primary Contact`, `Primary Email`, `Phone`, `Active`, `Groups`, `Date Created`.
2. **Thống kê tổng quan (Customer Summary):** Hiển thị các khối chỉ số phía trên bảng gồm: *Total Customers*, *Active Customers*, *Inactive Customers*, *Active Contacts*, *Inactive Contacts*.
3. **Phân trang (Pagination):** Cho phép chọn số bản ghi trên 1 trang: 10, 25, 50, 100, All.
4. **Tìm kiếm thời gian thực (Live Search):** Ô tìm kiếm lọc dữ liệu ngay khi người dùng gõ từ khóa (tìm theo Tên công ty, Contact, Email, Phone).
5. **Xuất dữ liệu (Export):** Cho phép xuất danh sách khách hàng hiện tại ra các định dạng file: CSV, Excel, PDF, Print.

### FR-02: Thêm mới Khách hàng (Add New Customer)
1. **Giao diện Form:** Form chia làm 2 Tab chính:
   * **Tab 1: Customer Details:** Các trường gồm *Company* (bắt buộc), *VAT Number*, *Phone*, *Website*, *Groups* (Multi-select), *Currency*, *Default Language*, *Address*, *City*, *State*, *Zip Code*, *Country*.
   * **Tab 2: Billing & Shipping:** Thông tin địa chỉ thanh toán và giao hàng.
2. **Ràng buộc dữ liệu (Validation Rules):**
   * `Company`: Trường bắt buộc (Required). Nếu để trống và bấm Save, hệ thống phải chặn submit và hiển thị lỗi validation.
   * `Phone`: Chỉ nhận định dạng số hoặc chuỗi số hợp lệ.
   * `Website`: Phải đúng định dạng URL (`http://` hoặc `https://`).
3. **Thao tác lưu:** 
   * Bấm nút `Save`: Lưu thông tin khách hàng và tự động chuyển về trang Profile chi tiết của khách hàng vừa tạo.

### FR-03: Chỉnh sửa & Đổi trạng thái (Edit Customer & Status)
1. **Chỉnh sửa thông tin:** Cho phép cập nhật tất cả các trường thông tin của khách hàng hiện có.
2. **Đổi trạng thái Active/Inactive:** 
   * Mặc định khách hàng mới tạo ở trạng thái `Active`.
   * Cho phép Admin đổi sang `Inactive`. Khi chuyển sang Inactive, số lượng thống kê trên khối Summary phải tự động cập nhật.

### FR-04: Xóa Khách hàng (Delete Customer)
1. **Xóa đơn lẻ:** Rê chuột vào dòng khách hàng trên bảng, hiển thị tùy chọn `Delete`.
2. **Xác nhận xóa:** Khi bấm Delete, pop-up Modal Confirm phải xuất hiện với câu hỏi xác nhận: *"Are you sure you want to delete this customer?"*.
   * Nếu chọn `OK`: Xóa bản ghi khỏi cơ sở dữ liệu.
   * Nếu chọn `Cancel`: Hủy thao tác, giữ nguyên dữ liệu.
3. **Xóa hàng loạt (Bulk Delete):** Tích chọn checkbox ở đầu nhiều dòng khách hàng → Click `Bulk Actions` → Chọn `Delete` để xóa đồng thời các bản ghi đã chọn.

---

## 🔒 3. YÊU CẦU PHI CHỨC NĂNG (NON-FUNCTIONAL REQUIREMENTS)

* **NFR-01 (Data Integrity):** Xóa khách hàng phải đảm bảo xử lý toàn vẹn dữ liệu liên quan (Cascade Delete hoặc Unlink Contacts/Invoices).
* **NFR-02 (Performance):** Thời gian tải danh sách khách hàng (< 1000 bản ghi) phải đạt dưới 2 giây.
* **NFR-03 (Security):** Mọi request tạo, sửa, xóa khách hàng đều phải có bảo vệ chống CSRF Token.
