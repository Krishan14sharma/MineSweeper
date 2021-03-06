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

    fun getCellGrid(): Array<Array<Cell>> {
        return cells
    }

    fun traverseCells(block: (cell: Cell) -> Unit) {
        for (arrayOfCells in cells) {
            for (cell in arrayOfCells) {
                block(cell)
            }
        }
    }

    fun open(cellId: Int) {
        val (row, column) = mineSweeperBoardGenerator.getCellIndices(cellId, level.columns)
        val cell = cells[row][column]
        val open = cell.open()
        if (!open.correct!!) {
            // todo send flagged wrong moves
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
        val openedCells: MutableList<Cell> = mutableListOf()
        fun openNeighbours(cell: Cell) {
            val neighbourIds = cell.neighbouringIds(level.rows, level.columns)
            for (pair in neighbourIds.map { id ->
                mineSweeperBoardGenerator.getCellIndices(id, level.columns)
            }) {
                val (row, column) = pair
                val neighbourCell = cells[row][column]
                if (neighbourCell.state is Cell.State.Close &&
                    neighbourCell.state.getDisplayState() == ""
                ) {
                    neighbourCell.open()
                    openedCells.add(neighbourCell)
                    openNeighbours(neighbourCell)
                }
            }
        }
        // use bfs to traverse each element and add if zero
        // 0
        openNeighbours(cell)
        return openedCells
    }

    fun flag(cellId: Int): Cell {
        val (row, column) = mineSweeperBoardGenerator.getCellIndices(cellId, level.columns)
        val cell = cells[row][column]
        cell.flag()
        return cell
    }

    fun unFlag(cellId: Int): Cell {
        val (row, column) = mineSweeperBoardGenerator.getCellIndices(cellId, level.columns)
        val cell = cells[row][column]
        cell.unFlag()
        return cell
    }
}

interface MineSweeperBoardListener {
    fun inCorrectMove(cell: Cell, inCorrectFlags: List<Cell> = emptyList())
    fun correctMove(cell: Cell, openCells: List<Cell>)
}


enum class LEVEL(val rows: Int, val columns: Int, val bombs: Int) {
    BEGINNER(10, 10, 10), INTERMEDIATE(16, 16, 40), HARD(16, 30, 100)
}

// when you click on zero value it opens all neighbouring with zeroes
// in the beginning it opens zeroes and zeroes neighbours some random numbers as well based on level of course