import javax.swing.*;
import java.util.stream.Collectors;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.io.*;
import java.util.*;

public class Main extends JFrame {
    
    private DefaultTableModel tableModel;
    private double monthlyBudget = 0;
    private JLabel budgetDisplayLabel;
    private JLabel remainingLabel;
    private java.util.List<Expense> allExpenses = new ArrayList<>();
    
    class PieChartPanel extends JPanel {

        private DefaultTableModel tableModel;
        private Map<String, Color> categoryColors = new HashMap<>();

        public PieChartPanel(DefaultTableModel tableModel) {
            this.tableModel = tableModel;
            setPreferredSize(new Dimension(900, 350));
            setBackground(Color.WHITE);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Map<String, Double> categoryTotals = new HashMap<>();

            for (int i = 0; i < tableModel.getRowCount(); i++) {
                try {
                    String category = tableModel.getValueAt(i, 2).toString();
                    double amount = Double.parseDouble(
                            tableModel.getValueAt(i, 1).toString()
                    );
                    categoryTotals.merge(category, amount, Double::sum);
                } catch (Exception ignored) {}
            }

            if (categoryTotals.isEmpty()) {
                g.drawString("No data to display", 20, 20);
                return;
            }

            int diameter = 250;
            int x = 50;
            int y = 50;

            double total = categoryTotals.values().stream().mapToDouble(Double::doubleValue).sum();
            int startAngle = 0;

            Random rand = new Random();

            for (String category : categoryTotals.keySet()) {
                categoryColors.putIfAbsent(category, new Color(rand.nextInt(200), rand.nextInt(200), rand.nextInt(200)));
            }

            for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {
                int arcAngle = (int) Math.round((entry.getValue() / total) * 360);

                g.setColor(categoryColors.get(entry.getKey()));
                g.fillArc(x, y, diameter, diameter, startAngle, arcAngle);

                startAngle += arcAngle;
            }

            int legendX = x + diameter + 40;
            int legendY = y;

            for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {
                g.setColor(categoryColors.get(entry.getKey()));
                g.fillRect(legendX, legendY - 12, 12, 12);

                g.setColor(Color.BLACK);
                g.drawString(
                        entry.getKey() + " - ₱" + String.format("%.2f", entry.getValue()),
                        legendX + 18,
                        legendY
                );
                legendY += 20;
            }

            String maxCategory = categoryTotals.entrySet()
                    .stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse("");

            if (!maxCategory.isEmpty()) {
                g.setFont(new Font("Segoe UI", Font.BOLD, 14));
                g.setColor(Color.DARK_GRAY);
                g.drawString(
                        "Most of your expenses go to: " + maxCategory,
                        legendX,
                        legendY + 20
                );
            }
        }
    }
    
