# TÀI LIỆU YÊU CẦU CHỨC NĂNG (REQUIREMENTS SPECIFICATION)
## MODULE: PROJECTS (QUẢN LÝ DỰ ÁN) - PERFEX CRM

> **Hệ thống:** Perfex CRM  
> **URL Module:** `https://crm.anhtester.com/admin/projects`  
> **Ngày cập nhật:** 28/07/2026  
> **Phiên bản:** 2.0  
> **Người thực hiện:** AI Agent (Antigravity)  
> **Phương pháp:** Khảo sát UI/DOM trực tiếp trên trình duyệt thực tế (Viewport 1920×1080)

---

## 1. TỔNG QUAN (OVERVIEW)

### 1.1. Mục đích
Module **Projects (Quản lý dự án)** trong hệ thống Perfex CRM cho phép quản trị viên (Admin) và nhân viên có thẩm quyền khởi tạo, quản lý, theo dõi tiến độ, chi phí và phân quyền truy cập dự án cho khách hàng. Module giúp tối ưu hóa luồng công việc giữa doanh nghiệp và khách hàng, đồng thời tích hợp chặt chẽ với các module khác như Tasks, Contracts, Invoices, Expenses, Timesheets và Milestones.

### 1.2. Phạm vi tài liệu
Tài liệu này đặc tả chi tiết giao diện, quy tắc nghiệp vụ (Business Rules), ràng buộc dữ liệu (Validation Rules) và tiêu chí chấp nhận (Acceptance Criteria) cho module Projects dựa trên khảo sát thực tế giao diện hệ thống Perfex CRM tại URL `https://crm.anhtester.com/admin/projects`.

---

## 2. YÊU CẦU CHỨC NĂNG (FUNCTIONAL REQUIREMENTS)

### US-01: Quan sát & Quản lý Danh sách Dự án (Projects List & Summary)
- **Mô tả:** Là một Quản trị viên / Quản lý dự án, tôi muốn xem danh sách toàn bộ các dự án kèm thông tin tóm tắt để nắm bắt trạng thái tổng quan.
- **Tiêu chí chấp nhận (Acceptance Criteria):**
  - **Tóm tắt (Projects Summary Cards):** Hiển thị 5 thẻ đếm số lượng dự án theo các trạng thái:
    - *Not Started* (Chưa bắt đầu)
    - *In Progress* (Đang thực hiện)
    - *On Hold* (Tạm dừng)
    - *Cancelled* (Đã hủy)
    - *Finished* (Đã hoàn thành)
  - Khi click vào từng thẻ trạng thái, bảng dữ liệu bên dưới tự động lọc danh sách theo trạng thái tương ứng.
  - **Bảng dữ liệu (Data Grid):**
    - Các cột hiển thị: `#` (ID/Mã dự án), `Project Name` (Tên dự án), `Customer` (Tên khách hàng), `Tags` (Thẻ phân loại), `Start Date` (Ngày bắt đầu), `Deadline` (Hạn chót), `Members` (Danh sách thành viên), `Status` (Trạng thái).
    - Cho phép sắp xếp (Sort ascending/descending) theo từng cột.
    - Hỗ trợ đổi số lượng bản ghi hiển thị trên mỗi trang (`10`, `25`, `50`, `100`, `All`).
    - Ô tìm kiếm nhanh (`Search`): Lọc thời gian thực theo tên dự án, khách hàng hoặc tag.
    - Nút `Export`: Cho phép xuất danh sách dự án ra file (Excel, CSV, PDF, Print...).
    - Thao tác nhanh trên mỗi dòng (Hover action links): `View`, `Copy Project`, `Edit`, `Delete`.

---

