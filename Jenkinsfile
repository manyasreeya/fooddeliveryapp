pipeline {
    agent any

    environment {
        DOCKER_IMAGE = "fooddeliveryapp"
        DOCKER_REGISTRY = "manyasreeya" 
        SONARQUBE_SERVER = "SonarQube-Server" // Configured in Jenkins global settings
    }

    stages {
        stage('1. Checkout Code') {
            steps {
                echo 'Checking out source code from repository...'
                checkout scm
            }
        }

        stage('2. Build') {
            steps {
                echo 'Building Spring Boot application with Maven...'
                bat 'mvnw.cmd clean package -DskipTests'
            }
        }

        stage('3. Test') {
            steps {
                echo 'Running unit tests...'
                bat 'mvnw.cmd test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('4. SonarQube Analysis') {
            steps {
                echo 'Running SonarQube quality checks...'
                withSonarQubeEnv(env.SONARQUBE_SERVER) {
                    bat 'mvnw.cmd sonar:sonar -Dsonar.projectKey=fooddeliveryapp'
                }
            }
        }

        stage('5. Docker Build') {
            steps {
                echo 'Building Docker container...'
                bat "docker build -t ${DOCKER_REGISTRY}/${DOCKER_IMAGE}:${env.BUILD_ID} ."
                bat "docker tag ${DOCKER_REGISTRY}/${DOCKER_IMAGE}:${env.BUILD_ID} ${DOCKER_REGISTRY}/${DOCKER_IMAGE}:latest"
            }
        }

        stage('6. Docker Push') {
            steps {
                echo 'Pushing Docker container to registry...'
                withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    bat 'echo %DOCKER_PASS% | docker login -u %DOCKER_USER% --password-stdin'
                    bat "docker push ${DOCKER_REGISTRY}/${DOCKER_IMAGE}:${env.BUILD_ID}"
                    bat "docker push ${DOCKER_REGISTRY}/${DOCKER_IMAGE}:latest"
                }
            }
        }

        stage('7. Deploy to EC2') {
            steps {
                echo 'Deploying to AWS EC2 using Docker Compose...'
                // Assuming Jenkins is running on the EC2 instance, or SSH is configured
                bat 'docker-compose down'
                bat 'docker-compose pull'
                bat 'docker-compose up -d --build'
            }
        }
    }

    post {
        success {
            echo "Pipeline completed successfully! Savor Delivery App is live."
        }
        failure {
            echo "Pipeline failed. Please check the logs."
        }
    }
}
