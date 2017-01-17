/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.login.fx;

import hu.daq.login.LoginService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Pair;
import org.controlsfx.dialog.LoginDialog;

/**
 *
 * @author DAQ
 */
public class LoginServiceDialogFactory {

    public static Dialog getLoginDialogWorking(LoginService ls) {
        Dialog<ButtonType> dialog = new Dialog<ButtonType>();
        dialog.setTitle("Bejelentkezés");
        dialog.setHeaderText("Az indításhoz jelentkezzen be!");
        //dialog.setGraphic(new ImageView(this.getClass().getResource("login.png").toString()));

// Set the button types.
        ButtonType loginButtonType = new ButtonType("Bejelentkezés", ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Mégse", ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, cancelButtonType);

// Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField username = new TextField();
        username.setPromptText("Flhasználó");
        PasswordField password = new PasswordField();
        password.setPromptText("Jelszó");

        grid.add(new Label("Felhasználó:"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("Jelszó:"), 0, 1);
        grid.add(password, 1, 1);

// Enable/Disable login button depending on whether a username was entered.
        Button loginButton = (Button) dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);
        Button cancelButton = (Button) dialog.getDialogPane().lookupButton(cancelButtonType);
        // Prevent closing dialog if not authenticated**
        loginButton.addEventFilter(ActionEvent.ACTION, (event) -> {
            if (!ls.login(username.getText(), password.getText())) {
                event.consume();
            }
        });
        /*cancelButton.addEventFilter(ActionEvent.ACTION, (event) -> {
            ((Stage)dialog.getDialogPane().getScene().getWindow()).close();
            //Platform.exit();
        });*/
// Do some validation (using the Java 8 lambda syntax).
        username.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);

// Request focus on the username field by default.
        Platform.runLater(() -> username.requestFocus());

// Convert the result to a username-password-pair when the login button is clicked.
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return ButtonType.OK;
            }
            if (dialogButton == cancelButtonType) {
                return ButtonType.CANCEL;
            }            
            return null;
        });
       
        return dialog;
    }

    public static LoginDialog getLoginDialog(LoginService ls) {
        LoginDialog ld = new LoginDialog(new Pair<String, String>("", ""), LoginServiceDialogFactory.makeAuthenticator(ls));
        return ld;
    }

    private static Callback<Pair<String, String>, Void> makeAuthenticator(LoginService ls) {
        Callback cb = new Callback<Pair<String, String>, Void>() {

            @Override

            public Void call(Pair<String, String> param) {
                if (!ls.login(param.getKey(), param.getValue())) {
                    throw new IllegalArgumentException("Ismeretlen felhasználó/jelszó.");
                }
                return null;
            }
        };
        return cb;
    }
}
