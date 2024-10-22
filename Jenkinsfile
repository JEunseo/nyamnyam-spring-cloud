pipeline {
    agent any

    environment {
        PUSH_VERSION = "1.0"
        DOCKER_CREDENTIALS_ID = 'dockerhub-id'
        DOCKER_IMAGE_PREFIX = 'jeunseo/nyamnyam'
        services = "eureka-server,gateway-server,admin-service,chat-service,post-service,restaurant-service,user-service"
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

                    dir('server/config-server/src/main/resources/secret-server') {
                        git branch: 'main', url: 'https://github.com/JEunseo/nyamnyam-secret-server.git', credentialsId: 'github_personal_access_token'
                    }
                }
            }
        }

        stage('Build JAR') {
            steps {
                script {
                    sh 'chmod +x gradlew'

                    // services 환경 변수를 Groovy 리스트로 변환
                    def servicesList = env.services.split(',')

                    // 각 서비스에 대해 Gradle 빌드 수행 (테스트 제외)
                    servicesList.each { service ->
                        dir(service) {
                            sh "../../gradlew clean build --warning-mode all -x test"
                        }
                    }
                }
            }
        }

        stage('Docker Config-Server Image Build') {
            steps {
                script {
                    sh "cd server/config-server && docker build -t ${DOCKER_IMAGE_PREFIX}-config-server:${PUSH_VERSION} ."
                }
            }
        }

        stage('Docker Image Remove') {
            steps {
                script {
                    services.split(',').each { service ->
                        def imageExists = sh(script: "docker images -q ${DOCKER_IMAGE_PREFIX}-${service}:${PUSH_VERSION}", returnStdout: true).trim()
                        if (imageExists) {
                            sh "docker rmi -f ${DOCKER_IMAGE_PREFIX}-${service}:${PUSH_VERSION}"
                        } else {
                            echo "Image ${DOCKER_IMAGE_PREFIX}-${service}:${PUSH_VERSION} not found, skipping..."
                        }
                    }
                }
            }
        }

        stage('Docker Image Build') {
            steps {
                sh "docker-compose build"
            }
        }

        stage('Docker Push') {
            steps {
                script {
                    services.split(',').each { service ->
                        sh "docker push ${DOCKER_IMAGE_PREFIX}-${service}:${PUSH_VERSION}"
                    }
                    sh "docker push ${DOCKER_IMAGE_PREFIX}-config-server:${PUSH_VERSION}"  // Config-server 이미지도 푸시
                }
            }
        }

        stage('Cleaning up') {
            steps {
                script {
                    services.split(',').each { service ->
                        sh "docker rmi ${DOCKER_IMAGE_PREFIX}-${service}:${PUSH_VERSION}"
                    }
                    sh "docker rmi ${DOCKER_IMAGE_PREFIX}-config-server:${PUSH_VERSION}"  // Config-server 이미지도 제거
                }
            }
        }
    }
}
