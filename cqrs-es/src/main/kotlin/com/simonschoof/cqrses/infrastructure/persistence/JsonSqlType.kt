package com.simonschoof.cqrses.infrastructure.persistence

import com.fasterxml.jackson.databind.JavaType
import com.simonschoof.cqrses.configs.objectMapper
import org.ktorm.schema.BaseTable
import org.ktorm.schema.Column
import org.ktorm.schema.SqlType
import org.ktorm.schema.typeOf
import org.postgresql.util.PGobject
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Types

class JsonSqlType<T : Any>(
    private val javaType: JavaType
) : SqlType<T>(Types.VARCHAR, "jsonb") {

    override fun doSetParameter(ps: PreparedStatement, index: Int, parameter: T) {
        val eventJsonString = objectMapper.writeValueAsString(parameter)
        val eventJsonObject = PGobject()

        eventJsonObject.type = "json"
        eventJsonObject.value = eventJsonString

        ps.setObject(index, eventJsonObject)
    }

    override fun doGetResult(rs: ResultSet, index: Int): T? {
        val json = rs.getString(index)
        return if (json.isNullOrBlank()) {
            null
        } else {
            objectMapper.readValue(json, javaType)
        }
    }
}

inline fun <reified C : Any> BaseTable<*>.jsonb(
    name: String
): Column<C> {
    return registerColumn(name, JsonSqlType(objectMapper.constructType(typeOf<C>())))
}