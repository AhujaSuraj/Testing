pipeline {
	agent {
		label 'DETerminal'
	}
	stages{
		stage('ETLJackpot'){
			steps {
				build '/Team/Suraj/DWH/ETLJackpot'	
			}
		}
	}	
}
