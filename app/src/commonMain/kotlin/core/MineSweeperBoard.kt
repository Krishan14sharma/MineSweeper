package core

/**
 * Created by krishan on 18/07/20.
 */
class MineSweeperBoard(
    private val level: LEVEL,
    private val mineSweeperBoardGenerator: BoardGenerator
) {

    val noOfCells = level.rows * level.columns
    var mineSweeperBoardListener: MineSweeperBoardListener? = null

    private val cells: Array<Array<Cell>> = mineSweeperBoardGenerator.invoke(level)

    fun getCells(): List<Cell> {
        val cellList = mutableListOf<Cell>()
        traverseCells { cellList.add(it) }
        return cellList
    }


    fun traverseCells(block: (cell: Cell) -> Unit) {
        for (cell in cells) {
            for (cell in cell) {
                block(cell)
            }
        }
    }

    fun open(cellId: Int) {
        val (row, column) = mineSweeperBoardGenerator.getCellIndices(cellId, level.columns)
        val cell = cells[row][column]
        val open = cell.open()
        if (!open.correct!!) {
            mineSweeperBoardListener?.inCorrectMove(cell)
            return
        }
        // todo improve this zero
        if (open.getDisplayState() == "") {

        }
    }

    private fun openNeighbours(cell: Cell) {
        // clicked cell is 0
        cell.neighbourIds(level.rows, level.columns)
    }

    fun flag() {

    }

}

interface MineSweeperBoardListener {
    fun inCorrectMove(cell: Cell)
    fun correctMove(cell: Cell, openCells: List<Cell>)
}


enum class LEVEL(val rows: Int, val columns: Int, val bombs: Int) {
    BEGINNER(10, 10, 10), INTERMEDIATE(16, 16, 40), HARD(16, 30, 100)
}

// when you click on zero value it opens all neighbouring with zeroes
// in the beginning it opens zeroes and zeroes neighbours some random numbers as well based on level of course