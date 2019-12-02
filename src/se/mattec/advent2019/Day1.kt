package se.mattec.advent2019

import kotlin.math.max

fun main(args: Array<String>) {
    println(Day1.problem1())
    println(Day1.problem2())
}

object Day1 {

    fun problem1(): Int {
        return data
                .split("\n")
                .map { it.toInt() }
                .map { fuel(it) }
                .sumBy { it }
    }

    fun fuel(weight: Int): Int {
        return max((weight / 3) - 2, 0)
    }

    fun problem2(): Int {
        return data
                .split("\n")
                .map { it.toInt() }
                .map { fuel(it) }
                .map {
                    var fuel = it
                    var lastFuel = fuel

                    while (lastFuel != 0) {
                        lastFuel = fuel(lastFuel)
                        fuel += lastFuel
                    }

                    fuel
                }
                .sumBy { it }
    }

    private val data = """
147383
111288
130868
140148
79840
63305
98475
66403
68753
136306
94135
51317
136151
71724
68795
68526
130515
73606
56828
57778
86134
105030
123367
97633
85043
110888
110785
90662
128865
70997
90658
79944
141089
67543
78358
143579
146971
78795
94097
82473
73216
50919
100248
112751
86227
117399
123833
148570
141464
123266
94346
53871
51180
112900
119863
106694
129841
75990
63509
50135
140081
138387
112697
57023
114256
81429
95573
57056
52277
75137
53364
125823
113227
93993
129808
114025
101677
127114
65823
65834
57955
102314
60656
89982
61068
72089
71745
72460
142318
91951
111759
61177
143739
92202
70168
80164
77867
64235
141137
102636
    """.trimIndent()

}