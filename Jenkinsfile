pipeline {
    agent any

    environment {
        PUSH_VERSION = "1.0"
        DOCKER_CREDENTIALS_ID = 'jeunseo'
        DOCKER_IMAGE_PREFIX = 'jeunseo/nyamnyam'
        services = "config-server,eureka-server,gateway-server,admin-service,chat-service,post-service,restaurant-service,user-service"
    }

    stages {
        // GitHub에서 소스 코드 클론
        stage('Git Clone') {
            steps {
                git branch: 'main', credentialsId: 'github_personal_access_token', url: 'https://github.com/JEunseo/nyamnyam-spring-cloud.git'
            }
        }

        // Gradlew 실행 권한 부여 및 빌드
        stage("Java Build") {
            steps {
                script {
                    sh "chmod +x ./gradlew"  // gradlew에 실행 권한 부여
                    env.services.split(',').each { service ->  // env.services로 접근해야 함
                        sh "./gradlew clean build -p server/${service} --warning-mode all"  // 각 서비스 빌드
                    }
                }
            }
        }

        // Docker 이미지 제거 (옵션, 필요시 사용)
        stage("Docker Image Remove") {
            steps {
                script {
                    env.services.split(',').each { service ->  // env.services로 접근
                        sh "docker rmi -f ${DOCKER_IMAGE_PREFIX}/${service}:${PUSH_VERSION}"  // 이전 Docker 이미지 제거
                    }
                }
            }
        }

        // Docker 이미지 빌드
        stage("Docker Image Build") {
            steps {
                script {
                    env.services.split(',').each { service ->  // env.services로 접근
                        sh "docker build -t ${DOCKER_IMAGE_PREFIX}/${service}:${PUSH_VERSION} ."  // Docker 이미지 빌드
                    }
                }
            }
        }

        // Docker 이미지 푸시
        stage("Docker Push") {
            steps {
                script {
                    env.services.split(',').each { service ->  // env.services로 접근
                        sh "docker push ${DOCKER_IMAGE_PREFIX}/${service}:${PUSH_VERSION}"  // Docker 이미지 푸시
                    }
                }
            }
        }

        // Kubernetes 파일 적용
        stage('Apply Kubernetes files') {
            steps {
                withKubeConfig([credentialsId: 'kubeconfig']) {
                    env.services.split(',').each { service ->  // env.services로 접근
                        sh "kubectl --kubeconfig=$HOME/.ncloud/kubeconfig.yml apply -f ./k8s/${service}-deployment.yml"  // Kubernetes 배포 적용
                        sh "kubectl --kubeconfig=$HOME/.ncloud/kubeconfig.yml apply -f ./k8s/${service}-service.yml"  // Kubernetes 서비스 적용
                    }
                }
            }
        }
    }
}