### US-02: Thêm mới Dự án (Add New Project)
- **Mô tả:** Là một Quản trị viên, tôi muốn tạo mới một dự án với đầy đủ thông tin chi tiết và thiết lập quyền cho khách hàng.
- **Tiêu chí chấp nhận (Acceptance Criteria):**
  - Giao diện gồm 2 Tab chính: **Project** (Thông tin chung) và **Project Settings** (Cài đặt quyền hạn & hiển thị).
  - Phải nhập đầy đủ các trường bắt buộc (`Project Name`, `Customer`, `Billing Type`, `Start Date`).
  - Khi chọn `Billing Type = Fixed Rate`, hiển thị trường nhập `Total Rate`.
  - Khi chọn `Billing Type = Project Hours`, hiển thị trường nhập `Rate Per Hour`.
  - Khi bỏ tích `Calculate progress through tasks`, hiển thị thanh trượt chọn `% Tiến độ (Progress)` thủ công.
  - Hệ thống tự động báo lỗi trực tiếp bên dưới các trường bắt buộc khi người dùng bỏ trống và nhấn `Save`.
  - Nhấn `Save` thành công: Lưu dự án vào hệ thống, hiển thị thông báo thành công và chuyển về màn hình danh sách hoặc chi tiết dự án.

---

### US-03: Chỉnh sửa Dự án (Edit Project)
- **Mô tả:** Là một Quản trị viên, tôi muốn cập nhật thông tin hoặc cài đặt của một dự án đã tồn tại.
- **Tiêu chí chấp nhận (Acceptance Criteria):**
  - Cho phép thay đổi bất kỳ trường thông tin nào từ Tab Project và Project Settings.
  - Kiểm tra và giữ nguyên các ràng buộc validation như khi tạo mới.

---

### US-04: Sao chép Dự án (Copy Project)
- **Mô tả:** Là một Quản trị viên, tôi muốn sao chép cấu hình và dữ liệu của một dự án có sẵn sang dự án mới để tiết kiệm thời gian khởi tạo.
- **Tiêu chí chấp nhận (Acceptance Criteria):**
  - Hiển thị popup xác nhận sao chép kèm các tùy chọn dữ liệu cần sao chép (Tasks, Milestones, Members, Files...).
  - Tạo dự án mới chứa thông tin đã được sao chép.

---

### US-05: Xóa Dự án (Delete Project)
- **Mô tả:** Là một Quản trị viên, tôi muốn xóa một dự án không còn sử dụng.
- **Tiêu chí chấp nhận (Acceptance Criteria):**
  - Hiển thị hộp thoại cảnh báo xác nhận trước khi xóa.
  - Xóa dự án thành công và loại bỏ dự án khỏi danh sách.

---

## 3. ĐẶC TẢ TRƯỜNG DỮ LIỆU (FIELD SPECIFICATIONS)

### 3.1. Tab "Project" (Thông tin chi tiết Dự án)

| Tên trường (Label) | Loại UI (Type) | Bắt buộc (Mandatory) | Giá trị mặc định / Ràng buộc (Validation & Constraints) | Ghi chú (Notes) |
| :--- | :--- | :---: | :--- | :--- |
| **Project Name** | Textbox | **Có** | Không được để trống. Báo lỗi: *"This field is required."* | Tên nhận diện dự án |
| **Customer** | Combobox / Select Search | **Có** | Phải chọn 1 khách hàng từ danh sách. Báo lỗi khi trống. | Tìm kiếm & chọn Customer |
| **Calculate progress through tasks** | Checkbox | Không | Mặc định: `Checked` | Nếu tích: Tiến độ tự tính theo % số công việc hoàn thành |
| **Progress** | Range Slider | Không | Chỉ hiển thị khi `Calculate progress through tasks` = `Unchecked`. Dải giá trị: `0%` - `100%`. | Điều chỉnh % tiến độ thủ công |
| **Billing Type** | Dropdown Select | **Có** | Giá trị: `Fixed Rate`, `Project Hours`, `Task Hours`. Mặc định: `Task Hours`. | Kiểu tính phí dự án |
| **Total Rate** | Number (Spinbutton) | Không | Chỉ hiển thị khi `Billing Type` = `Fixed Rate`. Kiểu số >= 0. | Tổng chi phí cố định của dự án |
| **Rate Per Hour** | Number (Spinbutton) | Không | Chỉ hiển thị khi `Billing Type` = `Project Hours`. Kiểu số >= 0. | Chi phí theo từng giờ làm việc |
| **Status** | Dropdown Select | Không | Giá trị: `Not Started`, `In Progress`, `On Hold`, `Cancelled`, `Finished`. Mặc định: `In Progress`. | Trạng thái hiện tại của dự án |
| **Estimated Hours** | Number (Spinbutton) | Không | Kiểu số thực/nguyên >= 0. | Tổng số giờ ước tính cho dự án |
| **Members** | Multi-select Listbox | Không | Mặc định chọn người dùng đang đăng nhập (Admin Example). | Thành viên tham gia dự án |
| **Start Date** | Datepicker (Textbox) | **Có** | Định dạng `DD-MM-YYYY`. Mặc định: Ngày hiện tại. | Ngày bắt đầu dự án |
| **Deadline** | Datepicker (Textbox) | Không | Định dạng `DD-MM-YYYY`. Phải lớn hơn hoặc bằng `Start Date`. | Ngày kết thúc dự án |
| **Tags** | Tag Input | Không | Nhập chuỗi ký tự, nhấn Enter để thêm tag. | Phân loại dự án theo nhãn |
| **Description** | Rich Text Editor (TinyMCE) | Không | Hỗ trợ định dạng văn bản, chèn ảnh, bảng, link. | Mô tả nội dung chi tiết dự án |
| **Send project created email** | Checkbox | Không | Mặc định: `Unchecked`. | Gửi email thông báo cho thành viên/khách hàng khi lưu |

