# Currency Calculator API

This project is a currency calculator API that allows users to convert between currencies with configurable conversion fees. It includes two APIs:

- **Administrative API**: Allows an administrator to configure conversion fees for specific currency pairs.
- **Public API**: Allows users to perform currency conversions with real-time exchange rates and configured fees.

## Features

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
- **PostgreSQL**
- **Docker & Docker Compose**
