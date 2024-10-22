pipeline {
    agent any

    environment {
        DOCKER_CREDENTIALS_ID = 'jeunseo'
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

                    dir ('server/config-server/src/main/resources/secret-server') {
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

                    // 각 서비스에 대해 Gradle 빌드 및 테스트 수행
                    servicesList.each { service ->
                        dir(service) {
                            sh "../../gradlew clean build --warning-mode all"

                            // 테스트 실행 및 실패 시 처리
                            def testResult = sh(script: "../../gradlew test", returnStatus: true)
                            if (testResult != 0) {
                                error "Tests failed for ${service}"
                            }
                        }
                    }
                }
            }
        }
    }
}
