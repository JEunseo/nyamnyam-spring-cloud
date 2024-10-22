pipeline {
    agent any

    environment {
        DOCKER_CREDENTIALS_ID = 'jeunseo'
        DOCKER_IMAGE_PREFIX = 'jeunseo/nyamnyam-config-server'
    }

    stages {
        stage('Git Clone') {
            steps {
                script {
                    // .ncloud 디렉토리 내의 서버 경로로 이동 후 Git 클론 실행
                    dir('.ncloud/server/config-server') {
                        git branch: 'main', url: 'https://github.com/JEunseo/nyamnyam-config-server.git'
                    }
                }
            }
        }

        stage('Checkout SCM') {
            steps {
                script {
                    // 다른 리포지토리나 추가적인 소스 코드 체크아웃
                    dir('.ncloud/server/config-server') {
                        checkout scm
                    }
                }
            }
        }

        stage('Docker Build & Push') {
            steps {
                script {
                    // Docker 빌드 및 푸시를 실행할 디렉토리로 이동
                    dir('.ncloud/server/config-server') {
                        sh 'pwd'
                        // Docker 빌드 및 푸시 명령어 실행
                        sh "docker build -t ${DOCKER_IMAGE_PREFIX}:latest ."
                        sh "docker push ${DOCKER_IMAGE_PREFIX}:latest"
                    }
                }
            }
        }
    }
}
