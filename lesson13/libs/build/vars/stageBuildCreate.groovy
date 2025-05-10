def call(app, value) {
    return {
        stage(app) {
            dir(value.path) {
                sh "mvn -B -DskipTests -Dmaven.repo.local=${MAVEN_REPO_PATH} -Dversion.application=${env.VERSION} clean package"
            }
        }
    }
}