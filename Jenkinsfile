pipeline {

    agent any

    environment {
        IMAGE_REPO     = "manyasreeya/fooddelivery1"
        IMAGE_TAG      = "v${BUILD_NUMBER}"
        IMAGE_NAME     = "manyasreeya/fooddelivery1:v${BUILD_NUMBER}"
        EC2_HOST       = "98.81.194.160"
        EC2_USER       = "ec2-user"
        EC2_KEY        = "C:/keys/fooddelivery-key.pem"
        CONTAINER_NAME = "foodapp"
        APP_PORT       = "8080"
    }

    stages {

        stage('Clone Source Code') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/manyasreeya/fooddeliveryapp.git'
            }
        }

        stage('Build Maven Project') {
            steps {
                bat '.\\mvnw.cmd clean package -DskipTests'
            }
        }

        stage('Run Unit Tests') {
            steps {
                bat '.\\mvnw.cmd test'
            }
        }

        stage('Generate JaCoCo Report') {
            steps {
                bat '.\\mvnw.cmd jacoco:report'
            }
        }

        stage('SonarQube Analysis') {
            environment {
                SONAR_AUTH_TOKEN = credentials('tokenofsonar')
            }
            steps {
                withSonarQubeEnv('sonarqube') {
                    bat """
                    .\\mvnw.cmd sonar:sonar ^
                    -Dsonar.projectKey=fooddeliveryapp ^
                    -Dsonar.projectName=fooddeliveryapp ^
                    -Dsonar.login=%SONAR_AUTH_TOKEN%
                    """
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                bat 'docker build -t %IMAGE_NAME% .'
                bat 'docker tag %IMAGE_NAME% %IMAGE_REPO%:latest'
            }
        }

        stage('Docker Login and Push') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'dockercred1',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
                    bat 'docker login -u %DOCKER_USER% -p %DOCKER_PASS%'
                    bat 'docker push %IMAGE_NAME%'
                    bat 'docker push %IMAGE_REPO%:latest'
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                bat 'kubectl apply -f src/main/resources/k8s/deployment.yaml'
                bat 'kubectl apply -f src/main/resources/k8s/service.yaml'
            }
        }

        stage('Verify Kubernetes Pods') {
            steps {
                bat 'kubectl get pods'
            }
        }

        stage('Deploy To AWS EC2') {
            steps {
                script {
                    def remoteCmd = "sudo systemctl start docker && sudo docker stop ${CONTAINER_NAME} 2>/dev/null; sudo docker rm -f ${CONTAINER_NAME} 2>/dev/null; sudo docker pull ${IMAGE_REPO}:latest && sudo docker run -d --name ${CONTAINER_NAME} -p ${APP_PORT}:${APP_PORT} --restart always ${IMAGE_REPO}:latest"
                    bat "ssh -i ${EC2_KEY} -o StrictHostKeyChecking=no ${EC2_USER}@${EC2_HOST} \"${remoteCmd}\""
                }
            }
        }

        stage('Verify EC2 Deployment') {
            steps {
                script {
                    bat "ssh -i ${EC2_KEY} -o StrictHostKeyChecking=no ${EC2_USER}@${EC2_HOST} \"sudo docker ps | grep ${CONTAINER_NAME}\""
                }
            }
        }
    }

    post {
        success {
            echo "BUILD #${BUILD_NUMBER} SUCCESS - Image: ${IMAGE_NAME} deployed to EC2"
        }
        failure {
            echo "BUILD #${BUILD_NUMBER} FAILED - Check console output above"
        }
    }
}