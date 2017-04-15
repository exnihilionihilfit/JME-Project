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
import org.lwjgl.opengl.Display;

/**
 *
 * @author novo
 */
public class HUD {
    


    public HUD(Main main,Node guiNode, BitmapFont guiFont, AssetManager assetManager) {
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
        Container myWindow = new Container();
        guiNode.attachChild(myWindow);
            
        
        myWindow.setLocalTranslation(main.getScreenWidth()-100, 100, 0);
        
    
 // Add some elements
        myWindow.addChild(new Label("Menu"));
        Button clickMe = myWindow.addChild(new Button("Create Ship"));
        clickMe.addClickCommands(new Command<Button>() {
                @Override
                public void execute( Button source ) {
                    System.out.println("The world is yours.");
                }
            });            
    }    
    
}
