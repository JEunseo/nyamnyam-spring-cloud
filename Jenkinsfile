pipeline {
    agent any

    environment {
        DOCKER_CREDENTIALS_ID = 'jeunseo'
        DOCKERHUB_CREDENTIALS = credentials('dockerhub-id')
        DOCKER_IMAGE_PREFIX = 'jeunseo/nyamnyam-config-server'
        KUBECONFIG_CREDENTIALS_ID = 'kubeconfig'
        NCP_API_KEY = credentials('ncloud-api-key')
        NCP_SECRET_KEY = credentials('ncloud-secret-key')
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
                    dir('deploy') {
                        git branch: 'main', url: 'https://github.com/JEunseo/nyamnyam-deploy.git', credentialsId: 'github_personal_access_token'
                    }
                }
            }
        }

        stage('Create Namespace') {
                    steps {
                        script {
                            withCredentials([file(credentialsId: 'kubeconfig', variable: 'KUBECONFIG')]) {
                                sh '''
                                kubectl apply -f deploy/namespace/nyamnyam-namespace.yaml --kubeconfig=$KUBECONFIG
                                '''
                            }
                        }
                    }
                }

        stage('Build JAR') {
            steps {
                script {
                    sh 'chmod +x gradlew'

                    def servicesList = env.services.split(',')

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
                        // 각 서비스의 Docker 이미지를 push
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
                        def serviceName = service.split('/')[1] // 서비스 이름 추
                        sh "docker rmi ${DOCKER_CREDENTIALS_ID}/nyamnyam-${serviceName}:latest" // Clean up the pushed image
                    }
                }
            }
        }

        stage('Create ConfigMap') {
            steps {
                script {
                    withCredentials([file(credentialsId: 'kubeconfig', variable: 'KUBECONFIG')]) {
                        sh '''
                        # ConfigMap 생성 및 적용
                        kubectl create configmap config-server --from-file=server/config-server/src/main/resources/application.yaml -n nyamnyam --dry-run=client -o yaml | kubectl apply -f -
                        kubectl create configmap eureka-server --from-file=server/eureka-server/src/main/resources/application.yaml -n nyamnyam --dry-run=client -o yaml | kubectl apply -f -
                        kubectl create configmap gateway-server --from-file=server/gateway-server/src/main/resources/application.yaml -n nyamnyam --dry-run=client -o yaml | kubectl apply -f -
                        kubectl create configmap admin-service --from-file=service/admin-service/src/main/resources/application.yaml -n nyamnyam --dry-run=client -o yaml | kubectl apply -f -
                        kubectl create configmap chat-service --from-file=service/chat-service/src/main/resources/application.yaml -n nyamnyam --dry-run=client -o yaml | kubectl apply -f -
                        kubectl create configmap post-service --from-file=service/post-service/src/main/resources/application.yaml -n nyamnyam --dry-run=client -o yaml | kubectl apply -f -
                        kubectl create configmap restaurant-service --from-file=service/restaurant-service/src/main/resources/application.yaml -n nyamnyam --dry-run=client -o yaml | kubectl apply -f -
                        kubectl create configmap user-service --from-file=service/user-service/src/main/resources/application.yaml -n nyamnyam --dry-run=client -o yaml | kubectl apply -f -
                        '''
                    }
                }
            }
        }

        stage('Deploy Ingress Controller') {
             steps {
                 script {
                      withCredentials([file(credentialsId: 'kubeconfig', variable: 'KUBECONFIG')]) {
                          sh '''
                          kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/main/deploy/static/provider/cloud/deploy.yaml --kubeconfig=$KUBECONFIG
                          '''
                      }
                 }
             }
        }


        stage('Deploy to k8s') {
            steps {
                script {
                    withCredentials([file(credentialsId: 'kubeconfig', variable: 'KUBECONFIG')]) {
                        // 환경 변수로 API Key와 Secret Key 설정 후 ncp-iam에 전달
                        sh '''
                        export NCP_ACCESS_KEY=$NCP_API_KEY
                        export NCP_SECRET_KEY=$NCP_SECRET_KEY
                        kubectl apply -f deploy/was/config-server/config-server.yaml --kubeconfig=$KUBECONFIG
                        kubectl apply -f deploy/was/eureka-server/eureka-server.yaml --kubeconfig=$KUBECONFIG
                        kubectl apply -f deploy/was/gateway-server/gateway-server.yaml --kubeconfig=$KUBECONFIG
                        kubectl apply -f deploy/was/admin-service/admin-service.yaml --kubeconfig=$KUBECONFIG
                        kubectl apply -f deploy/was/chat-service/chat-service.yaml --kubeconfig=$KUBECONFIG
                        kubectl apply -f deploy/was/post-service/post-service.yaml --kubeconfig=$KUBECONFIG
                        kubectl apply -f deploy/was/restaurant-service/restaurant-service.yaml --kubeconfig=$KUBECONFIG
                        kubectl apply -f deploy/was/user-service/user-service.yaml --kubeconfig=$KUBECONFIG
                        '''
                    }
                }
            }
        }

        stage('Deploy Ingress'){
            steps {
                script {
                    withCredentials([file(credentialsId: 'kubeconfig', variable: 'KUBECONFIG')]) {
                        sh '''
                        kubectl apply -f deploy/ingress/nyamnyam-api-ingress.yaml --kubeconfig=$KUBECONFIG
                        '''
                    }
                }
            }
        }
    }
}
