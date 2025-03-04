package cleancode.studycafe.hg.io.input;

import java.util.List;

import cleancode.studycafe.hg.model.StudyCafePass;
import cleancode.studycafe.hg.model.StudyCafePassType;

public interface InputHandler {
	StudyCafePassType getPassTypeSelectingUserAction();

	StudyCafePass getSelectPass(List<StudyCafePass> passes);

	boolean getLockerSelection();
}
