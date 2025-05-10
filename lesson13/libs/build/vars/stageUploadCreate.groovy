
def call(app, value) {
    return {
        stage(app) {
            dir(value.path) {
                sh "mvn -DskipTests -s settings.xml -Dmaven.repo.local=${MAVEN_REPO_PATH} -Dversion.application=${env.VERSION} deploy"
            }
        }
    }
}