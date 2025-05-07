
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

        tools {
            maven "3.9.2"
        }

        environment {
            GITHUB_REPO_CRED = credentials("${config.GITHUB_REPO_CRED}")
            GITHUB_REPO_OWNER = "${config.GITHUB_REPO_OWNER}"
            GITHUB_REPO_NAME = "${config.GITHUB_REPO_NAME}"
            GITHUB_REPO_URL = "${config.GITHUB_REPO_URL}"
            VERSION = "1.0.${BUILD_NUMBER}"
            MAVEN_REPO_PATH = "${WORKSPACE}/.m2/repository"
            APPS_LIST_FILE = "${config.APPS_LIST_FILE}"
        }

        stages {
            stage('Prepare') {
//			    when {
//                    branch 'PR-*'
//                }
                steps {
                    script {
                        def apps = readJSON file: env.APPS_LIST_FILE
						if (env.BRANCH_NAME ==~ /PR-.*/) {
						    buildStages = stagePrepareMP(apps, "build, test")
						    uploadStages = stagePrepareMP(apps, "build, upload")
						}
						else if (env.BRANCH_NAME =='main') {
						    buildStages = stagePrepareMP(apps, "build, test")
						    uploadStages = stagePrepareMP(apps, "build, upload")
						}						
                        // uploadStages = stagePrepare(apps, "upload")
                    }
                }
            }

            stage('build, test') {
                steps {
                    script {
                        buildStages.each { build ->
                            parallel build
                        }
                    }
                }
				post {
				    success {
			    		echo 'Build and tests success complete'
                    }
				}				
            }

            stage('build, upload') {
                steps {
                    script {
                        uploadStages.each { upload ->
                            parallel upload
                        }
                    }
                }
				post {
				    success {
			    		echo 'Build and upload success complete'
						archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
						sh 'cp target/*.jar /home/vagrant/'
//						script {
//						    def jarFile = findFiles(glob: '**/target/*.jar')[0]
//						sh "cp ${jarFile} /var/lib/jenkins/app.jar"
//						archiveArtifacts '**/target/*.jar'
//						    sh "cp **/target/*.jar ~/app.jar"
//					    }
                    }
				}
            }
        }
    }
}