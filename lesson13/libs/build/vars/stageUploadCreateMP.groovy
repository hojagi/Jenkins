
def call(app, value) {
    return {
        stage(app) {
//            dir(value.path) {
//                sh "mvn -DskipTests -s settings.xml -Dmaven.repo.local=${MAVEN_REPO_PATH} -Dversion.application=${env.VERSION} deploy"
//            }
		    dir(value.path) {
			    sh "mvn package -DDB.url=jdbc:postgresql://192.168.56.112:5432/webbooks"
				sh "echo upload_work"
 
//            post {
//               success {
//				    echo 'Build, create artefact and pipeline success'
//                    archiveArtifacts '**/target/*.jar'
//                }
//            }
			}
        }
    }
}