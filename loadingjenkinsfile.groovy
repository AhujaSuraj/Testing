import jenkins.model.Jenkins
import java.text.SimpleDateFormat

buildrunning='false';
buildimportrunning='false';
buildcleanuprunning='false';
buildselfrunning='false';
buildDailyETLdate='01-01-1991';
buildDailyETLRunning='false';
String datepattern = "dd-MM-yyyy";
String timepattern="HH:MM";

//current date
SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datepattern);
currentdate = simpleDateFormat.format(new Date())-1;
println currentdate;

//currenttime
SimpleDateFormat simpleTimeFormat = new SimpleDateFormat(timepattern);
currenttime=simpleTimeFormat.format(new Date());
println currenttime;

//last build success date
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

//last build still running or not
def LASTBUILDRUNNING (jobname) {
	def item = Jenkins.instance.getItemByFullName(jobname);
	if (item.getLastBuild()) {
		if (item.lastBuild.building) {
			buildrunning='true';
		}	
		else {
			buildrunning='false';
		}
	}
	else {
		buildrunning='false';
	}
	println buildrunning;
}

//pipeline start
pipeline {
	agent {
		label 'DETerminal'
	}
	stages {
		stage ('LastBuildStatusARHBASE') {
			steps{
				script{ 
					LASTBUILDRUNNING ('/Team/Suraj/NapkinJobs/job1')
					buildselfrunning=buildrunning
				}
			}
		}
		stage('Build-AR-HBASE') {
			when {
				expression{return buildselfrunning=='false'}
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
						stage ('LoadDataDomainA') {
							when {
								allOf{
									expression {return buildimportrunning=='false'}
									expression {return buildcleanuprunning=='false'}
									expression {return buildselfrunning=='false'}
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
						stage ('LoadDataDomainB') {
							when {
								allOf{
									expression { return buildimportrunning=='false'}
									expression {return buildselfrunning=='false'}
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
						stage ('LoadDataDomainC') {
							when {
								allOf {
									expression {return buildimportrunning=='false'}
									expression {return buildselfrunning=='false'}
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
				//daily etl pipelin calling
				LASTBUILDRUNNING ('/Team/Suraj/Pipelinejobs/DailyETL')
				buildDailyETLRunning=buildrunning
				LASTSUCCESSFULBUILD('/Team/Suraj/Pipelinejobs/DailyETL')
				buildDailyETLdate=builddate
				if (buildDailyETLdate!=currentdate && buildDailyETLRunning=='false') {
					build job: '/Team/Suraj/Pipelinejobs/DailyETL'
				}
				else {
					echo "daily etl already running or its already done for today"
				}
				//regualr etljackpot calling
				if (currenttime=="11:00" || currenttime=="12:15" ) {
					build job: '/Team/Suraj/Pipelinejobs/JackpotETL'
				}
				else {
					echo "jackpot etl running time not yet"
				}
				
			}
		}
	}
}