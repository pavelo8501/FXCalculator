# FXCalculator Admin Dashboard

This project is an Angular-based admin dashboard that is hosted within a Spring Boot application. It provides an interface for managing various administrative tasks.
This project was generated with [Angular CLI](https://github.com/angular/angular-cli) version 18.1.3.

## Features

- **Fee** Management: Configure conversion fees for specific currency pairs. Add, edit, and delete fees.
- **Exchange Rates** Update: Fetch the latest exchange rates from the [European Central Bank](https://www.ecb.europa.eu/stats/policy_and_exchange_rates/euro_reference_exchange_rates/html/index.en.html) and refresh the cached data.


## Tech Stack

- **Angular**
- **TypeScript**
- **HTML**
- **CSS**
- **npm**

## Getting Started

### Prerequisites

- **Node.js**: Ensure you have Node.js installed. You can download it from [nodejs.org](https://nodejs.org/).
- **Angular CLI**: Install Angular CLI globally using npm.

### Installation

1)Install dependencies:
npm install

### Deployment

1)Build the project:
ng build --base-href http://localhost:8080/admin/ --output-hashing none

2)Copy the generated files from the dist folder to the resources/static/admin folder in the Spring Boot project.

3)Run the Spring Boot application and navigate to http://localhost:8080/admin/ to access the admin dashboard.

OR

run install.bat script found in the root directory of the repository to build the project and copy the generated files to the Spring Boot project.
