select * from account_role ar, role r, account a where ar.role_no = r.role_no and a.account_id = ar.account_id and a.account_id = 'pmerino'
select * from role r where r.name = 'QAA'
select * from account_role ar, role r, account a where ar.role_no = r.role_no and a.account_id = ar.account_id and r.name = 'QAA' and a.account_id = 'pmerino'