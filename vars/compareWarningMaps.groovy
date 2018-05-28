def call(Map before, Map after) {
    boolean result = true
    def msg = ''
    before.each { module, info ->
        info.each { checker, warnings ->
            def newWarnings = after[module][checker]
            if (newWarnings > warnings) {
                msg <<= "\n+ "
                result = false
            } else if (newWarnings < warnings) {
                msg <<= "\n- "
            } else
                msg <<= "\n= "
            msg <<= "${module}.${checker}: ${warnings}->${after[module][checker]}"
        }
    }
    echo "${msg}"
    if (result){
        echo "Ratcheting: no new issues found"
    } else {
        error "Ratcheting failed, see messages above."
    }
}



