#

CFILES=~/intellijProjects/Arahant/build/web/WEB-INF/classes
CFILES+=:~/intellijProjects/Arahant/lib/postgresql-9.4.1208.jre7.jar

CFILES+=:~/intellijProjects/Arahant/lib/log4j-1.2.16.jar

groovy -cp $CFILES $@

