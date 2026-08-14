pipeline {

    agent any

    tools {
        maven 'Maven'
    }

    options {
        timestamps()
        disableConcurrentBuilds()
        timeout(time: 15, unit: 'MINUTES')

        buildDiscarder(
            logRotator(
                numToKeepStr: '20'
            )
        )
    }

    environment {
        APP_NAME = 'payment-service'
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean compile'
            }
        }

        stage('Unit Test') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Package') {
            steps {
                sh 'mvn package -DskipTests'
            }
        }
        stage('Docker Build') {
            steps {
                sh '''
                    docker build \
                        -t payment-service:${BUILD_NUMBER} \
                        .
                '''
            }
        }
        stage('Security Scan') {
            steps {
                sh '''
                    trivy image \
                        --timeout 10m \
                        --severity HIGH,CRITICAL \
                        --exit-code 1 \
                        payment-service:${BUILD_NUMBER}
                '''
            }
        }
        stage('Docker Push') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'dockerhub-credentials',
                        usernameVariable: 'DOCKER_USERNAME',
                        passwordVariable: 'DOCKER_PASSWORD'
                    )
                ]) {
                    sh '''
                        echo "$DOCKER_PASSWORD" | docker login \
                            -u "$DOCKER_USERNAME" \
                            --password-stdin

                        docker tag payment-service:${BUILD_NUMBER} \
                            ${DOCKER_USERNAME}/payment-service:${BUILD_NUMBER}

                        docker push \
                            ${DOCKER_USERNAME}/payment-service:${BUILD_NUMBER}

                        docker logout
                    '''
                }
            }
        }
        stage('Archive Artifact') {
            steps {
                archiveArtifacts(
                    artifacts: 'target/*.jar',
                    fingerprint: true
                )
            }
        }
    }

    post {

        success {
            echo "CI pipeline completed successfully for ${APP_NAME}"
        }

        failure {
            echo "CI pipeline failed for ${APP_NAME}"
        }

        always {
            echo "Pipeline execution completed"
        }
    }
}