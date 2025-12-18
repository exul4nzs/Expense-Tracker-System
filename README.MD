# Expense Tracker with Analytics

A comprehensive Java Swing-based desktop application for tracking personal expenses with visual analytics and budget management capabilities.

## Features

### Core Functionality
- **Add, Edit, and Delete Expenses** - Manage your expenses with an intuitive interface
- **Expense Categorization** - Organize expenses by custom categories
- **Date-Based Filtering** - Filter expenses by specific dates using an interactive date picker
- **Persistent Storage** - All data is saved automatically using JSON format
- **Monthly Budget Management** - Set and track your monthly spending limits

### Analytics & Visualization
- **Pie Chart Visualization** - Visual breakdown of expenses by category with color-coded legend
- **Category Totals** - Automatic calculation of spending per category
- **Expense Insights** - Identifies your highest spending category
- **Real-Time Updates** - Charts update automatically as you add or modify expenses

### Budget Tracking
- **Budget vs. Spending** - Track remaining budget in real-time
- **Visual Indicators** - Color-coded remaining balance (green for positive, red for overspending)
- **Low Balance Warnings** - Automatic alerts when remaining budget falls below ₱500
- **Budget Validation** - Prevents setting budgets lower than existing expenses

## Requirements

### Dependencies
- **Java SE 8 or higher**
- **Gson Library** (com.google.gson) - For JSON serialization/deserialization
  - Download from [Maven Repository](https://mvnrepository.com/artifact/com.google.code.gson/gson)
  - Or add via Maven: `com.google.code.gson:gson:2.10.1`

### System Requirements
- Windows, macOS, or Linux with Java Runtime Environment (JRE)
- Minimum 512 MB RAM
- Screen resolution: 1280x720 or higher recommended

## Installation

1. **Clone or download the project files**
   ```bash
   git clone <repository-url>
   cd expense-tracker
   ```

2. **Add Gson library to your project**
   - Download the Gson JAR file
   - Add it to your project's classpath
   - If using an IDE (Eclipse/IntelliJ), add it as an external library

3. **Compile the project**
   ```bash
   javac -cp .:gson-2.10.1.jar Main.java Expense.java
   ```

4. **Run the application**
   ```bash
   java -cp .:gson-2.10.1.jar Main
   ```

## Usage Guide

### Setting Your Budget
1. Click the **"Add Your Budget"** button in the top-left section
2. Enter your monthly budget amount
3. Click **"Save Budget"**
4. Your budget will be displayed in the top-right corner along with remaining balance

### Adding Expenses
1. Click **"Add / Edit Expense"** button
2. Fill in the expense details:
   - **Description**: What you spent on (e.g., "Grocery shopping")
   - **Amount**: Cost in pesos (e.g., "1500")
   - **Category**: Type of expense (e.g., "Food", "Transport", "Entertainment")
3. Click **"Add Expense"**

### Editing Expenses
1. Select a row in the expense table
2. Click **"Add / Edit Expense"** button
3. Click **"Edit Expense"** in the dialog
4. Modify the details as needed
5. Click **"Update Expense"**

### Deleting Expenses
1. Select a row in the expense table
2. Click **"Add / Edit Expense"** button
3. Click **"Delete Expense"**
4. Confirm the deletion

### Filtering by Date
- Use the date picker in the top-right corner to select a specific date
- The table and pie chart will automatically update to show only expenses from that date

### Viewing Analytics
- The pie chart displays automatically below the expense table
- Shows percentage breakdown of spending by category
- Includes a legend with exact amounts per category
- Displays your highest spending category

## Data Storage

The application stores data locally in JSON format:

- **expenses.json** - Contains all expense records
- **budget.json** - Stores your monthly budget setting

These files are created automatically in the application's directory and persist between sessions.

## Project Structure

```
expense-tracker/
├── Main.java           # Main application class with GUI
├── Expense.java        # Expense data model class
├── expenses.json       # Auto-generated expense data
├── budget.json         # Auto-generated budget data
└── README.md          # This file
```

## Key Classes

### Main.java
The main application class containing:
- GUI components and layout management
- Event handlers for user interactions
- Data persistence methods (save/load)
- Budget tracking logic
- `PieChartPanel` - Inner class for rendering the expense analytics chart

### Expense.java
Data model class representing a single expense with:
- Description
- Amount
- Category
- Date

## Color Scheme
- **Primary Background**: Light Blue (#ADD8E6)
- **Panel Background**: Light Blue Gradient (#C8E6FA)
- **Add Button**: Blue (#007ACC)
- **Edit Button**: Orange (#FFA500)
- **Delete Button**: Crimson (#DC143C)
- **Budget Button**: Green (#228B22)

## Tips & Best Practices

1. **Consistent Categories** - Use the same category names for better analytics (e.g., always use "Food" instead of mixing "Food", "Groceries", "Eating")

2. **Regular Updates** - Add expenses daily for accurate budget tracking

3. **Budget Planning** - Set realistic budgets based on your income and essential expenses

4. **Review Analytics** - Regularly check the pie chart to identify spending patterns

5. **Backup Data** - Periodically backup your `expenses.json` and `budget.json` files

## Troubleshooting

**Issue**: Application won't start
- **Solution**: Ensure Java is properly installed and Gson library is in the classpath

**Issue**: Data not saving
- **Solution**: Check file permissions in the application directory

**Issue**: Budget warning appears repeatedly
- **Solution**: This is intentional when balance is below ₱500. Increase budget or reduce expenses

**Issue**: Pie chart not displaying
- **Solution**: Add at least one expense to generate the chart

## Future Enhancements

Potential features for future versions:
- Export data to CSV/Excel
- Monthly/yearly expense reports
- Multiple budget categories
- Recurring expense templates
- Dark mode theme
- Cloud sync capabilities
- Mobile companion app

## License

This project is open-source and available for educational purposes.

## Support

For issues, questions, or contributions, please open an issue in the project repository.

---

**Version**: 1.0  
**Last Updated**: December 2025  
**Author**: Bartley's Expense Tracker TeamTroubleshooting
Issue: Application won't start

Solution: Ensure Java is properly installed and Gson library is in the classpath

Issue: Data not saving

Solution: Check file permissions in the application directory

Issue: Budget warning appears repeatedly

Solution: This is intentional when balance is below ₱500. Increase budget or reduce expenses

Issue: Pie chart not displaying

Solution: Add at least one expense to generate the chart# Expense Tracker with Analytics

A comprehensive Java Swing-based desktop application for tracking personal expenses with visual analytics and budget management capabilities.

## Features

### Core Functionality
- **Add, Edit, and Delete Expenses** - Manage your expenses with an intuitive interface
- **Expense Categorization** - Organize expenses by custom categories
- **Date-Based Filtering** - Filter expenses by specific dates using an interactive date picker
- **Persistent Storage** - All data is saved automatically using JSON format
- **Monthly Budget Management** - Set and track your monthly spending limits

### Analytics & Visualization
- **Pie Chart Visualization** - Visual breakdown of expenses by category with color-coded legend
- **Category Totals** - Automatic calculation of spending per category
- **Expense Insights** - Identifies your highest spending category
- **Real-Time Updates** - Charts update automatically as you add or modify expenses

### Budget Tracking
- **Budget vs. Spending** - Track remaining budget in real-time
- **Visual Indicators** - Color-coded remaining balance (green for positive, red for overspending)
- **Low Balance Warnings** - Automatic alerts when remaining budget falls below ₱500
- **Budget Validation** - Prevents setting budgets lower than existing expenses

## Requirements

### Dependencies
- **Java SE 8 or higher**
- **Gson Library** (com.google.gson) - For JSON serialization/deserialization
  - Download from [Maven Repository](https://mvnrepository.com/artifact/com.google.code.gson/gson)
  - Or add via Maven: `com.google.code.gson:gson:2.10.1`

### System Requirements
- Windows, macOS, or Linux with Java Runtime Environment (JRE)
- Minimum 512 MB RAM
- Screen resolution: 1280x720 or higher recommended

## Installation

1. **Clone or download the project files**
   ```bash
   git clone <repository-url>
   cd expense-tracker
   ```

2. **Add Gson library to your project**
   - Download the Gson JAR file
   - Add it to your project's classpath
   - If using an IDE (Eclipse/IntelliJ), add it as an external library

3. **Compile the project**
   ```bash
   javac -cp .:gson-2.10.1.jar Main.java Expense.java
   ```

4. **Run the application**
   ```bash
   java -cp .:gson-2.10.1.jar Main
   ```

## Usage Guide

### Setting Your Budget
1. Click the **"Add Your Budget"** button in the top-left section
2. Enter your monthly budget amount
3. Click **"Save Budget"**
4. Your budget will be displayed in the top-right corner along with remaining balance

### Adding Expenses
1. Click **"Add / Edit Expense"** button
2. Fill in the expense details:
   - **Description**: What you spent on (e.g., "Grocery shopping")
   - **Amount**: Cost in pesos (e.g., "1500")
   - **Category**: Type of expense (e.g., "Food", "Transport", "Entertainment")
3. Click **"Add Expense"**

### Editing Expenses
1. Select a row in the expense table
2. Click **"Add / Edit Expense"** button
3. Click **"Edit Expense"** in the dialog
4. Modify the details as needed
5. Click **"Update Expense"**

### Deleting Expenses
1. Select a row in the expense table
2. Click **"Add / Edit Expense"** button
3. Click **"Delete Expense"**
4. Confirm the deletion

### Filtering by Date
- Use the date picker in the top-right corner to select a specific date
- The table and pie chart will automatically update to show only expenses from that date

### Viewing Analytics
- The pie chart displays automatically below the expense table
- Shows percentage breakdown of spending by category
- Includes a legend with exact amounts per category
- Displays your highest spending category

## Data Storage

The application stores data locally in JSON format:

- **expenses.json** - Contains all expense records
- **budget.json** - Stores your monthly budget setting

These files are created automatically in the application's directory and persist between sessions.

## Project Structure

```
expense-tracker/
├── Main.java           # Main application class with GUI
├── Expense.java        # Expense data model class
├── expenses.json       # Auto-generated expense data
├── budget.json         # Auto-generated budget data
└── README.md          # This file
```

## Key Classes

### Main.java
The main application class containing:
- GUI components and layout management
- Event handlers for user interactions
- Data persistence methods (save/load)
- Budget tracking logic
- `PieChartPanel` - Inner class for rendering the expense analytics chart

### Expense.java
Data model class representing a single expense with:
- Description
- Amount
- Category
- Date

## Color Scheme
- **Primary Background**: Light Blue (#ADD8E6)
- **Panel Background**: Light Blue Gradient (#C8E6FA)
- **Add Button**: Blue (#007ACC)
- **Edit Button**: Orange (#FFA500)
- **Delete Button**: Crimson (#DC143C)
- **Budget Button**: Green (#228B22)

## Tips & Best Practices

1. **Consistent Categories** - Use the same category names for better analytics (e.g., always use "Food" instead of mixing "Food", "Groceries", "Eating")

2. **Regular Updates** - Add expenses daily for accurate budget tracking

3. **Budget Planning** - Set realistic budgets based on your income and essential expenses

4. **Review Analytics** - Regularly check the pie chart to identify spending patterns

5. **Backup Data** - Periodically backup your `expenses.json` and `budget.json` files

## Troubleshooting

**Issue**: Application won't start
- **Solution**: Ensure Java is properly installed and Gson library is in the classpath

**Issue**: Data not saving
- **Solution**: Check file permissions in the application directory

**Issue**: Budget warning appears repeatedly
- **Solution**: This is intentional when balance is below ₱500. Increase budget or reduce expenses

**Issue**: Pie chart not displaying
- **Solution**: Add at least one expense to generate the chart

## Future Enhancements

Potential features for future versions:
- Export data to CSV/Excel
- Monthly/yearly expense reports
- Multiple budget categories
- Recurring expense templates
- Dark mode theme
- Cloud sync capabilities
- Mobile companion app

## License

This project is open-source and available for educational purposes.

## Support

For issues, questions, or contributions, please open an issue in the project repository.

---

**Version**: 1.0  
**Last Updated**: December 2025  
**Author**: Expense Tracker Team