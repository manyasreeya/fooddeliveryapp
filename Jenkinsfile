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
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('3. Test') {
            steps {
                echo 'Running unit tests...'
                sh 'mvn test'
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
                    sh 'mvn sonar:sonar -Dsonar.projectKey=fooddeliveryapp'
                }
            }
        }

        stage('5. Docker Build') {
            steps {
                echo 'Building Docker container...'
                sh "docker build -t ${DOCKER_REGISTRY}/${DOCKER_IMAGE}:${env.BUILD_ID} ."
                sh "docker tag ${DOCKER_REGISTRY}/${DOCKER_IMAGE}:${env.BUILD_ID} ${DOCKER_REGISTRY}/${DOCKER_IMAGE}:latest"
            }
        }

        stage('6. Docker Push') {
            steps {
                echo 'Pushing Docker container to registry...'
                withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh 'echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin'
                    sh "docker push ${DOCKER_REGISTRY}/${DOCKER_IMAGE}:${env.BUILD_ID}"
                    sh "docker push ${DOCKER_REGISTRY}/${DOCKER_IMAGE}:latest"
                }
            }
        }

        stage('7. Deploy to EC2') {
            steps {
                echo 'Deploying to AWS EC2 using Docker Compose...'
                // Assuming Jenkins is running on the EC2 instance, or SSH is configured
                sh 'docker-compose down'
                sh 'docker-compose pull'
                sh 'docker-compose up -d --build'
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
