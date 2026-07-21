# Workflows — Kịch bản thực thi step-by-step

Thư mục này chứa các **workflow** — kịch bản thực thi tuần tự từng bước, tương ứng với slash commands của AI.

## Mục đích
- Tự động hóa các quy trình lặp lại nhiều bước.
- Đảm bảo AI thực hiện đúng thứ tự và không bỏ sót bước nào.
- Hoạt động như slash commands: người dùng gõ lệnh, AI thực thi cả quy trình.

## Cách dùng
Mỗi file `.md` định nghĩa một workflow với các bước rõ ràng.  
Gọi workflow bằng cách nhắc AI: _"Chạy workflow `tên-file`"_ hoặc dùng slash command tương ứng.

## Cấu trúc file workflow
```markdown
# Tên Workflow

## Trigger
Điều kiện hoặc lệnh kích hoạt workflow này.

## Steps
1. Bước 1: Mô tả hành động
2. Bước 2: Mô tả hành động
3. ...

## Output
Kết quả mong đợi sau khi hoàn thành.
```

## Ví dụ file
- `create-feature-test.md` — Quy trình tạo test đầy đủ cho một feature mới.
- `run-regression.md` — Quy trình chạy regression và báo cáo kết quả.
- `onboard-new-module.md` — Quy trình khởi tạo module test mới.
