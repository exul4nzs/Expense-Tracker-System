Expense Tracker Application
A desktop expense tracking application built with Java Swing that allows users to add, edit, delete, and persist expense records locally using JSON file storage.

📋 Table of Contents

Features
Requirements
Installation
Project Structure
Detailed Implementation
Method Documentation
Data Model
File Storage
Usage Guide
Troubleshooting


✨ Features

Add Expenses: Record new expenses with description, amount, category, and date
Edit Expenses: Modify existing expense records
Delete Expenses: Remove expenses with confirmation dialog
Date Selection: Built-in date picker for selecting expense dates
Persistent Storage: Automatically saves all expenses to local JSON file
Auto-Load: Loads previously saved expenses on application startup
Modern UI: Clean, intuitive interface with light blue color scheme
Table View: Display all expenses in an organized, scrollable table


🔧 Requirements
Software Requirements

Java Development Kit (JDK): Version 8 or higher
Gson Library: Version 2.8.0 or higher

Hardware Requirements

OS: Windows, macOS, or Linux
RAM: Minimum 512MB
Screen Resolution: 1280x720 or higher (recommended)


📥 Installation
Step 1: Download Gson Library

Download the Gson JAR file from Maven Repository
Or use Maven:

xml<dependency>
    <groupId>com.google.code.gson</groupId>
    <artifactId>gson</artifactId>
    <version>2.10.1</version>
</dependency>
Step 2: Add Gson to Your Project
For Eclipse:

Right-click on your project → Build Path → Configure Build Path
Click "Add External JARs"
Select the downloaded gson-x.x.x.jar file

For IntelliJ IDEA:

File → Project Structure → Libraries
Click "+" → Java
Select the downloaded gson-x.x.x.jar file

For Command Line:
bashjavac -cp .:gson-2.10.1.jar Main.java Expense.java
java -cp .:gson-2.10.1.jar Main
Step 3: Compile and Run
bashjavac Main.java Expense.java
java Main


📁 Project Structure
expense-tracker/
├── Main.java              # Main application class with GUI
├── Expense.java           # Data model class for expenses
├── expenses.json          # Auto-generated storage file
└── README.md             # This file

🔍 Detailed Implementation
Application Architecture
The Expense Tracker follows a Model-View pattern:

View Layer: Swing components (JFrame, JPanel, JTable, JButton, etc.)
Model Layer: Expense class representing expense data
Persistence Layer: JSON file-based storage using Gson

GUI Components Breakdown
1. Main Frame
javasetSize(1280, 720);
setLocationRelativeTo(null);  // Centers the window
setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
setLayout(new BorderLayout());

