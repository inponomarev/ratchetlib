def call(modules = []) {
    def warningsMap = [:]
    if (modules) {
        modules.each { module ->
            warningsMap.put(module, countModule("${env.WORKSPACE}/${module}"))
        }
    } else {
        warningsMap.put("project", countModule("${env.WORKSPACE}"))
    }
    warningsMap
}

private Map countModule(prefix) {
    def count = [:]
    new File("${prefix}/target/checkstyle-result.xml").with {
        if (exists()) {
            def checkstyle = new XmlSlurper().parseText(text)
            count.put("checkstyle", checkstyle.file.error.size())
        }
    }
    new File("${prefix}/target/spotbugsXml.xml").with {
        if (exists()) {
            def bugCollection = new XmlSlurper().parseText(text)
            count.put("findbugs", bugCollection.BugInstance.size())
        }
    }
    new File("${prefix}/target/eslint.xml").with {
        if (exists()) {
            def eslint = new XmlSlurper().parseText(text)
            count.put("eslint", eslint.file.error.size())
        }
    }
    count
}
