import jenkins.model.Jenkins
import java.text.SimpleDateFormat

buildrunning='false';
buildimportArunning='false';
buildcleanupArunning='false';
buildimportBrunning='false';
buildcleanupBrunning='false';
buildimportCrunning='false';
buildcleanupCrunning='false';
buildDailyETLdate='01-01-1991';
buildDailyETLRunning='false';
String datepattern = "dd-MM-yyyy";
String timepattern="HH:MM";

//current date
SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datepattern);
currentdate = simpleDateFormat.format(new Date());
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
		if(ff) {
			builddate=ff.getTime().format("dd-MM-yyyy");
		}
		else {
			builddate=currentdate;
		}
	}
	else {
		builddate=currentdate-1;
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
		///ar to hbase
		stage('Build-AR-HBASE') {
		 	steps {
				build '/Team/Suraj/NapkinJobs/job1'
			}
		}
		//hbase to dwhsrc
		stage('HBASE-DWHSRC') {
			parallel {
				stage('HBASE-DWHSRC-A') {
					stages { 
						stage ('DomainALastBuildStatus') {
							steps{
								script{ 
									LASTBUILDRUNNING ('/Team/Suraj/DWH/import')
									buildimportArunning=buildrunning
									LASTBUILDRUNNING ('/Team/Suraj/DWH/Cleanup_dwhsrc')
									buildcleanupArunning=buildrunning
								}
							}
						}
						stage ('LoadDataDomainA') {
							when {
								allOf{
									expression {return buildimportArunning=='false'}
									expression {return buildcleanupArunning=='false'}
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
									buildimportBrunning=buildrunning
								}
							}
						}
						stage ('LoadDataDomainB') {
							when {
								allOf{
									expression { return buildimportBrunning=='false'}
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
									buildimportCrunning=buildrunning

								}
							}
						}
						stage ('LoadDataDomainC') {
							when {
								allOf {
									expression {return buildimportCrunning=='false'}
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
		stage ('DWH Pipeline jobs') {
			parallel {
				stage ('Daily ETL') {
					steps {
						script {
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
						}
					}
				}
				stage ('Jackpot ETL') {
					steps {
						script {
							if (currenttime>="06:00" || currenttime=="12:15" ) {
								build job: '/Team/Suraj/Pipelinejobs/JackpotETL'
							}
							else {
								echo "jackpot etl running time not yet"
							}
						}
					}
				}
			}
		}
	}
}