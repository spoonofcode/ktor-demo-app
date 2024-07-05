package com.spoonofcode.repository

import com.spoonofcode.plugins.dbQuery
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

interface CrudRepository<RQ, RS> {
    suspend fun create(request: RQ): RS
    suspend fun read(id: Int): RS?
    suspend fun update(id: Int, request: RQ): Boolean
    suspend fun delete(id: Int): Boolean
    suspend fun readAll(): List<RS>
}

abstract class GenericCrudRepository<T : IntIdTable, RQ, RS>(
    private val table: T,
    private val toResultRow: (RQ) -> Map<Column<*>, Any?>,
    val toResponse: (ResultRow) -> RS
) : CrudRepository<RQ, RS> {

    override suspend fun create(request: RQ): RS = dbQuery {
        val id = table.insertAndGetId { statement ->
            toResultRow(request).forEach { (column, value) ->
                statement[column as Column<Any?>] = value
            }
        }.value

        val insertedRow = table.select { (table.primaryKey!!.columns[0] as Column<Int>) eq id }.single()
        toResponse(insertedRow)
    }

    override suspend fun read(id: Int): RS? = dbQuery {
        table.select { (table.primaryKey!!.columns[0] as Column<Int>) eq id }
            .singleOrNull()?.let { toResponse(it) }
    }

    override suspend fun update(id: Int, request: RQ): Boolean = dbQuery {
        table.update({ (table.primaryKey!!.columns[0] as Column<Int>) eq id }) { statement ->
            toResultRow(request).forEach { (column, value) ->
                statement[column as Column<Any?>] = value
            }
        } > 0
    }

    override suspend fun delete(id: Int): Boolean = dbQuery {
        table.deleteWhere { (table.primaryKey!!.columns[0] as Column<Int>) eq id } > 0
    }

    override suspend fun readAll(): List<RS> = dbQuery {
        table.selectAll().map(toResponse)
    }
}