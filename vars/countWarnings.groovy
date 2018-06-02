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
    def text = new File("${prefix}/target/checkstyle-result.xml").text
    def checkstyle = new XmlSlurper().parseText(text)
    def csCount = checkstyle.file.error.size()

    text = new File("${prefix}/target/spotbugsXml.xml").text
    def bugCollection = new XmlSlurper().parseText(text)
    def fbCount = bugCollection.BugInstance.size()

    [checkstyle: csCount, findbugs: fbCount]
}
