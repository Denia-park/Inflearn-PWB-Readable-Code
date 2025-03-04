package cleancode.studycafe.hg.io.output;

import java.util.List;

import cleancode.studycafe.hg.model.StudyCafeLockerPass;
import cleancode.studycafe.hg.model.StudyCafePass;

public interface OutputHandler {
	void showWelcomeMessage();

	void showAnnouncement();

	void askPassTypeSelection();

	void showPassListForSelection(List<StudyCafePass> passes);

	void askLockerPass(StudyCafeLockerPass lockerPass);

	void showPassOrderSummary(StudyCafePass selectedPass, StudyCafeLockerPass lockerPass);

	void showSimpleMessage(String message);
}
