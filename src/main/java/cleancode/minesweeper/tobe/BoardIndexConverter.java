package cleancode.minesweeper.tobe;

public class BoardIndexConverter {
	private static final char BASE_CHAR_FOR_COL = 'a';

	public int getSelectedColIndex(String cellInput) {
		char cellInputCol = cellInput.charAt(0);
		return convertColFrom(cellInputCol);
	}

	private int convertColFrom(char cellInputCol) {
		int colIndex = cellInputCol - BASE_CHAR_FOR_COL;

		if(colIndex < 0) {
			throw new GameException("잘못된 입력입니다.");
		}

		return colIndex;
	}

	public int getSelectedRowIndex(String cellInput) {
		String cellInputRow = cellInput.substring(1);
		return convertRowFrom(cellInputRow);
	}

	private int convertRowFrom(String cellInputRow) {
		final int rowIndex = Integer.parseInt(cellInputRow) - 1; //index 값이기 때문에 -1을 빼준다
		if(rowIndex < 0) {
			throw new GameException("잘못된 입력입니다.");
		}

		return rowIndex;
	}
}
