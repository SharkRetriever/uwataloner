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

import me.rayzz.uwataloner.apimodels.Subject

/**
 * Stores a list of all known subjects, stored in Subject objects
 */
@Deprecated("Unused -- to be refactored into newer service in later version")
object SubjectsList {
    // GET /codes/subjects.{format}
    fun getSubjectsList(): List<Subject> {
        return listOf(
            Subject("ACC", "Accounting"),
            Subject("ACINTY", "Academic Integrity"),
            Subject("ACTSC", "Actuarial Science"),
            Subject("AFM", "Accounting and Financial Managment"),
            Subject("AHS", "Applied Health Sciences"),
            Subject("AMATH", "Applied Mathematics"),
            Subject("ANTH", "Anthropology"),
            Subject("ARBUS", "Arts and Business"),
            Subject("ARCH", "Architecture"),
            Subject("ARTS", "Arts"),
            Subject("AVIA", "Aviation"),
            Subject("BE", "Business Entrepreneurship"),
            Subject("BET", "Business, Entrepreneurship and Technology"),
            Subject("BIOL", "Biology"),
            Subject("BME", "Biomedical Engineering"),
            Subject("BUS", "Business"),
            Subject("CHE", "Chemical Engineering"),
            Subject("CHEM", "Chemistry"),
            Subject("CHINA", "Chinese"),
            Subject("CIVE", "Civil Engineering"),
            Subject("CLAS", "Classical Studies"),
            Subject("CM", "Computational Mathematics"),
            Subject("CO", "Combinatorics and Optimization"),
            Subject("COMM", "Commerce"),
            Subject("CS", "Computer Science"),
            Subject("CT", "Catholic Thought"),
            Subject("CULT", "Cultural Studies"),
            Subject("DAC", "Digital Arts Communication"),
            Subject("DEI", "Digital Experience Innovation"),
            Subject("DRAMA", "Drama"),
            Subject("DUTCH", "Dutch"),
            Subject("EARTH", "Earth Science"),
            Subject("EASIA", "East Asian Studies"),
            Subject("ECE", "Electrical and Computer Engineering"),
            Subject("ECON", "Economics"),
            Subject("EFAS", "English for Academic Success"),
            Subject("ELPE", "English Language Proficiency Examination"),
            Subject("EMLS", "English for Multilingual Speakers"),
            Subject("ENBUS", "Environment and Business"),
            Subject("ENGL", "English"),
            Subject("ENVE", "Environmental Engineering"),
            Subject("ENVS", "Environmental Studies"),
            Subject("ERS", "Environment and Resource Studies"),
            Subject("FINE", "Fine Arts"),
            Subject("FR", "French Studies"),
            Subject("GBDA", "Global Business and Digital Arts"),
            Subject("GENE", "General Engineering"),
            Subject("GEOE", "Geological Engineering"),
            Subject("GEOG", "Geography"),
            Subject("GER", "German"),
            Subject("GERON", "Gerontology"),
            Subject("GGOV", "Global Governance"),
            Subject("GS", "Graduate Studies"),
            Subject("HIST", "History"),
            Subject("HLTH", "Health Studies"),
            Subject("HRM", "Human Resources Management"),
            Subject("INDEV", "International Development"),
            Subject("INTEG", "Integrated Studies"),
            Subject("INTST", "International Studies"),
            Subject("IS", "Independent Studies"),
            Subject("ITALST", "Italian Studies"),
            Subject("JAPAN", "Japanese"),
            Subject("JS", "Jewish Studies"),
            Subject("KIN", "Kinesiology"),
            Subject("KOREA", "Korean"),
            Subject("LS", "Legal Studies"),
            Subject("MATH", "Mathematics"),
            Subject("ME", "Mechanical Engineering"),
            Subject("MNS", "Materials and Nano-Sciences"),
            Subject("MSCI", "Management Sciences"),
            Subject("MTE", "Mechatronics Engineering"),
            Subject("MTHEL", "Mathematics Elective"),
            Subject("MUSIC", "Music"),
            Subject("NANO", "Nanotechnology"),
            Subject("NE", "Nanotechnology Engineering"),
            Subject("OPTOM", "Optometry"),
            Subject("PACS", "Peace and Conflict Studies"),
            Subject("PD", "Professional Development"),
            Subject("PDARCH", "Professional Development for Architecture Students"),
            Subject("PDPHRM", "Professional Development for Pharmacy Students"),
            Subject("PHARM", "Pharmacy"),
            Subject("PHIL", "Philosophy"),
            Subject("PHS", "Public Health Sciences"),
            Subject("PHYS", "Physics"),
            Subject("PLAN", "Planning"),
            Subject("PMATH", "Pure Mathematics"),
            Subject("PS", "Public Serivce"),
            Subject("PSCI", "Political Science"),
            Subject("PSYCH", "Psychology"),
            Subject("QIC", "Quantum Information and Computation"),
            Subject("REC", "Recreation and Leisure Studies"),
            Subject("RS", "Religious Studies"),
            Subject("SCI", "Science"),
            Subject("SDS", "Social Development Studies"),
            Subject("SE", "Software Engineering"),
            Subject("SI", "Studies in Islam"),
            Subject("SMF", "Sexuality, Marriage and the Family"),
            Subject("SOC", "Sociology"),
            Subject("SOCIN", "Social Innovation"),
            Subject("SOCWK", "Social Work (Social Development Studies)"),
            Subject("SPAN", "Spanish"),
            Subject("SPCOM", "Speech Communication"),
            Subject("STAT", "Statistics"),
            Subject("STV", "Society, Technology and Values"),
            Subject("SUSM", "Sustainability Management"),
            Subject("SWK", "Social Work"),
            Subject("SWREN", "Social Work (Bachelor of Social Work)"),
            Subject("SYDE", "Systems Design Engineering"),
            Subject("TAX", "Taxation"),
            Subject("TN", "Theoretical Neuroscience"),
            Subject("TPM", "Technical Presentation Milestone"),
            Subject("TS", "Theological Studies"),
            Subject("VCULT", "Visual Culture"),
            Subject("WHMIS", "Workplace Hazardous Materials Information Systems"),
            Subject("WKRPT", "Work-term Report"),
            Subject("WS", "Women's Studies")
        )
    }
}
