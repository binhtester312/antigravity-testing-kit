# TÀI LIỆU ĐẶC TẢ YÊU CẦU NGHIỆP VỤ (REQUIREMENTS SPECIFICATION)
# MODULE: DASHBOARD - HỆ THỐNG PERFEX CRM

## 1. TỔNG QUAN (OVERVIEW)

### 1.1. Mục đích
Module Dashboard đóng vai trò là trung tâm chỉ huy và tổng hợp thông tin quan trọng nhất của hệ thống Perfex CRM. Module cung cấp góc nhìn toàn cảnh về tình hình tài chính, chỉ số dự án, tiến độ công việc, lịch biểu, ghi chú cá nhân và các hoạt động gần đây của toàn bộ hệ thống.

### 1.2. Phạm vi và Đối tượng sử dụng
- Đối tượng: Quản trị viên (Admin), Quản lý dự án, Nhân viên (Staff).
- Giao diện chính: https://crm.anhtester.com/admin/
- Mục tiêu chính: Giúp người dùng nắm bắt số liệu kinh doanh nhanh chóng, truy cập các đường dẫn tắt và thực hiện các thao tác tạo mới dữ liệu một cách tiện lợi nhất.

---

## 2. YÊU CẦU CHỨC NĂNG (FUNCTIONAL REQUIREMENTS)

### 2.1. Quản lý tùy chỉnh Widget (Dashboard Options)
- Tên tính năng: Tùy chỉnh hiển thị các thành phần trên Dashboard.
- Mô tả: Cho phép người dùng bật/tắt sự xuất hiện của từng Widget trên giao diện cá nhân hoặc đặt lại cấu hình mặc định.
- Tiêu chí chấp nhận (Acceptance Criteria):
  1. Khi nhấn vào nút Dashboard Options ở góc trên bên phải giao diện, một vùng điều khiển sẽ mở ra hiển thị danh sách các Widget.
  2. Danh sách các checkbox Widget bao gồm: Quick Statistics, Finance Overview, User Widget, Calendar, Payment Records, Contracts Expiring Soon, My To Do Items, Leads Chart, Projects Chart, Latest Project Activity.
  3. Bỏ tích chọn một Widget sẽ làm ẩn Widget tương ứng trên màn hình ngay lập tức.
  4. Nút Reset Dashboard cho phép khôi phục toàn bộ giao diện về trạng thái ban đầu.
  5. Nút View Widgetable Area hiển thị các vùng chứa Widget hợp lệ.

### 2.2. Khối thống kê nhanh (Quick Statistics)
- Tên tính năng: Thống kê chỉ số hoạt động tổng quan.
- Mô tả: Hiển thị các thẻ chỉ số chính dạng số lượng và tỷ lệ.
- Tiêu chí chấp nhận:
  1. Invoices Awaiting Payment: Hiển thị số lượng hóa đơn chờ thanh toán trên tổng số hóa đơn.
  2. Converted Leads: Hiển thị số lượng khách hàng tiềm năng đã chuyển đổi trên tổng số leads.
  3. Projects In Progress: Hiển thị số lượng dự án đang thực hiện trên tổng số dự án.
  4. Tasks Not Finished: Hiển thị số lượng công việc chưa hoàn thành trên tổng số công việc.
  5. Khi nhấn vào các thẻ thống kê, hệ thống sẽ điều hướng người dùng tới danh sách chi tiết tương ứng.

### 2.3. Tổng quan Tài chính (Finance Overview)
- Tên tính năng: Báo cáo chỉ số tài chính theo năm.
- Mô tả: Tổng hợp trạng thái của Hóa đơn, Báo giá và Đề xuất kinh doanh.
- Tiêu chí chấp nhận:
  1. Người dùng có thể chọn Năm để lọc dữ liệu tài chính (ví dụ: 2026, 2025, 2024).
  2. Phân đoạn Invoice Overview: Hiển thị số lượng và phần trăm của các trạng thái Draft, Not Sent, Unpaid, Partially Paid, Overdue, Paid kèm theo thanh tiến trình màu sắc tương ứng.
  3. Phân đoạn Estimate Overview: Hiển thị trạng thái Draft, Not Sent, Sent, Expired, Declined, Accepted.
  4. Phân đoạn Proposal Overview: Hiển thị trạng thái Draft, Sent, Open, Revised, Declined, Accepted.
  5. Hiển thị tổng giá trị tiền tệ cho: Outstanding Invoices (Hóa đơn chờ thu), Past Due Invoices (Hóa đơn quá hạn), Paid Invoices (Hóa đơn đã thu).

