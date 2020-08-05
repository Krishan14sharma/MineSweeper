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
        // todo improve this zero state matching
        if (open.getDisplayState() == "") {
            val listCells = openNeighbours(cell)
            mineSweeperBoardListener?.correctMove(cell, listCells)
        } else {
            mineSweeperBoardListener?.correctMove(cell, emptyList())
        }
    }

    private fun openNeighbours(cell: Cell): List<Cell> {

        // use bfs to traverse each element and add if zero
        if (cell.state.getDisplayState() == "") {
            // 0
            val neighbourIds = cell.neighbourIds(level.rows, level.columns)
            neighbourIds.forEachIndexed { index, id ->
                val (row, column) = mineSweeperBoardGenerator.getCellIndices(id, level.columns)
                cells[row][column]
            }
        }
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