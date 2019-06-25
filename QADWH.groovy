pipeline {
	agent {
		label 'DETerminal'
	}
	stages{
		stage('QA_DWH'){
			steps {
				build '/Team/Suraj/DWH/QA_DWH'	
			}
		}
	}	
}
