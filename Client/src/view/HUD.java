/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.scene.Node;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.style.BaseStyles;
import main.Main;

/**
 *
 * @author novo
 */
public class HUD {

    private final Container menuContainer;
    private Main main;
    
    public static boolean IS_CREATE_ENTITY_BUTTON_PRESSED;
       


    public HUD(Main main,Node guiNode, BitmapFont guiFont, AssetManager assetManager) {
        
        this.main = main;
        
        /** Write text on the screen (HUD) */
        guiNode.detachAllChildren();
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        BitmapText helloText = new BitmapText(guiFont, false);
        helloText.setSize(guiFont.getCharSet().getRenderedSize());
        helloText.setText("Hello World");
        helloText.setLocalTranslation(300, helloText.getLineHeight(), 0);
        guiNode.attachChild(helloText);   
        
        
         // Initialize the globals access so that the defualt
        // components can find what they need.
        GuiGlobals.initialize(main);
            
        // Load the 'glass' style
        BaseStyles.loadGlassStyle();
            
        // Set 'glass' as the default style when not specified
        GuiGlobals.getInstance().getStyles().setDefaultStyle("glass");
    
        // Create a simple container for our elements
        menuContainer = new Container();
        guiNode.attachChild(menuContainer);
            
        
         menuContainer.setLocalTranslation(main.getScreenWidth()-100, main.getScreenHeight() - 100, 0);
        
    
 // Add some elements
        menuContainer.addChild(new Label("Menu"));
        Button createEntityButton = menuContainer.addChild(new Button("Create Ship"));
        
        createEntityButton.addClickCommands(new Command<Button>() {
           
                @Override
                public void execute( Button source ) {
                    IS_CREATE_ENTITY_BUTTON_PRESSED = true;
                    
                  
                    System.out.println("Create Entity");
                }
            });            
    }    
    
    public void updateMenu()
    {
         menuContainer.setLocalTranslation(main.getScreenWidth()-100, main.getScreenHeight() - 100, 0);
    }
    
    public boolean isCreateEntityButtonIsPressed()
    {
        return IS_CREATE_ENTITY_BUTTON_PRESSED;
    }
    
    private void resetIsCreateEntityButtonPressed()
    {
        IS_CREATE_ENTITY_BUTTON_PRESSED = false;
    }
   
    public void  resetInput()
    {
        resetIsCreateEntityButtonPressed();
    }
    
    
    
}
