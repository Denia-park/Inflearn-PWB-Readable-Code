package cleancode.studycafe.tobe.io;

import java.util.List;

import cleancode.studycafe.tobe.model.StudyCafeLockerPass;
import cleancode.studycafe.tobe.model.StudyCafePass;
import cleancode.studycafe.tobe.model.StudyCafePassType;

public class StudyCafeIOHandler {
	private final InputHandler inputHandler = new InputHandler();
	private final OutputHandler outputHandler = new OutputHandler();

	public void showWelcomeMessage() {
		outputHandler.showWelcomeMessage();
	}

	public void showAnnouncement() {
		outputHandler.showAnnouncement();
	}

	public void showPassOrderSummary(final StudyCafePass selectedPass, final StudyCafeLockerPass lockerPass) {
		outputHandler.showPassOrderSummary(selectedPass, lockerPass);
	}

	public void showPassOrderSummary(final StudyCafePass selectedPass) {
		outputHandler.showPassOrderSummary(selectedPass);
	}

	public void showSimpleMessage(final String message) {
		outputHandler.showSimpleMessage(message);
	}

	public StudyCafePassType askPassTypeSelecting() {
		outputHandler.askPassTypeSelection();
		return inputHandler.getPassTypeSelectingUserAction();
	}

	public StudyCafePass askPassSelecting(final List<StudyCafePass> passCandidates) {
		outputHandler.showPassListForSelection(passCandidates);

		return inputHandler.getSelectPass(passCandidates);
	}

	public boolean askLockerPass(final StudyCafeLockerPass lockerPassCandidate) {
		outputHandler.askLockerPass(lockerPassCandidate);
		return inputHandler.getLockerSelection();
	}
}
