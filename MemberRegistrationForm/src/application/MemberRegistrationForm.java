package application;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

import java.time.LocalDate;
import java.time.Period;
import java.util.regex.Pattern;

public class MemberRegistrationForm extends Application {

    // Validation patterns
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$");
    private static final Pattern PHONE_PATTERN = 
        Pattern.compile("^\\+?[0-9]{10,15}$");

    @Override
    public void start(Stage primaryStage) {
        // Form controls
        TextField memberIdField = new TextField();
        memberIdField.setText("AUTO-" + System.currentTimeMillis()); // Auto-generated ID
        memberIdField.setEditable(false);
        memberIdField.setDisable(true);
        styleAsReadonly(memberIdField);

        TextField fullNameField = new TextField();
        TextField emailField = new TextField();
        TextField phoneField = new TextField();
        DatePicker dobPicker = new DatePicker();
        TextArea addressArea = new TextArea();
        TextField emergencyNameField = new TextField();
        TextField emergencyPhoneField = new TextField();
        
        ComboBox<String> planBox = new ComboBox<>();
        planBox.getItems().addAll("Basic", "Standard", "Premium");
        planBox.setValue("Basic"); // Default selection

        ToggleGroup paymentGroup = new ToggleGroup();
        RadioButton cashRadio = new RadioButton("Cash");
        RadioButton gcashRadio = new RadioButton("GCash");
        RadioButton bankRadio = new RadioButton("Bank Transfer");
        cashRadio.setToggleGroup(paymentGroup);
        gcashRadio.setToggleGroup(paymentGroup);
        bankRadio.setToggleGroup(paymentGroup);
        cashRadio.setSelected(true); // Default selection

        CheckBox termsCheckBox = new CheckBox("I agree to the terms and conditions");
        termsCheckBox.setTextFill(Color.web("#1e88e5"));

        // Validation labels
        Label fullNameError = createErrorLabel();
        Label emailError = createErrorLabel();
        Label phoneError = createErrorLabel();
        Label dobError = createErrorLabel();
        Label addressError = createErrorLabel();
        Label emergencyNameError = createErrorLabel();
        Label emergencyPhoneError = createErrorLabel();
        Label planError = createErrorLabel();
        Label paymentError = createErrorLabel();
        Label termsError = createErrorLabel();

        // Submit button
        Button submitButton = new Button("Register Member");
        submitButton.getStyleClass().add("submit-button");
        submitButton.setOnAction(e -> {
            boolean isValid = true;

            // Reset errors
            clearErrors(fullNameError, emailError, phoneError, dobError, 
                       addressError, emergencyNameError, emergencyPhoneError,
                       planError, paymentError, termsError);

            // Validate required fields
            if (fullNameField.getText().trim().isEmpty()) {
                fullNameError.setText("Full Name is required");
                isValid = false;
            }

            if (emailField.getText().trim().isEmpty()) {
                emailError.setText("Email is required");
                isValid = false;
            } else if (!EMAIL_PATTERN.matcher(emailField.getText()).matches()) {
                emailError.setText("Invalid email format");
                isValid = false;
            }

            if (phoneField.getText().trim().isEmpty()) {
                phoneError.setText("Phone Number is required");
                isValid = false;
            } else if (!PHONE_PATTERN.matcher(phoneField.getText()).matches()) {
                phoneError.setText("Invalid phone format (10-15 digits)");
                isValid = false;
            }

            if (dobPicker.getValue() == null) {
                dobError.setText("Date of Birth is required");
                isValid = false;
            } else {
                LocalDate today = LocalDate.now();
                Period age = Period.between(dobPicker.getValue(), today);
                if (age.getYears() < 18) {
                    dobError.setText("Must be at least 18 years old");
                    isValid = false;
                }
            }

            if (addressArea.getText().trim().isEmpty()) {
                addressError.setText("Address is required");
                isValid = false;
            }

            if (emergencyNameField.getText().trim().isEmpty()) {
                emergencyNameError.setText("Emergency Contact Name is required");
                isValid = false;
            }

            if (emergencyPhoneField.getText().trim().isEmpty()) {
                emergencyPhoneError.setText("Emergency Contact Number is required");
                isValid = false;
            } else if (!PHONE_PATTERN.matcher(emergencyPhoneField.getText()).matches()) {
                emergencyPhoneError.setText("Invalid phone format (10-15 digits)");
                isValid = false;
            }

            if (planBox.getValue() == null) {
                planError.setText("Membership Plan is required");
                isValid = false;
            }

            if (paymentGroup.getSelectedToggle() == null) {
                paymentError.setText("Payment Method is required");
                isValid = false;
            }

            if (!termsCheckBox.isSelected()) {
                termsError.setText("You must agree to the terms");
                isValid = false;
            }

            if (isValid) {
                showAlert("Registration Successful", 
                         "Member registered successfully!\nID: " + memberIdField.getText());
            }
        });

        // Layout setup
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(12);
        grid.setPadding(new Insets(20));
        grid.setAlignment(Pos.TOP_CENTER);

        // Add rows to grid
        int row = 0;
        grid.add(new Label("Member ID:"), 0, row);
        grid.add(memberIdField, 1, row++);
        
        grid.add(new Label("Full Name *:"), 0, row);
        grid.add(fullNameField, 1, row);
        grid.add(fullNameError, 2, row++);
        
        grid.add(new Label("Email *:"), 0, row);
        grid.add(emailField, 1, row);
        grid.add(emailError, 2, row++);
        
        grid.add(new Label("Phone Number *:"), 0, row);
        grid.add(phoneField, 1, row);
        grid.add(phoneError, 2, row++);
        
        grid.add(new Label("Date of Birth *:"), 0, row);
        grid.add(dobPicker, 1, row);
        grid.add(dobError, 2, row++);
        
        grid.add(new Label("Address *:"), 0, row);
        grid.add(addressArea, 1, row);
        grid.add(addressError, 2, row++);
        
        grid.add(new Label("Emergency Contact Name *:"), 0, row);
        grid.add(emergencyNameField, 1, row);
        grid.add(emergencyNameError, 2, row++);
        
        grid.add(new Label("Emergency Contact Number *:"), 0, row);
        grid.add(emergencyPhoneField, 1, row);
        grid.add(emergencyPhoneError, 2, row++);
        
        grid.add(new Label("Membership Plan *:"), 0, row);
        grid.add(planBox, 1, row);
        grid.add(planError, 2, row++);
        
        grid.add(new Label("Payment Method *:"), 0, row);
        VBox paymentBox = new VBox(5, cashRadio, gcashRadio, bankRadio);
        grid.add(paymentBox, 1, row);
        grid.add(paymentError, 2, row++);
        
        grid.add(new Label("Terms *:"), 0, row);
        grid.add(termsCheckBox, 1, row);
        grid.add(termsError, 2, row++);
        
        grid.add(submitButton, 1, row, 2, 1);
        GridPane.setHalignment(submitButton, HPos.RIGHT);

        // Main layout
        VBox mainLayout = new VBox(20);
        mainLayout.setPadding(new Insets(20));
        mainLayout.getChildren().addAll(
            new Label("Member Registration Form"),
            grid
        );
        mainLayout.setAlignment(Pos.TOP_CENTER);
        
        // Styling
        Scene scene = new Scene(mainLayout, 600, 700);
        scene.getStylesheets().add(getClass().getResource("form-styles.css").toExternalForm());
        
        primaryStage.setTitle("Member Registration");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Label createErrorLabel() {
        Label label = new Label();
        label.setTextFill(Color.RED);
        label.setWrapText(true);
        return label;
    }

    private void clearErrors(Label... labels) {
        for (Label label : labels) {
            label.setText("");
        }
    }

    private void styleAsReadonly(TextField field) {
        field.setStyle("-fx-background-color: #f0f0f0; -fx-text-fill: gray;");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}