import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class AppUI extends JFrame {

    // --- Data Models ---
    private final ArrayList<Product> products = new ArrayList<>();
    private final ArrayList<Agent> agents = new ArrayList<>();
    private final ArrayList<Order> orders = new ArrayList<>();
    private int orderCounter = 1000;

    // --- UI Components ---
    private DefaultTableModel productTableModel;
    private JTextField productIdField, productNameField, productQtyField;
    private JButton addProductButton;
    private DefaultTableModel agentTableModel;
    private JTextField agentIdField, agentNameField;
    private JButton addAgentButton;
    private DefaultTableModel orderTableModel;
    private JTable orderTable;
    private JComboBox<Product> productDropdown;
    private JComboBox<Agent> agentDropdown;
    private JTextField orderQtyField;
    private JButton createOrderButton, dispatchButton, deliverButton;
    private JTextArea reportArea;
    private JButton generateReportButton;


    public AppUI() {
        setTitle("Warehouse Manager");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Inventory", createInventoryPanel());
        tabbedPane.addTab("Agents", createAgentsPanel());
        tabbedPane.addTab("Orders", createOrdersPanel());
        tabbedPane.addTab("Reports", createReportsPanel());

        initActionListeners();

        add(tabbedPane);
    }

    private JPanel createInventoryPanel() {
        JPanel inventoryPanel = new JPanel(new BorderLayout(10, 10));
        JPanel formPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        productIdField = new JTextField(5);
        productNameField = new JTextField(10);
        productQtyField = new JTextField(5);
        addProductButton = new JButton("Add Product");
        formPanel.add(new JLabel("ID:"));
        formPanel.add(productIdField);
        formPanel.add(new JLabel("Name:"));
        formPanel.add(productNameField);
        formPanel.add(new JLabel("Quantity:"));
        formPanel.add(productQtyField);
        formPanel.add(addProductButton);

        String[] columnNames = {"Product ID", "Name", "Quantity"};
        productTableModel = new DefaultTableModel(columnNames, 0);
        JTable productTable = new JTable(productTableModel);

        inventoryPanel.add(formPanel, BorderLayout.NORTH);
        inventoryPanel.add(new JScrollPane(productTable), BorderLayout.CENTER);
        return inventoryPanel;
    }

    private JPanel createAgentsPanel() {
        JPanel agentPanel = new JPanel(new BorderLayout(10, 10));
        agentTableModel = new DefaultTableModel(new String[]{"ID", "Name"}, 0);
        JTable agentTable = new JTable(agentTableModel);
        agentPanel.add(new JScrollPane(agentTable), BorderLayout.CENTER);

        JPanel agentForm = new JPanel(new FlowLayout(FlowLayout.LEFT));
        agentIdField = new JTextField(5);
        agentNameField = new JTextField(10);
        addAgentButton = new JButton("Add Agent");
        agentForm.add(new JLabel("ID:"));
        agentForm.add(agentIdField);
        agentForm.add(new JLabel("Name:"));
        agentForm.add(agentNameField);
        agentForm.add(addAgentButton);

        agentPanel.add(agentForm, BorderLayout.SOUTH);
        return agentPanel;
    }

    private JPanel createOrdersPanel() {
        JPanel ordersPanel = new JPanel(new BorderLayout(10, 10));
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        dispatchButton = new JButton("Dispatch Selected Order");
        deliverButton = new JButton("Deliver Selected Order");
        statusPanel.add(dispatchButton);
        statusPanel.add(deliverButton);

        orderTableModel = new DefaultTableModel(new String[]{"Order ID", "Product", "Agent", "Quantity", "Status"}, 0);
        orderTable = new JTable(orderTableModel);
        ordersPanel.add(new JScrollPane(orderTable), BorderLayout.CENTER);

        JPanel orderForm = new JPanel(new FlowLayout(FlowLayout.LEFT));
        productDropdown = new JComboBox<>();
        agentDropdown = new JComboBox<>();
        orderQtyField = new JTextField(5);
        createOrderButton = new JButton("Create Order");
        orderForm.add(new JLabel("Product:"));
        orderForm.add(productDropdown);
        orderForm.add(new JLabel("Agent:"));
        orderForm.add(agentDropdown);
        orderForm.add(new JLabel("Qty:"));
        orderForm.add(orderQtyField);
        orderForm.add(createOrderButton);

        ordersPanel.add(statusPanel, BorderLayout.NORTH);
        ordersPanel.add(orderForm, BorderLayout.SOUTH);
        return ordersPanel;
    }

    private JPanel createReportsPanel() {
        JPanel reportsPanel = new JPanel(new BorderLayout(10, 10));
        reportsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel reportTitle = new JLabel("Warehouse Reports");
        reportTitle.setFont(reportTitle.getFont().deriveFont(18f));
        reportTitle.setHorizontalAlignment(SwingConstants.CENTER);
        reportArea = new JTextArea(15, 40);
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        generateReportButton = new JButton("Generate Report");
        reportsPanel.add(reportTitle, BorderLayout.NORTH);
        reportsPanel.add(new JScrollPane(reportArea), BorderLayout.CENTER);
        reportsPanel.add(generateReportButton, BorderLayout.SOUTH);
        return reportsPanel;
    }

    private void initActionListeners() {
        addProductButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(productIdField.getText());
                String name = productNameField.getText().trim();
                int qty = Integer.parseInt(productQtyField.getText());

                // *** NEW FEATURE: Check for duplicate Product ID ***
                for (Product p : products) {
                    if (p.getId() == id) {
                        JOptionPane.showMessageDialog(this, "Product ID " + id + " already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                        return; // Stop execution
                    }
                }

                if (!name.isEmpty() && qty >= 0) {
                    Product newProduct = new Product(id, name, qty);
                    products.add(newProduct);
                    productTableModel.addRow(new Object[]{id, name, qty});
                    productDropdown.addItem(newProduct);

                    productIdField.setText("");
                    productNameField.setText("");
                    productQtyField.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Product name cannot be empty and quantity cannot be negative.", "Input Error", JOptionPane.WARNING_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numbers for ID and Quantity!", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        addAgentButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(agentIdField.getText());
                String name = agentNameField.getText().trim();

                // *** NEW FEATURE: Check for duplicate Agent ID ***
                for (Agent a : agents) {
                    if (a.getId() == id) {
                        JOptionPane.showMessageDialog(this, "Agent ID " + id + " already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                        return; // Stop execution
                    }
                }

                if (!name.isEmpty()) {
                    Agent newAgent = new Agent(id, name);
                    agents.add(newAgent);
                    agentTableModel.addRow(new Object[]{id, name});
                    agentDropdown.addItem(newAgent);

                    agentIdField.setText("");
                    agentNameField.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Agent name cannot be empty!", "Input Error", JOptionPane.WARNING_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid ID for the agent!", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        createOrderButton.addActionListener(e -> {
            try {
                Product selectedProduct = (Product) productDropdown.getSelectedItem();
                Agent selectedAgent = (Agent) agentDropdown.getSelectedItem();

                if (selectedProduct == null || selectedAgent == null) {
                    JOptionPane.showMessageDialog(this, "Please create and select a product and an agent first!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                int qtyToOrder = Integer.parseInt(orderQtyField.getText());

                if (qtyToOrder <= 0) {
                    JOptionPane.showMessageDialog(this, "Quantity must be a positive number.", "Input Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (selectedProduct.getQuantity() < qtyToOrder) {
                    JOptionPane.showMessageDialog(this, "Not enough stock! Available quantity: " + selectedProduct.getQuantity(), "Stock Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                orderCounter++;
                Order newOrder = new Order(orderCounter, selectedProduct, selectedAgent, qtyToOrder);
                orders.add(newOrder);
                orderTableModel.addRow(new Object[]{newOrder.getOrderId(), selectedProduct, selectedAgent, qtyToOrder, newOrder.getStatus()});

                int newQuantity = selectedProduct.getQuantity() - qtyToOrder;
                selectedProduct.setQuantity(newQuantity);

                for (int i = 0; i < productTableModel.getRowCount(); i++) {
                    if (productTableModel.getValueAt(i, 0).equals(selectedProduct.getId())) {
                        productTableModel.setValueAt(newQuantity, i, 2);
                        break;
                    }
                }
                orderQtyField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number for quantity!", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dispatchButton.addActionListener(e -> {
            int selectedRow = orderTable.getSelectedRow();
            if (selectedRow >= 0) {
                // To be safe, convert view row to model row in case of sorting/filtering
                int modelRow = orderTable.convertRowIndexToModel(selectedRow);
                Order selectedOrder = orders.get(modelRow);
                selectedOrder.dispatch();
                orderTableModel.setValueAt(selectedOrder.getStatus(), modelRow, 4);
            } else {
                JOptionPane.showMessageDialog(this, "Please select an order from the table to dispatch!", "Selection Required", JOptionPane.WARNING_MESSAGE);
            }
        });

        deliverButton.addActionListener(e -> {
            int selectedRow = orderTable.getSelectedRow();
            if (selectedRow >= 0) {
                int modelRow = orderTable.convertRowIndexToModel(selectedRow);
                Order selectedOrder = orders.get(modelRow);
                selectedOrder.deliver();
                orderTableModel.setValueAt(selectedOrder.getStatus(), modelRow, 4);
            } else {
                JOptionPane.showMessageDialog(this, "Please select an order from the table to deliver!", "Selection Required", JOptionPane.WARNING_MESSAGE);
            }
        });

        generateReportButton.addActionListener(e -> {
            long deliveredCount = orders.stream().filter(o -> "Delivered".equals(o.getStatus())).count();
            long dispatchedCount = orders.stream().filter(o -> "Dispatched".equals(o.getStatus())).count();
            long pendingCount = orders.stream().filter(o -> "Pending".equals(o.getStatus())).count();

            String report = "📦 WAREHOUSE REPORT (" + java.time.format.DateTimeFormatter.ofPattern("dd-MMM-yyyy").format(java.time.LocalDate.now()) + ")\n"
                    + "===================================\n\n"
                    + "INVENTORY SUMMARY:\n"
                    + " - Total Unique Products: " + products.size() + "\n\n"
                    + "OPERATIONS SUMMARY:\n"
                    + " - Total Agents: " + agents.size() + "\n"
                    + " - Total Orders: " + orders.size() + "\n\n"
                    + "ORDER STATUS:\n"
                    + " - Pending:    " + pendingCount + "\n"
                    + " - Dispatched: " + dispatchedCount + "\n"
                    + " - Delivered:  " + deliveredCount + "\n";
            reportArea.setText(report);
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AppUI().setVisible(true));
    }
}