# Mobile Number Portability (MNP) Service

## Description
Implements a Mobile Number Portability (MNP) service. [Ref](https://www.tra.gov.eg/wp-content/uploads/2020/11/Number-Portability-User-Guide-Summary.pdf).

## Installation

### Requirements
JDK17, Docker, and Docker Compose.

### Steps
#### Method 1
- execute the run script:
  ```console
    ./run_application.shell
  ```

#### Method 2
1. Clone the repository to your local environment and cd inside:
  ```console
    git clone https://github.com/mostafaism1/mobile-number-portability
  ```

  ```console
    cd mobile-number-portability
  ```

2. Build and package the app:
  ```console
    ./mvnw clean package
  ```

2. Build the docker image:
  ```console
    docker build -t mnp:1.0.0 .
  ```

3. Run docker compose
  ```console
    docker-compose up
  ```

- Head to: http://localhost:8080/swagger-ui/index.html for API documentation.
