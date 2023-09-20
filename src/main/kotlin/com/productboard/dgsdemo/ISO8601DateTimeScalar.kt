package com.productboard.dgsdemo

import com.netflix.graphql.dgs.DgsScalar
import graphql.GraphQLContext
import graphql.execution.CoercedVariables
import graphql.language.StringValue
import graphql.language.Value
import graphql.schema.Coercing
import graphql.schema.CoercingParseLiteralException
import graphql.schema.CoercingParseValueException
import graphql.schema.CoercingSerializeException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@DgsScalar(name = "ISO8601DateTime")
class ISO8601DateTimeScalar : Coercing<LocalDateTime, String> {
    @Throws(CoercingSerializeException::class)
    override fun serialize(dataFetcherResult: Any, graphQLContext: GraphQLContext, locale: Locale): String {
        return when (dataFetcherResult) {
            is LocalDateTime -> DateTimeFormatter.ISO_DATE_TIME.format(dataFetcherResult)
            else -> throw CoercingSerializeException("Not a valid ISO8601DateTime")
        }
    }

    @Throws(CoercingParseValueException::class)
    override fun parseValue(input: Any, graphQLContext: GraphQLContext, locale: Locale): LocalDateTime {
        try {
            return LocalDateTime.parse(input as String, DateTimeFormatter.ISO_DATE_TIME)
        } catch (e: Exception) {
            throw CoercingParseValueException("Not a valid ISO8601DateTime", e)
        }
    }

    @Throws(CoercingParseLiteralException::class)
    override fun parseLiteral(
        input: Value<*>,
        coersedVariables: CoercedVariables,
        graphQLContext: GraphQLContext,
        locale: Locale
    ): LocalDateTime? {
        if (input is StringValue) {
            try {
                return LocalDateTime.parse(input.value, DateTimeFormatter.ISO_DATE_TIME)
            } catch (e: Exception) {
                throw CoercingParseLiteralException("Value is not a valid ISO8601DateTime", e)
            }
        }
        throw CoercingParseLiteralException("Value is not a valid ISO8601DateTime")
    }
}