    public Main() {
        super("Expense Tracker");
        
	     final int[] editingRow = { -1 };
	     int FIELD_WIDTH = 500;
	     int FIELD_HEIGHT = 45;

        setSize(1280, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        getContentPane().setBackground(new Color(173, 216, 230));
        
	     Dimension fieldSize = new Dimension(FIELD_WIDTH, FIELD_HEIGHT);
	     
	     JPanel centerContent = new JPanel();
	        centerContent.setLayout(new BoxLayout(centerContent, BoxLayout.Y_AXIS));
	        centerContent.setBackground(new Color(173, 216, 230));
	     
	     JScrollPane centerScrollPane = new JScrollPane(centerContent);
	     centerScrollPane.setBorder(null);
	     centerScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        JDialog expenseDialog = new JDialog(this, "Add / Edit Expense", true);
        expenseDialog.setSize(600, 550);
        expenseDialog.setLocationRelativeTo(this);
        expenseDialog.setLayout(new BorderLayout());
        expenseDialog.getContentPane().setBackground(new Color(173, 216, 230));
        expenseDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        JDialog budgetDialog = new JDialog(this, "Set Monthly Budget", true);
        budgetDialog.setSize(400, 300);
        budgetDialog.setLocationRelativeTo(this);
        budgetDialog.setLayout(new BorderLayout());
        budgetDialog.getContentPane().setBackground(new Color(173, 216, 230));
        budgetDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(173, 216, 230));
        topPanel.setLayout(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(173, 216, 230));
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));

        JLabel title = new JLabel("Expense Tracker");
        title.setFont(new Font("Segoe UI", Font.BOLD, 45));
        title.setForeground(Color.BLACK);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel welcome = new JLabel("Welcome back, Bartley!");
        welcome.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        welcome.setForeground(Color.DARK_GRAY);
        welcome.setAlignmentX(Component.LEFT_ALIGNMENT);

        titlePanel.add(title);
        titlePanel.add(welcome);
        topPanel.add(titlePanel, BorderLayout.WEST);
        
        JButton budgetButton = new JButton("Add Your Budget");
        budgetButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        budgetButton.setBackground(new Color(34, 139, 34)); // green
        budgetButton.setForeground(Color.WHITE);
        budgetButton.setFocusPainted(false);
        budgetButton.setPreferredSize(new Dimension(220, 45));
        budgetButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JButton toggleFormButton = new JButton("Add / Edit Expense");
        toggleFormButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        toggleFormButton.setBackground(new Color(0, 122, 204));
        toggleFormButton.setForeground(Color.WHITE);
        toggleFormButton.setFocusPainted(false);
        toggleFormButton.setPreferredSize(new Dimension(220, 45));
        
        titlePanel.add(Box.createRigidArea(new Dimension(0, 15)));
        titlePanel.add(budgetButton);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        titlePanel.add(toggleFormButton);

        JPanel formPanel = new JPanel();
        formPanel.setBackground(new Color(200, 230, 250));
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        formPanel.setMaximumSize(new Dimension(1200, Integer.MAX_VALUE));

        JLabel descLabel = new JLabel("Description:");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        JTextField descField = new JTextField();
        descField.setMaximumSize(new Dimension(500, 40));
        descField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        descField.setPreferredSize(fieldSize);
        descField.setMaximumSize(fieldSize);
        descField.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel amountLabel = new JLabel("Amount:");
        amountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        JTextField amountField = new JTextField();
        amountField.setMaximumSize(new Dimension(500, 40));
        amountField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        amountField.setPreferredSize(fieldSize);
        amountField.setMaximumSize(fieldSize);
        amountField.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel categoryLabel = new JLabel("Category:");
        categoryLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        JTextField categoryField = new JTextField();
        categoryField.setMaximumSize(new Dimension(500, 40));
        categoryField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        categoryField.setPreferredSize(fieldSize);
        categoryField.setMaximumSize(fieldSize);
        categoryField.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton addButton = new JButton("Add Expense");
        addButton.setFont(new Font("Segoe UI", Font.BOLD, 20));
        addButton.setBackground(new Color(0, 122, 204));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setMaximumSize(new Dimension(500, 50));

        JButton editButton = new JButton("Edit Expense");
        editButton.setFont(new Font("Segoe UI", Font.BOLD, 20));
        editButton.setBackground(new Color(255, 165, 0));
        editButton.setForeground(Color.WHITE);
        editButton.setFocusPainted(false);
        editButton.setMaximumSize(new Dimension(500, 50));

        JButton deleteButton = new JButton("Delete Expense");
        deleteButton.setFont(new Font("Segoe UI", Font.BOLD, 20));
        deleteButton.setBackground(new Color(220, 20, 60));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFocusPainted(false);
        deleteButton.setMaximumSize(new Dimension(500, 50));
        
        JPanel budgetPanel = new JPanel();
        budgetPanel.setBackground(new Color(200, 230, 250));
        budgetPanel.setLayout(new BoxLayout(budgetPanel, BoxLayout.Y_AXIS));
        budgetPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel budgetLabel = new JLabel("Enter your budget:");
        budgetLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        budgetLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField budgetField = new JTextField();
        Dimension budgetSize = new Dimension(300, 40);
        budgetField.setPreferredSize(budgetSize);
        budgetField.setMaximumSize(budgetSize);
        budgetField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        budgetField.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton saveBudgetButton = new JButton("Save Budget");
        saveBudgetButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        saveBudgetButton.setBackground(new Color(34, 139, 34));
        saveBudgetButton.setForeground(Color.WHITE);
        saveBudgetButton.setFocusPainted(false);
        saveBudgetButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        budgetPanel.add(budgetLabel);
        budgetPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        budgetPanel.add(budgetField);
        budgetPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        budgetPanel.add(saveBudgetButton);

        budgetDialog.add(budgetPanel, BorderLayout.CENTER);

        formPanel.add(descLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0,8)));
        formPanel.add(descField);
        formPanel.add(Box.createRigidArea(new Dimension(0,15)));

        formPanel.add(amountLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0,8)));
        formPanel.add(amountField);
        formPanel.add(Box.createRigidArea(new Dimension(0,15)));

        formPanel.add(categoryLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0,8)));
        formPanel.add(categoryField);
        formPanel.add(Box.createRigidArea(new Dimension(0,25)));

        formPanel.add(addButton);
        formPanel.add(Box.createRigidArea(new Dimension(0,15)));
        formPanel.add(editButton);
        formPanel.add(Box.createRigidArea(new Dimension(0,15)));
        formPanel.add(deleteButton);

        expenseDialog.add(formPanel, BorderLayout.CENTER);
        
        toggleFormButton.addActionListener(e -> {
            editingRow[0] = -1;
            addButton.setText("Add Expense");

            descField.setText("");
            amountField.setText("");
            categoryField.setText("");

            expenseDialog.setTitle("Add Expense");
            expenseDialog.setVisible(true);
        });
        
        budgetButton.addActionListener(e -> {
            budgetField.setText("");
            budgetDialog.setVisible(true);
        });
        
        String[] columnNames = {"Description", "Amount", "Category", "Date"};
        tableModel = new DefaultTableModel(columnNames, 0);
        JTable expenseTable = new JTable(tableModel);
        expenseTable.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        expenseTable.setRowHeight(30);
        expenseTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));
        
        PieChartPanel pieChartPanel = new PieChartPanel(tableModel);

        pieChartPanel.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20),
                BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(Color.GRAY),
                    "Expenses by Category"
                )
            )
        );

        JScrollPane tableScroll = new JScrollPane(expenseTable);
        
        JLabel totalLabel = new JLabel("Total: ₱0.00");
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        totalLabel.setForeground(Color.BLACK);

        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(new Color(230, 240, 250));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 20));

        footerPanel.add(totalLabel, BorderLayout.EAST);

        JPanel tableWrapper = new JPanel(new BorderLayout());
        tableWrapper.setBackground(new Color(173, 216, 230));
        tableWrapper.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        tableWrapper.add(tableScroll, BorderLayout.CENTER);
        tableWrapper.add(footerPanel, BorderLayout.SOUTH);
        tableWrapper.setMinimumSize(new Dimension(1200, 250));
        tableWrapper.setPreferredSize(new Dimension(1200, 300));
        tableWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 400));

        centerContent.add(tableWrapper);
        centerContent.add(pieChartPanel);
        centerContent.add(Box.createRigidArea(new Dimension(0, 20)));

        JSpinner datePicker = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editor = new JSpinner.DateEditor(datePicker, "MMM dd, yyyy");
        datePicker.setEditor(editor);

        datePicker.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        datePicker.setPreferredSize(new Dimension(140, 30));
        
        datePicker.addChangeListener(e -> {
            String selectedDate = new SimpleDateFormat("MMM dd, yyyy").format(datePicker.getValue());

            java.util.List<Expense> filteredExpenses = allExpenses.stream()
                    .filter(expense -> expense.getDate().equals(selectedDate))
                    .collect(Collectors.toList());

            refreshTable(filteredExpenses);

            updateTotal(totalLabel);
            pieChartPanel.repaint();
        });

	     JPanel rightInfoPanel = new JPanel();
	     rightInfoPanel.setLayout(new BoxLayout(rightInfoPanel, BoxLayout.Y_AXIS));
	     rightInfoPanel.setBackground(new Color(173, 216, 230));
	     rightInfoPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 30));

	     JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	     datePanel.setBackground(new Color(173, 216, 230));
	     datePanel.add(datePicker);

	     budgetDisplayLabel = new JLabel("Budget: ₱0.00");
	     budgetDisplayLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
	     budgetDisplayLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
	     
	     remainingLabel = new JLabel("Remaining: ₱0.00");
	     remainingLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
	     remainingLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
	     
	     rightInfoPanel.add(datePanel);
	     rightInfoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
	     rightInfoPanel.add(budgetDisplayLabel);
	     rightInfoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
	     rightInfoPanel.add(remainingLabel);

	     topPanel.add(rightInfoPanel, BorderLayout.EAST);

	     loadBudget();         
	     loadExpensesToTable(); 
	     updateTotal(totalLabel); 
	     updateBudgetDisplay();  
	     
	     saveBudgetButton.addActionListener(e -> {
	    	    try {
	    	        double inputBudget = Double.parseDouble(budgetField.getText());
	    	        double totalExpenses = getTotalExpenses();

	    	        if (inputBudget < totalExpenses) {
	    	            JOptionPane.showMessageDialog(
	    	                budgetDialog,
	    	                "Cannot add budget.\nYour expenses already exceed this amount.",
	    	                "Budget Too Low",
	    	                JOptionPane.ERROR_MESSAGE
	    	            );
	    	            return;
	    	        }

	    	        monthlyBudget = inputBudget;
	    	        saveBudget(); 
	    	        updateBudgetDisplay();

	    	        JOptionPane.showMessageDialog(
	    	            budgetDialog,
	    	            "Budget saved successfully!",
	    	            "Success",
	    	            JOptionPane.INFORMATION_MESSAGE
	    	        );

	    	        budgetDialog.dispose();

	    	    } catch (NumberFormatException ex) {
	    	        JOptionPane.showMessageDialog(
	    	            budgetDialog,
	    	            "Please enter a valid number.",
	    	            "Invalid Input",
	    	            JOptionPane.ERROR_MESSAGE
	    	        );
	    	    }
	    	});

	     addButton.addActionListener(e -> {
	         String desc = descField.getText();
	         String amount = amountField.getText();
	         String category = categoryField.getText();
	         String date = new SimpleDateFormat("MMM dd, yyyy").format(datePicker.getValue());
	
	         if (editingRow[0] != -1) {
	             tableModel.setValueAt(desc, editingRow[0], 0);
	             tableModel.setValueAt(amount, editingRow[0], 1);
	             tableModel.setValueAt(category, editingRow[0], 2);
	             tableModel.setValueAt(date, editingRow[0], 3);
	
	             editingRow[0] = -1;
	             addButton.setText("Add Expense");
	         } else {
	             if (!desc.isEmpty() && !amount.isEmpty() && !category.isEmpty()) {
	                 tableModel.addRow(new Object[]{desc, amount, category, date});
	             } else {
	                 JOptionPane.showMessageDialog(this, "Please fill all fields.", "Warning", JOptionPane.WARNING_MESSAGE);
	                 return;
	             }
	         }
	
	         saveExpenses();
	
	         descField.setText("");
	         amountField.setText("");
	         categoryField.setText("");
	         
	         expenseDialog.dispose();
	         
	         updateTotal(totalLabel);
	         updateBudgetDisplay();
	         pieChartPanel.repaint();
	     });
	     
	     editButton.addActionListener(e -> {
	    	    int selectedRow = expenseTable.getSelectedRow();

	    	    if (selectedRow != -1) {
	    	        descField.setText(tableModel.getValueAt(selectedRow, 0).toString());
	    	        amountField.setText(tableModel.getValueAt(selectedRow, 1).toString());
	    	        categoryField.setText(tableModel.getValueAt(selectedRow, 2).toString());

	    	        editingRow[0] = selectedRow;
	    	        addButton.setText("Update Expense");

	    	        expenseDialog.setTitle("Edit Expense");
	    	        expenseDialog.setVisible(true);
	    	    } else {
	    	        JOptionPane.showMessageDialog(this,
	    	                "Please select a row to edit.",
	    	                "Warning",
	    	                JOptionPane.WARNING_MESSAGE);
	    	    }
	    	    
	    	    updateTotal(totalLabel);
	    	    updateBudgetDisplay();
	    	    pieChartPanel.repaint();
	    	});

	     deleteButton.addActionListener(e -> {
	         int selectedRow = expenseTable.getSelectedRow();
	
	         if (selectedRow != -1) {
	             int confirm = JOptionPane.showConfirmDialog(
	                     this,
	                     "Are you sure you want to delete the selected expense?",
	                     "Confirm Delete",
	                     JOptionPane.YES_NO_OPTION
	             );
	
	             if (confirm == JOptionPane.YES_OPTION) {
	            	    tableModel.removeRow(selectedRow);
	            	    
	            	    updateTotal(totalLabel);
	            	    updateBudgetDisplay(); 
	            	    pieChartPanel.repaint();
	            	    saveExpenses();
	
	                 if (editingRow[0] == selectedRow) {
	                     editingRow[0] = -1;
	                     addButton.setText("Add Expense");
	                     descField.setText("");
	                     amountField.setText("");
	                     categoryField.setText("");
	                 }
	             }
	         } else {
	             JOptionPane.showMessageDialog(this, "Please select a row to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
	         }
	     });

        add(topPanel, BorderLayout.NORTH);
        add(centerScrollPane, BorderLayout.CENTER);  
        
        setVisible(true);
    }
    
    
    private void saveExpenses() {
        try (Writer writer = new FileWriter("expenses.json")) {
            Gson gson = new Gson();
            java.util.List<Expense> expenses = new ArrayList<>();
            
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String desc = tableModel.getValueAt(i, 0).toString();
                String amount = tableModel.getValueAt(i, 1).toString();
                String category = tableModel.getValueAt(i, 2).toString();
                String date = tableModel.getValueAt(i, 3).toString();
                expenses.add(new Expense(desc, amount, category, date));
            }
            
            gson.toJson(expenses, writer);
            System.out.println("Expenses saved successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving expenses: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadExpensesToTable() {
        try (Reader reader = new FileReader("expenses.json")) {
            Gson gson = new Gson();
            java.lang.reflect.Type listType = new TypeToken<java.util.List<Expense>>(){}.getType();
            java.util.List<Expense> expenses = gson.fromJson(reader, listType);

            if (expenses != null) {
                allExpenses.clear();
                allExpenses.addAll(expenses);

                refreshTable(allExpenses);
                System.out.println("Loaded " + expenses.size() + " expenses");
            }
        } catch (FileNotFoundException e) {
            System.out.println("No existing expenses file found. Starting fresh.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void refreshTable(java.util.List<Expense> expensesToShow) {
        tableModel.setRowCount(0); 
        for (Expense expense : expensesToShow) {
            tableModel.addRow(new Object[]{
                expense.getDescription(),
                expense.getAmount(),
                expense.getCategory(),
                expense.getDate()
            });
        }
    }
    
    private void updateTotal(JLabel totalLabel) {
        double total = 0;

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            try {
                total += Double.parseDouble(
                    tableModel.getValueAt(i, 1).toString()
                );
            } catch (NumberFormatException ignored) {}
        }

        totalLabel.setText(String.format("Total: ₱%,.2f", total));
    }
    
    private double getTotalExpenses() {
        double total = 0;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            try {
                total += Double.parseDouble(
                    tableModel.getValueAt(i, 1).toString()
                );
            } catch (NumberFormatException ignored) {}
        }
        return total;
    }
    
    private void updateBudgetDisplay() {
        double totalExpenses = getTotalExpenses();
        double remaining = monthlyBudget - totalExpenses;

        budgetDisplayLabel.setText(String.format("Budget: ₱%,.2f", monthlyBudget));
        remainingLabel.setText(String.format("Remaining: ₱%,.2f", remaining));

        remainingLabel.setForeground(remaining < 0 ? Color.RED : new Color(0, 128, 0));
        
        if (remaining > 0 && remaining < 500) {
            JOptionPane.showMessageDialog(
                this,
                "Warning: Your remaining budget is below ₱500!\nRemaining: ₱" + String.format("%.2f", remaining),
                "Budget Warning",
                JOptionPane.WARNING_MESSAGE
            );
        }
    }
    
    private void saveBudget() {
        try (Writer writer = new FileWriter("budget.json")) {
            Gson gson = new Gson();
            gson.toJson(monthlyBudget, writer);
            System.out.println("Budget saved successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Error saving budget: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    private void loadBudget() {
        try (Reader reader = new FileReader("budget.json")) {
            Gson gson = new Gson();
            Double loadedBudget = gson.fromJson(reader, Double.class);
            if (loadedBudget != null) {
                monthlyBudget = loadedBudget;
            }
        } catch (FileNotFoundException e) {
            System.out.println("No budget file found. Starting fresh.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        updateBudgetDisplay(); 
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main());
    }
}