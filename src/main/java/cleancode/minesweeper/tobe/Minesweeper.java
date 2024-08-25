package cleancode.minesweeper.tobe;

import cleancode.minesweeper.tobe.game.GameInitializable;
import cleancode.minesweeper.tobe.game.GameRunnable;
import cleancode.minesweeper.tobe.gamelevel.GameLevel;
import cleancode.minesweeper.tobe.io.InputHandler;
import cleancode.minesweeper.tobe.io.OutputHandler;

public class Minesweeper implements GameInitializable, GameRunnable {
	private final GameBoard gameBoard;
	private final BoardIndexConverter boardIndexConverter = new BoardIndexConverter();

	// 상위 모듈[Minesweeper]에서 하위 모듈[ConsoleXxxHandler]을 직접적으로 의존하면, 변경에 유연하게 대처하지 못함 (Ex. Console -> Web)
	private final InputHandler inputHandler;
	private final OutputHandler outputHandler;

	private int gameStatus = 0; // 0: 게임 중, 1: 승리, -1: 패배

	public Minesweeper(GameLevel gameLevel, InputHandler inputHandler, OutputHandler outputHandler) {
		gameBoard = new GameBoard(gameLevel);
		this.inputHandler = inputHandler;
		this.outputHandler = outputHandler;
	}

	@Override
	public void initialize() {
		gameBoard.initializeGame();
	}

	@Override
	public void run() {
		outputHandler.showGameStartComments();

		while (true) {
			try {
				outputHandler.showBoard(gameBoard);

				if (doesUserWinTheGame()) {
					outputHandler.showGameWinningComment();
					break;
				}
				if (doesUserLoseTheGame()) {
					outputHandler.showGameLosingComment();
					break;
				}

				final String cellInput = getCellInputFromUser();
				final String userActionInput = getUserActionInputFromUser();
				actOnCell(cellInput, userActionInput);
			} catch (GameException e) {
				outputHandler.showExceptionMessage(e);
			} catch (Exception e) {
				outputHandler.showSimpleMessage("프로그램에 문제가 생겼습니다.");
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
		outputHandler.showCommentForUserAction();
		return inputHandler.getUserInput();
	}

	private String getCellInputFromUser() {
		outputHandler.showCommentForSelectingCell();
		return inputHandler.getUserInput();
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
