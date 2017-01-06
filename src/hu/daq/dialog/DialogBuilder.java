/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.dialog;

import javafx.scene.control.Alert;

/**
 *
 * @author DAQ
 */
public class DialogBuilder {
    
    public static Alert getErrorDialog(String headertext, String text) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Hiba");
        alert.setHeaderText(headertext);
        alert.setContentText(text);
        return alert;
    }

    public static Alert getConfirmDialog(String headertext, String text) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(null);
        alert.setHeaderText(headertext);
        alert.setContentText(text);
        return alert;
    }    
}
