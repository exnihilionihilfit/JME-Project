/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Control;

import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;

/**
 *
 * @author novo
 */
public class InputListener {

 

    private long lastMouseInput;
    private long lastKeyInput;

    public static boolean IS_MOUSE_BUTTON_PRESSED = false;
    public static boolean IS_LEFT_MOUSE_BUTTON_PRESSED = false;
    public static boolean IS_RIGHT_MOUSE_BUTTON_PRESSED = false;

    public static boolean IS_RIGHT_PRESSED = false;
    public static boolean IS_ROTATE_PRESSED = false;
      public static boolean IS_LEFT_PRESSED = false;
    
    public static boolean IS_KEY_PRESSED = false;
    public static boolean IS_CONTROL_DOWN = false;

    public static boolean IS_WHEEL_FORWARD = false;
    public static boolean IS_WHEEL_BACKWARD = false;

    public static int KEY_CODE = -1;

    public InputListener() {

    }

    public static void resetInput() {
        IS_MOUSE_BUTTON_PRESSED = false;
        IS_LEFT_MOUSE_BUTTON_PRESSED = false;
        IS_RIGHT_MOUSE_BUTTON_PRESSED = false;

        IS_CONTROL_DOWN = false;

        KEY_CODE = -1;

        IS_WHEEL_FORWARD = false;
        IS_WHEEL_BACKWARD = false;
        
        IS_ROTATE_PRESSED = false;
        IS_RIGHT_PRESSED = false;
        IS_LEFT_PRESSED = false;
    }

    /**
     * Use ActionListener to respond to pressed/released inputs (key presses,
     * mouse clicks)
     */
    public static final com.jme3.input.controls.ActionListener MOUSE_INPUT_LISTENER = new ActionListener() {
        @Override
        public void onAction(String name, boolean pressed, float tpf) {
            if (pressed) {
                IS_MOUSE_BUTTON_PRESSED = true;

                IS_LEFT_MOUSE_BUTTON_PRESSED = "LeftMouse".equals(name);

                IS_RIGHT_MOUSE_BUTTON_PRESSED = "RightMouse".equals(name);

                IS_WHEEL_FORWARD = "MouseWheelForward".equals(name);

                IS_WHEEL_BACKWARD = "MouseWheelBackward".endsWith(name);
            }
        }
    };

    public static AnalogListener KEY_INPUT_LISTENER= new AnalogListener() {
 
  
        @Override
        public void onAnalog(String name, float value, float tpf) {
        IS_KEY_PRESSED = true;
        if (name.equals("Rotate")) {
            IS_ROTATE_PRESSED = true;
     
        }
        if (name.equals("Right")) {
           IS_RIGHT_PRESSED = true;
        }
        if (name.equals("Left")) {
          IS_LEFT_PRESSED = true;
        }
       else {
        // System.out.println("Press P to unpause.");
      }
        }
    };
    
}
