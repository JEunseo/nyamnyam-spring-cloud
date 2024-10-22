pipeline {
    agent any

    environment {
        DOCKER_CREDENTIALS_ID = 'jeunseo'
        DOCKER_IMAGE_PREFIX = 'jeunseo/nyamnyam-config-server'
    }

    stages {
        stage('Checkout SCM') {
            steps {
                checkout scm
            }
        }

        stage('Git Clone') {
            steps {
                script {
                    dir('server/config-server') {
                        git branch: 'main', url: 'https://github.com/JEunseo/nyamnyam-config-server.git', credentialsId: 'github_personal_access_token'
                    }

                    dir ('server/config-server/src/main/resources/secret-server') {
                        git branch: 'main', url: 'https://github.com/JEunseo/nyamnyam-secret-server.git', credentialsId: 'github_personal_access_token'

                    }
                }
            }
        }

        // 프로젝트 빌드 단계 추가
                stage('Build Project') {
                    steps {
                        script {
                            dir('server/config-server') {
                                sh './gradlew build'  // Gradle 빌드를 수행
                            }
                        }
                    }
                }

        stage('Docker Build & Push') {
            steps {
                script {
                    dir('server/config-server') {
                        sh 'pwd'
                        // Docker 빌드 및 푸시 명령어 추가
                        sh "docker build -t ${DOCKER_IMAGE_PREFIX}/config-server:latest ."
                        sh "docker push ${DOCKER_IMAGE_PREFIX}/config-server:latest"
                    }
                }
            }
        }
    }
}
