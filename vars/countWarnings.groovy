def call(modules = ['.']){
    def warningsMap = [:]
    for (module in modules){

        def text = new File("${env.WORKSPACE}/${module}/target/checkstyle-result.xml").text
        def checkstyle = new XmlSlurper().parseText(text)
        def csCount = checkstyle.file.error.size()

        text = new File("${env.WORKSPACE}/${module}/target/spotbugsXml.xml").text
        def bugCollection = new XmlSlurper().parseText(text)
        def fbCount = bugCollection.BugInstance.size()

        warningsMap.put(module, [checkstyle: csCount, findbugs: fbCount])
    }
    return warningsMap
}
