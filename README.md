# Store management tool

## Overview

The store management tool is an API which supports CRUD mainly operations where store products and inventories can be managed by various users.

## Technology Stack

- **Backend:** [Java](https://www.java.com/) with [Spring Boot](https://spring.io/projects/spring-boot) - for creating
  RESTful services.
- **Security:** [Spring Security](https://spring.io/projects/spring-security) with JWT for authentication - ensures
  secure access control.
- **Database:** [H2 Database](https://www.h2database.com/) - in-memory database for rapid development and testing.
- **Object-Relational Mapping (ORM):** [Hibernate](https://hibernate.org/)
  with [JPA](https://jakarta.ee/specifications/persistence/) - for efficient database operations and easy data
  manipulation.
- **Data Validation:** Enforced by the [Jakarta Persistence API](https://jakarta.ee/specifications/persistence/) - for
  robust data integrity.
- **Build and Dependency Management:** Managed with [Maven](https://maven.apache.org/) - simplifies project builds and
  manages dependencies.
- **Code Simplification:** [Lombok](https://projectlombok.org/)- minimizes boilerplate code for cleaner and more
  readable codebase.
- **API Design:** RESTful APIs - for scalable and maintainable service architecture.

## Features

- **Store manage:** Admin users can create and manage stores by assigning them to different users.
- **Product and category creation:** Admin and manager users can manage products and categories.
- **Users manage:** Users can be created an managed by users with Admin role.
- **Authentication and authorization:** Users can register and then authenticate. Endpoints authorize their requests based on roles and permissions

## User roles

#### Admin
- Can create, update, and delete stores
- Manages user access and roles
- Manages products and categories

#### Manager
- Can create, update, delete and manage products
- Can manage inventories

#### Employee
- Can only manage products

## Installation Steps

To get started with the store management tool, follow these steps:

### 1. **Clone the Repository:**

```bash
git clone https://github.com/bogdaniordan/store-management-tool.git
```

### 2. Navigate to the Project Directory:

```bash
cd store-management-tool
```

### 3. Ensure Prerequisites are Installed:

Ensure Java and Maven are installed and properly configured on your system before starting.
The application is tested with Java 22 and Maven 3.9.9.

### 4. Start the application:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev