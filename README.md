# WheelWiz 🚗

A comprehensive automobile price analysis and search engine that helps users find the best car deals through intelligent data collection, processing, and search capabilities.

## 📋 Project Overview

WheelWiz is a Java-based application that combines web scraping, data analysis, and search engine technologies to provide users with an intelligent automobile shopping experience. The system collects real-time pricing data from major Canadian car manufacturer websites and provides advanced search functionality with features like spell checking, word completion, and price analysis.

## 🎯 Purpose

This project solves the problem of scattered automobile information across multiple dealer websites by:
- Aggregating car data from multiple manufacturers (Hyundai, Chevrolet, Nissan, Mitsubishi)
- Providing intelligent search with auto-completion and spell checking
- Analyzing trends and finding best deals based on user preferences
- Offering price validation and budget-based recommendations

## 🛠️ Tech Stack

- **Language**: Java 100%
- **Web Scraping**: Selenium WebDriver with ChromeDriver
- **HTML Parsing**: Jsoup
- **Data Processing**: OpenCSV for CSV file operations
- **Data Structures**: Custom Trie implementation for efficient search
- **Build System**: IntelliJ IDEA project configuration

## 📁 Project Structure

### Source Code (`/src/`)
- **`AutomobilePriceAnalysis.java`** - Main application entry point with user interface
- **`WebCrawler.java`** - Web crawler for collecting URLs from manufacturer sites
- **Scraping Classes**:
  - `HyundaiScraping.java` - Selenium-based scraper for Hyundai Canada
  - `ChevroletScraping.java` - Scraper for Chevrolet Canada
  - `NissanScraping.java` - Scraper for Nissan Canada
  - `MitsubishiScraping.java` - Scraper for Mitsubishi Motors Canada
- **Search & Analysis**:
  - `TrieInvertedIndex.java` - Trie-based inverted index for fast searching
  - `SearchFrequency.java` - Tracks and analyzes search patterns
  - `WordCompletion.java` - Auto-completion functionality
  - `SpellCheck.java` - Spell checking for user input
  - `PageRankCalculator.java` - Page ranking algorithm implementation
- **Data Processing**:
  - `DataExtractionAndValidation.java` - Input validation and data extraction
  - `CarDetails.java` - Car information management
  - `BestDeal.java` - Best deal analysis and recommendations
  - `FrequencyCount.java` - Category frequency analysis
  - `PrintStatements.java` - User interface messaging

### Data Files
- **`scraped_*.csv`** - Scraped vehicle data (Hyundai, Chevrolet, Nissan, Mitsubishi)
- **`crawled_urls.csv`** - Collected URLs from web crawling
- **`inverted_index.csv`** - Processed inverted index data
- **`ranked_index.csv`** - Page-ranked search results
- **`sf_dataset.csv`** - Search frequency dataset
- **`CarBrands.txt`** - List of supported car brands

## 🚀 Key Features

### 1. **Real-time Data Collection**
- Automated web scraping from major Canadian car manufacturer websites
- Structured data extraction (Brand, Model, Year, Price, Category)
- CSV-based data storage and management

### 2. **Intelligent Search Engine**
- Trie-based inverted indexing for fast keyword searches
- Auto-completion with word suggestions
- Spell checking with brand name correction
- Page ranking algorithm for result relevance

### 3. **Advanced User Experience**
- Interactive command-line interface with ASCII art branding
- Input validation for names, email, phone, and budget
- Best deal recommendations based on user preferences
- Trending categories and most searched items display

### 4. **Data Analysis & Insights**
- Price trend analysis across different categories
- Search frequency tracking and analytics
- Budget-based filtering and recommendations
- Category popularity insights

## 🔧 Setup Instructions

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- Chrome browser installed
- ChromeDriver compatible with your Chrome version
- Internet connection for web scraping

### Dependencies
Ensure the following libraries are in your classpath:
- Selenium WebDriver
- Jsoup HTML parser
- OpenCSV

### Installation Steps

1. **Clone the Repository**
   ```bash
   git clone https://github.com/vicky-vn/WheelWiz.git
   cd WheelWiz
   ```

2. **Setup ChromeDriver**
   - Download ChromeDriver from [official site](https://chromedriver.chromium.org/)
   - Update the driver path in scraping classes:
     ```java
     System.setProperty("webdriver.chrome.driver", "/path/to/your/chromedriver");
     ```

3. **Configure Dependencies**
   - Add required JAR files to your IDE project
   - Or use Maven/Gradle (configuration files would need to be added)

### Running the Application

1. **Data Collection (Optional - CSV files are included)**
   ```bash
   # Run individual scrapers to update data
   java HyundaiScraping
   java ChevroletScraping
   java NissanScraping
   java MitsubishiScraping
   
   # Run web crawler
   java WebCrawler
   ```

2. **Start Main Application**
   ```bash
   java AutomobilePriceAnalysis
   ```

3. **Follow Interactive Prompts**
   - Enter personal information (name, email, phone)
   - Specify budget requirements
   - Select vehicle category with auto-completion
   - Choose brand with spell-check assistance
   - Search for specific models using inverted index

## 📊 Usage Example

```
*******************************************************************************************
██╗    ██╗██╗  ██╗███████╗███████╗██╗     ██╗    ██╗██╗███████╗
██║    ██║██║  ██║██╔════╝██╔════╝██║     ██║    ██║██║╚══███╔╝
██║ █╗ ██║███████║█████╗  █████╗  ██║     ██║ █╗ ██║██║  ███╔╝ 
██║███╗██║██╔══██║██╔══╝  ██╔══╝  ██║     ██║███╗██║██║ ███╔╝  
╚███╔███╔╝██║  ██║███████╗███████╗███████╗╚███╔███╔╝██║███████╗
 ╚══╝╚══╝ ╚═╝  ╚═╝╚══════╝╚══════╝╚══════╝ ╚══╝╚══╝ ╚═╝╚══════╝
*******************************************************************************************

!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!Best Deals!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

Trending Categories: Crossover-SUV, Compact-Sedan, Hybrid-Electric
Most Searched: Hyundai, Toyota, Honda

Enter your first name: John
Enter your last name: Smith
Enter contact details (email and phone): john.smith@email.com 416-555-0123
Enter your maximum budget: $50000
Enter vehicle category: SUV
Enter brand name: Hyundai

[Results display with matching vehicles within budget]
```



## 📄 License

This project is available for educational and research purposes. Please check with contributors for commercial usage.

---

*WheelWiz - Your intelligent automobile shopping companion* 🚗✨
