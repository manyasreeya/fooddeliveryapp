pipeline {

    agent any

    environment {

        IMAGE_REPO = "manyasreeya/fooddelivery1"
        IMAGE_TAG  = "v${BUILD_NUMBER}"
        IMAGE_NAME = "${IMAGE_REPO}:v${BUILD_NUMBER}"

        ADMIN_EMAIL = "23mh1a0510@acoe.edu.in"
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

            post {

                always {

                    junit '**/target/surefire-reports/*.xml'
                }
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
                    -Dsonar.projectName=FusionCloud ^
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

        stage('Push Docker Image') {

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

        stage('Deploy to AWS EKS') {

            steps {

                withCredentials([
                    string(credentialsId: 'AWS_ACCESS_KEY_ID', variable: 'AWS_ACCESS_KEY'),
                    string(credentialsId: 'AWS_SECRET_ACCESS_KEY', variable: 'AWS_SECRET_KEY')
                ]) {

                    bat '''
                    aws configure set aws_access_key_id %AWS_ACCESS_KEY%
                    aws configure set aws_secret_access_key %AWS_SECRET_KEY%

                    aws eks update-kubeconfig --region us-east-1 --name fusion-eks-cluster

                    kubectl apply -f src/main/resources/k8s/deployment.yaml
                    kubectl apply -f src/main/resources/k8s/service.yaml

                    kubectl get pods
                    kubectl get svc
                    '''
                }
            }
        }

        stage('Deploy to Azure AKS') {

            steps {

                withCredentials([
                    string(credentialsId: 'AZURE_CLIENT_ID', variable: 'AZ_CLIENT'),
                    string(credentialsId: 'AZURE_CLIENT_SECRET', variable: 'AZ_SECRET'),
                    string(credentialsId: 'AZURE_TENANT_ID', variable: 'AZ_TENANT'),
                    string(credentialsId: 'AZURE_SUBSCRIPTION_ID', variable: 'AZ_SUB')
                ]) {

                    bat '''
                    az login --service-principal -u %AZ_CLIENT% -p %AZ_SECRET% --tenant %AZ_TENANT%

                    az account set --subscription %AZ_SUB%

                    az aks get-credentials --resource-group fusion-cloud-rg --name fusion-aks-cluster --overwrite-existing

                    kubectl apply -f src/main/resources/k8s/deployment.yaml
                    kubectl apply -f src/main/resources/k8s/service.yaml

                    kubectl get pods
                    kubectl get svc
                    '''
                }
            }
        }

        stage('Run AI Analyzer') {

            steps {

                bat 'python ai-monitor/log_analyzer.py'
            }
        }

        stage('Health Verification') {

            steps {

                bat 'kubectl get pods'
                bat 'kubectl get svc'
            }
        }
    }

    post {

        success {

            mail to: "${ADMIN_EMAIL}",
            subject: "Fusion Cloud SUCCESS - Build #${BUILD_NUMBER}",

            body: """
Fusion Cloud Pipeline Successful

Build Number: ${BUILD_NUMBER}

Completed:
- Maven Build
- SonarQube Scan
- Docker Build
- Docker Push
- AWS EKS Deployment
- Azure AKS Deployment
- AI Analyzer
- Kubernetes Verification

Monitoring:
- Datadog Active
- Alerts Enabled

Jenkins Console:
${BUILD_URL}console
"""
        }

        failure {

            mail to: "${ADMIN_EMAIL}",
            subject: "Fusion Cloud FAILED - Build #${BUILD_NUMBER}",

            body: """
Fusion Cloud Pipeline Failed

Check Jenkins Console:
${BUILD_URL}console

Possible Causes:
- Docker issue
- Kubernetes issue
- AWS auth failure
- Azure auth failure
"""
        }

        always {

            cleanWs()
        }
    }
}