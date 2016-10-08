/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.keyevent;

import java.util.HashMap;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 *
 * @author DAQ
 * @param <T>
 */
public class KeyEventHandler implements EventHandler<KeyEvent>{

    HashMap<KeyCode, Button> buttons;

    public KeyEventHandler() {
        this.buttons = new HashMap<KeyCode, Button>();

    }

    public void bindButton(KeyCode key, Button button) {
        this.buttons.put(key, button);
    }

    public void bindButton(String key, Button button) throws IllegalArgumentException {
        KeyCode k = KeyCode.getKeyCode(key);
        if (k == null) {
            throw new IllegalArgumentException("No such keycode");
        }
        this.buttons.put(k, button);
    }

    @Override
    public void handle(KeyEvent event) {
        Button btn = this.buttons.get(event.getCode());
        if (btn!=null){
            btn.fire();
            event.consume();
        }
    }


}
