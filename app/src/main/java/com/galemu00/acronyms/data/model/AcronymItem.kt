package com.galemu00.acronyms.data.model

data class AcronymItem(
    // list of definitions for an acronym
    val lfs: List<Lf>,
    // the acronym
    val sf: String
)