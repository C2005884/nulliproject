package com.example.nulli.api.reverse_geocoding


import androidx.annotation.Keep

@Keep
data class ReverseGeocodingResponse(
    val results: List<Result>,
    val status: Status
) {
    @Keep
    data class Result(
        val code: Code,
        val land: Land,
        val name: String,
        val region: Region
    ) {
        @Keep
        data class Code(
            val id: String,
            val mappingId: String,
            val type: String
        )

        @Keep
        data class Land(
            val addition0: Addition0,
            val addition1: Addition1,
            val addition2: Addition2,
            val addition3: Addition3,
            val addition4: Addition4,
            val coords: Coords,
            val name: String,
            val number1: String,
            val number2: String,
            val type: String
        ) {
            @Keep
            data class Addition0(
                val type: String,
                val value: String
            )

            @Keep
            data class Addition1(
                val type: String,
                val value: String
            )

            @Keep
            data class Addition2(
                val type: String,
                val value: String
            )

            @Keep
            data class Addition3(
                val type: String,
                val value: String
            )

            @Keep
            data class Addition4(
                val type: String,
                val value: String
            )

            @Keep
            data class Coords(
                val center: Center
            ) {
                @Keep
                data class Center(
                    val crs: String,
                    val x: Double,
                    val y: Double
                )
            }
        }

        @Keep
        data class Region(
            val area0: Area0,
            val area1: Area1,
            val area2: Area2,
            val area3: Area3,
            val area4: Area4
        ) {
            @Keep
            data class Area0(
                val coords: Coords,
                val name: String
            ) {
                @Keep
                data class Coords(
                    val center: Center
                ) {
                    @Keep
                    data class Center(
                        val crs: String,
                        val x: Double,
                        val y: Double
                    )
                }
            }

            @Keep
            data class Area1(
                val alias: String,
                val coords: Coords,
                val name: String
            ) {
                @Keep
                data class Coords(
                    val center: Center
                ) {
                    @Keep
                    data class Center(
                        val crs: String,
                        val x: Double,
                        val y: Double
                    )
                }
            }

            @Keep
            data class Area2(
                val coords: Coords,
                val name: String
            ) {
                @Keep
                data class Coords(
                    val center: Center
                ) {
                    @Keep
                    data class Center(
                        val crs: String,
                        val x: Double,
                        val y: Double
                    )
                }
            }

            @Keep
            data class Area3(
                val coords: Coords,
                val name: String
            ) {
                @Keep
                data class Coords(
                    val center: Center
                ) {
                    @Keep
                    data class Center(
                        val crs: String,
                        val x: Double,
                        val y: Double
                    )
                }
            }

            @Keep
            data class Area4(
                val coords: Coords,
                val name: String
            ) {
                @Keep
                data class Coords(
                    val center: Center
                ) {
                    @Keep
                    data class Center(
                        val crs: String,
                        val x: Double,
                        val y: Double
                    )
                }
            }
        }
    }

    @Keep
    data class Status(
        val code: Int,
        val message: String,
        val name: String
    )

    fun getLocation() : String {
        val location =
            results[0].region.area1.name + " " +
                    results[0].region.area2.name +
                    results[0].land.name + " " +
                    results[0].land.number1 + " " +
                    results[0].land.number2 + " " +
                    results[0].land.addition0.value

        return location
    }
}