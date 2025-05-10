
def call(body) {
    def config= [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()


    pipeline {
        agent any
        
        options {
            buildDiscarder(logRotator(numToKeepStr: '2'))
            disableConcurrentBuilds()
            timestamps()
        }

//        tools {
//            maven "3.9.2"
//        }

        environment {
//            GITHUB_REPO_CRED = credentials("${config.GITHUB_REPO_CRED}")
//            GITHUB_REPO_OWNER = "${config.GITHUB_REPO_OWNER}"
//            GITHUB_REPO_NAME = "${config.GITHUB_REPO_NAME}"
//            GITHUB_REPO_URL = "${config.GITHUB_REPO_URL}"
//            VERSION = "1.0.${BUILD_NUMBER}"
//            MAVEN_REPO_PATH = "${WORKSPACE}/.m2/repository"
//            APPS_LIST_FILE = "${config.APPS_LIST_FILE}"
			remoteHost = "${config.remoteHost}"
//			'192.168.56.112'
			remoteUser = "${config.remoteUser}"
//			'vagrant'
			localJarPath = "${config.localJarPath}"
//			'/home/vagrant/DigitalLibrary-0.0.1-SNAPSHOT.jar'
			remoteJarPath = "${config.remoteJarPath}"
//			'/home/vagrant/app.jar'
        }

        stages {
//            stage('Prepare') {
//                steps {
//                    script {
//                        def apps = readJSON file: env.APPS_LIST_FILE
//						    deplyStages = stagePrepareDeploy(apps, "deploy")
//						    runStages = stagePrepareDeploy(apps, "run")
//						}						
 //                   }
 //               }
//            }

            stage('deploy') {
                steps {
                    script {
                        sh "scp -i /var/lib/jenkins/workspace/private_key ${localJarPath} ${remoteUser}@${remoteHost}:${remoteJarPath}"
                        }
                    }
                
				post {
				    success {
			    		echo 'Deploy complete'
                    }
				}				
            }

            stage('run') {
                steps {
                    script {
                        sh "ssh -i /var/lib/jenkins/workspace/private_key ${remoteUser}@${remoteHost} 'sudo systemctl restart webbooks.service'"
                        }
                    }
                }
				
           }
        }
    
}