---

### 3.2. Tab "Project Settings" (Cài đặt Quyền hạn & Hiển thị)

| Tên trường (Label) | Loại UI (Type) | Bắt buộc (Mandatory) | Giá trị mặc định / Ràng buộc | Ghi chú (Notes) |
| :--- | :--- | :---: | :--- | :--- |
| **Send contacts notifications** | Dropdown Select | **Có** | Giá trị: `To all contacts with notifications for projects enabled`, `Specific contacts`, `Do not send notifications`. Mặc định: `To all contacts...` | Cấu hình gửi thông báo cho liên hệ |
| **Visible Tabs** | Multi-select Listbox | Không | Chọn các Tab hiển thị trong chi tiết dự án (Overview, Tasks, Timesheets, Milestones, Files, Discussions, Gantt, Tickets, Contracts, Proposals, Estimates, Invoices, Subscriptions, Expenses, Credit Notes, Notes, Activity). | Tab `Overview` cố định không được bỏ chọn |
| **Allow customer to view tasks** | Checkbox | Không | Mặc định: `Unchecked`. | Cho phép khách hàng xem danh sách công việc |
| **Allow customer to create tasks** | Checkbox | Không | Phụ thuộc: Chỉ enable khi `Allow customer to view tasks` được chọn. | Cho phép khách hàng tạo mới công việc |
| **Allow customer to edit tasks** | Checkbox | Không | Phụ thuộc: Chỉ enable khi `Allow customer to view tasks` được chọn. | Chỉ sửa task do chính contact tạo |
| **Allow customer to comment on project tasks** | Checkbox | Không | Phụ thuộc: Chỉ enable khi `Allow customer to view tasks` được chọn. | Cho phép bình luận trong task |
| **Allow customer to view task comments** | Checkbox | Không | Phụ thuộc: Chỉ enable khi `Allow customer to view tasks` được chọn. | Cho phép xem bình luận task |
| **Allow customer to view task attachments** | Checkbox | Không | Phụ thuộc: Chỉ enable khi `Allow customer to view tasks` được chọn. | Cho phép xem đính kèm task |
| **Allow customer to view task checklist items** | Checkbox | Không | Phụ thuộc: Chỉ enable khi `Allow customer to view tasks` được chọn. | Cho phép xem checklist task |
| **Allow customer to upload attachments on tasks** | Checkbox | Không | Phụ thuộc: Chỉ enable khi `Allow customer to view tasks` được chọn. | Cho phép tải lên đính kèm task |
| **Allow customer to view task total logged time** | Checkbox | Không | Phụ thuộc: Chỉ enable khi `Allow customer to view tasks` được chọn. | Cho phép xem tổng thời gian đã log |
| **Allow customer to view finance overview** | Checkbox | Không | Mặc định: `Checked`. | Cho phép xem tổng quan tài chính |
| **Allow customer to upload files** | Checkbox | Không | Mặc định: `Checked`. | Cho phép khách hàng tải file lên dự án |
| **Allow customer to open discussions** | Checkbox | Không | Mặc định: `Checked`. | Cho phép mở thảo luận mới |
| **Allow customer to view milestones** | Checkbox | Không | Mặc định: `Checked`. | Cho phép xem cột mốc dự án |
| **Allow customer to view Gantt** | Checkbox | Không | Mặc định: `Checked`. | Cho phép xem sơ đồ Gantt |
| **Allow customer to view timesheets** | Checkbox | Không | Mặc định: `Checked`. | Cho phép xem bảng chấm công |
| **Allow customer to view activity log** | Checkbox | Không | Mặc định: `Checked`. | Cho phép xem nhật ký hoạt động |
| **Allow customer to view team members** | Checkbox | Không | Mặc định: `Checked`. | Cho phép xem danh sách thành viên dự án |
| **Hide project tasks on main tasks table** | Checkbox | Không | Mặc định: `Unchecked`. | Ẩn các công việc dự án trên bảng Tasks chính của Admin |

