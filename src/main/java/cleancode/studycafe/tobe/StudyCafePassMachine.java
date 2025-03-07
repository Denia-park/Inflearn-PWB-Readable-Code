package cleancode.studycafe.tobe;

import java.util.List;
import java.util.Optional;

import cleancode.studycafe.tobe.exception.AppException;
import cleancode.studycafe.tobe.io.StudyCafeFileHandler;
import cleancode.studycafe.tobe.io.StudyCafeIOHandler;
import cleancode.studycafe.tobe.model.pass.locker.StudyCafeLockerPass;
import cleancode.studycafe.tobe.model.pass.locker.StudyCafeLockerPasses;
import cleancode.studycafe.tobe.model.pass.StudyCafeSeatPass;
import cleancode.studycafe.tobe.model.pass.StudyCafePassType;
import cleancode.studycafe.tobe.model.pass.StudyCafeSeatPasses;

public class StudyCafePassMachine {
	private final StudyCafeIOHandler ioHandler = new StudyCafeIOHandler();
	private final StudyCafeFileHandler studyCafeFileHandler = new StudyCafeFileHandler();

	public void run() {
		try {
			ioHandler.showWelcomeMessage();
			ioHandler.showAnnouncement();

			final StudyCafeSeatPass selectedPass = selectPass();
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

	private StudyCafeSeatPass selectPass() {
		StudyCafePassType passType = ioHandler.askPassTypeSelecting();
		final List<StudyCafeSeatPass> passCandidates = findPassCandidatesBy(passType);

		return ioHandler.askPassSelecting(passCandidates);
	}

	private List<StudyCafeSeatPass> findPassCandidatesBy(final StudyCafePassType studyCafePassType) {
		StudyCafeSeatPasses allPasses = studyCafeFileHandler.readStudyCafePasses();

		return allPasses.findPassBy(studyCafePassType);
	}

	private Optional<StudyCafeLockerPass> selectLockerPass(final StudyCafeSeatPass selectedPass) {
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

	private Optional<StudyCafeLockerPass> findLockerPassCandidateBy(final StudyCafeSeatPass pass) {
		StudyCafeLockerPasses allLockerPasses = studyCafeFileHandler.readLockerPasses();

		return allLockerPasses.findLockerPassBy(pass);
	}

}