### 2.4. Khối Công việc & Dự án cá nhân (User Widget)
- Tên tính năng: Quản lý danh mục cá nhân qua thẻ Tab.
- Mô tả: Cung cấp các tab danh sách thông tin cá nhân liên quan.
- Tiêu chí chấp nhận:
  1. Tab My Tasks: Danh sách các công việc được giao. Cho phép lọc số lượng hiển thị (10, 25, 50, 100, All), chuyển trang (Jump page), xuất dữ liệu (Export) và tải lại bảng.
  2. Mỗi dòng công việc hiển thị: Mã ID, Tên công việc (Name), Trạng thái (Status), Ngày bắt đầu (Start Date), Thẻ (Tags), Độ ưu tiên (Priority).
  3. Hành động trên công việc: Cho phép bật/tắt bộ đếm thời gian (Start/Stop Timer), Edit công việc, Delete công việc.
  4. Tab My Projects: Hiển thị danh sách dự án cá nhân phụ trách.
  5. Tab My Reminders: Hiển thị các nhắc nhở công việc sắp tới.
  6. Tab Tickets: Hiển thị các yêu cầu hỗ trợ được phân công.
  7. Tab Announcements: Hiển thị các thông báo chung của công ty.
  8. Nút View All điều hướng tới trang danh sách tương ứng.

### 2.5. Lịch làm việc & Sự kiện (Calendar & Add Event)
- Tên tính năng: Xem và quản lý lịch biểu sự kiện.
- Mô tả: Hiển thị các sự kiện, hạn chót công việc, dự án dưới dạng lịch.
- Tiêu chí chấp nhận:
  1. Người dùng có thể chuyển đổi chế độ xem theo Tháng (Month), Tuần (Week), Ngày (Day), Today hoặc Expand lịch.
  2. Nút Filter By hỗ trợ lọc sự kiện theo phân loại.
  3. Cho phép nhấn vào ngày bất kỳ trên lịch hoặc sử dụng chức năng tạo sự kiện để mở modal Add new event.
  4. Modal Add new event cho phép nhập tiêu chuẩn: Event title (bắt buộc), Description, Start Date (bắt buộc), End Date, Notification (số và loại: Minutes/Hours/Days/Weeks), Public Event (checkbox).

### 2.6. Báo cáo Thanh toán & Hợp đồng hết hạn
- Tên tính năng: Theo dõi thanh toán và rủi ro hợp đồng.
- Mô tả: Giám sát các giao dịch tiền về và hợp đồng sắp đến hạn thanh lý.
- Tiêu chí chấp nhận:
  1. Payment Records: Hiển thị biểu đồ thanh toán, hỗ trợ xem theo tuần (Weekly) và liên kết tới Báo cáo đầy đủ (Full Report).
  2. Contracts Expiring Soon: Liệt kê các hợp đồng sẽ hết hạn trong vòng 7 ngày tới. Hiển thị thông báo trống khi không có hợp đồng sắp hết hạn.

### 2.7. Danh sách việc cần làm cá nhân (My To Do Items)
- Tên tính năng: Ghi chú To-Do cá nhân.
- Mô tả: Lưu trữ danh sách công việc nhỏ cần tự xử lý.
- Tiêu chí chấp nhận:
  1. Phân chia 2 nhóm: Latest to do's (Chưa xong) và Latest finished to do's (Đã hoàn thành).
  2. Nút New To Do mở modal Add New Todo chỉ chứa trường Description.
  3. Người dùng có thể tích chọn checkbox để đánh dấu hoàn thành, sửa nội dung hoặc xóa ghi chú.

