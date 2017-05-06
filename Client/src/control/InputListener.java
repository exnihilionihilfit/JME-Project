/**
 * To prevent problems with multi-pressed keys
 * and massive typing we set each key stroke to a field
 * and reset it after a time.
 * Also it should be timed (not yet) so only each time intervall
 * the keys will be set
 */
package control;

import com.jme3.input.controls.ActionListener;

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
    public static boolean IS_RESET_CAMERA_PRESSED = false;
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

        IS_RESET_CAMERA_PRESSED = false;
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

    public static final ActionListener ACTION_LISTENER = new ActionListener() {
        @Override
        public void onAction(String name, boolean isPressed, float tpf) {
            IS_KEY_PRESSED = true;
            
            
            
            if (name.equals("ResetCamera")) {
                IS_RESET_CAMERA_PRESSED = true;
              

            }
            if (name.equals("Right")) {
                IS_RIGHT_PRESSED = true;
            }
            if (name.equals("Left")) {
                IS_LEFT_PRESSED = true;
            }
            if (name.equals("MouseWheelForward")) {
                IS_WHEEL_FORWARD = true;
            }
            if (name.equals("MouseWheelBackward")) {
                IS_WHEEL_BACKWARD = true;
            }

        }
    };

}
