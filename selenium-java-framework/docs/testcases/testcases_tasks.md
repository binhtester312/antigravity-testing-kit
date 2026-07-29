# BỘ TEST CASES MANUAL - MODULE TASKS (PERFEX CRM)

| TC ID | Module | Risk Level | Test Scenario | Pre-Condition | Test Steps | Test Data | Expected Result | Priority |
| --- | --- | --- | --- | --- | --- | --- | --- | --- |
| CRM_TASK_TC_001 | Tasks | High Risk | Tạo mới công việc thành công với thông tin hợp lệ | Đã đăng nhập Admin | 1. Vào Tasks -> New Task<br>2. Nhập Subject, Start Date, Priority, Assignees<br>3. Bấm Save | Subject: Auto Task 01, Priority: High | Công việc được tạo thành công | High |
| CRM_TASK_TC_002 | Tasks | High Risk | Xác thực trường bắt buộc khi để trống Subject | Đang ở màn hình New Task | 1. Để trống Subject<br>2. Chọn Start Date<br>3. Bấm Save | Subject: [Để trống] | Báo lỗi trường Subject là bắt buộc | High |
| CRM_TASK_TC_003 | Tasks | High Risk | Chuyển trạng thái công việc (Not Started -> In Progress -> Complete) | Công việc tồn tại | 1. Click đổi trạng thái công việc sang Complete | Status: Complete | Trạng thái công việc cập nhật thành công | High |
| CRM_TASK_TC_004 | Tasks | Medium Risk | Bắt đầu bộ đếm thời gian làm việc (Start Timer) | Đang xem chi tiết task | 1. Click Start Timer | Nút: Start Timer | Bộ đếm thời gian bắt đầu ghi nhận | Medium |
| CRM_TASK_TC_005 | Tasks | High Risk | Xóa công việc khỏi hệ thống | Công việc tồn tại | 1. Click Delete Task<br>2. Xác nhận | Nút: Delete | Công việc bị xóa thành công | High |
