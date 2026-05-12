pipeline {

    agent any

    environment {

        IMAGE_NAME = "manyasreeya/fooddelivery1"
        IMAGE_TAG  = "v${BUILD_NUMBER}"

        SONAR_TOKEN = credentials('tokenofsonar')

        DATADOG_API_KEY = credentials('DATADOG_API_KEY')
    }

    stages {

        stage('Clone Code') {

            steps {

                git branch: 'main',
                url: 'https://github.com/manyasreeya/fooddeliveryapp.git'
            }
        }

        stage('Clean Workspace') {

            steps {

                bat '.\\mvnw.cmd clean'
            }
        }

        stage('Build Application') {

            steps {

                bat '.\\mvnw.cmd clean verify'
            }
        }

        stage('Run Tests') {

            steps {

                bat '.\\mvnw.cmd test'
            }
        }

        stage('SonarQube Analysis') {

            steps {

                withSonarQubeEnv('sonarqube') {

                    bat """
                    .\\mvnw.cmd sonar:sonar ^
                    -Dsonar.projectKey=fooddeliveryapp ^
                    -Dsonar.projectName=FusionCloud ^
                    -Dsonar.host.url=http://3.85.207.201:9000 ^
                    -Dsonar.login=%SONAR_TOKEN%
                    """
                }
            }
        }

        stage('Build Docker Image') {

            steps {

                bat "docker build -t %IMAGE_NAME%:%IMAGE_TAG% ."

                bat "docker tag %IMAGE_NAME%:%IMAGE_TAG% %IMAGE_NAME%:latest"
            }
        }

        stage('Docker Login') {

            steps {

                withCredentials([usernamePassword(
                    credentialsId: 'dockercred1',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {

                    bat 'docker login -u %DOCKER_USER% -p %DOCKER_PASS%'
                }
            }
        }

        stage('Push Docker Image') {

            steps {

                bat "docker push %IMAGE_NAME%:%IMAGE_TAG%"

                bat "docker push %IMAGE_NAME%:latest"
            }
        }

        stage('Kubernetes Deploy') {

            steps {

                bat 'kubectl apply -f src/main/resources/k8s/deployment.yaml'

                bat 'kubectl apply -f src/main/resources/k8s/service.yaml'
            }
        }

        stage('Verify Kubernetes Deployment') {

            steps {

                bat 'kubectl get pods'

                bat 'kubectl get svc'

                bat 'kubectl rollout status deployment/fooddelivery-deployment'
            }
        }

        stage('Verify Datadog Monitoring') {

            steps {

                bat 'kubectl get pods -n datadog'

                bat 'kubectl top pods'
            }
        }

        stage('AI Monitoring Analysis') {

            steps {

                bat 'python ai-monitor/log_analyzer.py'
            }
        }

        stage('Send Deployment Event To Datadog') {

            steps {

                bat """
                curl -X POST "https://api.datadoghq.com/api/v1/events" ^
                -H "DD-API-KEY: %DATADOG_API_KEY%" ^
                -H "Content-Type: application/json" ^
                -d "{\\"title\\":\\"Fusion Cloud Deployment Success\\",\\"text\\":\\"Build ${BUILD_NUMBER} deployed successfully to Kubernetes with AI monitoring enabled\\"}"
                """
            }
        }
    }

    post {

        always {

            echo 'Pipeline execution completed'
        }

        success {

            mail(
                to: 'tejaswipolanati@gmail.com',
                subject: "Fusion Cloud Pipeline Success - Build ${BUILD_NUMBER}",
                body: """
Fusion Cloud CI/CD Pipeline Completed Successfully

Build Number: ${BUILD_NUMBER}

Completed Stages:
- Build Success
- Test Success
- SonarQube Analysis Passed
- Docker Image Build Success
- Docker Push Success
- Kubernetes Deployment Success
- Datadog Monitoring Verified
- AI Monitoring Analysis Completed

Application deployed successfully on Kubernetes.

Regards,
Fusion Cloud DevOps Platform
"""
            )

            echo 'Fusion Cloud Success Email Sent'
        }

        failure {

            script {

                bat 'python ai-monitor/log_analyzer.py'
            }

            mail(
                to: 'tejaswipolanati@gmail.com',
                subject: "Fusion Cloud Pipeline Failed - Build ${BUILD_NUMBER}",
                body: """
Fusion Cloud Pipeline Failed

Build Number: ${BUILD_NUMBER}

Possible Causes:
- Kubernetes deployment issue
- Docker image issue
- Pod crash
- Resource issue
- Cluster issue
- SonarQube connectivity issue

Recommended Actions:
1. Check Jenkins logs
2. Check Kubernetes pod logs
3. Check Datadog dashboards
4. Review AI analyzer report

Regards,
Fusion Cloud AI Monitoring System
"""
            )

            echo 'Fusion Cloud Failure Email Sent'
        }
    }
}