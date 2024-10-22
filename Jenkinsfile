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

        stage('Build JAR') {
            steps {
                script {
                    sh 'chmod +x gradlew' // gradlew에 실행 권한 부여

                    // 각 서버에 대해 gradlew를 실행하고, --warning-mode all 옵션 추가
                    def services = [
                        'server/config-server',
                        'server/eureka-server',
                        'server/gateway-server',
                        'service/admin-service',
                        'service/chat-service',
                        'service/post-service',
                        'service/restaurant-service',
                        'service/user-service'
                    ]

                    for (service in services) {
                        // post-service만 빌드하고 테스트하도록 조건 추가
                        if (service == 'service/post-service') {
                            dir(service) {
                                sh "../../gradlew clean build --warning-mode all"

                                // 테스트 실행 및 실패 시 처리 //
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
}
