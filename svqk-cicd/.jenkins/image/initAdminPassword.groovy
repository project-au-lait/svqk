import jenkins.model.*

def jenkins = Jenkins.getInstance()
def user = jenkins.getSecurityRealm().getUser('admin')
user.addProperty(hudson.security.HudsonPrivateSecurityRealm.Details.fromPlainPassword("admin"))
