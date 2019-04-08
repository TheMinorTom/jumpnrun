/*
 * This File is licensed under the MIT License. You should have already received a copy located at LICENSE.txt
 * Copyright 2019 MinorTom <mail in license file>
 */
package worldeditor;

import javafx.stage.Stage;
import static worldeditor.WorldEditor.initBlocksArr;

public class InGameSceneWorldEditor extends GUI {
    public InGameSceneWorldEditor(){
        initBlocksArr();
        WorldEditor.gui = this;
    }
    
    @Override
    public void start(Stage stage){
        super.start(stage);
    }
}
