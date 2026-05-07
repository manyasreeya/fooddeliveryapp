pipeline {
    agent any

    environment {
        IMAGE_NAME = "manyasreeya/fooddelivery1:v1"
    }

    stages {

        stage('Clone Source Code') {
            steps {
                git 'https://github.com/manyasreeya/fooddeliveryapp.git'
            }
        }

        stage('Build Maven Project') {
            steps {
                bat '.\\mvnw.cmd clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                bat 'docker build -t %IMAGE_NAME% .'
            }
        }

        stage('Push Docker Image') {
            steps {
                bat 'docker push %IMAGE_NAME%'
            }
        }

        stage('Deploy To Kubernetes') {
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
    }
}