### 2.8. Biểu đồ Khách hàng tiềm năng & Dự án
- Tên tính năng: Thống kê trực quan bằng biểu đồ.
- Mô tả: Cung cấp biểu đồ tròn/cột phân tích dữ liệu.
- Tiêu chí chấp nhận:
  1. Leads Overview: Hiển thị phân bổ khách hàng tiềm năng theo trạng thái.
  2. Statistics by Project Status: Hiển thị phân bổ dự án theo trạng thái (Not Started, In Progress, On Hold, Cancelled, Finished).

### 2.9. Nhật ký hoạt động dự án gần đây (Latest Project Activity)
- Tên tính năng: Dòng thời gian hoạt động hệ thống.
- Mô tả: Ghi nhận realtime các hành động của người dùng trên hệ thống liên quan đến dự án.
- Tiêu chí chấp nhận:
  1. Hiển thị thời gian tương đối (ví dụ: 3 HRS AGO, 3 DAYS AGO).
  2. Hiển thị người thực hiện, hành động (Created project, Added team member, Added task assignee...) và tên dự án liên quan.

### 2.10. Thanh công cụ phía trên (Header Toolbar & Quick Search)
- Tên tính năng: Tìm kiếm nhanh và Thanh điều hướng đỉnh trang.
- Mô tả: Cung cấp ô tìm kiếm toàn cục và các nút tác vụ nhanh.
- Tiêu chí chấp nhận:
  1. Ô tìm kiếm #search_input (Search...): Cho phép gõ từ khóa tìm kiếm nhanh đối tượng trong CRM.
  2. Nút Tạo nhanh (+): Cho phép tạo nhanh Invoice, Estimate, Proposal, Credit Note, Customer, Subscription, Project, Task, Expense, Contract, Article, Ticket, Event.
  3. Nút Todo items: Mở nhanh modal tạo mới To Do.
  4. Nút Top Timers: Hiển thị danh sách các bộ đếm thời gian công việc đang chạy.
  5. Menu tài khoản cá nhân: Truy cập My Profile, My Timesheets, Edit Profile, Logout.

---

## 3. ĐẶC TẢ TRƯỜNG DỮ LIỆU (FIELD SPECIFICATIONS)

Bảng chi tiết các trường thông tin quan sát được trên giao diện và các modal của Module Dashboard:

| Tên Trường (Label) | Loại UI Element | Bắt buộc | Quy tắc dữ liệu & Ràng buộc | Ghi chú |
| --- | --- | --- | --- | --- |
| Search... | Input Text (Search) | Không | Nhập chuỗi tự do, lọc realtime | ID: search_input trên Header |
| Years Filter | Select Dropdown | Không | Các giá trị: 2026, 2025, 2024 | Lọc năm cho Finance Overview |
| Length Menu (Tasks) | Select Dropdown | Không | Các giá trị: 10, 25, 50, 100, All | Phân trang bảng My Tasks |
| Page Jump (Tasks) | Select Dropdown | Không | Giá trị số từ 1 đến N (ví dụ: 1-9) | Nhảy trang bảng My Tasks |
| Dashboard Widget Checkboxes | Checkbox | Không | Checked (Bật) / Unchecked (Tắt) | Trong vùng Dashboard Options |
| Event Title | Input Text | Bắt buộc | Chuỗi văn bản, không để trống | Trong modal Add new event (ID: title) |
| Description (Event) | Textarea | Không | Chuỗi văn bản nhiều dòng | Trong modal Add new event (ID: description) |
| Start Date | Input Datetime | Bắt buộc | Định dạng ngày giờ chuẩn | Trong modal Add new event (ID: start) |
| End Date | Input Datetime | Không | Định dạng ngày giờ chuẩn | Trong modal Add new event (ID: end) |
| Notification (Reminder) | Input Number | Bắt buộc | Giá trị số nguyên dương, mặc định 30 | Trong modal Add new event (ID: reminder_before) |
| Notification Unit | Select Dropdown | Bắt buộc | Các giá trị: Minutes, Hours, Days, Weeks | Trong modal Add new event (ID: reminder_before_type) |
| Public Event | Checkbox | Không | Checked / Unchecked | Trong modal Add new event (ID: public) |
| Description (Todo) | Textarea | Bắt buộc | Chuỗi văn bản nội dung công việc | Trong modal Add New Todo (ID: description) |

