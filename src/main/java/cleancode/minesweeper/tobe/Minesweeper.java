package cleancode.minesweeper.tobe;

import cleancode.minesweeper.tobe.gamelevel.GameLevel;
import cleancode.minesweeper.tobe.io.ConsoleInputHandler;
import cleancode.minesweeper.tobe.io.ConsoleOutputHandler;

public class Minesweeper {
	private final GameBoard gameBoard;
	private final BoardIndexConverter boardIndexConverter = new BoardIndexConverter();
	private final ConsoleInputHandler consoleInputHandler = new ConsoleInputHandler();
	private final ConsoleOutputHandler consoleOutputHandler = new ConsoleOutputHandler();
	private int gameStatus = 0; // 0: 게임 중, 1: 승리, -1: 패배

	public Minesweeper(GameLevel gameLevel) {
		gameBoard = new GameBoard(gameLevel);
	}

	public void run() {
		consoleOutputHandler.showGameStartComments();
		gameBoard.initializeGame();

		while (true) {
			try {
				consoleOutputHandler.showBoard(gameBoard);

				if (doesUserWinTheGame()) {
					consoleOutputHandler.printGameWinningComment();
					break;
				}
				if (doesUserLoseTheGame()) {
					consoleOutputHandler.printGameLosingComment();
					break;
				}

				final String cellInput = getCellInputFromUser();
				final String userActionInput = getUserActionInputFromUser();
				actOnCell(cellInput, userActionInput);
			} catch (GameException e) {
				consoleOutputHandler.printExceptionMessage(e);
			} catch (Exception e) {
				consoleOutputHandler.printSimpleMessage("프로그램에 문제가 생겼습니다.");
				// e.printStackTrace(); //실무에서는 안티 패턴
			}
		}
	}

	private void actOnCell(String cellInput, String userActionInput) {
		final int selectedColIndex = boardIndexConverter.getSelectedColIndex(cellInput, gameBoard.getColSize());
		final int selectedRowIndex = boardIndexConverter.getSelectedRowIndex(cellInput, gameBoard.getRowSize());

		if (doesUserChooseToPlantFlag(userActionInput)) {
			gameBoard.flag(selectedRowIndex, selectedColIndex);
			checkIfGameIsOver();
			return;
		}

		if (doesUserChooseToOpenCell(userActionInput)) {
			if (gameBoard.isLandMineCell(selectedRowIndex, selectedColIndex)) {
				gameBoard.open(selectedRowIndex, selectedColIndex);
				changeGameStatusToLose();
				return;
			}

			gameBoard.openSurroundedCells(selectedRowIndex, selectedColIndex);
			checkIfGameIsOver();
			return;
		}

		throw new GameException("잘못된 번호를 선택하셨습니다.");
	}

	private void changeGameStatusToLose() {
		gameStatus = -1;
	}

	private boolean doesUserChooseToOpenCell(String userActionInput) {
		return userActionInput.equals("1");
	}

	private boolean doesUserChooseToPlantFlag(String userActionInput) {
		return userActionInput.equals("2");
	}

	private String getUserActionInputFromUser() {
		consoleOutputHandler.printCommentForUserAction();
		return consoleInputHandler.getUserInput();
	}

	private String getCellInputFromUser() {
		consoleOutputHandler.printCommentForSelectingCell();
		return consoleInputHandler.getUserInput();
	}

	private boolean doesUserLoseTheGame() {
		return gameStatus == -1;
	}

	private boolean doesUserWinTheGame() {
		return gameStatus == 1;
	}

	// check 로 시작하는 메서드들은 보통 void 반환형을 가진다.
	private void checkIfGameIsOver() {
		if (gameBoard.isAllCellChecked()) {
			changeGameStatusToWin();
		}
	}

	private void changeGameStatusToWin() {
		gameStatus = 1;
	}
}
