package com.productboard.dgsdemo.plane

import com.productboard.dgddemo.jooq.Tables.PLANES
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.impl.DSL
import org.springframework.stereotype.Repository

@Repository
class PlaneRepository(private val jooqContext: DSLContext) {

    fun get(sign: PlaneSign): Plane? =
        jooqContext.select(DSL.asterisk()).from(PLANES).where(PLANES.SIGN.eq(sign)).fetchOne { deserializePlane(it) }

    fun create(createPlane: CreatePlane): Plane =
        jooqContext
            .insertInto(PLANES)
            .set(PLANES.SIGN, createPlane.sign)
            .set(PLANES.TYPE, createPlane.type)
            .returningResult(DSL.asterisk())
            .fetchOne { deserializePlane(it) }!!

    private fun deserializePlane(record: Record): Plane = Plane(sign = record[PLANES.SIGN], type = record[PLANES.TYPE])
}
