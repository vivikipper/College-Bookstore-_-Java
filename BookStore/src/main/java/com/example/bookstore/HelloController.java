package com.example.bookstore;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.sql.*;


public class HelloController {
    @FXML
    private TextField subjectStudy;

    @FXML
    private TextField booksQuantity;

    @FXML
    private TextField discountCode;

    @FXML
    private Text displayValue;

    @FXML
    private Text errorMessage;

    @FXML
    protected void checkOut() {

        String SQL_SELECT = "Select price from books where subject=?";
        double price = 0;
        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3307/bookstore", "root", "root123");
             PreparedStatement preparedStatement = conn.prepareStatement(SQL_SELECT)) {
            preparedStatement.setString(1,subjectStudy.getText());

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                price = resultSet.getDouble("Price");
            }
            if(price==0){
                errorMessage.setVisible(true);
                displayValue.setVisible(false);
            }else{
                errorMessage.setVisible(false);
                displayValue.setVisible(true);

                double bookqtty = Double.parseDouble(booksQuantity.getText());
                double payment = price * bookqtty;

                String couponCode = discountCode.getText();
                if(couponCode.equals("SOMA")) {
                    payment *= 0.9; //  10% discount if coupon code is "SOMA"
                }
                displayValue.setText("Your checkout is "+payment+ "." + "Thank you!");
            }


            System.out.println("the price is " + price);

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}