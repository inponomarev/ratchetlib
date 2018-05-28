def call(Map before, Map after) {
    echo "${before.size()}"
    boolean result = true
    before.each { module, info ->
        echo "ANALYZING ${module}, ${info.size()} steps"
        info.each { checker, warnings ->
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



