# Audit Log Service

## Tiếng Việt

### Hướng dẫn tạo hệ thống mẫu với Audit Log, Authentication & Authorization cho người dùng

Hệ thống mẫu này được thiết kế để giúp bạn hiểu cách áp dụng audit log cùng với các chức năng xác thực (authentication) và phân quyền (authorization) cho người dùng. Dưới đây là các bước để thiết lập hệ thống:

### 1. Cài đặt môi trường

- **Java**: Đảm bảo bạn đã cài đặt JDK 11 hoặc cao hơn.
- **Maven**: Sử dụng Maven để quản lý các phụ thuộc.
- **Database**: Sử dụng MySQL hoặc PostgreSQL để lưu trữ dữ liệu.

### 2. Tạo dự án

- Sử dụng Maven để tạo một dự án mới:
  
  ```
  mvn archetype:generate -DgroupId=com.shongon.audit_log -DartifactId=audit-log-service -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false
  ```

### 3. Cấu hình database

- Tạo một cơ sở dữ liệu mới trong MySQL hoặc PostgreSQL.
- Cập nhật thông tin kết nối trong file ```application.yaml:```

  ```
  spring:
    datasource:
      url: jdbc:mysql://localhost:3306/your_database_name
      username: your_username
      password: your_password
  // -------------------------------------------------------
  spring:
    datasource:
      url: jdbc:postgresql://localhost:5432/your_database_name
      username: your_username
      password: your_password
  ```

### 4. Cài đặt các phụ thuộc

- Thêm các phụ thuộc cần thiết vào file ```pom.xml:```
  
  ```
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-security</artifactId>
  </dependency>
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-data-jpa</artifactId>
  </dependency>
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
  </dependency>
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-aop</artifactId>
  </dependency>
  ```

### 5. Tạo các lớp cho Audit Log

- Tạo một lớp AuditLog để lưu trữ thông tin audit.
- Sử dụng AOP để ghi lại các hành động của người dùng.

### 6. Cài đặt Authentication & Authorization

- Sử dụng Spring Security để cấu hình xác thực và phân quyền cho người dùng.
- Tạo các lớp User, Role, và các dịch vụ liên quan để quản lý người dùng.

### 7. Chạy ứng dụng

- Sử dụng lệnh sau để chạy ứng dụng:
  
  ```
  mvn spring-boot:run
  ```

### 8. Kiểm tra

- Sử dụng Postman hoặc một công cụ tương tự để kiểm tra các API đã được tạo ra.
- Đảm bảo rằng các hành động của người dùng được ghi lại trong audit log.

---

## English

### Guide to Create a Sample System with Audit Log, Authentication & Authorization for Users

This sample system is designed to help you understand how to apply audit logs along with authentication and authorization functionalities for users. Below are the steps to set up the system:

### 1. Set Up Environment

- **Java**: Ensure you have JDK 11 or higher installed.
- **Maven**: Use Maven to manage dependencies.
- **Database**: Use MySQL or PostgreSQL to store data.

### 2. Create Project

- Use Maven to create a new project:
  
  ```
  mvn archetype:generate -DgroupId=com.shongon.audit_log -DartifactId=audit-log-service -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=falsE
  ```
### 3. Configure Database

- Create a new database in MySQL or PostgreSQL.
- Update the connection information in the ```application.yaml: ```

```
  spring:
    datasource:
      url: jdbc:mysql://localhost:3306/your_database_name
      username: your_username
      password: your_password
  // -------------------------------------------------------
  spring:
    datasource:
      url: jdbc:postgresql://localhost:5432/your_database_name
      username: your_username
      password: your_password
  ```

### 4. Install Dependencies

- Add necessary dependencies to the ```pom.xml:```
  
  ```
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-security</artifactId>
  </dependency>
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-data-jpa</artifactId>
  </dependency>
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
  </dependency>
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-aop</artifactId>
  </dependency>
  ```

### 5. Create Classes for Audit Log

- Create an AuditLog class to store audit information.
- Use AOP to log user actions.

### 6. Set Up Authentication & Authorization

- Use Spring Security to configure authentication and authorization for users.
- Create User, Role, and related service classes to manage users.

### 7. Run the Application
- Use the following command to run the application:
  
  ```
  mvn spring-boot:run
  ```

### 8. Testing

- Use Postman or a similar tool to test the created APIs.
- Ensure that user actions are logged in the audit log.
---
