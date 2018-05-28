def call(Map before, Map after) {
    boolean result = true
    before.forEach { module, info ->
        info.forEach { checker, warnings ->
            def msg
            def newWarnings = after[module][checker]
            if (newWarnings > warnings) {
                msg = "+ "
                result = false
            } else if (newWarnings < warnings) {
                msg = "- "
            } else
                msg = "= "
            echo "$msg ${module}.${checker}: ${warnings}->${after[module][checker]}"
        }
    }
    if (result){
        echo "Ratcheting: no new issues found"
    } else {
        error "Ratcheting failed, see messages above."
    }
}



