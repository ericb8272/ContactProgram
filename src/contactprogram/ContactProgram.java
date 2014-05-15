/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package contactprogram;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Eric Benedict
 */
public class ContactProgram extends JFrame implements ActionListener {

    // Text Field for name
    private JTextField nameTextField = new JTextField(10);
    // Text Field for age
    private JTextField ageTextField = new JTextField(10);
    // Text Field for email address
    private JTextField emailAddressTextField = new JTextField(10);
    // Text Field for cell phone
    private JTextField cellPhoneTextField = new JTextField(10);
    // Button to save to file
    private JButton saveButton = new JButton("Save Data");
    // Button to read data from file
    private JButton readButton = new JButton("Display Data");
    // Button to exit from application
    private JButton exitButton = new JButton("Exit");
    // Print writer
    private PrintWriter output;
    private DefaultTableModel defaultTableModel;
    private static final String CONTACT_FILE_NAME = "contactRecords.txt";

    public ContactProgram() {
        super("Contact Writer");
        // Layout elements in a Grid Layout
        JPanel inputSectionPanel = new JPanel(new GridLayout(2, 4, 10, 20));
        JLabel nameLabel = new JLabel("Name:", JLabel.RIGHT);
        JLabel ageLabel = new JLabel("Age:", JLabel.RIGHT);
        JLabel emailAddressLabel = new JLabel("Email Address:", JLabel.RIGHT);
        JLabel cellPhonbeLabel = new JLabel("Cell phone:", JLabel.RIGHT);
        inputSectionPanel.add(nameLabel);
        inputSectionPanel.add(nameTextField);
        inputSectionPanel.add(ageLabel);
        inputSectionPanel.add(ageTextField);
        inputSectionPanel.add(emailAddressLabel);
        inputSectionPanel.add(emailAddressTextField);
        inputSectionPanel.add(cellPhonbeLabel);
        inputSectionPanel.add(cellPhoneTextField);

        // buttonPanel to hold buttons
        JPanel buttonPanel = new JPanel();
        saveButton.addActionListener(this);
        readButton.addActionListener(this);
        exitButton.addActionListener(this);
        buttonPanel.add(saveButton);
        buttonPanel.add(readButton);
        buttonPanel.add(exitButton);

        defaultTableModel = new DefaultTableModel(0, 4);
        JTable jTable = new JTable();
        jTable.setModel(defaultTableModel);
        // Create JScrollPane to contain the table
        JScrollPane tableScroller = new JScrollPane(jTable,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        tableScroller.setViewportView(jTable);

        // Create Container to hold the table and buttons
        Container mainContainer = getContentPane();
        mainContainer.setLayout(new BorderLayout());
        mainContainer.setLayout(new BoxLayout(mainContainer, BoxLayout.Y_AXIS));
        mainContainer.add(inputSectionPanel, BorderLayout.WEST);
        mainContainer.add(buttonPanel, BorderLayout.CENTER);
        mainContainer.add(tableScroller, BorderLayout.SOUTH);
        tableScroller.setViewportView(jTable);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        pack();
    }

    /**
     * Read data from file and populate in table
     */
    private void createContactTable() {
        // Create Table model and set its column headers
        // Set column headers
        String col[] = {"Name", "Age", "Email Address", "Cell Phone"};
        defaultTableModel.setColumnIdentifiers(col);

        File file = new File(CONTACT_FILE_NAME);
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = bufferedReader.readLine();
            int counter = 0;
            while (line != null) {
                // Data is saved in file delimited by comma
                String[] dataArray = line.split(",");
                // Increment the number of rows in table model
                defaultTableModel.setRowCount(counter + 1);
                // Set each value of the column one by one
                for (int i = 0; i < dataArray.length; i++) {
                    defaultTableModel.setValueAt(dataArray[i], counter, i);
                }
                // Read next line
                line = bufferedReader.readLine();
                // Increment the row counter
                counter++;
            }
            // Close file
            bufferedReader.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Unable to read the file");
        }

    }

    /**
     * Handle all the events
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        JButton source = (JButton) event.getSource();
        if (source == saveButton) {
            addContactToFile();
        } else if (source == exitButton) {
            System.exit(0);
        } else if (source == readButton) {
            createContactTable();
        }
    }

    private boolean isBlank(String inputData) {
        if (inputData == null || inputData.trim().equals("")) {
            return true;
        }
        return false;
    }

    /**
     * Add contact to file if all data entered
     */
    void addContactToFile() {
        String name = nameTextField.getText();
        if (isBlank(name)) {
            JOptionPane.showMessageDialog(null, "Please enter name", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        // Check age
        int age = 0;
        try {
            age = Integer.parseInt(ageTextField.getText());
            if (age < 0 || age > 120) {
                JOptionPane.showMessageDialog(null,
                        "Age should be between 0 and 120.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                ageTextField.setText(null);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Age should be integer value.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            ageTextField.setText(null);
            return;
        }

        String emailAddress = emailAddressTextField.getText();
        if (isBlank(emailAddress)) {
            JOptionPane.showMessageDialog(null, "Please enter email address",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String cellPhone = cellPhoneTextField.getText();
        if (isBlank(cellPhone)) {
            JOptionPane.showMessageDialog(null, "Please enter cell phone",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            output = new PrintWriter(new FileWriter(CONTACT_FILE_NAME, true));
            output.write(name + "," + age + "," + emailAddress + ","
                    + cellPhone + "\n");
            JOptionPane.showMessageDialog(null, "Contact added successfully.");
            output.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "An exception happened");
        }
        // Clear values for next entry
        nameTextField.setText(null);
        ageTextField.setText(null);
        emailAddressTextField.setText(null);
        cellPhoneTextField.setText(null);

    }

    public static void main(String[] args) {
        ContactProgram obj = new ContactProgram();
        obj.setVisible(true);
    }
}
