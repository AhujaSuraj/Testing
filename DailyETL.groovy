pipeline {
	agent {
		label 'DETerminal'
	}
	stages{
		stage('import'){
			steps {
				build '/Team/Suraj/DWH/Import'	
			}
		}
		stage('Cleanup_DWHJobssrc'){
			steps {
				build '/Team/Suraj/DWH/Cleanup_dwhsrc'
			}
		}
		stage('TransformDataDerive'){
			steps {
				build '/Team/Suraj/DWH/TransformDataDerive'
			}
		}
		stage('LoadDataDWHJobs'){
			steps {
				build '/Team/Suraj/DWH/LoadDataDWH'
			}
		}
		stage('QADWH Pipeline'){
			steps {
				build '/Team/Suraj/Pipelinejobs/QADWH'
			}
		}
		stage('SelligentUpload'){
			steps {
				build '/Team/Suraj/DWH/SelligentUpload'
			}
		}
		stage('UpdateAnalyticalAL'){
			steps {
				build '/Team/Suraj/DWH/UpdateAnalyticalAL'
			}
		}
		stage('QAAL'){
			steps {
				build '/Team/Suraj/DWH/QAAL'
			}
		}
		stage('ProcessBissDataTabular'){
			steps {
				build '/Team/Suraj/DWH/ProcessBissDataTabular' 
			}
		}
		stage('QABissTabular'){
			steps {
				build '/Team/Suraj/DWH/QABissTabular'
			}
		}
		stage('LoadDAData'){
			steps {
				build '/Team/Suraj/DWH/LoadDAData'
			}
		}
		stage('ExportDAData'){
			steps {
				build '/Team/Suraj/DWH/ExportDAData'
			}
		}
	}	
}
