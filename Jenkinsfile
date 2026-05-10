pipeline {

    agent any

    environment {
        IMAGE_NAME = "manyasreeya/fooddelivery1:v3"
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

                bat 'docker push %IMAGE_NAME%'
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
        bat """
        ssh -i C:\\keys\\fooddelivery-key.pem -o StrictHostKeyChecking=no ec2-user@98.81.194.160 ^
        "sudo systemctl start docker; ^
        sudo docker stop foodapp 2>/dev/null || true; ^
        sudo docker rm -f foodapp 2>/dev/null || true; ^
        sudo docker ps -q --filter publish=8080 | xargs -r sudo docker stop; ^
        sudo docker ps -aq --filter publish=8080 | xargs -r sudo docker rm -f; ^
        sudo docker pull manyasreeya/fooddelivery1:v3; ^
        sudo docker run -d --name foodapp -p 8080:8080 --restart always manyasreeya/fooddelivery1:v3"
        """
    }
}
    }

    post {

        success {

            echo 'Pipeline executed successfully!'
        }

        failure {

            echo 'Pipeline failed. Check Jenkins console output.'
        }
    }
}