---

## 4. LUỒNG XỬ LÝ VÀ QUY TẮC NGHIỆP VỤ (BUSINESS RULES & VALIDATIONS)

### 4.1. Thông báo lỗi Validation (Expected Error Messages)
1. Để trống trường `Project Name` và nhấn `Save`:
   - Thông báo bên dưới field: `"This field is required."`
2. Chưa chọn `Customer` và nhấn `Save`:
   - Thông báo bên dưới field: `"Select and begin typing"` / `"This field is required."`
3. Để trống trường `Start Date`:
   - Hệ thống tự động điền ngày hiện tại hoặc yêu cầu chọn ngày hợp lệ.
4. Chọn `Deadline` nhỏ hơn `Start Date`:
   - Hệ thống hiển thị cảnh báo ngày kết thúc phải sau hoặc bằng ngày bắt đầu.

### 4.2. Logic phụ thuộc giao diện (Dynamic UI Rules)
- **Billing Type Selection:**
  - Nếu chọn `Fixed Rate` -> Ẩn `Rate Per Hour`, hiện `Total Rate`.
  - Nếu chọn `Project Hours` -> Ẩn `Total Rate`, hiện `Rate Per Hour`.
  - Nếu chọn `Task Hours` -> Ẩn cả `Total Rate` và `Rate Per Hour` (vì chi phí dựa theo từng Task).
- **Progress Calculation:**
  - Nếu `Calculate progress through tasks = Checked` -> Ẩn/Disable thanh trượt `Progress`, tự động tính `%` tiến độ theo số Task hoàn thành (`Completed Tasks / Total Tasks * 100%`).
  - Nếu `Calculate progress through tasks = Unchecked` -> Hiển thị thanh trượt cho phép kéo chọn `%` tiến độ từ `0%` đến `100%`.
- **Customer Task Permissions:**
  - Các checkbox `create tasks`, `edit tasks`, `comment on project tasks`, `view task comments`, `view task attachments`, `view task checklist items`, `upload attachments on tasks`, `view task total logged time` bị **disabled** cho tới khi checkbox `Allow customer to view tasks` được chọn (**checked**).

---

## 5. CÂU HỎI & LÀM RÕ VỚI PO / USER (OPEN QUESTIONS)

1. **Giới hạn số lượng ký tự cho Project Name:** Hệ thống hiện tại có giới hạn độ dài tối đa (maxlength) của tên dự án hay không?
2. **Quy tắc tạo Mã Dự Án (Project ID):** Mã dự án được tự động tăng (Auto-increment) hay cho phép tùy chỉnh tiền tố (Prefix)?
3. **Phân quyền người dùng (Role-based Access Control):** Nhân viên với vai trò Staff (non-admin) có những hạn chế gì khi truy cập module Projects so me với Admin?
