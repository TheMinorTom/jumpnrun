/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jumpnrun;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 *
 * @author d.betko
 */
public class WinScreen extends GridPane {

    private static Font font1 = new Font("Maiandra GD", 100);
    private static Font font2 = new Font("Maiandra GD", 50);
    private Label leftLbl, rightLbl;
    private Button backToMenu;

    public WinScreen(JumpNRun game) {
        leftLbl = new Label();
        leftLbl.setFont(font1);
        leftLbl.setAlignment(Pos.CENTER);
        leftLbl.setPrefHeight(4000);
        rightLbl = new Label();
        rightLbl.setFont(font2);
        rightLbl.setAlignment(Pos.CENTER);

        add(leftLbl, 0, 1);
        add(rightLbl, 1, 1);
        setGridLinesVisible(true);
        backToMenu = new Button("Zurück zum Hauptmenu");
        backToMenu.setFont(font2);
        backToMenu.setOnAction((ActionEvent e) -> {
            game.openMainMenu();
        });
        add(backToMenu, 0, 0);
    }

    public void setWinner(int id) {
        font1 = new Font("Maiandra GD", JumpNRun.getWidth() / 15);
        font2 = new Font("Maiandra GD", JumpNRun.getWidth() / 30);
        rightLbl.setPrefWidth(JumpNRun.getWidth() / 2);
        leftLbl.setPrefWidth(JumpNRun.getWidth() / 2);

        if (id == 1) {

            leftLbl.setText("Gewonnen!");
            leftLbl.setTextFill(Color.GOLD);
            leftLbl.setFont(font1);
            rightLbl.setText("Verloren...\nViel Glück beim nächsten mal!");
            rightLbl.setTextFill(Color.GRAY);

            rightLbl.setFont(font2);
        } else if (id == 2) {
            rightLbl.setText("Gewonnen!");
            rightLbl.setTextFill(Color.GOLD);
            rightLbl.setFont(font2);
            leftLbl.setText("Verloren...\nViel Glück beim nächsten mal!");
            leftLbl.setTextFill(Color.GRAY);
            leftLbl.setFont(font2);

        } else {
            leftLbl.setText("Unentschieden!");
            leftLbl.setFont(font1);
            leftLbl.setTextFill(Color.CHOCOLATE);

            rightLbl.setText("Unentschieden!");
            rightLbl.setFont(font1);
            rightLbl.setTextFill(Color.CHOCOLATE);
        }
    }
}
