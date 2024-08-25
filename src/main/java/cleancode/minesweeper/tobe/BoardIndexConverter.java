package cleancode.minesweeper.tobe;

import static cleancode.minesweeper.tobe.Minesweeper.*;

public class BoardIndexConverter {
	private static final char BASE_CHAR_FOR_COL = 'a';

	public int getSelectedColIndex(String cellInput, int colSize) {
		char cellInputCol = cellInput.charAt(0);
		return convertColFrom(cellInputCol, colSize);
	}

	private int convertColFrom(char cellInputCol, int colSize) {
		int colIndex = cellInputCol - BASE_CHAR_FOR_COL;

		if(colIndex < 0 || colIndex >= colSize) {
			throw new GameException("잘못된 입력입니다.");
		}

		return colIndex;
	}

	public int getSelectedRowIndex(String cellInput, int rowSize) {
		String cellInputRow = cellInput.substring(1);
		return convertRowFrom(cellInputRow, rowSize);
	}

	private int convertRowFrom(String cellInputRow, int rowSize) {
		final int rowIndex = Integer.parseInt(cellInputRow) - 1; //index 값이기 때문에 -1을 빼준다
		if(rowIndex < 0 || rowIndex >= rowSize) {
			throw new GameException("잘못된 입력입니다.");
		}

		return rowIndex;
	}
}
