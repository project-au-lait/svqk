
def file = new File(request.artifactId + '/.gitignore.archetype')
file.renameTo request.artifactId + '/.gitignore'
