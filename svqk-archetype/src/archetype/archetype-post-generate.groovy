def renameFile(String fromPath, String toPath) {
    def file = new File(fromPath)
    file.renameTo(new File(toPath))
}

def makeFileExecutable(String path) {
    def file = new File(path)
    file.setExecutable(true, true)
}

def projectDir = request.artifactId

renameFile("${projectDir}/.gitignore.archetype", "${projectDir}/.gitignore")

def osName = System.getProperty("os.name").toLowerCase()
if (!osName.contains("windows")) {
    makeFileExecutable("${projectDir}/mvnw")
}
