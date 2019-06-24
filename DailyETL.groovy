pipeline {
	agent {
		label 'DETerminal'
	}
	stages{
		stage('import'){
			steps {
				build '/Team/Suraj/DWHJobsJobs/Import'	
			}
		}
		stage('Cleanup_DWHJobssrc'){
			steps {
				build '/Team/Suraj/DWHJobs/Cleanup_DWHJobssrc'
			}
		}
		stage('TransformDataDerive'){
			steps {
				build '/Team/Suraj/DWHJobs/TransformDataDerive'
			}
		}
		stage('LoadDataDWHJobs'){
			steps {
				build '/Team/Suraj/DWHJobs/LoadDataDWHJobs'
			}
		}
		stage('QA_DWH'){
			steps {
				build '/Team/Suraj/DWHJobs/QA_DWH'
			}
		}
		stage('SelligentUpload'){
			steps {
				build '/Team/Suraj/DWHJobs/SelligentUpload'
			}
		}
		stage('UpdateAnalyticalAL'){
			steps {
				build '/Team/Suraj/DWHJobs/UpdateAnalyticalAL'
			}
		}
		stage('QAAL'){
			steps {
				build '/Team/Suraj/DWHJobs/QAAL'
			}
		}
		stage('ProcessBissDataTabular'){
			steps {
				build '/Team/Suraj/DWHJobs/	ProcessBissDataTabular'
			}
		}
		stage('QABissTabular'){
			steps {
				build '/Team/Suraj/DWHJobs/QABissTabular'
			}
		}
		stage('LoadDAData'){
			steps {
				build '/Team/Suraj/DWHJobs/LoadDAData'
			}
		}
		stage('ExportDAData'){
			steps {
				build '/Team/Suraj/DWHJobs/ExportDAData'
			}
		}
	}	
}
