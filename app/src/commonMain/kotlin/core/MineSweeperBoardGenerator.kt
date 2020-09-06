package core

import core.BoardGenerator.Index

interface BoardGenerator {
    operator fun invoke(level: LEVEL): Array<Array<Cell>>
    fun getCellIndices(cellId: Int, columnSize: Int): Index
    data class Index(val row: Int, val column: Int)
}

internal open class MineSweeperBoardGenerator : BoardGenerator {

    override operator fun invoke(level: LEVEL): Array<Array<Cell>> {
        return fillCells(level)
    }

    private fun fillCells(level: LEVEL): Array<Array<Cell>> {
        val bombIds = randomBombIds(level)
        return Array(level.rows) { i ->
            Array(level.columns) { j ->
                val id = generateUniqueId(j, level.columns, i)
                if (bombIds.contains(id)) {
                    Cell(id, Value.BOMB)
                } else {
                    val value = countNeighbouringBombs(bombIds, id, level.columns, level.rows).first
                    Cell(id, Value.NUMBER(value))
                }
            }
        }
    }

    internal fun generateUniqueId(columnIndex: Int, columnSize: Int, rowIndex: Int): Int {
        return columnIndex + columnSize * rowIndex
    }

    override fun getCellIndices(cellId: Int, columnSize: Int): Index {
        val rowIndex: Int = cellId / columnSize
        val columnIndex = cellId - columnSize * rowIndex
        return Index(rowIndex, columnIndex)
    }

    protected open fun randomBombIds(level: LEVEL): List<Int> {
        val totalCells = level.columns * level.rows
        return (0 until totalCells).shuffled().take(level.bombs).sorted()
    }


    internal fun countNeighbouringBombs(bombIds: List<Int>, id: Int, columnSize: Int, rowSize: Int)
            : Pair<Int, List<Int>> {
        val cell = Cell(id, Value.NUMBER(0))
        val arrayOfNeighbours = cell.neighbouringIds(columnSize = columnSize, rowSize = rowSize)

        var totalBombs = 0
        val bombsNear = mutableListOf<Int>()
        arrayOfNeighbours.forEach {
            if (bombIds.contains(it)) {
                totalBombs++
                bombsNear.add(it)
            }
        }
        return totalBombs to bombsNear
    }
}

internal fun Cell.neighbouringIds(rowSize: Int, columnSize: Int): List<Int> {
    //neighbours to right
    val r1 = id + 1
    val r2 = id - columnSize + 1
    val r3 = id + columnSize + 1
    // neighbours to left
    val l1 = id - 1
    val l2 = id - columnSize - 1
    val l3 = id + columnSize - 1
    // bottom
    val b1 = id + columnSize
    // top
    val t1 = id - columnSize

    val rightColumn = arrayOf(r1, r2, r3)
    val leftColumn = arrayOf(l1, l2, l3)
    val arrayOfPossibleNeighbours = mutableListOf(*leftColumn, *rightColumn, b1, t1)
    if (isAtRightBorder(columnSize)) {
        arrayOfPossibleNeighbours.removeAll(rightColumn)
    } else if (isAtLeftBorder(columnSize)) {
        arrayOfPossibleNeighbours.removeAll(leftColumn)
    }

    if (isAtTopBorder(columnSize)) {
        arrayOfPossibleNeighbours.removeAll(listOf(r2, t1, l2))
    } else if (isAtBottomBorder(columnSize, rowSize)) {
        arrayOfPossibleNeighbours.removeAll(listOf(r3, b1, l3))
    }
    return arrayOfPossibleNeighbours
}

internal fun Cell.isAtRightBorder(columnSize: Int) = (id + 1) % columnSize == 0

internal fun Cell.isAtTopBorder(columnSize: Int) = id < columnSize

internal fun Cell.isAtBottomBorder(columnSize: Int, rowSize: Int) =
    id > ((columnSize * rowSize) - columnSize) - 1

internal fun Cell.isAtLeftBorder(columnSize: Int) = id % columnSize == 0