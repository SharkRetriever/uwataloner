/*
MIT License

Copyright (c) 2018 Yuanmeng Zhao (SharkRetriever)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */
package me.rayzz.uwataloner.generatedapiobjects

/**
 * Stores a map from every building to known classrooms
 * Map<Building, List<String>>
 * GetKey then forms a BuildingsList!
 */
object BuildingRoomsListMap {
    fun getBuildingRoomsListMap(): HashMap<String, List<String>> {
        return hashMapOf(
            "AHS – AHS Expansion" to listOf("1686", "1689", "3683", "3686", "3688", "3689"),
            "AL – Arts Lecture Hall" to listOf("105", "113", "116", "124", "208", "209", "210", "211", "6"),
            "ARC – School of Architecture" to listOf("1001", "1006", "1101", "2026", "3102"),
            "B1 – Biology 1" to listOf("271", "370"),
            "B2 – Biology 2" to listOf("151", "351"),
            "BMH – B.C. Matthews Hall" to listOf("1005", "1016", "1633", "2703"),
            "C2 – Chemistry 2" to listOf("273", "278"),
            "CGR – Conrad Grebel University College Apartments" to listOf("1208", "2201", "4224"),
            "CPH – Carl A. Pollock Hall" to listOf("1333", "1346", "3623", "3679", "3681", "4333", "4335"),
            "DC – William G. Davis Computer Research Centre" to listOf("1351", "2568", "3702"),
            "DWE – Douglas Wright Engineering Building" to listOf("1407", "1427", "1501", "1502", "1513", "1518", "2402", "2527", "2529", "3516", "3517", "3518", "3522", "3522A"),
            "E2 – Engineering 2" to listOf("1310", "2317", "2334", "2356", "2356A", "2363", "2364", "3341", "3344", "3346", "3347", "3348", "3356"),
            "E3 – Engineering 3" to listOf("2103", "2119", "3164"),
            "E5 – Engineering 5" to listOf("3037", "3101", "3102", "4106", "5106", "6004", "6005", "6006", "6008", "6111", "6127"),
            "E6 – Engineering 6" to listOf("2024", "4022"),
            "EC4 – East Campus 4" to listOf("1104", "2013", "2017", "2019", "2038"),
            "ECH – East Campus Hall" to listOf("1205", "1218", "1224A", "1230"),
            "EIT – Centre for Environmental and Information Technology" to listOf("1009", "1013", "1015", "2053", "3141", "3145", "3151"),
            "ERC – Energy Research Centre" to listOf("3012"),
            "ESC – Earth Sciences & Chemistry" to listOf("149"),
            "EV1 – Environment 1" to listOf("131", "132", "134", "240", "350"),
            "EV2 – Environment 2" to listOf("1002A", "2002"),
            "EV3 – Environment 3" to listOf("1408", "3406", "3408", "3412", "4408", "4412"),
            "HH – J.G. Hagey Hall of the Humanities" to listOf("1101", "1102", "1104", "1106", "1108", "119", "123", "124", "138", "139", "150", "180", "2104", "2107", "227", "259", "280", "334", "336", "344", "345"),
            "IHB – Integrated Health Building" to listOf("2018"),
            "M3 – Mathematics 3" to listOf("1006", "2134", "3103"),
            "MC – Mathematics & Computer Building" to listOf("1056", "1085", "2017", "2034", "2035", "2038", "2054", "2065", "2066", "3003", "4020", "4021", "4040", "4041", "4042", "4045", "4058", "4059", "4060", "4061", "4063", "4064", "5417", "6486"),
            "ML – Modern Languages" to listOf("113", "117", "246", "311", "349", "354"),
            "OPT – Optometry" to listOf("309", "347"),
            "PAS – Psychology, Anthropology, Sociology" to listOf("1229", "1237", "1241", "1418", "2083", "2084", "2085", "2086", "2259", "3026", "4032", "4053"),
            "PHR – Pharmacy" to listOf("1004", "1008", "2019", "3026"),
            "PHY – Physics" to listOf("235", "309A", "309J", "313", "318", "321"),
            "QNC – Mike & Ophelia Lazaridis Quantum-Nano Centre" to listOf("1201", "1502", "1506", "1507", "2501", "2502", "2611", "4104"),
            "RCH – J.R. Coutts Engineering Lecture Hall" to listOf("101", "103", "105", "108", "110", "112", "204", "205", "206", "207", "208", "209", "211", "301", "302", "305", "306", "307", "308", "309"),
            "REN – Renison University College Original Building" to listOf("0104", "0402", "0403", "1918", "2102", "2104", "2106", "2107", "2918", "2920"),
            "STC – Science Teaching Complex" to listOf("0010", "0020", "0040", "0050", "0060", "1012", "3018", "3019", "3028", "3029", "4008", "4009", "4018", "4019", "5002"),
            "STP – St. Paul's United College Main Building" to listOf("105", "201")
        )
    }
}