# VD5-AJAX-CRUD - Mục 3 + 4 + 5

Project gộp 3 mục của phần "về nhà làm":

- **Mục 3**: CRUD API cho `Category` (theo "HƯỚNG DẪN CRUD API CATEGORY TRÊN SPRING BOOT 3"), mở rộng thêm CRUD API cho `Product`.
- **Mục 4**: Cấu hình Swagger 3 (OpenAPI) bằng `springdoc-openapi-starter-webmvc-ui`.
- **Mục 5**: Viết API + render lên AJAX (jQuery) cho chức năng CRUD trên bảng `Product` và bảng `Category`.

## Kiến trúc

```
Controller (@RestController /api/**) -> Service -> Repository (JPA) -> SQL Server
Trang admin/ajax.html (Thymeleaf) gọi các API trên bằng jQuery AJAX, không reload trang.
```

- `entity/Category.java`, `entity/Product.java` - Entity ánh xạ bảng `categories`, `products`.
- `repository/` - JpaRepository.
- `services/` - Xử lý nghiệp vụ, upload/xóa file ảnh/icon vào `uploads/categories`, `uploads/products`.
- `controllers/api/CategoryAPIController.java`, `ProductApiController.java` - REST API CRUD (GET/POST/PUT/DELETE).
- `templates/admin/ajax.html` - 1 trang duy nhất, 2 bảng (Category, Product), thêm/sửa qua modal Bootstrap, xóa có confirm — toàn bộ thao tác gọi AJAX, không reload trang.

## Cấu hình DB

Mặc định trỏ tới SQL Server cùng server/user với 2 project VD1-2, VD3:

```properties
spring.datasource.url=jdbc:sqlserver://localhost\\SQLEXPRESS01;databaseName=webst2_ajax_crud;...
spring.datasource.username=iot_user
spring.datasource.password=27122006
```

Tạo database trước khi chạy (SSMS):

```sql
IF DB_ID('webst2_ajax_crud') IS NULL CREATE DATABASE webst2_ajax_crud;
GO
USE webst2_ajax_crud;
GO
IF NOT EXISTS (SELECT * FROM sys.database_principals WHERE name = 'iot_user')
    CREATE USER iot_user FOR LOGIN iot_user;
ALTER ROLE db_owner ADD MEMBER iot_user;
GO
```

## Chạy project

```bash
mvn spring-boot:run
```

- Trang CRUD AJAX: http://localhost:8097/
- Swagger UI: http://localhost:8097/swagger-ui.html

## Đưa lên Github (mục 6)

```bash
git init
git add .
git commit -m "Muc 3+4+5: CRUD API Category+Product, Swagger3, AJAX render"
git branch -M main
git remote add origin <link-repo-github-cua-ban>
git push -u origin main
```

Copy link repo Github vừa tạo, nộp vào utexlms.
