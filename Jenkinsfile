pipeline {

    agent any

    environment {
        IMAGE_REPO  = "manyasreeya/fooddelivery1"
        IMAGE_TAG   = "v${BUILD_NUMBER}"
        IMAGE_NAME  = "manyasreeya/fooddelivery1:v${BUILD_NUMBER}"

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

        stage('Generate JaCoCo Coverage Report') {
            steps {
                bat '.\\mvnw.cmd jacoco:report'
            }

            post {
                always {
                    jacoco(
                        execPattern: '**/target/jacoco.exec',
                        classPattern: '**/target/classes',
                        sourcePattern: '**/src/main/java',
                        exclusionPattern: '**/test/**'
                    )
                }
            }
        }

        stage('Code Quality - SonarQube') {

            environment {
                SONAR_AUTH_TOKEN = credentials('tokenofsonar')
            }

            steps {

                withSonarQubeEnv('sonarqube') {

                    bat """
                    .\\mvnw.cmd sonar:sonar ^
                    -Dsonar.projectKey=fooddeliveryapp ^
                    -Dsonar.projectName=FusionCloud-FoodDelivery ^
                    -Dsonar.login=%SONAR_AUTH_TOKEN% ^
                    -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml
                    """
                }
            }
        }

        stage('Build Docker Image') {

            steps {

                bat 'docker build -t %IMAGE_NAME% .'
                bat 'docker tag %IMAGE_NAME% %IMAGE_REPO%:latest'

                echo "Docker image built successfully"
            }
        }

        stage('Push Docker Image to Docker Hub') {

            steps {

                withCredentials([usernamePassword(
                    credentialsId: 'dockercred1',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {

                    bat 'docker login -u %DOCKER_USER% -p %DOCKER_PASS%'

                    bat 'docker push %IMAGE_NAME%'
                    bat 'docker push %IMAGE_REPO%:latest'

                    echo "Docker image pushed successfully"
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

                    kubectl rollout status deployment/fooddelivery-deployment --timeout=180s
                    '''
                }
            }
        }

        stage('Verify AWS EKS Deployment') {

            steps {

                bat 'kubectl get nodes'
                bat 'kubectl get pods -o wide'
                bat 'kubectl get svc'
                bat 'kubectl get hpa'
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

                    kubectl rollout status deployment/fooddelivery-deployment --timeout=180s
                    '''
                }
            }
        }

        stage('Verify Azure AKS Deployment') {

            steps {

                bat 'kubectl get nodes'
                bat 'kubectl get pods -o wide'
                bat 'kubectl get svc'
                bat 'kubectl get hpa'
            }
        }

        stage('Run AI Incident Analyzer') {

            steps {

                bat 'python ai-monitor/log_analyzer.py'
            }
        }

        stage('Health Check') {

            steps {

                bat 'kubectl get pods'
                bat 'kubectl get svc'

                echo "Fusion Cloud deployment verified successfully"
            }
        }
    }

    post {

        success {

            mail to: "${ADMIN_EMAIL}",
                 subject: "SUCCESS: Fusion Cloud Build #${BUILD_NUMBER}",

                 body: """
====================================================

Fusion Cloud - Multi-Cloud Deployment Successful

====================================================

Build Number : #${BUILD_NUMBER}

Docker Image : ${IMAGE_NAME}

Deployment Status:
- AWS EKS : SUCCESS
- Azure AKS : SUCCESS
- Docker Hub : SUCCESS
- Kubernetes : SUCCESS
- AI Analyzer : SUCCESS

Reports:
- Jenkins Console : ${BUILD_URL}console
- JaCoCo Report : ${BUILD_URL}jacoco

Next Steps:
1. Verify Datadog dashboards
2. Monitor Kubernetes alerts
3. Review AI analyzer output

Fusion Cloud Automation Platform

====================================================
"""
        }

        failure {

            mail to: "${ADMIN_EMAIL}",
                 subject: "FAILED: Fusion Cloud Build #${BUILD_NUMBER}",

                 body: """
====================================================

Fusion Cloud Pipeline Failed

====================================================

Build Number : #${BUILD_NUMBER}

Action Required:
1. Open Jenkins Console
2. Review failed stage
3. Check Kubernetes logs
4. Review AI analyzer

Build Logs:
${BUILD_URL}console

====================================================
"""
        }

        always {

            cleanWs()
        }
    }
}