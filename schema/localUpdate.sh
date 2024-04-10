#

pg_dump -s -U postgres $1 > local.schema
./genupdate.sh local.schema
psql -U postgres -f update.txt $1 
