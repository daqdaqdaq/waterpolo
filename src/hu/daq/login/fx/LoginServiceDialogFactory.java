/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.login.fx;

import hu.daq.login.LoginService;
import javafx.util.Callback;
import javafx.util.Pair;
import org.controlsfx.dialog.LoginDialog;

/**
 *
 * @author DAQ
 */
public class LoginServiceDialogFactory {

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
