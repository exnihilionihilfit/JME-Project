/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.TextField;
import com.simsilica.lemur.core.GuiLayout;
import com.simsilica.lemur.style.BaseStyles;
import control.InputServerData;
import main.Main;

/**
 *
 * @author novo
 */
public class HUD {

    private final Container menuContainer;
    private Main main;

    public static boolean IS_CREATE_ENTITY_BUTTON_PRESSED;
    public static boolean IS_SERVER_ADRESS_ENTERD;
    private final Container serverAdressContainer;

    public HUD(Main main, Node guiNode, BitmapFont guiFont, AssetManager assetManager) {

        this.main = main;

        /**
         * Write text on the screen (HUD)
         */
        guiNode.detachAllChildren();
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        BitmapText helloText = new BitmapText(guiFont, false);
        helloText.setSize(guiFont.getCharSet().getRenderedSize());
        helloText.setText("Info: \n hit create Entity button to get an entity \n left click selction and movement \n right click deselect ");
        helloText.setLocalTranslation(200, helloText.getLineHeight() + 150, 0);
       

        // Initialize the globals access so that the defualt
        // components can find what they need.
        GuiGlobals.initialize(main);

        // Load the 'glass' style
        BaseStyles.loadGlassStyle();

        // Set 'glass' as the default style when not specified
        GuiGlobals.getInstance().getStyles().setDefaultStyle("glass");

        // Create a simple container for our elements
        menuContainer = new Container();
        menuContainer.setPreferredSize(new Vector3f(200, 100, 10));

        

        menuContainer.setLocalTranslation(main.getScreenWidth() - 200, main.getScreenHeight() - 100, 0);

        // Add some elements
        menuContainer.addChild(new Label("Menu"));
        Button createEntityButton = menuContainer.addChild(new Button("Create Ship"));
        createEntityButton.setAlpha(0.5f);

        createEntityButton.addClickCommands(new Command<Button>() {

            @Override
            public void execute(Button source) {
                IS_CREATE_ENTITY_BUTTON_PRESSED = true;

                System.out.println("Create Entity");
            }
        });

        serverAdressContainer = new Container();
        serverAdressContainer.addChild(new Label("Insert server adress"));
        serverAdressContainer.setPreferredSize(new Vector3f(300, 100, 10));
        serverAdressContainer.setLocalTranslation(main.getScreenWidth() -300, 100, 0);
        guiNode.attachChild(serverAdressContainer);

        TextField serverAdressInput = serverAdressContainer.addChild(new TextField("localhost"));
        Button connectToServer = serverAdressContainer.addChild(new Button("connect"));
        
        connectToServer.addClickCommands(new Command<Button>(){
            @Override
            public void execute(Button s) {
               guiNode.attachChild(menuContainer);
               guiNode.detachChild(serverAdressContainer);
               guiNode.attachChild(helloText);
              
               
               if(InputServerData.checkServerAdress(serverAdressInput.getText()))
               {
                   InputServerData.SERVER_ADRESS = serverAdressInput.getText();
                   InputServerData.IS_VALID_SERVER_DATA = true;
                   
                    IS_SERVER_ADRESS_ENTERD = true;
                 
                helloText.setText(" try to connect to server ");
               }
              
            }
            
        });

    }

    public void updateMenu() {
        menuContainer.setLocalTranslation(main.getScreenWidth() - 200, main.getScreenHeight() - 100, 0);
        serverAdressContainer.setLocalTranslation(main.getScreenWidth() -300, 100, 0);
    }

    public boolean isCreateEntityButtonIsPressed() {
        return IS_CREATE_ENTITY_BUTTON_PRESSED;
    }

    private void resetIsCreateEntityButtonPressed() {
        IS_CREATE_ENTITY_BUTTON_PRESSED = false;
    }

    public void resetInput() {
        resetIsCreateEntityButtonPressed();
    }

}
