package cleancode.studycafe.tobe.io;

import java.util.List;

import cleancode.studycafe.tobe.model.pass.locker.StudyCafeLockerPass;
import cleancode.studycafe.tobe.model.pass.StudyCafeSeatPass;
import cleancode.studycafe.tobe.model.pass.StudyCafePassType;

public class StudyCafeIOHandler {
	private final InputHandler inputHandler = new InputHandler();
	private final OutputHandler outputHandler = new OutputHandler();

	public void showWelcomeMessage() {
		outputHandler.showWelcomeMessage();
	}

	public void showAnnouncement() {
		outputHandler.showAnnouncement();
	}

	public void showPassOrderSummary(final StudyCafeSeatPass selectedPass, final StudyCafeLockerPass lockerPass) {
		outputHandler.showPassOrderSummary(selectedPass, lockerPass);
	}

	public void showPassOrderSummary(final StudyCafeSeatPass selectedPass) {
		outputHandler.showPassOrderSummary(selectedPass);
	}

	public void showSimpleMessage(final String message) {
		outputHandler.showSimpleMessage(message);
	}

	public StudyCafePassType askPassTypeSelecting() {
		outputHandler.askPassTypeSelection();
		return inputHandler.getPassTypeSelectingUserAction();
	}

	public StudyCafeSeatPass askPassSelecting(final List<StudyCafeSeatPass> passCandidates) {
		outputHandler.showPassListForSelection(passCandidates);

		return inputHandler.getSelectPass(passCandidates);
	}

	public boolean askLockerPass(final StudyCafeLockerPass lockerPassCandidate) {
		outputHandler.askLockerPass(lockerPassCandidate);
		return inputHandler.getLockerSelection();
	}
}
