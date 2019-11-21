# Requirements
    
* docker-machine (Selenium Grid) or natively using the suitable drivers
* java
* gradle

# Get the project from Git

    https://bitbucket.sco.alma.cl/projects/ALMA/repos/almasw/browse/IRM/RegressionTests/ARCHIVE/archive_services

# Tests configuration file

    /etc/offline/config/aatConfig.properties

 
# Starting cucumber tests with gradle
    
1. Install docker-machine

2. Start docker-machine

        cd <archive_services-dir>/src/main/resources
        docker-machine start default    
        docker-compose up -d
        
3. Execute gradle cucumber task providing expected parameters
    
        gradle cucumber -Dwebriver.chrome.driver=/Users/ldoming/workspace/utils/drivers/chromedriver -DtestPhase=PHAA -DtestEnv=2018jun -PsuiteTag=@Sanity -Dbrowser=chrome
        gradle cucumber -Dwebriver.firefox.driver=/Users/ldoming/workspace/utils/drivers/gheckodriver -DtestPhase=PHAA -DtestEnv=2018jun -PsuiteTag=@Sanity -Dbrowser=firefox

        
        Parameters:
        -Dwebriver.firefox.driver=/Users/ldoming/workspace/utils/drivers/gheckodriver 
        -DtestPhase=PHAA 
        -DtestEnv=2018jun 
        -PsuiteTag=@Sanity 
        -Dbrowser=firefox

        
        

# Starting cucumber tests with java (from the IDE)
  
1. Install "Cucumber for Java" Plugin 
        
        https://plugins.jetbrains.com/plugin/7212-cucumber-for-java

1. Glue
        
        alma.aat.web.browser.setup alma.aat.web.browser.steps
    
2. VM Options:
    
        -Dwebriver.firefox.driver=/Users/ldoming/workspace/utils/drivers/gheckodriver -Dbrowser=firefox -DseleniumVersion=3 -DtestPhase=PHAA -DtestEnv=2018jul



# Good to know

## Cucumber patch to continue after failed steps
https://github.com/cucumber/cucumber-jvm/compare/master...slaout:continue-next-steps-for-exceptions-1.2.4