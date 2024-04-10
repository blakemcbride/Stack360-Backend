#

CFILES=../../../../../classes
CFILES+=:../../../../../../../../shared/lib/postgresql-9.4.1208.jre7.jar

CFILES+=:../../../../../../../../shared/lib/log4j-1.2.16.jar

groovy -cp $CFILES $@ 
