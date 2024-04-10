
This package is used to generate either an empty database (one needed
to boot the system for a new user) or a demo database (the same thing
only with some demo data added).  Keep in mind that an "empty"
database requires some data in order to have screen information and
login information in order to allow the system to function minimumly.

Reset.java is run to create (re re-initialize) this database.

base_setup.sql contains the boot (base) data in order for the system
to run minimumly.  If updates are needed to this file do the
following:

1.  Run Reset.java in order to create the database.

2.  Run the Arahant server to perform any data changes needed (SQL
script updates, user config changes, etc.).

3.  Export DATA ONLY from PostgreSQL
  #  cd src/java/com/arahant/utils/dbreset/
  #  pg_dump -a --column-inserts arahant -U postgres >f1


4.  Run the result through "fix-data" in order to produce the
base_setup.sql file.

  #  ./fix-data f1 >f2
  #  less f2
  #  mv f2 base_setup.sql
  #  rm f1

