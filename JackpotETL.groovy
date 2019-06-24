pipeline {
	agent {
		label 'DETerminal'
	}
	stages{
		stage('ETLJackpot'){
			step {
				build '/Team/Suraj/DWHJobsJobs/ETLJackpot'	
			}
		}
	}	
}
