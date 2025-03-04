package cleancode.studycafe.tobe;

import java.util.List;
import java.util.Optional;

import cleancode.studycafe.tobe.exception.AppException;
import cleancode.studycafe.tobe.io.InputHandler;
import cleancode.studycafe.tobe.io.OutputHandler;
import cleancode.studycafe.tobe.io.StudyCafeFileHandler;
import cleancode.studycafe.tobe.model.StudyCafeLockerPass;
import cleancode.studycafe.tobe.model.StudyCafePass;
import cleancode.studycafe.tobe.model.StudyCafePassType;

public class StudyCafePassMachine {

	private final InputHandler inputHandler = new InputHandler();
	private final OutputHandler outputHandler = new OutputHandler();
	private final StudyCafeFileHandler studyCafeFileHandler = new StudyCafeFileHandler();

	public void run() {
		try {
			outputHandler.showWelcomeMessage();
			outputHandler.showAnnouncement();

			final StudyCafePass selectedPass = selectPass();
			final Optional<StudyCafeLockerPass> optionalLockerPass = selectLockerPass(selectedPass);

			optionalLockerPass.ifPresentOrElse(
				lockerPass -> outputHandler.showPassOrderSummary(selectedPass, lockerPass),
				() -> outputHandler.showPassOrderSummary(selectedPass)
			);
		} catch (AppException e) {
			outputHandler.showSimpleMessage(e.getMessage());
		} catch (Exception e) {
			outputHandler.showSimpleMessage("알 수 없는 오류가 발생했습니다.");
		}
	}

	private StudyCafePass selectPass() {
		outputHandler.askPassTypeSelection();
		StudyCafePassType passType = inputHandler.getPassTypeSelectingUserAction();

		final List<StudyCafePass> passCandidates = findPassCandidatesBy(passType);

		outputHandler.showPassListForSelection(passCandidates);
		return inputHandler.getSelectPass(passCandidates);
	}

	private List<StudyCafePass> findPassCandidatesBy(final StudyCafePassType studyCafePassType) {
		List<StudyCafePass> allPasses = studyCafeFileHandler.readStudyCafePasses();

		return allPasses.stream()
			.filter(studyCafePass -> studyCafePass.getPassType() == studyCafePassType)
			.toList();
	}

	private Optional<StudyCafeLockerPass> selectLockerPass(final StudyCafePass selectedPass) {
		if (selectedPass.getPassType() != StudyCafePassType.FIXED){
			return Optional.empty();
		}

		final StudyCafeLockerPass lockerPassCandidate = findLockerPassCandidateBy(selectedPass);

		if (lockerPassCandidate != null) {
			outputHandler.askLockerPass(lockerPassCandidate);
			boolean isLockerSelected = inputHandler.getLockerSelection();

			if (isLockerSelected) {
				return Optional.of(lockerPassCandidate);
			}
		}

		return Optional.empty();
	}

	private StudyCafeLockerPass findLockerPassCandidateBy(final StudyCafePass pass) {
		List<StudyCafeLockerPass> allLockerPasses = studyCafeFileHandler.readLockerPasses();

		return allLockerPasses.stream()
			.filter(lockerPass ->
				lockerPass.getPassType() == pass.getPassType()
					&& lockerPass.getDuration() == pass.getDuration()
			)
			.findFirst()
			.orElse(null);
	}

}
