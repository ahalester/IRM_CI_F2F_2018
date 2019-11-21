/* Sometimes when a database schema is created or synchronised the sequence */
/* counter is too low and is not a new unique value. When this happens we */
/* need to set the next sequence value higher than the highest value in the */
/* table ID column */

/*
select max(id) from asa_delivery_status; 
select asa_sequence.nextval from dual;
*/

drop sequence asa_sequence;
create sequence asa_sequence start with 100000;

exit
