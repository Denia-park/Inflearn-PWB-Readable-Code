package cleancode.minesweeper.tobe.position;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cleancode.minesweeper.tobe.cell.Cell;

public class CellPositions {
	private final List<CellPosition> positions;

	private CellPositions(List<CellPosition> positions) {
		this.positions = positions;
	}

	public static CellPositions of(List<CellPosition> cellPositions) {
		return new CellPositions(cellPositions);
	}

	public static CellPositions from(Cell[][] board) {
		List<CellPosition> cellPositions = new ArrayList<>();

		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {
				cellPositions.add(CellPosition.of(row, col));
			}
		}

		return of(cellPositions);
	}

	public List<CellPosition> getPositions() {
		return new ArrayList<>(this.positions);
	}

	public List<CellPosition> extractRandomPositions(int count) {
		List<CellPosition> cellPositions = new ArrayList<>(this.positions);

		Collections.shuffle(cellPositions);
		return cellPositions.subList(0, count);
	}

	public List<CellPosition> subtract(List<CellPosition> positionListToSubtract) {
		List<CellPosition> cellPositions = new ArrayList<>(this.positions);
		CellPositions positionsToSubtract = CellPositions.of(positionListToSubtract);

		return cellPositions.stream()
			.filter(positionsToSubtract::doesNotContain)
			.toList();
	}

	private boolean doesNotContain(CellPosition position) {
		return !positions.contains(position);
	}
}
