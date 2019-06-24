import jenkins.model.Jenkins
import java.text.SimpleDateFormat

buildrunning='false';
buildimportrunning='false';
buildcleanuprunning='false';
buildselfrunning='false';
buildpipelinrunning='false';
buildDailyETLdate='01-01-1991';
buildDailyETLRunning='false';
String pattern = "dd-MM-yyyy";

SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
String currentdate = simpleDateFormat.format(new Date())-1;
println currentdate;

def LASTSUCCESSFULBUILD(jobname){
	def item = Jenkins.instance.getItemByFullName(jobname);
	if (item.getLastBuild()) {
		def ff=item.getLastSuccessfulBuild();
		builddate=ff.getTime().format("dd-MM-yyyy");
	}
	else {
		builddate=currentdate;
	}
	println builddate;
}

def LASTBUILDRUNNING (jobname) {
	def item = Jenkins.instance.getItemByFullName(jobname);
	if (item.lastBuild.building) {
		buildrunning='true';
	}	
	else {
		buildrunning='false';
	}
	println buildrunning;
}

pipeline {
	agent {
		label 'DETerminal'
	}
	stages {
		stage ('LastBuildStatusARHBASE') {
			steps{
				script{ 
					LASTBUILDRUNNING ('/Team/Suraj/Pipelinejobs/ARToHBASEToDWHSRC')
					buildpipelinrunning=buildrunning
				}
			}
		}
		stage('Build-AR-HBASE') {
			when {
				expression{return buildpipelinrunning=='false'}
			}
		 	steps {
				build '/Team/Suraj/NapkinJobs/job1'
			}
		}
		stage('HBASE-DWHSRC') {
			parallel {
				stage('HBASE-DWHSRC-A') {
					stages { 
						stage ('DomainALastBuildStatus') {
							steps{
								script{ 
									LASTBUILDRUNNING ('/Team/Suraj/DWH/import')
									buildimportrunning=buildrunning
									LASTBUILDRUNNING ('/Team/Suraj/DWH/Cleanup_dwhsrc')
									buildcleanuprunning=buildrunning
									LASTBUILDRUNNING ('/Team/Suraj/NapkinJobs/job2')
									buildselfrunning=buildrunning
								}
							}
						}
						stage {'LoadDataDomainA'} {
							when {
								allof{
									expression {return buildimportrunning=='false'}
									expression {return buildcleanuprunning=='false'}
									expression {return buildselfrunning=='false'}
									expression {return buildpipelinrunning=='false'}
								}
							}
							steps {
								build '/Team/Suraj/NapkinJobs/job2'
							}
						}
					}
				}
				stage('HBASE-DWHSRC-B') {
					stages { 
						stage ('DomainBLastBuildStatus') {
							steps{
								script{ 
									LASTBUILDRUNNING ('/Team/Suraj/DWH/QA_DWH')
									buildimportrunning=buildrunning
									LASTBUILDRUNNING ('/Team/Suraj/NapkinJobs/job3')
									buildselfrunning=buildrunning
								}
							}
						}
						stage {'LoadDataDomainB'} {
							when {
								allof{
									expression { return buildimportrunning=='false'}
									expression {return buildselfrunning=='false'}
									expression {return buildpipelinrunning=='false'}
								}
							}
							steps {
								build '/Team/Suraj/NapkinJobs/job3'
							}
						}
					}
				}
				stage('HBASE-DWHSRC-C') {
					stages { 
						stage ('DomainCLastBuildStatus') {
							steps{
								script{ 
									LASTBUILDRUNNING ('/Team/Suraj/DWH/ETLJackpot')
									buildimportrunning=buildrunning
									LASTBUILDRUNNING ('/Team/Suraj/NapkinJobs/job4')
									buildselfrunning=buildrunning
								}
							}
						}
						stage {'LoadDataDomainC'} {
							when {
								allof {
									expression {return buildimportrunning=='false'}
									expression {return buildselfrunning=='false'}
									expression {return buildpipelinrunning=='false'}
								}	
							}
							steps {
								build '/Team/Suraj/NapkinJobs/job4'
							}
						}
					}
				}
			}
		}
	}
	post {
		success {
			script {
				LASTBUILDRUNNING ('/Team/Suraj/Pipelinejobs/DailyETL')
				buildDailyETLRunning=buildrunning
				LASTSUCCESSFULBUILD('/Team/Suraj/Pipelinejobs/DailyETL')
				buildDailyETLdate=builddate
				if (buildDailyETLdate!=currentdate && buildDailyETLRunning=='false') {
					build '/Team/Suraj/Pipelinejobs/DailyETL'
				}
			}
		}
	}
}