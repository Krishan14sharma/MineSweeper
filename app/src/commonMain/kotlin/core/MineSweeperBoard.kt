package core

/**
 * Created by krishan on 18/07/20.
 */
class MineSweeperBoard(level: LEVEL, mineSweeperBoardGenerator: BoardGenerator) {

    val noOfCells = level.rows * level.columns

    val cells: Array<Array<Cell>> = mineSweeperBoardGenerator.invoke(level)

    fun traverseCells(block: (cell: Cell) -> Unit) {
        for (cell in cells) {
            for (cell in cell) {
                block(cell)
            }
        }
    }

}

enum class LEVEL(val rows: Int, val columns: Int, val bombs: Int) {
    BEGINNER(10, 10, 10), INTERMEDIATE(16, 16, 40), HARD(16, 30, 100)
}
