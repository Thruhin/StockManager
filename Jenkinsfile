pipeline {
  
  agent any
  
  tools {
    maven 'Maven Dir'
  }
  stages {
    
    stage("build") {
      steps {
        echo 'building the application...'
        dir("/var/jenkins_home/workspace/StockManager-Pipeline_dev/stock-manager"){
          sh 'mvn -B -DskipTests clean package'
        }
        echo 'done with build'
      }
    }
    stage("test") {
      steps {
        echo 'Invoking integration tests for the application...'
        echo 'done with Testing'
      }
    }
    
    stage("deploy") {
      steps {
        echo 'deploying the application...'
        echo 'done with Deployment'
      }
    }
    
  }
  
  
}
