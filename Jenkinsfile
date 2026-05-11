pipeline {

    agent any

    environment {

        IMAGE_NAME = "manyasreeya/fooddelivery1"
        IMAGE_TAG  = "v${BUILD_NUMBER}"

        SONAR_TOKEN = credentials('tokenofsonar')
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

                bat '.\\mvnw.cmd package -DskipTests'
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

        stage('Verify Deployment') {

            steps {

                bat 'kubectl get pods'

                bat 'kubectl get svc'
            }
        }
    }

    post {

        success {

            echo 'Fusion Cloud Pipeline Success'
        }

        failure {

            echo 'Fusion Cloud Pipeline Failed'
        }
    }
}