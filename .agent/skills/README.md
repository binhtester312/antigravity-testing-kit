# Skills — Kỹ năng chuyên biệt cho AI

Thư mục này chứa các **skill** — bộ hướng dẫn chuyên biệt giúp AI thực hiện các tác vụ phức tạp một cách chính xác.

## Mục đích
- Cung cấp context chuyên sâu cho từng loại tác vụ cụ thể.
- Giúp AI hiểu đúng pattern, template, và best practice của dự án.
- Tái sử dụng được cho nhiều task khác nhau.

## Cách dùng
Mỗi file `.md` là một skill độc lập với cấu trúc YAML frontmatter:

```yaml
---
name: tên-skill
description: Mô tả khi nào dùng skill này
---
```

AI sẽ tự động kích hoạt skill phù hợp dựa trên `description` của task.

## Ví dụ file
- `create-test-class.md` — Kỹ năng tạo test class mới theo chuẩn dự án.
- `write-page-object.md` — Kỹ năng viết Page Object Model.
- `debug-flaky-test.md` — Kỹ năng phân tích và sửa flaky test.
