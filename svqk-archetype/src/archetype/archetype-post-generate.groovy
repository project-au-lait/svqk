def setupGitignore() {
    def projectDir = request.artifactId
    def fromPath = "${projectDir}/.gitignore.archetype"
    def toPath = "${projectDir}/.gitignore"
    def file = new File(fromPath)
    file.renameTo(new File(toPath))
}

def makeMvnwExecutableIfNotWin() {
    def osName = System.getProperty("os.name").toLowerCase()
    if (!osName.contains("windows")) {
        def projectDir = request.artifactId
        def file = new File("${projectDir}/mvnw")
        file.setExecutable(true, true)
    }
}

setupGitignore()
makeMvnwExecutableIfNotWin()
