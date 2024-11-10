import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class BillingSoftware {
    private JFrame frame;
    private JTextArea billArea;
    private JTextField customerNameField, customerPhoneField, productNameField, productRateField, productQuantityField;
    private JButton printButton;
    private ArrayList<Item> items = new ArrayList<>();
    private String billNo;

    public BillingSoftware() {
        frame = new JFrame("Billing Software");
        frame.setSize(1280, 720);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        
        billNo = String.valueOf(new Random().nextInt(9000) + 1000);

        JLabel title = new JLabel("Billing Software");
        title.setFont(new Font("Arial", Font.BOLD, 30));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBounds(0, 20, 1280, 40);
        title.setBackground(Color.RED);
        title.setForeground(Color.WHITE);
        title.setOpaque(true);
        frame.add(title);

        // Customer Details
        JPanel customerPanel = new JPanel();
        customerPanel.setBorder(BorderFactory.createTitledBorder("Customer Details"));
        customerPanel.setBounds(10, 80, 1250, 100);
        customerPanel.setLayout(new GridLayout(2, 4, 10, 10));
        frame.add(customerPanel);

        JLabel customerNameLabel = new JLabel("Customer Name:");
        customerPanel.add(customerNameLabel);
        customerNameField = new JTextField();
        customerPanel.add(customerNameField);

        JLabel customerPhoneLabel = new JLabel("Phone Number:");
        customerPanel.add(customerPhoneLabel);
        customerPhoneField = new JTextField();
        customerPanel.add(customerPhoneField);

        // Product Details
        JPanel productPanel = new JPanel();
        productPanel.setBorder(BorderFactory.createTitledBorder("Product Details"));
        productPanel.setBounds(10, 200, 630, 400);
        productPanel.setLayout(new GridLayout(4, 2, 10, 10));
        frame.add(productPanel);

        JLabel productNameLabel = new JLabel("Product Name:");
        productPanel.add(productNameLabel);
        productNameField = new JTextField();
        productPanel.add(productNameField);

        JLabel productRateLabel = new JLabel("Product Rate:");
        productPanel.add(productRateLabel);
        productRateField = new JTextField();
        productPanel.add(productRateField);

        JLabel productQuantityLabel = new JLabel("Product Quantity:");
        productPanel.add(productQuantityLabel);
        productQuantityField = new JTextField();
        productPanel.add(productQuantityField);

        // Bill Area
        JPanel billPanel = new JPanel();
        billPanel.setBounds(660, 200, 600, 400);
        billPanel.setLayout(new BorderLayout());
        frame.add(billPanel);

        JLabel billAreaTitle = new JLabel("Bill Area");
        billAreaTitle.setFont(new Font("Arial", Font.BOLD, 20));
        billAreaTitle.setHorizontalAlignment(SwingConstants.CENTER);
        billPanel.add(billAreaTitle, BorderLayout.NORTH);

        billArea = new JTextArea();
        billArea.setFont(new Font("Arial", Font.PLAIN, 14));
        billArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(billArea);
        billPanel.add(scrollPane, BorderLayout.CENTER);
        welcomeMessage();

        // Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBounds(10, 620, 1250, 60);
        buttonPanel.setLayout(new GridLayout(1, 4, 10, 10));
        frame.add(buttonPanel);

        JButton addButton = new JButton("Add Item");
        addButton.addActionListener(e -> addItem());
        buttonPanel.add(addButton);

        JButton generateBillButton = new JButton("Generate Bill");
        generateBillButton.addActionListener(e -> generateBill());
        buttonPanel.add(generateBillButton);

        JButton editItemButton = new JButton("Edit Item");
        editItemButton.addActionListener(e -> editItem());
        buttonPanel.add(editItemButton);

        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> clearFields());
        buttonPanel.add(clearButton);

        // Print Button (Initially Hidden)
        printButton = new JButton("Print");
        printButton.setVisible(false);
        printButton.addActionListener(e -> printBill());
        buttonPanel.add(printButton);

        frame.setVisible(true);
    }

    private void welcomeMessage() {
        billArea.setText("\t\t Welcome to Tanish RETAILERS\t\t");
        billArea.append("\nBill Number: " + billNo);
        billArea.append("\nCustomer Name: " + customerNameField.getText());
        billArea.append("\nPhone Number: " + customerPhoneField.getText());
        billArea.append("\n===========================================================================\n");
        billArea.append("\nProduct\t\tQTY\t\tPrice");
        billArea.append("\n===========================================================================\n");
    }

    private void addItem() {
        String productName = productNameField.getText();
        int rate = Integer.parseInt(productRateField.getText());
        int quantity = Integer.parseInt(productQuantityField.getText());
        int price = rate * quantity;

        // Create an item and add it to the list
        Item item = new Item(productName, rate, quantity, price);
        items.add(item);

        // Update the bill area
        updateBillArea();

        // Clear the text fields after adding the item
        productNameField.setText("");
        productRateField.setText("");
        productQuantityField.setText("");
    }

    private void updateBillArea() {
        billArea.setText("");
        welcomeMessage();
        for (Item item : items) {
            billArea.append(item.getName() + "\t\t" + item.getQuantity() + "\t\t" + item.getPrice() + "\n");
        }
    }

    private void generateBill() {
        if (customerNameField.getText().isEmpty() || customerPhoneField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Customer details are required", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            // Get existing text from billArea
            String existingText = billArea.getText();

            // Update customer details within the existing text
            String updatedText = existingText.replaceFirst("Customer Name:.*", "Customer Name: " + customerNameField.getText());
            updatedText = updatedText.replaceFirst("Phone Number:.*", "Phone Number: " + customerPhoneField.getText());

            // Set the updated text back to the billArea
            billArea.setText(updatedText);

            // Calculate total amounts
            double totalAmount = items.stream().mapToDouble(Item::getPrice).sum();
            double cgst = totalAmount * 0.025;
            double sgst = totalAmount * 0.025;
            double finalAmount = totalAmount + cgst + sgst;

            // Append total, GST, SGST, and final amount to the bill
            billArea.append("");
            billArea.append("\nTotal Amount: " + totalAmount);
            billArea.append("\nGST (2.5%): " + cgst);
            billArea.append("\nSGST (2.5%): " + sgst);
            billArea.append("\n===========================================================================\n");
            billArea.append("\nFinal Amount: " + finalAmount);
            billArea.append("\n===========================================================================\n");

            // Make the Print button visible
            printButton.setVisible(true);
        }
    }

    private void editItem() {
        String productName = JOptionPane.showInputDialog(frame, "Enter the product name to edit/delete:");
        if (productName != null && !productName.isEmpty()) {
            Item itemToEdit = null;
            for (Item item : items) {
                if (item.getName().equalsIgnoreCase(productName)) {
                    itemToEdit = item;
                    break;
                }
            }

            if (itemToEdit != null) {
                String[] options = {"Edit", "Delete"};
                int choice = JOptionPane.showOptionDialog(frame, "What would you like to do?", "Edit or Delete",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

                if (choice == 0) { // Edit
                    String newRate = JOptionPane.showInputDialog(frame, "Enter new rate:", itemToEdit.getRate());
                    String newQuantity = JOptionPane.showInputDialog(frame, "Enter new quantity:", itemToEdit.getQuantity());

                    if (newRate != null && newQuantity != null) {
                        int rate = Integer.parseInt(newRate);
                        int quantity = Integer.parseInt(newQuantity);
                        int price = rate * quantity;

                        itemToEdit.setRate(rate);
                        itemToEdit.setQuantity(quantity);
                        itemToEdit.setPrice(price);
                        updateBillArea();
                    }
                } else if (choice == 1) { // Delete
                    items.remove(itemToEdit);
                    updateBillArea();
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Product not found!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearFields() {
        customerNameField.setText("");
        customerPhoneField.setText("");
        productNameField.setText("");
        productRateField.setText("");
        productQuantityField.setText("");
        items.clear();
        printButton.setVisible(false); // Hide the Print button after clearing fields
        welcomeMessage();
    }

    private void printBill() {
        String billContent = billArea.getText();

        // Choose file location
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Bill");
        int userSelection = fileChooser.showSaveDialog(frame);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            try (FileWriter writer = new FileWriter(fileChooser.getSelectedFile() + ".txt")) {
                writer.write(billContent);
                JOptionPane.showMessageDialog(frame, "Bill saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(frame, "Error saving bill: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BillingSoftware::new);
    }

    // Item class representing a product
    class Item {
        private String name;
        private int rate;
        private int quantity;
        private int price;

        public Item(String name, int rate, int quantity, int price) {
            this.name = name;
            this.rate = rate;
            this.quantity = quantity;
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public int getRate() {
            return rate;
        }

        public void setRate(int rate) {
            this.rate = rate;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }
    }
}
