package cleancode.studycafe.hg.io.input;

import java.util.List;
import java.util.Scanner;

import cleancode.studycafe.hg.model.StudyCafePass;
import cleancode.studycafe.hg.model.StudyCafePassType;

public class ConsoleInputHandler implements InputHandler {

    private static final Scanner SCANNER = new Scanner(System.in);

    @Override
    public StudyCafePassType getPassTypeSelectingUserAction() {
        String userInput = SCANNER.nextLine();

        return StudyCafePassType.getPassType(userInput);
    }

    @Override
    public StudyCafePass getSelectPass(List<StudyCafePass> passes) {
        String userInput = SCANNER.nextLine();
        int selectedIndex = Integer.parseInt(userInput) - 1;
        return passes.get(selectedIndex);
    }

    @Override
    public boolean getLockerSelection() {
        String userInput = SCANNER.nextLine();
        return "1".equals(userInput);
    }

}
