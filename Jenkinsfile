pipeline {
    agent any

    stages {

        stage('Checkout Code') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/bue-dio/springboot-microservice.git'
            }
        }

        stage('Build') {
            steps {
                bat './mvnw clean package -DskipTests'
            }
        }

        stage('Run Tests') {
            steps {
                bat './mvnw test'
            }
        }
    }
}
