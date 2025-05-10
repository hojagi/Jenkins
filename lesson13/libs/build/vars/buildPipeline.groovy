
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
                steps {
                    script {
                        def apps = readJSON file: env.APPS_LIST_FILE
                        buildStages = stagePrepare(apps, "build")
                        // uploadStages = stagePrepare(apps, "upload")
                    }
                }
            }

            stage('Build') {
                steps {
                    script {
                        buildStages.each { build ->
                            parallel build
                        }
                    }
                }
            }

            // stage('Upload') {
            //     steps {
            //         script {
            //             uploadStages.each { upload ->
            //                 parallel upload
            //             }
            //         }
            //     }
            // }
        }
    }
}