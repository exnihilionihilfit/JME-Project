/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control.state;

/**
 *
 * @author novo
 */
public class ProgramState extends State {

    private static StatesEnum.Program current;
    private static GameState gameState;
    private final StartUp startUp;

    public ProgramState() {
        startUp = new StartUp();
        current = StatesEnum.Program.STARTUP;

    }

    @Override
    public boolean execute() {

        switch (current) {
            case STARTUP:
                startUp.execute();
                break;
            case RUNNING:
                gameState.execute();
                break;

        }
        return false;

    }

    private static class StartUp extends State {

        @Override
        public boolean execute() {
            gameState = new GameState();
            changeState(StatesEnum.Program.RUNNING);
            return false;

        }

        @Override
        public boolean changeState(StatesEnum.Program RUNNING) {
            ProgramState.current = RUNNING;
            return false;
        }

    }

}
