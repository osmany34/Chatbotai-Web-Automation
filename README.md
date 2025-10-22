# 🤖 ChatbotAI Web Test Automation (Gauge + Selenium + Java 17)

This repository contains the automated **web testing framework** for [ChatbotAI](https://chatbotai.com/).
It is designed to validate the functional, regression, and smoke test cases of the web platform using **Gauge** (BDD), **Selenium WebDriver**, and **Java 17**.

The framework follows a modular and maintainable architecture based on:
- **BDD (Behavior Driven Development)** syntax
- **Page Object Model (POM)** pattern
- **CSS-based element mapping (element.json)**
- **Reusable and parameterized steps**

---

## 🧩 Tech Stack

| Component | Version | Description |
|------------|---------|-------------|
| **Java** | 17      | Main programming language |
| **Gauge** | 1.6.4   | BDD testing framework |
| **Selenium WebDriver** | 4.37    | Web browser automation |
| **Maven** | ≥ 3.8   | Build and dependency management |
| **JUnit 5** | 5.10.0  | Assertions and test control |

---


## 🧰 Prerequisites

1. Install Java and Maven
   Check your Java version:
    ```bash
    java -version

2. Install Gauge CLI and Plugins
   Install Gauge:

    ```bash
    choco install gauge        # Windows
    brew install gauge         # macOS
    sudo apt install gauge     # Ubuntu

3. Configure ChromeDriver
   Download the correct ChromeDriver version for your Chrome browser and add it to your system PATH:

    ```bash
   chromedriver --version

## Reporting
Gauge automatically generates HTML reports after each test execution. For advanced reporting, Allure or ExtentReports can be integrated.

HTML Report Path: reports/html-report/index.html

Each report includes:

Total passed/failed/skipped specs

Step execution duration

Screenshots on failure

Stack trace of failed steps

## Developer
    Osman Yelek
    QA Automation Engineer

🔗 [LinkedIn](https://www.linkedin.com/in/osmanyelek/)