pipeline {
	agent {
		label 'DETerminal'
	}
	stages{
		stage('import'){
			step {
				build '/Team/Suraj/DWHJobsJobs/Import'	
			}
		}
		stage('Cleanup_DWHJobssrc'){
			step {
				build '/Team/Suraj/DWHJobs/Cleanup_DWHJobssrc'
			}
		}
		stage('TransformDataDerive'){
			step {
				build '/Team/Suraj/DWHJobs/TransformDataDerive'
			}
		}
		stage('LoadDataDWHJobs'){
			step {
				build '/Team/Suraj/DWHJobs/LoadDataDWHJobs'
			}
		}
		stage('QA_DWH'){
			step {
				build '/Team/Suraj/DWHJobs/QA_DWH'
			}
		}
		stage('SelligentUpload'){
			step {
				build '/Team/Suraj/DWHJobs/SelligentUpload'
			}
		}
		stage('UpdateAnalyticalAL'){
			step {
				build '/Team/Suraj/DWHJobs/UpdateAnalyticalAL'
			}
		}
		stage('QAAL'){
			step {
				build '/Team/Suraj/DWHJobs/QAAL'
			}
		}
		stage('ProcessBissDataTabular'){
			step {
				build '/Team/Suraj/DWHJobs/	ProcessBissDataTabular'
			}
		}
		stage('QABissTabular'){
			step {
				build '/Team/Suraj/DWHJobs/QABissTabular'
			}
		}
		stage('LoadDAData'){
			step {
				build '/Team/Suraj/DWHJobs/LoadDAData'
			}
		}
		stage('ExportDAData'){
			step {
				build '/Team/Suraj/DWHJobs/ExportDAData'
			}
		}
	}	
}
