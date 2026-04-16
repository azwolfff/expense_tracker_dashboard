import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class ExpenseTracker extends JFrame {

    private ArrayList<String[]> allExpensesData;

    private double overallTotalAmount = 0.0;

    private JTable expenseTable;
    private DefaultTableModel tableModel;

    private JLabel totalExpenseLabel;
    private JLabel monthlyExpenseLabel;
    private JLabel entryCountLabel;

    private JTextField nameInputField;
    private JTextField amountInputField;

    private JTextField dateInputField;
    private JTextField searchInputField;

    private JComboBox<String> categoryDropdown;
    private JComboBox<String> monthDropdown;

    private final Color APP_BG_COLOR = new Color(248, 249, 250);
    private final Color HEADER_BG_COLOR = new Color(44, 62, 80);

    private final Color PANEL_BG_COLOR = Color.WHITE;
    private final Color TEXT_DARK_COLOR = new Color(33, 37, 41);

    private final Color BTN_ADD_GREEN = new Color(40, 167, 69);
    private final Color BTN_DELETE_RED = new Color(220, 53, 69);
    private final Color BTN_CLEAR_GRAY = new Color(108, 117, 125);

    private final Color BTN_SEARCH_BLUE = new Color(23, 162, 184);
    private final Color BTN_DASHBOARD_BLUE = new Color(0, 123, 255);

    private final Font FONT_REGULAR = new Font("Segoe UI", Font.PLAIN, 15);
    private final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 15);

    public ExpenseTracker() {
        initializeBasicData();
        configureMainFrame();
        buildApplicationInterface();
    }

    private void initializeBasicData() {
        allExpensesData = new ArrayList<>();
    }

    private void configureMainFrame() {
        setTitle("Expense Tracker Dashboard Pro");
        setSize(900, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(APP_BG_COLOR);
    }

    private void buildApplicationInterface() {
        setLayout(new BorderLayout(15, 15));

        JPanel topRegionPanel = new JPanel(new BorderLayout());
        topRegionPanel.add(createHeaderPanel(), BorderLayout.NORTH);

        topRegionPanel.add(createDashboardSummaryPanel(), BorderLayout.CENTER);

        topRegionPanel.add(createSearchPanel(), BorderLayout.SOUTH);

        add(topRegionPanel, BorderLayout.NORTH);

        add(createTablePanel(), BorderLayout.CENTER);

        JPanel bottomRegionPanel = new JPanel(new BorderLayout(0, 15));
        bottomRegionPanel.setBackground(APP_BG_COLOR);
        bottomRegionPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        bottomRegionPanel.add(createInputFormPanel(), BorderLayout.NORTH);

        JPanel controlsRegion = new JPanel(new BorderLayout(0, 15));
        controlsRegion.setBackground(APP_BG_COLOR);
        controlsRegion.add(createMainActionPanel(), BorderLayout.NORTH);

        controlsRegion.add(createMonthDashboardPanel(), BorderLayout.SOUTH);

        bottomRegionPanel.add(controlsRegion, BorderLayout.CENTER);

        add(bottomRegionPanel, BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(HEADER_BG_COLOR);

        JLabel headerLabel = new JLabel("Expense Tracker Dashboard", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));

        headerLabel.setForeground(Color.WHITE);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        headerPanel.add(headerLabel, BorderLayout.CENTER);
        return headerPanel;
    }

    private JPanel createDashboardSummaryPanel() {
        JPanel summaryPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        summaryPanel.setBackground(PANEL_BG_COLOR);
        summaryPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        totalExpenseLabel = new JLabel("Total Expense: ₹0.00", SwingConstants.CENTER);
        totalExpenseLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        totalExpenseLabel.setForeground(TEXT_DARK_COLOR);

        monthlyExpenseLabel = new JLabel("Monthly Expense: Pending", SwingConstants.CENTER);
        monthlyExpenseLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        monthlyExpenseLabel.setForeground(BTN_DASHBOARD_BLUE);

        entryCountLabel = new JLabel("Entries: 0", SwingConstants.CENTER);
        entryCountLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        entryCountLabel.setForeground(BTN_ADD_GREEN);

        summaryPanel.add(totalExpenseLabel);
        summaryPanel.add(monthlyExpenseLabel);

        summaryPanel.add(entryCountLabel);

        return summaryPanel;
    }

    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        searchPanel.setBackground(APP_BG_COLOR);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        JLabel searchLabel = new JLabel("Search by Name:");
        searchLabel.setFont(FONT_BOLD);
        searchLabel.setForeground(TEXT_DARK_COLOR);

        searchInputField = new JTextField(20);
        searchInputField.setFont(FONT_REGULAR);

        JButton performSearchBtn = createBaseButton("Filter", BTN_SEARCH_BLUE);
        performSearchBtn.setPreferredSize(new Dimension(100, 35));
        performSearchBtn.addActionListener(e -> handleSearchFilterEvent());

        JButton clearSearchBtn = createBaseButton("Reset", BTN_CLEAR_GRAY);
        clearSearchBtn.setPreferredSize(new Dimension(100, 35));
        clearSearchBtn.addActionListener(e -> handleSearchResetEvent());

        searchPanel.add(searchLabel);
        searchPanel.add(searchInputField);

        searchPanel.add(performSearchBtn);
        searchPanel.add(clearSearchBtn);

        return searchPanel;
    }

    private JPanel createTablePanel() {
        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setBackground(APP_BG_COLOR);

        wrapperPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        String[] tableColumns = {"Expense Name", "Amount (₹)", "Date", "Category"};

        tableModel = new DefaultTableModel(tableColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        expenseTable = new JTable(tableModel);
        expenseTable.setFont(FONT_REGULAR);

        expenseTable.setRowHeight(40);
        expenseTable.setBackground(PANEL_BG_COLOR);

        expenseTable.setForeground(TEXT_DARK_COLOR);

        expenseTable.setSelectionBackground(new Color(220, 235, 250));

        expenseTable.getTableHeader().setFont(FONT_BOLD);
        expenseTable.getTableHeader().setBackground(HEADER_BG_COLOR);

        expenseTable.getTableHeader().setForeground(Color.WHITE);

        expenseTable.getTableHeader().setPreferredSize(new Dimension(0, 45));

        DefaultTableCellRenderer centerAligntment = new DefaultTableCellRenderer();
        centerAligntment.setHorizontalAlignment(JLabel.CENTER);

        for (int i = 0; i < expenseTable.getColumnCount(); i++) {
            expenseTable.getColumnModel().getColumn(i).setCellRenderer(centerAligntment);
        }

        JScrollPane tableScroller = new JScrollPane(expenseTable);
        tableScroller.getViewport().setBackground(APP_BG_COLOR);

        wrapperPanel.add(tableScroller, BorderLayout.CENTER);
        return wrapperPanel;
    }

    private JPanel createInputFormPanel() {
        JPanel inputGridPanel = new JPanel(new GridLayout(2, 4, 15, 10));
        inputGridPanel.setBackground(APP_BG_COLOR);

        inputGridPanel.add(createBoldLabel("Expense Name:"));
        nameInputField = new JTextField();

        nameInputField.setFont(FONT_REGULAR);
        inputGridPanel.add(nameInputField);

        inputGridPanel.add(createBoldLabel("Amount (₹):"));
        amountInputField = new JTextField();
        amountInputField.setFont(FONT_REGULAR);

        inputGridPanel.add(amountInputField);

        inputGridPanel.add(createBoldLabel("Date (YYYY-MM-DD):"));
        dateInputField = new JTextField();
        dateInputField.setFont(FONT_REGULAR);

        inputGridPanel.add(dateInputField);

        inputGridPanel.add(createBoldLabel("Category:"));
        String[] categoriesList = {"Food", "Travel", "Bills", "Others"};

        categoryDropdown = new JComboBox<>(categoriesList);
        categoryDropdown.setFont(FONT_REGULAR);
        inputGridPanel.add(categoryDropdown);

        return inputGridPanel;
    }

    private JPanel createMainActionPanel() {

        JPanel actionContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        actionContainer.setBackground(APP_BG_COLOR);

        JButton addBtn = createBaseButton("Add Expense", BTN_ADD_GREEN);
        addBtn.addActionListener(e -> handleAddExpenseEvent());

        JButton delBtn = createBaseButton("Delete Selected", BTN_DELETE_RED);
        delBtn.addActionListener(e -> handleDeleteExpenseEvent());

        JButton clearBtn = createBaseButton("Clear All", BTN_CLEAR_GRAY);
        clearBtn.addActionListener(e -> handleClearAllEvent());

        actionContainer.add(addBtn);
        actionContainer.add(delBtn);

        actionContainer.add(clearBtn);

        return actionContainer;
    }

    private JPanel createMonthDashboardPanel() {

        JPanel monthContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        monthContainer.setBackground(APP_BG_COLOR);

        monthContainer.add(createBoldLabel("Select Month for Report:"));

        String[] standardMonths = {
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        };
        monthDropdown = new JComboBox<>(standardMonths);
        monthDropdown.setFont(FONT_REGULAR);

        monthContainer.add(monthDropdown);

        JButton calcMonthBtn = createBaseButton("Show Monthly Total", BTN_DASHBOARD_BLUE);
        calcMonthBtn.setPreferredSize(new Dimension(200, 40));

        calcMonthBtn.addActionListener(e -> handleCalculateMonthlyTotalEvent());

        monthContainer.add(calcMonthBtn);
        return monthContainer;
    }

    private JLabel createBoldLabel(String labelText) {

        JLabel label = new JLabel(labelText);
        label.setFont(FONT_BOLD);
        label.setForeground(TEXT_DARK_COLOR);
        return label;
    }

    private JButton createBaseButton(String title, Color bgColor) {

        JButton newButton = new JButton(title);
        newButton.setFont(FONT_BOLD);

        newButton.setBackground(bgColor);

        newButton.setForeground(Color.WHITE);
        newButton.setFocusPainted(false);
        newButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        newButton.setPreferredSize(new Dimension(150, 45));
        return newButton;
    }

    private void handleAddExpenseEvent() {
        String nameStr = nameInputField.getText().trim();
        String amountStr = amountInputField.getText().trim();

        String dateStr = dateInputField.getText().trim();
        String categoryStr = (String) categoryDropdown.getSelectedItem();

        if (!validateNonEmptyFields(nameStr, amountStr, dateStr)) {
            showErrorPopup("Please fill in completely all the required input fields.");
            return;
        }

        if (!validateStrictDateFormat(dateStr)) {
            showErrorPopup("Format is invalid! Format must strictly match YYYY-MM-DD.");
            return;
        }

        double cleanAmount = validateAndParseNumericalAmount(amountStr);
        if (cleanAmount <= 0) {
            showErrorPopup("The amount value must be a positive numerical input.");
            return;
        }

        String nicelyFormattedAmount = String.format("%.2f", cleanAmount);

        String[] completeExpenseEntry = {nameStr, nicelyFormattedAmount, dateStr, categoryStr};

        allExpensesData.add(completeExpenseEntry);

        refreshVisualTableAndSummaries();
        wipeInputBoxContents();

        monthlyExpenseLabel.setText("Monthly Expense: Pending refresh...");
    }

    private void handleDeleteExpenseEvent() {
        String currentlySearching = searchInputField.getText().trim();
        if (!currentlySearching.isEmpty()) {

            showWarningPopup("Please clear the search filter before attempting to delete securely.");
            return;
        }

        int highlightIndex = expenseTable.getSelectedRow();
        if (highlightIndex == -1) {
            showWarningPopup("You must select an existing row from the visible table.");
            return;
        }

        allExpensesData.remove(highlightIndex);
        refreshVisualTableAndSummaries();
        monthlyExpenseLabel.setText("Monthly Expense: Pending refresh...");
    }

    private void handleClearAllEvent() {
        if (allExpensesData.isEmpty()) {
            return;
        }

        int userChoice = JOptionPane.showConfirmDialog(
                this,
                "Are you absolutely certain you want to purge all active data?",
                "Clear Data Warning",
                JOptionPane.YES_NO_OPTION
        );

        if (userChoice == JOptionPane.YES_OPTION) {
            allExpensesData.clear();
            refreshVisualTableAndSummaries();
            monthlyExpenseLabel.setText("Monthly Expense: Cleared");
        }
    }

    private void handleSearchFilterEvent() {
        String userInputQuery = searchInputField.getText().trim().toLowerCase();

        if (userInputQuery.isEmpty()) {
            refreshVisualTableAndSummaries();
            return;
        }

        tableModel.setRowCount(0);

        for (String[] individualRecord : allExpensesData) {

            String recordName = individualRecord[0].toLowerCase();

            if (recordName.contains(userInputQuery)) {
                tableModel.addRow(individualRecord);
            }
        }
    }

    private void handleSearchResetEvent() {
        searchInputField.setText("");
        refreshVisualTableAndSummaries();
    }

    private void handleCalculateMonthlyTotalEvent() {
        int dropdownIndexLocation = monthDropdown.getSelectedIndex();
        String literalMonthName = monthDropdown.getItemAt(dropdownIndexLocation);

        String numericalMonthTarget = String.format("%02d", dropdownIndexLocation + 1);

        double accumulatedSum = 0.0;

        for (String[] documentObject : allExpensesData) {
            double documentedAmount = Double.parseDouble(documentObject[1]);
            String documentedDate = documentObject[2];

            String[] dateSegments = documentedDate.split("-");

            if (dateSegments.length == 3) {
                String extractedMonthSegment = dateSegments[1];

                if (extractedMonthSegment.equals(numericalMonthTarget)) {
                    accumulatedSum += documentedAmount;
                }
            }
        }

        String formattingResult = String.format("%.2f", accumulatedSum);
        monthlyExpenseLabel.setText("Monthly (" + literalMonthName + "): ₹" + formattingResult);
    }

    private boolean validateNonEmptyFields(String... fields) {
        for (String f : fields) {
            if (f == null || f.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private boolean validateStrictDateFormat(String dateRawFormat) {
        return dateRawFormat.matches("\\d{4}-\\d{2}-\\d{2}");
    }

    private double validateAndParseNumericalAmount(String monetaryInput) {
        try {
            return Double.parseDouble(monetaryInput);
        } catch (NumberFormatException e) {
            return -1.0;
        }
    }

    private void refreshVisualTableAndSummaries() {
        tableModel.setRowCount(0);

        double liveTotalAmountTracking = 0.0;

        for (String[] activeRow : allExpensesData) {
            tableModel.addRow(activeRow);
            liveTotalAmountTracking += Double.parseDouble(activeRow[1]);
        }

        overallTotalAmount = liveTotalAmountTracking;

        updateDashboardSummaryMetrics();
    }

    private void updateDashboardSummaryMetrics() {
        totalExpenseLabel.setText("Total Expense: ₹" + String.format("%.2f", overallTotalAmount));
        entryCountLabel.setText("Entries: " + allExpensesData.size());
    }

    private void wipeInputBoxContents() {
        nameInputField.setText("");
        amountInputField.setText("");
        dateInputField.setText("");
    }

    private void showErrorPopup(String errorContent) {
        JOptionPane.showMessageDialog(this, errorContent, "Validation Error Details", JOptionPane.ERROR_MESSAGE);
    }

    private void showWarningPopup(String warningContent) {
        JOptionPane.showMessageDialog(this, warningContent, "System Process Blocked", JOptionPane.WARNING_MESSAGE);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception runtimeLookError) {
            runtimeLookError.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {

            ExpenseTracker trackerSoftware = new ExpenseTracker();
            trackerSoftware.setVisible(true);
        });
    }
}
