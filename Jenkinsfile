pipeline {
    agent any

    stages {
        stage('Print Working Directory') {
            steps {
                script {
                    sh 'pwd'
                }
            }
        }
        // 이후 Git Clone 및 Docker Build 단계를 이어서 작업
    }
}