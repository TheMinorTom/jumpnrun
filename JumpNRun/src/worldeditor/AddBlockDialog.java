/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package worldeditor;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author Norbert
 */
public class AddBlockDialog extends Dialog{
    Button ok,cancel,choseUrl;
    TextField urlTextField;
    VBox root;
    HBox buttonBox,urlBox;
    public AddBlockDialog()
    {
        super();
        ok = new Button("Ok");
        ok.setMinWidth(50);
        ok.setOnAction((ActionEvent e) -> {
            this.hide();
        });
        
    }
    
}
