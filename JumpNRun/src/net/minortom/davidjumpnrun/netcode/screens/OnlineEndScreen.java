/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minortom.davidjumpnrun.netcode.screens;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import jumpnrun.JumpNRun;

/**
 *
 * @author DavidPrivat
 */
public class OnlineEndScreen extends StackPane {

    private Label heading;
    private Button toMainMenuBt, toSpectatorModeBt;
    private TableView table;
    private TableColumn userPlacement, userName, userKills, userDeaths;
    private JumpNRun game;
    private int currPlacement;

    public OnlineEndScreen(JumpNRun game) {
        table = new TableView();
        userPlacement = new TableColumn();
        userDeaths = new TableColumn();
        userKills = new TableColumn();
        userName = new TableColumn();

        userPlacement.setCellValueFactory(
                new PropertyValueFactory<PlayerStatsElement, String>("placement")
        );
        userDeaths.setCellValueFactory(
                new PropertyValueFactory<PlayerStatsElement, String>("deaths")
        );
        userName.setCellValueFactory(
                new PropertyValueFactory<PlayerStatsElement, String>("name")
        );
        userKills.setCellValueFactory(
                new PropertyValueFactory<PlayerStatsElement, String>("kills")
        );

        table.getColumns().addAll(userPlacement, userName, userKills, userDeaths);
        this.game = game;
        currPlacement = 0;
        getChildren().add(table);
    }

    public void updateStrings() {
        userPlacement.setText(game.language.EndGamePlacement);
        userDeaths.setText(game.language.EndGameDeaths);
        userKills.setText(game.language.EndGameKills);
        userName.setText(game.language.EndGameName);
    }

    public void startInserting() {
        table.getItems().clear();
    }

    public void showEndDeathLimitGame() {

    }

    public void addPlayerEntry(String[] currArgs) {
        table.getItems().add(new PlayerStatsElement(currArgs[0], currArgs[1], currArgs[2], currArgs[3]));
    }

    public static class PlayerStatsElement {

        private final SimpleStringProperty placement, name, kills, deaths;

        public PlayerStatsElement(String placement, String name, String kills, String deaths) {
            this.placement = new SimpleStringProperty(placement);
            this.name = new SimpleStringProperty(name);
            this.kills = new SimpleStringProperty(kills);
            this.deaths = new SimpleStringProperty(deaths);
        }

        public String getPlacement() {
            return placement.get();
        }

        public String getName() {
            return name.get();
        }

        public String getKills() {
            return kills.get();
        }

        public String getDeaths() {
            return deaths.get();
        }
        
        public void setPlacement(String s) {
            placement.set(s);
        }
        
        public void setName(String s) {
            name.set(s);
        }
        
        public void setKills(String s) {
            kills.set(s);
        }
        
        public void setDeaths(String s) {
            deaths.set(s);
        }
    }

    public enum EndScreenState {

    }
}
