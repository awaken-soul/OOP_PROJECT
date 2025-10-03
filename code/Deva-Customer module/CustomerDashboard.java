package logistics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class CustomerDashboard extends JFrame {
    private JPanel rightPane;   // right side content area
    private User user;
    private ProductDAO productDAO = new ProductDAO();

    public CustomerDashboard(User user) {
        this.user = user;
        setTitle("Customer Dashboard");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setVisible(true);

        // ---- Left Pane ----
        JPanel leftPane = new JPanel();
        leftPane.setLayout(new BoxLayout(leftPane, BoxLayout.Y_AXIS));
        leftPane.setBackground(new Color(200, 255, 200));
        leftPane.setPreferredSize(new Dimension(150, getHeight()));

        JButton btnProducts = createMenuButton("Products");
        JButton btnActiveOrders = createMenuButton("Active Orders");
        JButton btnPendingPayments = createMenuButton("Pending Payments");
        JButton btnTrackDelivery = createMenuButton("Track Delivery");

        Dimension buttonSize = new Dimension(150, 35);
        btnProducts.setMaximumSize(buttonSize);
        btnActiveOrders.setMaximumSize(buttonSize);
        btnPendingPayments.setMaximumSize(buttonSize);
        btnTrackDelivery.setMaximumSize(buttonSize);

        btnProducts.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnActiveOrders.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnPendingPayments.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnTrackDelivery.setAlignmentX(Component.CENTER_ALIGNMENT);

        leftPane.add(Box.createVerticalStrut(20));
        leftPane.add(btnProducts);
        leftPane.add(Box.createVerticalStrut(15));
        leftPane.add(btnActiveOrders);
        leftPane.add(Box.createVerticalStrut(15));
        leftPane.add(btnPendingPayments);
        leftPane.add(Box.createVerticalStrut(15));
        leftPane.add(btnTrackDelivery);

        rightPane = new JPanel();
        rightPane.setBackground(new Color(200, 255, 200));
        rightPane.setLayout(new BorderLayout());

        JLabel defaultLabel = new JLabel("Welcome! Select an option from the left.", SwingConstants.CENTER);
        defaultLabel.setVerticalAlignment(SwingConstants.TOP);
        defaultLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        defaultLabel.setForeground(Color.BLACK);
        rightPane.add(defaultLabel, BorderLayout.CENTER);

        add(leftPane, BorderLayout.WEST);
        add(rightPane, BorderLayout.CENTER);

        btnProducts.addActionListener((ActionEvent e) -> showProducts());
        btnActiveOrders.addActionListener((ActionEvent e) -> showActiveOrders(user.getUserId()));
        btnPendingPayments.addActionListener((ActionEvent e) -> showPendingPayments());
        btnTrackDelivery.addActionListener((ActionEvent e) -> showTrackDelivery());
    }

    // Create styled menu button
    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        Color normalColor = new Color(255, 102, 178);
        Color hoverColor = new Color(204, 0, 102);

        button.setBackground(normalColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(normalColor);
            }
        });
        return button;
    }

    // ---- Show Products  ----
    private void showProducts() {
        rightPane.removeAll();

        String[] columns = {"Product ID", "Name", "Description", "Price", "Quantity"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        // Fetch products via DAO
        List<Product> products = productDAO.getAllProducts();
        for (Product p : products) {
            model.addRow(new Object[]{
                    p.getProductId(),
                    p.getName(),
                    p.getDescription(),
                    p.getPrice(),
                    p.getQuantity()
            });
        }

        JTable table = new JTable(model);
        table.setFillsViewportHeight(true);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setForeground(Color.BLACK);
        table.setBackground(Color.WHITE);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(table);
        rightPane.add(scrollPane, BorderLayout.CENTER);

        // ---- Place Order button ----
        JButton placeOrderBtn = new JButton("Place Order");
        placeOrderBtn.setFont(new Font("Arial", Font.BOLD, 14));
        placeOrderBtn.setBackground(new Color(255, 105, 180));
        placeOrderBtn.setForeground(Color.WHITE);

        placeOrderBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a product before placing an order.",
                        "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int productId = (int) table.getValueAt(selectedRow, 0);
            String productName = table.getValueAt(selectedRow, 1).toString();

            int availableQty = productDAO.getAvailableQuantity(productId);

            String qtyStr = JOptionPane.showInputDialog(this,
                    "Enter quantity for " + productName + " (Available: " + availableQty + "):",
                    "Order Quantity", JOptionPane.QUESTION_MESSAGE);
            if (qtyStr == null || qtyStr.trim().isEmpty()) return;

            int qty;
            try {
                qty = Integer.parseInt(qtyStr.trim());
                if (qty <= 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid quantity entered.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (qty > availableQty) {
                JOptionPane.showMessageDialog(this,
                        "Insufficient stock. Available: " + availableQty,
                        "Stock Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Multi-step order placement logic stays in UI
            String[] orderTypes = {"Purchase", "Shipment", "Transport"};
            String orderType = (String) JOptionPane.showInputDialog(
                    this, "Select order type:", "Order Type",
                    JOptionPane.QUESTION_MESSAGE, null, orderTypes, orderTypes[0]);
            if (orderType == null) return;

            String sourceAddress, destinationAddress;
            if ("Purchase".equals(orderType)) {
                sourceAddress = "Warehouse";
                destinationAddress = JOptionPane.showInputDialog(this,
                        "Enter destination address:", "Destination Address", JOptionPane.QUESTION_MESSAGE);
                if (destinationAddress == null || destinationAddress.trim().isEmpty()) return;
            } else {
                sourceAddress = JOptionPane.showInputDialog(this,
                        "Enter source address:", "Source Address", JOptionPane.QUESTION_MESSAGE);
                if (sourceAddress == null || sourceAddress.trim().isEmpty()) return;

                destinationAddress = JOptionPane.showInputDialog(this,
                        "Enter destination address:", "Destination Address", JOptionPane.QUESTION_MESSAGE);
                if (destinationAddress == null || destinationAddress.trim().isEmpty()) return;
            }

            // Call DAO instead of embedding SQL
            boolean success = productDAO.placeOrderWithPayment(
                    user.getUserId(), productId, qty, orderType, sourceAddress, destinationAddress);

            if (success) {
                table.setValueAt(availableQty - qty, selectedRow, 4);
                JOptionPane.showMessageDialog(this,
                        "Order placed!\n\nProduct: " + productName +
                                "\nQuantity: " + qty +
                                "\nOrder Type: " + orderType +
                                "\nFrom: " + sourceAddress +
                                "\nTo: " + destinationAddress +
                                "\nPayment status: Pending",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Error placing order. Please try again.",
                        "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(placeOrderBtn);
        rightPane.add(buttonPanel, BorderLayout.SOUTH);

        refreshRightPane();
    }

 
    // ---- Active Orders ----
    private void showActiveOrders(int userId) {
        rightPane.removeAll();
        String[] columns = {"Order ID", "Product ID", "Order Type", "Source Address", "Destination Address", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        OrderDAO orderDAO = new OrderDAO();
        for (Order o : orderDAO.getActiveOrders(userId)) {
            Object[] row = {
                    o.getOrderId(),
                    o.getProductId(), // We can replace with product name if needed via JOIN
                    o.getOrderType(),
                    o.getSourceAddress(),
                    o.getDestinationAddress(),
                    o.getStatus()
            };
            model.addRow(row);
        }

        JTable table = new JTable(model);
        rightPane.add(new JScrollPane(table), BorderLayout.CENTER);
        refreshRightPane();
    }

    // ---- Pending Payments ----
    private void showPendingPayments() {
        rightPane.removeAll();
        String[] columns = {"Payment ID", "Order ID", "Amount", "Method", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        PaymentDAO paymentDAO = new PaymentDAO();
        for (Payment p : paymentDAO.getPendingPayments(user.getUserId())) {
            Object[] row = {
                    p.getPaymentId(),
                    p.getOrderId(),
                    p.getAmount(),
                    p.getMethod() != null ? p.getMethod() : "N/A",
                    p.getStatus()
            };
            model.addRow(row);
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        rightPane.add(scrollPane, BorderLayout.CENTER);

        JButton payButton = new JButton("Make Payment");
        payButton.setFont(new Font("Arial", Font.BOLD, 14));
        payButton.setBackground(new Color(0, 153, 76));
        payButton.setForeground(Color.WHITE);

        payButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a payment to process.",
                        "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int paymentId = (int) model.getValueAt(selectedRow, 0);
            int orderId = (int) model.getValueAt(selectedRow, 1);

            String[] methods = {"Card", "UPI", "Cash"};
            String method = (String) JOptionPane.showInputDialog(
                    this, "Select Payment Method:", "Payment Method",
                    JOptionPane.QUESTION_MESSAGE, null, methods, methods[0]);
            if (method == null) return;

            if (paymentDAO.completePayment(paymentId, method)) {
                JOptionPane.showMessageDialog(this,
                        "Payment successful!\nMethod: " + method +
                                "\nOrder " + orderId + " is now Ready for shipment.",
                        "Payment Completed", JOptionPane.INFORMATION_MESSAGE);
                showPendingPayments();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Error processing payment.",
                        "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(payButton);
        rightPane.add(buttonPanel, BorderLayout.SOUTH);

        refreshRightPane();
    }

    // ---- Track Delivery ----
    private void showTrackDelivery() {
        rightPane.removeAll();

        String[] columns = {"Order ID", "Product", "Order Status", "Current Status", "Last Location", "Updated At"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        TrackingDAO trackingDAO = new TrackingDAO();
        for (Tracking t : trackingDAO.getActiveTracking(user.getUserId())) {
            Object[] row = {
                    t.getOrderId(),
                    t.getProductName(),
                    t.getOrderStatus(),
                    t.getCurrentStatus(),
                    t.getLocation(),
                    (t.getUpdatedAt() != null) ? t.getUpdatedAt().toString() : "N/A"
            };
            model.addRow(row);
        }

        JTable table = new JTable(model);
        table.setFillsViewportHeight(true);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setForeground(Color.BLACK);
        table.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(table);
        rightPane.add(scrollPane, BorderLayout.CENTER);

        refreshRightPane();
    }

    private void refreshRightPane() {
        rightPane.revalidate();
        rightPane.repaint();
    }
}
