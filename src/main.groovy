boolean compareWarnings(before, after) {
    boolean result = true
    before.forEach()
    before.forEach { module, info ->
        info.forEach { checker, warnings ->
            if (after[module][checker] > warnings) {
                println "${module}.${checker}: ${warnings}->${after[module][checker]}"
                result = false
            }
        }
    }
    return result
}


