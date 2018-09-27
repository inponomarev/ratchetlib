def call(Map before, Map after) {
    boolean result = true
    msg = ''

    Set modules = before.keySet() + after.keySet()

    modules.each { module ->
        oldInfo = before[module] ?: [:]
        newInfo = after[module] ?: [:]

        Set checkers = oldInfo.keySet() + newInfo.keySet()
        checkers.each {checker ->
            oldWarnings = oldInfo[checker] ?: 0
            newWarnings = newInfo[checker] ?: 0

            if (newWarnings > oldWarnings) {
                msg <<= "\n+ "
                result = false
            } else if (newWarnings < oldWarnings) {
                msg <<= "\n- "
            } else
                msg <<= "\n= "
            msg <<= "${module}.${checker}: ${oldWarnings}->${newWarnings}"
        }
    }

    echo "${msg}"
    if (result) {
        echo "Ratcheting: no new issues found"
    } else {
        error "Ratcheting failed, see messages above."
    }
}
