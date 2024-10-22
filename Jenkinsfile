pipeline {
    agent any

    environment {
        DOCKER_CREDENTIALS_ID = 'jeunseo'
        DOCKERHUB_CREDENTIALS = credentials('dockerhub-id')
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
                            sh "../../gradlew clean bootJar"
                        }
                    }
                }
            }
        }

        stage('Docker Image Build') {
            steps {
                sh "cd server/config-server && docker build -t ${DOCKER_CREDENTIALS_ID}/nyamnyam-config-server:latest ."
                sh "docker-compose build"
            }
        }

        stage('Login to Docker Hub') {
            steps {
                sh '''
                echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin
                '''
            }
        }

        stage('Docker Push') {
            steps {
                script {
                    def servicesList = env.services.split(',')

                    servicesList.each { service ->
                        def serviceName = service.split('/')[1] // 서비스 이름 추출
                        // 각 서비스의 Docker 이미지를 푸시
                        sh "docker push ${DOCKER_CREDENTIALS_ID}/nyamnyam-${serviceName}:latest"
                    }
                }
            }
        }

        stage('Cleaning up') {
            steps {
                script {
                    // 각 서비스의 이미지 삭제
                    def servicesList = env.services.split(',')
                    servicesList.each { service ->
                        def serviceName = service.split('/')[1] // 서비스 이름 추출
                        sh "docker rmi ${DOCKER_CREDENTIALS_ID}/nyamnyam-${serviceName}:latest" // Clean up the pushed image
                    }
                }
            }
        }
    }
}
