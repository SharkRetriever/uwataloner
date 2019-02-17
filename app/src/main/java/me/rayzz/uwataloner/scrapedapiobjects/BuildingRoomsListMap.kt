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
package me.rayzz.uwataloner.scrapedapiobjects

/**
 * Stores a map from every building to known classrooms
 * Map<Building, List<String>>
 * GetKey then forms a BuildingsList!
 */
object BuildingRoomsListMap {
    fun getBuildingRoomsListMap(): HashMap<String, List<String>> {
        return getFullRiskBuildingRoomsListMap()
    }

    private fun getFullRiskBuildingRoomsListMap(): HashMap<String, List<String>> {
        return hashMapOf(
            "AHS – AHS Expansion" to listOf("1689"),
            "AL – Arts Lecture Hall" to listOf("105", "113", "116", "124", "208", "209", "210", "211", "6"),
            "ARC – School of Architecture" to listOf("1001", "1101", "3102", "3103"),
            "B1 – Biology 1" to listOf("271", "370", "374"),
            "B2 – Biology 2" to listOf("150", "151", "350", "351", "355"),
            "BMH – B.C. Matthews Hall" to listOf("1016", "1621", "1703", "2401", "2703"),
            "C2 – Chemistry 2" to listOf("160"),
            "CGR – Conrad Grebel University College Apartments" to listOf("1111", "1208", "1300", "1301", "1302"),
            "CPH – Carl A. Pollock Hall" to listOf("1324", "1333", "1346", "2367", "3681"),
            "DC – William G. Davis Computer Research Centre" to listOf("1350", "1351"),
            "DMS – Digital Media Stratford" to listOf("1004", "2022", "2024", "3022", "3024"),
            "DWE – Douglas Wright Engineering Building" to listOf("1407", "1427", "1501", "1502", "1515", "1518", "2402", "2527", "2529", "3516", "3517", "3518", "3519", "3522", "3522A"),
            "E2 – Engineering 2" to listOf("1732", "1736", "1792", "2356", "2356A", "2363", "3341", "3344", "3346", "3347", "3348"),
            "E3 – Engineering 3" to listOf("2103", "2119", "3155", "3164", "3178"),
            "E5 – Engineering 5" to listOf("3101", "3102", "4106", "4128", "6004", "6006", "6008", "6127"),
            "E6 – Engineering 6" to listOf("2024", "4022"),
            "E7 – Engineering 7" to listOf("2317", "2328", "3343", "3353", "4043", "4053", "4417", "4433", "5343"),
            "ECH – East Campus Hall" to listOf("1205", "1218", "1219", "1220", "1222", "1224A", "1230", "1237"),
            "EIT – Centre for Environmental and Information Technology" to listOf("1009", "1013", "1015"),
            "EV1 – Environment 1" to listOf("131", "132", "225", "240", "350"),
            "EV2 – Environment 2" to listOf("1002A", "2002", "2069"),
            "EV3 – Environment 3" to listOf("1408", "2402", "3406", "3408", "3412", "4408", "4412"),
            "HH – J.G. Hagey Hall of the Humanities" to listOf("1101", "1102", "1104", "1106", "1108", "119", "123", "124", "138", "139", "150", "159", "180", "2104", "2107", "227", "259", "280", "334", "336", "344", "345"),
            "IHB – Integrated Health Building" to listOf("0018", "2018"),
            "M3 – Mathematics 3" to listOf("1006"),
            "MC – Mathematics & Computer Building" to listOf("1056", "1085", "2017", "2034", "2035", "2038", "2054", "2062", "2065", "2066", "3003", "3004", "3005", "3006", "3027", "4020", "4021", "4040", "4041", "4042", "4045", "4058", "4059", "4060", "4061", "4063", "4064"),
            "ML – Modern Languages" to listOf("109", "113", "117", "246", "259", "349", "354"),
            "OPT – Optometry" to listOf("1129", "149", "347", "401", "402", "420", "440"),
            "PAS – Psychology, Anthropology, Sociology" to listOf("1229", "1237", "1241", "2083", "2084", "2085", "2086", "2271"),
            "PHR – Pharmacy" to listOf("1004", "1008", "1012", "2019", "3026"),
            "PHY – Physics" to listOf("145", "150", "235", "309J", "313", "342"),
            "QNC – Mike & Ophelia Lazaridis Quantum-Nano Centre" to listOf("1502", "1506", "1507", "2501", "2502", "2611"),
            "RCH – J.R. Coutts Engineering Lecture Hall" to listOf("101", "103", "105", "106", "108", "109", "110", "112", "204", "205", "206", "207", "208", "209", "211", "212", "301", "302", "305", "306", "307", "308", "309"),
            "REN – Renison University College Original Building" to listOf("0104", "0201", "0402", "0403", "0901", "1918", "1928", "2102", "2104", "2106", "2107", "2918", "2920", "2922", "2924", "2926", "2928", "2930"),
            "SJ1 – N/A" to listOf("2009", "2011", "3013", "3014", "3015", "3016", "3020", "3027"),
            "SJ2 – N/A" to listOf("1002", "1004", "2001", "2002", "2003", "2007"),
            "STC – Science Teaching Complex" to listOf("0010", "0020", "0040", "0050", "0060", "1012", "3029", "3039"),
            "STP – St. Paul's United College Main Building" to listOf("105", "201", "256")
        )
    }
}