select *
 from account
 where account_id = 'pmerino'

select *
 from account_role
 where account_id = 'pmerino'

select *
 from account_role, role
 where account_role.role_no = role.role_no
  and account_role.account_id = 'pmerino'

-- Removing AOD
DELETE FROM account_role
 WHERE account_role.account_id = 'pmerino'
 and account_role.role_no = 90;
commit;

select *
 from role
 where role.name = 'QAA'

-- Adding QAA
INSERT INTO account_role VALUES(98, 'pmerino');
commit;

-- Removing QAA
DELETE FROM account_role
 WHERE account_role.account_id = 'pmerino'
 and account_role.role_no = 98;
commit;

select *
 from role
 where role.name = 'ARCA'

-- Adding ARCA
INSERT INTO account_role VALUES(92, 'pmerino');
commit;

-- Removing QAA
DELETE FROM account_role
 WHERE account_role.account_id = 'pmerino'
 and account_role.role_no = 92;
commit;

select *
 from role
 where role.name = 'CSV_ASTRONOMER'

-- Adding CSV_ASTRONOMER
INSERT INTO account_role VALUES(116, 'pmerino');
commit;

-- Removing QAA
DELETE FROM account_role
 WHERE account_role.account_id = 'pmerino'
 and account_role.role_no = 116;
commit;

select *
 from role
 where role.name = 'DSO_ASTRONOMER'

-- Adding CSV_ASTRONOMER
INSERT INTO account_role VALUES(113, 'pmerino');
commit;

-- Removing DSO_ASTRONOMER
DELETE FROM account_role
 WHERE account_role.account_id = 'pmerino'
 and account_role.role_no = 113;
commit;

select *
 from role
 where role.name = 'DSO_STAFF'

-- Adding DSO_STAFF
INSERT INTO account_role VALUES(119, 'pmerino');
commit;

-- Removing DSO_ASTRONOMER
DELETE FROM account_role
 WHERE account_role.account_id = 'pmerino'
 and account_role.role_no = 119;
commit;

select *
 from role
 where role.name = 'ENGINEERING_STAFF'

-- Adding DSO_STAFF
INSERT INTO account_role VALUES(118, 'pmerino');
commit;

-- Removing DSO_ASTRONOMER
DELETE FROM account_role
 WHERE account_role.account_id = 'pmerino'
 and account_role.role_no = 118;
commit;

select *
 from role
 where role.name = 'SIST_ASTRONOMER'

-- Adding DSO_STAFF
INSERT INTO account_role VALUES(117, 'pmerino');
commit;

-- Removing DSO_ASTRONOMER
DELETE FROM account_role
 WHERE account_role.account_id = 'pmerino'
 and account_role.role_no = 117;
commit;