package cleancode.studycafe.hg;

import java.util.List;

import cleancode.studycafe.hg.exception.AppException;
import cleancode.studycafe.hg.io.file.StudyCafeFileHandler;
import cleancode.studycafe.hg.io.input.ConsoleInputHandler;
import cleancode.studycafe.hg.io.input.InputHandler;
import cleancode.studycafe.hg.io.output.ConsoleOutputHandler;
import cleancode.studycafe.hg.io.output.OutputHandler;
import cleancode.studycafe.hg.model.StudyCafeLockerPass;
import cleancode.studycafe.hg.model.StudyCafePass;
import cleancode.studycafe.hg.model.StudyCafePassType;

public class StudyCafePassMachine {

	private final InputHandler inputHandler = new ConsoleInputHandler();
	private final OutputHandler outputHandler = new ConsoleOutputHandler();

	public void run() {
		try {
			outputHandler.showWelcomeMessage();
			outputHandler.showAnnouncement();

			outputHandler.askPassTypeSelection();
			StudyCafePassType studyCafePassType = inputHandler.getPassTypeSelectingUserAction();

			if (studyCafePassType == StudyCafePassType.HOURLY) {
				final StudyCafePass selectedPass = getStudyCafePass(StudyCafePassType.HOURLY);
				outputHandler.showPassOrderSummary(selectedPass, null);
			} else if (studyCafePassType == StudyCafePassType.WEEKLY) {
				final StudyCafePass selectedPass = getStudyCafePass(StudyCafePassType.WEEKLY);
				outputHandler.showPassOrderSummary(selectedPass, null);
			} else if (studyCafePassType == StudyCafePassType.FIXED) {
				final StudyCafePass selectedPass = getStudyCafePass(StudyCafePassType.FIXED);

				List<StudyCafeLockerPass> lockerPasses = StudyCafeFileHandler.readLockerPasses();
				StudyCafeLockerPass lockerPass = lockerPasses.stream()
					.filter(option ->
						option.getPassType() == selectedPass.getPassType()
							&& option.getDuration() == selectedPass.getDuration()
					)
					.findFirst()
					.orElse(null);

				boolean lockerSelection = false;
				if (lockerPass != null) {
					outputHandler.askLockerPass(lockerPass);
					lockerSelection = inputHandler.getLockerSelection();
				}

				if (lockerSelection) {
					outputHandler.showPassOrderSummary(selectedPass, lockerPass);
				} else {
					outputHandler.showPassOrderSummary(selectedPass, null);
				}
			}
		} catch (AppException e) {
			outputHandler.showSimpleMessage(e.getMessage());
		} catch (Exception e) {
			outputHandler.showSimpleMessage("알 수 없는 오류가 발생했습니다.");
		}
	}

	private StudyCafePass getStudyCafePass(final StudyCafePassType studyCafePassType) {
		List<StudyCafePass> studyCafePasses = StudyCafeFileHandler.readStudyCafePasses();
		List<StudyCafePass> passes = studyCafePasses.stream()
			.filter(studyCafePass -> studyCafePass.getPassType() == studyCafePassType)
			.toList();

		outputHandler.showPassListForSelection(passes);

		return inputHandler.getSelectPass(passes);
	}

}
