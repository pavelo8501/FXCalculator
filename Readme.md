# Currency Calculator API

This project is a currency calculator API that allows users to convert between currencies with configurable conversion fees. It includes two APIs:

- **Administrative API**: Allows an administrator to configure conversion fees for specific currency pairs.
- **Public API**: Allows users to perform currency conversions with real-time exchange rates and configured fees.

## Features

- **Fee Management**: Configure conversion fees for specific currency pairs. Add, edit, and delete fees.
- **Exchange Rates Update**: Fetch the latest exchange rates from the [European Central Bank](https://www.ecb.europa.eu/stats/policy_and_exchange_rates/euro_reference_exchange_rates/html/index.en.html) and refresh the cached data.
- **Currency Conversion**: Convert an amount from one currency to another, applying the relevant conversion fee.
- **Fee Persistence**: Configured fees are persisted to the database.
- **Docker Support**: Run the application and a database instance using Docker Compose.
- **API Documentation**: Swagger UI for exploring and testing the API endpoints.
- **API Versioning**: Version the API to maintain backward compatibility.
- **Exchanger Rates Caching**: Retrieve and cache exchange rates at application startup.
- **Error Handling**: Provides meaningful error messages for API requests.
- **Robust Class Design**: Utilizes DTOs, services, repositories, and controllers for a clean architecture and further extensibility.
- **Unit & Integration Tests**: Ensure the correctness of the application logic.

### Administrative API

- **List Fees**: Retrieve all configured conversion fees.
- **Add Fee**: Add a fee for a specific currency pair.
- **Edit Fee**: Modify the fee for an existing currency pair.
- **Remove Fee**: Delete the fee for a specific currency pair.

### Public API

- **Convert Currency**: Convert an amount from one currency to another, applying the relevant conversion fee.
- **Refresh Exchange Rates**: Fetch the latest exchange rates from the [European Central Bank](https://www.ecb.europa.eu/stats/policy_and_exchange_rates/euro_reference_exchange_rates/html/index.en.html) and refresh the cached data.

## Tech Stack

- **Kotlin**
- **Spring Boot**
- **PostgresSQL**
- **Docker & Docker Compose**
- **Swagger**
- **JUnit & Mockito**
- **Angular** (for the frontend admin dashboard)

## Getting Started

### Prerequisites

- **JDK 21**: Ensure you have JDK 21 installed. You can download it from [AdoptOpenJDK](https://adoptopenjdk.net/).
- **Docker**: Install Docker to run the application and database using Docker Compose.

  !!! If you intend to launch the application on host other than localhost, you need to rebuild Admin Dashboard with the correct base-href:
- **Node.js**: Ensure you have Node.js installed. You can download it from [Node.js.org](https://nodejs.org/).
- **Angular CLI**: Install Angular CLI globally using npm. Run `npm install -g @angular/cli`.

### Installation

#### Manual Installation

1. Manual installation:

1.1 Clone the repository:

```bash
  git clone
```

1.2 Navigate to the project directory:

```bash 
cd currency-calculator-api
```

1.3. Build the project:

```bash
./gradlew build
```

    !!! If you intend to launch the application on host other than localhost, you need to rebuild Admin Dashboard with the correct base-href:
    !!! This step 1.4.* is not required if you are running the application on localhost.

1.4 Build the Angular project (Admin Dashboard):

1.4.1 Install Node.js:
    You can download it from [Node.js.org](https://nodejs.org/).

1.4.2 Install Angular CLI:

```bash
npm install -g @angular/cli
```

1.4.3 Install dependencies:

```bash
npm install
```
1.4.4 Build the Angular project:

```bash
ng build --base-href http://{Desired Host Name}:8080/admin/ --output-hashing none
```
1.4.5 Copy the generated files from the `dist/fxcalculator-admin/browser` folder to the `resources/static/admin` folder in the Spring Boot project.

1.5 Set up the database:

1.6 Configure setup through .env file:
    Copy the .env.example file to .env and configure the database connection settings and default conversion fee.

1.6. Run the application:

```bash
java -jar build/libs/currency-calculator-api-1.0.jar
```

#### Automated Installation

1. Automated installation:

1.1. Configure setup through .env file:
    Copy the .env.example file to .env and configure the database connection settings, host name  and default conversion fee.

1.2. Run the installation script:
    
```bash
./install.bat
```

1.3. Run the application:

```bash 
java -jar build/libs/currency-calculator-api-1.0.jar
```
#### Docker Installation

1. Docker installation:
1.1. Build and run the application using Docker Compose:

```bash
docker-compose up
```

### API Documentation

- **Swagger UI**: Access the API documentation at [http://localhost:8080/swagger-ui/](http://localhost:8080/swagger-ui/).
- **API Versioning**: The API is versioned, and the latest version is `v1`.
- **API Endpoints**: Explore and test the API endpoints using Swagger UI.

### Testing Calculations
- **Application**: The application is hosted at [http://localhost:8080/](http://localhost:8080/).

### Accessing the Admin Dashboard

- **Admin Dashboard**: The admin dashboard is an Angular-based frontend hosted within the Spring Boot application.
- **URL**: Navigate to [http://localhost:8080/admin/](http://localhost:8080/admin/) to access the admin dashboard.

### Running Tests

```bash
./gradlew test
```