package ru.curs.ratcheting

boolean compareWarnings(before, after) {
    boolean result = true
    before.forEach { module, info ->
        info.forEach { checker, warnings ->
            printf "${module}.${checker}: ${warnings}->${after[module][checker]}"
            if (after[module][checker] > warnings) {
                println("<-------------!!!!")
                result = false
            } else println()
        }

    }
    return result
}

return this


