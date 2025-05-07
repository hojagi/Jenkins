def call(app, value) {
    return {
        stage(app) {
		
//            dir(value.path) {

//                sh "mvn -B -DskipTests -Dmaven.repo.local=${MAVEN_REPO_PATH} -Dversion.application=${env.VERSION} clean package"
//            }
            dir(value.path) {
//			    steps {
//			        dir('lesson13') {
//			    	    sh "chmod -R 777 webbooks"
//				    }
//                    dir('lesson13/webbooks') {
				sh "chmod -R 777 value.path"
			    sh "./mvnw package -DDB.url=jdbc:postgresql://192.168.56.112:5432/webbooks"
//                    }
//                }

                post {
                    success {
			    		echo 'Build and tests success complete'
                    }
                }
			}
        }
    }
}
