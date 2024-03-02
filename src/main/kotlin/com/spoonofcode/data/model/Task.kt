package com.spoonofcode.data.model

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import org.jetbrains.exposed.sql.transactions.transaction

@Serializable
data class TaskRequest(
    val description: String,
    val isCompleted: Boolean = false,
//    val category: TaskCategory,
//    val userId: Int,
)

@Serializable
data class TaskResponse(
    val id: Int,
    val description: String,
    val creationDate: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.UTC),
    val updateDate: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.UTC),
    val isCompleted: Boolean,
//    val category: TaskCategory,
//    val userId: Int,
)

object Tasks : IntIdTable() {
    val description = varchar("description", 128)
    val creationDate = datetime("creation_date").defaultExpression(CurrentDateTime)
    val updateDate = datetime("update_date").defaultExpression(CurrentDateTime)
    val isCompleted = bool("is_completed").default(false)
//    val category = reference("category_id", TaskCategories)
//    val userId = reference("user_id", Users)
}

// We need trigger to update updateDate value on each row update
// In Exposed we don't have now any function like updateExpression
// https://github.com/JetBrains/Exposed/issues/89
fun updateTaskTrigger() {
    val sql = """
        CREATE TRIGGER update_date_trigger
        BEFORE UPDATE ON ${Tasks.tableName}
        FOR EACH ROW
        SET NEW.update_date = CURRENT_TIMESTAMP(6);
    """.trimIndent()

    transaction {
        exec(sql)
    }
}

// Custom expression
// val updateDate = datetime("update_date").defaultExpression(CustomLocalDateTime)
// Now we received:
// ERROR Exposed - MySQL 8.3 doesn't support expression 'CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6)'
// as default value. Column will be created with NULL marker.
//
//
//object CustomLocalDateTime : Expression<LocalDateTime>() {
//    override fun toQueryBuilder(queryBuilder: QueryBuilder) = queryBuilder {
//        append("CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6)")
//    }
//}