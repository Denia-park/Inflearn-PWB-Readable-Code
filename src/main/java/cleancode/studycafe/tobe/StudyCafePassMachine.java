package cleancode.studycafe.tobe;

import java.util.List;
import java.util.Optional;

import cleancode.studycafe.tobe.exception.AppException;
import cleancode.studycafe.tobe.io.StudyCafeFileHandler;
import cleancode.studycafe.tobe.io.StudyCafeIOHandler;
import cleancode.studycafe.tobe.model.StudyCafeLockerPass;
import cleancode.studycafe.tobe.model.StudyCafeLockerPasses;
import cleancode.studycafe.tobe.model.StudyCafePass;
import cleancode.studycafe.tobe.model.StudyCafePassType;
import cleancode.studycafe.tobe.model.StudyCafePasses;

public class StudyCafePassMachine {
	private final StudyCafeIOHandler ioHandler = new StudyCafeIOHandler();
	private final StudyCafeFileHandler studyCafeFileHandler = new StudyCafeFileHandler();

	public void run() {
		try {
			ioHandler.showWelcomeMessage();
			ioHandler.showAnnouncement();

			final StudyCafePass selectedPass = selectPass();
			final Optional<StudyCafeLockerPass> optionalLockerPass = selectLockerPass(selectedPass);

			optionalLockerPass.ifPresentOrElse(
				lockerPass -> ioHandler.showPassOrderSummary(selectedPass, lockerPass),
				() -> ioHandler.showPassOrderSummary(selectedPass)
			);
		} catch (AppException e) {
			ioHandler.showSimpleMessage(e.getMessage());
		} catch (Exception e) {
			ioHandler.showSimpleMessage("알 수 없는 오류가 발생했습니다.");
		}
	}

	private StudyCafePass selectPass() {
		StudyCafePassType passType = ioHandler.askPassTypeSelecting();
		final List<StudyCafePass> passCandidates = findPassCandidatesBy(passType);

		return ioHandler.askPassSelecting(passCandidates);
	}

	private List<StudyCafePass> findPassCandidatesBy(final StudyCafePassType studyCafePassType) {
		StudyCafePasses allPasses = studyCafeFileHandler.readStudyCafePasses();

		return allPasses.findPassBy(studyCafePassType);
	}

	private Optional<StudyCafeLockerPass> selectLockerPass(final StudyCafePass selectedPass) {
		// 고정 좌석 타입이 아닌가?
		// 사물함 옵션을 사용할 수 있는 타입이 아닌가?
		if (selectedPass.cannotUseLocker()){
			return Optional.empty();
		}

		final Optional<StudyCafeLockerPass> lockerPassCandidate = findLockerPassCandidateBy(selectedPass);

		if (lockerPassCandidate.isPresent()) {
			final StudyCafeLockerPass lockerPass = lockerPassCandidate.get();

			final boolean isLockerSelected = ioHandler.askLockerPass(lockerPass);
			if (isLockerSelected) {
				return Optional.of(lockerPass);
			}
		}

		return Optional.empty();
	}

	private Optional<StudyCafeLockerPass> findLockerPassCandidateBy(final StudyCafePass pass) {
		StudyCafeLockerPasses allLockerPasses = studyCafeFileHandler.readLockerPasses();

		return allLockerPasses.findLockerPassBy(pass);
	}

}