---

## 4. LUỒNG XỬ LÝ VÀ QUY TẮC NGHIỆP VỤ (BUSINESS RULES & FLOWS)

### 4.1. Luồng Tùy chỉnh Widget Dashboard (Customize Dashboard Flow)
1. Bước 1: Người dùng nhấn vào nút Dashboard Options ở phía trên cùng bên phải giao diện.
2. Bước 2: Khung điều khiển danh sách Widget mở ra.
3. Bước 3: Người dùng tích chọn hoặc bỏ tích chọn các checkbox đại diện cho từng Widget (Quick Statistics, Finance Overview, User Widget, Calendar, Payment Records, Contracts Expiring Soon, My To Do Items, Leads Chart, Projects Chart, Latest Project Activity).
4. Bước 4: Hệ thống cập nhật hiển thị các Widget trên màn hình tương ứng với lựa chọn ngay lập tức.
5. Bước 5: Người dùng có thể nhấn Reset Dashboard để đưa toàn bộ trạng thái giao diện về mặc định ban đầu.

### 4.2. Luồng Tạo mới Sự kiện trên Lịch (Add Event Flow)
1. Bước 1: Người dùng nhấn vào ô ngày trên Calendar hoặc chọn sự kiện tạo mới.
2. Bước 2: Modal Add new event hiển thị.
3. Bước 3: Người dùng nhập các thông tin: Event title, Description, Start Date, End Date, Notification, Notification Unit, Public Event.
4. Bước 4: Nhấn nút Save để gửi thông tin.
5. Bước 5: Hệ thống kiểm tra điều kiện dữ liệu (Validation):
   - Nếu Event title hoặc Start Date bị trống: Hệ thống hiển thị thông báo lỗi "This field is required." tại chân trường tương ứng và không cho phép lưu.
   - Nếu dữ liệu hợp lệ: Hệ thống lưu sự kiện, đóng modal và cập nhật sự kiện mới lên biểu đồ Calendar.

### 4.3. Luồng Tạo mới và Quản lý Việc cần làm (To-Do Management Flow)
1. Bước 1: Người dùng nhấn vào nút New To Do trong Widget My To Do Items hoặc icon Todo trên Header.
2. Bước 2: Modal Add New Todo xuất hiện.
3. Bước 3: Người dùng nhập nội dung công việc vào ô Description.
4. Bước 4: Nhấn Save. Dữ liệu được lưu và hiển thị trong nhóm Latest to do's.
5. Bước 5: Khi hoàn thành công việc, người dùng tích chọn checkbox đầu item. Item đó lập tức được chuyển sang danh sách Latest finished to do's.
6. Bước 6: Người dùng có thể nhấn biểu tượng Edit để sửa nội dung hoặc biểu tượng Delete (xóa) để loại bỏ To-Do item.

### 4.4. Quy tắc Báo lỗi và Xác thực (Validation Rules)
1. Xác thực trường bắt buộc (Mandatory Validation):
   - Trường Event Title (Modal Event): Không được để trống. Thông báo lỗi: "This field is required.".
   - Trường Start Date (Modal Event): Không được để trống. Thông báo lỗi: "This field is required.".
   - Trường Notification (Modal Event): Phải là số dương hợp lệ.
2. Xác thực Token an toàn (CSRF Protection):
   - Mọi form tương tác (Event, Todo, Options) đều chứa trường ẩn csrf_token_name để đảm bảo an toàn giao dịch.
3. Phân quyền và Bảo mật (Access Control):
   - Chỉ người dùng đã đăng nhập thành công mới được truy cập vào giao diện Dashboard (/admin/).
   - Dữ liệu hiển thị trong User Widget tự động lọc theo phân quyền và nhiệm vụ cá nhân của người dùng hiện tại.
