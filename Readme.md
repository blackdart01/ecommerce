# Ecommerce Backend using Spring Boot and PostgreSQL

This project is an ecommerce backend system developed using Spring Boot and PostgreSQL. It provides RESTful APIs for managing various aspects of an ecommerce platform.

## Getting Started

### Live Branch
- develop-live

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- Apache Maven
- PostgreSQL
- Postman
- Web Browser to view swagger

### Installation
1. Clone the repository:
    ```bash
    git clone https://github.com/blackdart01/ecommerce.git
    cd ecommerce
    git checkout <branchName>
    ```

2. Set up the PostgreSQL database:
    - Create a new database named \
    - database : `ecommerce_db` with \
    - user : `admin` and \
    - password : `admin` running at\
    - port : `5432`.

3. Build the project:
    ```bash
    mvn clean package
    ```

4. Run tests:
    ```bash
    mvn clean test
    ```

5. Build the project skipping tests:
    ```bash
    mvn clean package -DskipTests
    ```

### Running the Application
To run the application locally, use the following command:
```bash
mvn spring-boot:run
```

The application will be accessible at `http://localhost:8092`.

### API Documentation (Swagger)
Swagger UI is integrated into the application for easy API documentation and testing.
You can access the Swagger UI using the following URL when the application is running:
```
http://localhost:8092/swagger-ui/index.html
```

## Contributing
Contributions are welcome! Please feel free to fork the repository and submit pull requests to contribute to this project.

## License
This project is licensed under the [MIT License](LICENSE).
