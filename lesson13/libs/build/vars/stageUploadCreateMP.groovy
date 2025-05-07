
def call(app, value) {
    return {
        stage(app) {
//            dir(value.path) {
//                sh "mvn -DskipTests -s settings.xml -Dmaven.repo.local=${MAVEN_REPO_PATH} -Dversion.application=${env.VERSION} deploy"
//            }
		    when {
                branch 'main'
            }
            steps {
			    dir('lesson13') {
				    sh "chmod -R 777 webbooks"
				}
                dir('lesson13/webbooks') {
					sh "./mvnw package -DDB.url=jdbc:postgresql://192.168.56.112:5432/webbooks"
					build job: 'copy and run jar', wait: false
                }
            }
 
            post {
                success {
				    echo 'Build, create artefact and pipeline success'
                    archiveArtifacts '**/target/*.jar'
                }
            }
        }
    }
}