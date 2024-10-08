# Store management tool

## Overview

The store management tool is an API which mainly supports CRUD operations where store products and inventories can be managed by various users.
The API also offers authentication and authorization mechanisms.

## Technology Stack

- **Backend:** [Java](https://www.java.com/) with [Spring Boot](https://spring.io/projects/spring-boot) - for creating
  RESTful services.
- **Security:** [Spring Security](https://spring.io/projects/spring-security) with JWT for authentication - ensures
  secure access control.
- **Database:** [H2 Database](https://www.h2database.com/) - in-memory database for rapid development and testing.
- **Object-Relational Mapping (ORM):** [Hibernate](https://hibernate.org/)
  with [JPA](https://jakarta.ee/specifications/persistence/) - for efficient database operations and easy data
  manipulation.
- **Data validation:** Enforced by the [Jakarta Persistence API](https://jakarta.ee/specifications/persistence/) - for
  robust data integrity.
- **Build and dependency management:** Managed with [Maven](https://maven.apache.org/) - simplifies project builds and
  manages dependencies.
- **Code simplification:** [Lombok](https://projectlombok.org/) - minimizes boilerplate code for cleaner and more
  readable codebase.
- **API Design:** RESTful APIs - for scalable and maintainable service architecture.
- **Testing:** [JUnit](https://junit.org/junit5/) and [Mockito](https://site.mockito.org/) - for unit testing.

## Features

- **Store manage:** Admin users can create and manage stores by assigning them to different users.
- **Product and category creation:** Admin and manager users can manage products and categories.
- **Users manage:** Users can be created an managed by users with Admin role.
- **Authentication and authorization:** Users can register and authenticate. REST endpoints authorize the requests based on roles and permissions.

## User roles and permissions

#### Admin
- Can create, update, assign and delete stores
- Manage user access and roles
- Manage inventories, products and categories
- Permissions: store, category, user, inventory and product manage

#### Manager
- Can create, update, delete and manage products
- Manage inventories and categories
- Permissions: category, inventory and product manage

#### Employee
- Manage products and categories
- Permissions: category and product manage

## Installation Steps

To get started with the store management tool, follow these steps:

### 1. **Clone the Repository:**

```bash
git clone https://github.com/bogdaniordan/store-management-tool.git
```

### 2. Navigate to the project directory:

```bash
cd store-management-tool
```

### 3. Ensure prerequisites are installed:

Ensure Java and Maven are installed and properly configured on your system before starting.
The application is tested with Java 22 and Maven 3.9.9.

### 4. Start the application:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev