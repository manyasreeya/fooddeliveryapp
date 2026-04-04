pipeline {
    agent any

    tools {
        jdk 'jdk17'
        maven 'maven3'
    }

    environment {
        DOCKER_IMAGE = "food-delivery-app-app"
        DOCKER_REGISTRY = "manyasreeya" 
        SONARQUBE_SERVER = "SonarQube"
    }

    stages {
        stage('Build Jar') {
            steps {
                echo 'Building Spring Boot JAR with Maven...'
                bat 'mvnw.cmd clean package -DskipTests'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                echo 'Running SonarQube quality checks...'
                withSonarQubeEnv(env.SONARQUBE_SERVER) {
                    bat 'mvnw.cmd sonar:sonar -Dsonar.projectKey=fooddeliveryapp'
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                echo 'Building Docker container...'
                bat "docker build -t ${DOCKER_REGISTRY}/${DOCKER_IMAGE}:latest ."
            }
        }

        stage('Docker Login') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    bat 'echo %DOCKER_PASS% | docker login -u %DOCKER_USER% --password-stdin'
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                echo 'Pushing Docker container to registry...'
                bat "docker push ${DOCKER_REGISTRY}/${DOCKER_IMAGE}:latest"
            }
        }
    }

    post {
        success {
            echo "Pipeline completed successfully with all green boxes!"
        }
        failure {
            echo "Pipeline failed. Please check the logs."
        }
    }
}
