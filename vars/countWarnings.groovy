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
    def f = new File("${prefix}/target/checkstyle-result.xml")
    if (f.exists()) {
        def checkstyle = new XmlSlurper().parseText(f.text)
        count.put("checkstyle", checkstyle.file.error.size())
    }

    f = new File("${prefix}/target/spotbugsXml.xml")
    if (f.exists()) {
        def bugCollection = new XmlSlurper().parseText(f.text)
        count.put("findbugs", bugCollection.BugInstance.size())
    }

    f = new File("${prefix}/target/eslint.xml")
    if (f.exists()) {
        def eslint = new XmlSlurper().parseText(f.text)
        count.put("eslint", eslint.file.error.size())
    }

    count
}
