pipeline {
    agent any

    environment {
        DOCKER_CREDENTIALS_ID = 'dockerhub-id'
        DOCKER_IMAGE_PREFIX = 'jeunseo/nyamnyam-config-server'
        services = "server/config-server,server/eureka-server,server/gateway-server,service/admin-service,service/chat-service,service/post-service,service/restaurant-service,service/user-service"
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

        stage('Docker Image Build') {
            steps {
                sh "cd server/config-server && docker build -t jeunseo/nyamnyam-config-server:latest ."
            }
        }

        stage('Compose Up') { // docker-compose up을 실행하는 단계
            steps {
                script {
                    dir('/var/lib/jenkins/workspace/nyamnyam') { // docker-compose.yaml이 있는 경로
                        // docker-compose.yaml 파일이 있는 디렉토리에서 compose up 실행
                        sh 'docker-compose up -d'
                    }
                }
            }
        }
    }
}