Size: 1280x720 pixels
Layout: BorderLayout for flexible component positioning
Background: Light blue (#ADD8E6)

2. Top Panel (NORTH)
Contains:

Title: "Expense Tracker" (45pt bold)
Welcome Message: "Welcome back, Bartley!" (18pt)
Date Picker: JSpinner with date selection

3. Left Panel (WEST) - Form Panel
Input fields for expense data:

Description Field: 500x40 pixels, 16pt font
Amount Field: 500x40 pixels, 16pt font
Category Field: 500x40 pixels, 16pt font
Action Buttons:

Add/Update Button (Blue - #007ACC)
Edit Button (Orange - #FFA500)
Delete Button (Red - #DC143C)



4. Center Panel (CENTER) - Table

Columns: Description, Amount, Category, Date
Features: Scrollable, sortable, row selection
Dimensions: 700px preferred width, responsive height


📚 Method Documentation
Constructor: Main()
Purpose: Initializes the entire GUI and loads saved expenses.
Implementation Details:

Sets up the main JFrame window
Creates and arranges all GUI components
Attaches event listeners to buttons
Loads existing expenses from JSON file
Displays the window

Key Steps:
javapublic Main() {
    // 1. Setup frame
    super("Expense Tracker");
    setSize(1280, 720);
    
    // 2. Create GUI components
    // ... (top panel, form panel, table)
    
    // 3. Load saved data
    loadExpensesToTable();
    
    // 4. Show window
    setVisible(true);
}

Method: saveExpenses()
Signature: private void saveExpenses()
Purpose: Saves all current expenses from the table to a JSON file.
Algorithm:

Create a new ArrayList to hold Expense objects
Iterate through each row in the table model
Extract data from each column (description, amount, category, date)
Create an Expense object and add to list
Use Gson to serialize the list to JSON
Write JSON to expenses.json file

Implementation:
javaprivate void saveExpenses() {
    try (Writer writer = new FileWriter("expenses.json")) {
        Gson gson = new Gson();
        java.util.List<Expense> expenses = new ArrayList<>();
        
        // Extract data from table
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String desc = tableModel.getValueAt(i, 0).toString();
            String amount = tableModel.getValueAt(i, 1).toString();
            String category = tableModel.getValueAt(i, 2).toString();
            String date = tableModel.getValueAt(i, 3).toString();
            expenses.add(new Expense(desc, amount, category, date));
        }
        
        // Serialize to JSON
        gson.toJson(expenses, writer);
        System.out.println("Expenses saved successfully!");
    } catch (IOException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, 
            "Error saving expenses: " + e.getMessage(), 
            "Error", JOptionPane.ERROR_MESSAGE);
    }
}
Error Handling:

Catches IOException if file cannot be written
Displays error dialog to user
Prints stack trace for debugging

When Called:

After adding a new expense
After updating an existing expense
After deleting an expense


Method: loadExpensesToTable()
Signature: private void loadExpensesToTable()
Purpose: Loads expenses from the JSON file and populates the table on application startup.
Algorithm:

Open and read expenses.json file
Use Gson to deserialize JSON into List<Expense>
Iterate through the list
Add each expense as a new row in the table model

Implementation:
javaprivate void loadExpensesToTable() {
    try (Reader reader = new FileReader("expenses.json")) {
        Gson gson = new Gson();
        java.lang.reflect.Type listType = 
            new TypeToken<java.util.List<Expense>>(){}.getType();
        java.util.List<Expense> expenses = gson.fromJson(reader, listType);
        
        if (expenses != null) {
            for (Expense expense : expenses) {
                tableModel.addRow(new Object[]{
                    expense.getDescription(),
                    expense.getAmount(),
                    expense.getCategory(),
                    expense.getDate()
                });
            }
            System.out.println("Loaded " + expenses.size() + " expenses");
        }
    } catch (FileNotFoundException e) {
        System.out.println("No existing expenses file found. Starting fresh.");
    } catch (IOException e) {
        e.printStackTrace();
    }
}
Error Handling:

FileNotFoundException: Silently handled (file doesn't exist on first run)
IOException: Prints stack trace
Null check: Prevents NullPointerException if JSON is empty

Type Safety:
Uses TypeToken to preserve generic type information during deserialization:
javajava.lang.reflect.Type listType = 
    new TypeToken<java.util.List<Expense>>(){}.getType();
When Called:

Once during application initialization (in constructor)


Event Handler: Add/Update Button
Purpose: Adds a new expense or updates an existing one.
Logic Flow:
User clicks "Add Expense" or "Update Expense"
    ↓
Extract values from input fields
    ↓
Is editing mode active? (editingRow[0] != -1)
    ↓
YES → Update existing row in table
    ↓
    Reset editing mode
    ↓
    Change button text to "Add Expense"
    ↓
NO → Validate all fields are filled
    ↓
    Add new row to table
    ↓
Save all expenses to JSON file
    ↓
Clear input fields
Implementation:
javaaddButton.addActionListener(e -> {
    String desc = descField.getText();
    String amount = amountField.getText();
    String category = categoryField.getText();
    String date = new SimpleDateFormat("MMM dd, yyyy")
        .format(datePicker.getValue());

    if (editingRow[0] != -1) {
        // UPDATE MODE
        tableModel.setValueAt(desc, editingRow[0], 0);
        tableModel.setValueAt(amount, editingRow[0], 1);
        tableModel.setValueAt(category, editingRow[0], 2);
        tableModel.setValueAt(date, editingRow[0], 3);

        editingRow[0] = -1;
        addButton.setText("Add Expense");
    } else {
        // ADD MODE
        if (!desc.isEmpty() && !amount.isEmpty() && !category.isEmpty()) {
            tableModel.addRow(new Object[]{desc, amount, category, date});
        } else {
            JOptionPane.showMessageDialog(this, 
                "Please fill all fields.", 
                "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
    }

    saveExpenses();  // Persist changes

    // Clear fields
    descField.setText("");
    amountField.setText("");
    categoryField.setText("");
});
Validation:

Checks if all fields (description, amount, category) are non-empty
Shows warning dialog if validation fails
Returns early without saving if validation fails


Event Handler: Edit Button
Purpose: Loads selected expense data into input fields for editing.
Logic Flow:
User clicks "Edit Expense"
    ↓
Get selected row from table
    ↓
Is a row selected?
    ↓
YES → Load row data into input fields
    ↓
    Store row index in editingRow[0]
    ↓
    Change "Add Expense" button to "Update Expense"
    ↓
NO → Show warning dialog
Implementation:
javaeditButton.addActionListener(e -> {
    int selectedRow = expenseTable.getSelectedRow();

    if (selectedRow != -1) {
        // Load data into fields
        descField.setText(
            tableModel.getValueAt(selectedRow, 0).toString());
        amountField.setText(
            tableModel.getValueAt(selectedRow, 1).toString());
        categoryField.setText(
            tableModel.getValueAt(selectedRow, 2).toString());

        // Set editing mode
        editingRow[0] = selectedRow;
        addButton.setText("Update Expense");
    } else {
        JOptionPane.showMessageDialog(this, 
            "Please select a row to edit.", 
            "Warning", JOptionPane.WARNING_MESSAGE);
    }
});
State Management:

Uses editingRow[0] array to track which row is being edited
Value of -1 means not in editing mode
Any other value is the index of the row being edited


Event Handler: Delete Button
Purpose: Removes selected expense from table with confirmation.
Logic Flow:
User clicks "Delete Expense"
    ↓
Get selected row from table
    ↓
Is a row selected?
    ↓
YES → Show confirmation dialog
    ↓
    User confirms?
    ↓
    YES → Remove row from table
        ↓
        Save updated expenses
        ↓
        If deleted row was being edited, reset editing mode
        ↓
NO → Show warning dialog
Implementation:
javadeleteButton.addActionListener(e -> {
    int selectedRow = expenseTable.getSelectedRow();

    if (selectedRow != -1) {
        // Confirmation dialog
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete the selected expense?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            tableModel.removeRow(selectedRow);

            // Save changes
            saveExpenses();

            // Reset if editing this row
            if (editingRow[0] == selectedRow) {
                editingRow[0] = -1;
                addButton.setText("Add Expense");
                descField.setText("");
                amountField.setText("");
                categoryField.setText("");
            }
        }
    } else {
        JOptionPane.showMessageDialog(this, 
            "Please select a row to delete.", 
            "Warning", JOptionPane.WARNING_MESSAGE);
    }
});
Safety Features:

Requires user confirmation before deletion
Prevents accidental data loss
Clears input fields if deleted row was being edited


📊 Data Model
Expense Class
Purpose: Represents a single expense record.
Structure:
javapublic class Expense {
    private String description;  // What was purchased
    private String amount;       // Cost (stored as String)
    private String category;     // Type of expense
    private String date;         // When it occurred

    // Constructor
    public Expense(String description, String amount, 
                   String category, String date) {
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.date = date;
    }

    // Getters and Setters for each field
    public String getDescription() { return description; }
    public void setDescription(String description) { 
        this.description = description; 
    }
    
    // ... (similar for amount, category, date)
}
Design Decisions:

String for Amount: Allows flexibility for currency symbols and decimals
Private Fields: Encapsulation ensures data integrity
Public Getters/Setters: Controlled access to data
Serializable by Gson: No special annotations needed


💾 File Storage
JSON Format
Expenses are stored in expenses.json in the following format:
json[
  {
    "description": "Grocery Shopping",
    "amount": "150.50",
    "category": "Food",
    "date": "Nov 26, 2025"
  },
  {
    "description": "Gas",
    "amount": "45.00",
    "category": "Transportation",
    "date": "Nov 25, 2025"
  }
]
File Location

Default Location: Same directory as the application
File Name: expenses.json
Auto-Creation: Created automatically on first save
Permissions: Read/Write required

Gson Serialization
Why Gson?

Simple API for JSON conversion
No manual parsing required
Handles type safety
Lightweight and fast

Serialization Example:
javaGson gson = new Gson();
String json = gson.toJson(expensesList);  // Object → JSON
Deserialization Example:
javaGson gson = new Gson();
List<Expense> expenses = gson.fromJson(json, listType);  // JSON → Object

📖 Usage Guide
Adding an Expense

Fill in the Description field (e.g., "Lunch at Restaurant")
Enter the Amount (e.g., "25.50")
Type the Category (e.g., "Food")
Select the Date using the date picker (defaults to today)
Click "Add Expense" button
The expense appears in the table and is saved automatically

Editing an Expense

Click on the expense row in the table to select it
Click "Edit Expense" button
The expense data loads into the input fields
The "Add Expense" button changes to "Update Expense"
Modify any fields as needed
Click "Update Expense" to save changes

Deleting an Expense

Click on the expense row in the table to select it
Click "Delete Expense" button
A confirmation dialog appears
Click "Yes" to confirm deletion
The expense is removed from the table and file

Date Selection

Click on the date picker spinner
Use arrow buttons to navigate months/days/years
Or click and type the date directly
Format: MMM dd, yyyy (e.g., "Nov 26, 2025")


🐛 Troubleshooting
Issue: "Type mismatch" error with java.lang.reflect.Type
Cause: Conflict between java.lang.reflect.Type and java.awt.Window.Type
Solution: Use fully qualified class name:
javajava.lang.reflect.Type listType = 
    new TypeToken<java.util.List<Expense>>(){}.getType();
Issue: "The type List is not generic"
Cause: Wrong List import (using java.awt.List instead of java.util.List)
Solution: Remove this import:
java// import java.awt.List;  // DELETE THIS
Issue: Expenses not saving
Check:

File write permissions in the application directory
Gson library is properly added to classpath
Console for error messages
expenses.json file exists and is not corrupted

Debug: Add this line in saveExpenses():
javaSystem.out.println("Saving " + expenses.size() + " expenses");
Issue: Expenses not loading on startup
Check:

expenses.json file exists in the correct directory
JSON format is valid (use a JSON validator)
Console for error messages

Debug: Add this line in loadExpensesToTable():
javaSystem.out.println("File path: " + new File("expenses.json").getAbsolutePath());
Issue: Application window too small/large
Solution: Adjust the frame size in constructor:
javasetSize(1280, 720);  // Change these values

🔒 Data Integrity
Validation Rules

Description: Cannot be empty
Amount: Cannot be empty (no format validation currently)
Category: Cannot be empty
Date: Always valid (controlled by date picker)

Future Enhancements
Consider adding:

Numeric validation for amount field
Currency formatting
Category dropdown (predefined categories)
Search/filter functionality
Export to CSV/PDF
Summary statistics (total spent, by category)
Date range filtering


🏗️ Code Organization Best Practices
Why Instance Variable for TableModel?
javaprivate DefaultTableModel tableModel;
Benefits:

All methods can access the table
Avoids passing tableModel as parameter
Cleaner method signatures
Easier to maintain

Why Lambda Expressions for Event Handlers?
javaaddButton.addActionListener(e -> {
    // Handler code here
});
Benefits:

More concise than anonymous inner classes
Easier to read
Modern Java style (Java 8+)

Why Try-With-Resources?
javatry (Writer writer = new FileWriter("expenses.json")) {
    // Code here
}
Benefits:

Automatic resource cleanup
No need for explicit .close()
Prevents resource leaks
Exception-safe


📝 License
This project is open source and available for educational purposes.

👤 Author
Bartley Josh D. Teleron

🙏 Acknowledgments

Java Swing: GUI framework
Google Gson: JSON serialization library
SimpleDateFormat: Date formatting


📞 Support
For issues or questions:

Check the Troubleshooting section
Review console output for error messages
Verify all dependencies are properly installed