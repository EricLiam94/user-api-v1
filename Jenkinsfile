pipeline {
    agent none
    stages {
        stage("Build"){
         agent {
                docker {
                    image 'gradle:6.4-jdk11'
                }}
            steps{
                 echo "start building"
                 sh "gradle clean build"
                 sh "ls build/libs/*"
            }
            post{
                always{
                    junit 'build/test-results/**/**xml'
                }
            }
        }
        stage("Publish"){
            agent any
            steps{
             echo "start building in ${env.BUILD_ID}"
             sh "ls ."
             script {
                def image = docker.build("ericfox/user-api-v1:${env.BUILD_ID}")
                docker.withRegistry('',"docker_hub_id"){
                    image.push()
                }
             }
            }
        }
    }
}